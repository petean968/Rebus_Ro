package sandpa.vafex.rebus_ro.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import sandpa.vafex.rebus_ro.data.Rebus
import sandpa.vafex.rebus_ro.data.RebusRepository // ⚠️ Folosim RebusRepository

// ======================================================
// 1. DATA MODEL PENTRU UI
// ======================================================

// Celula individuală din grilă
data class Cell(
    val row: Int,
    val col: Int,
    val value: String, // Conținutul celulei (ex: "B", "1H", "2V", etc.)
    val index: Int? = null, // Indicele de start mic (dacă există)
    val userInput: String = "", // Litera introdusă de utilizator
    val isBlack: Boolean = false // Dacă este celulă neagră
)

// Starea care gestionează grila și celula selectată
data class RebusState(
    val grid: List<List<Cell>> = emptyList(),
    val selectedCell: Pair<Int, Int>? = null // (row, col) a celulei selectate
)

// Stări posibile pentru interfața utilizator (Încărcare, Succes, Eroare)
sealed interface RebusUiState {
    object Loading : RebusUiState
    data class Success(val rebusList: List<Rebus>) : RebusUiState
    data class Error(val message: String) : RebusUiState
}


// ======================================================
// 2. VIEWMODEL
// ======================================================

class RebusViewModel(
    private val repository: RebusRepository = RebusRepository() // Asigură-te că RebusRepository este accesibil
) : ViewModel() {

    private val _state = MutableStateFlow(RebusState())
    val state: StateFlow<RebusState> = _state.asStateFlow()

    private val _uiState = MutableStateFlow<RebusUiState>(RebusUiState.Loading)
    val uiState: StateFlow<RebusUiState> = _uiState.asStateFlow()

    private val _selectedRebus = MutableStateFlow<Rebus?>(null)
    val selectedRebus = _selectedRebus.asStateFlow()

    private val _isGameWon = MutableStateFlow(false)
    val isGameWon = _isGameWon.asStateFlow()

    init {
        //loadRebus()
        fetchRebusList()
    }

    private fun fetchRebusList() {
        viewModelScope.launch {
            try {
                val rebusList = repository.getRebusList()
                _uiState.value = RebusUiState.Success(rebusList)
                // AM ȘTERS: Partea care făcea _state.update cu primul rebus
            } catch (e: Exception) {
                _uiState.value = RebusUiState.Error("Eroare la încărcare: ${e.localizedMessage}")
            }
        }
    }

    fun onRebusSelected(rebus: Rebus) {
        _selectedRebus.value = rebus
        _state.update {
            it.copy(
                grid = mapRebusToCells(rebus), // Folosește funcția ta existentă
                selectedCell = null
            )
        }
    }



    // Funcție pentru a transforma grila de String-uri în grila de Cell-uri
    private fun mapRebusToCells(rebus: Rebus): List<List<Cell>> {
        var indexCounter = 1

        return rebus.grila.mapIndexed { r, row ->
            row.mapIndexed { c, value ->

                val isBlackCell = value == "B" // ⚠️ Dacă e "B", este neagră

                // Indicele este calculat doar dacă valoarea conține V sau H (ex: "1V", "3H")
                val index = if (value.contains("H") || value.contains("V")) {
                    indexCounter++
                } else {
                    null
                }

                Cell(
                    row = r,
                    col = c,
                    value = value,
                    index = index,
                    userInput = "",
                    isBlack = isBlackCell // Setează proprietatea isBlack
                )
            }
        }
    }
// ...
    // 3. LOGICĂ TASTATURĂ/SELECTARE

    /**
     * Selectează o celulă la click. Deselectează dacă este deja selectată sau dacă este celulă neagră.
     */
    fun selectCell(row: Int, col: Int) {
        val currentGrid = _state.value.grid

        // Asigură-te că celula nu este neagră
        if (currentGrid.getOrNull(row)?.getOrNull(col)?.isBlack == true) {
            return
        }

        _state.update { currentState ->
            val newSelectedCell =
                if (currentState.selectedCell?.first == row && currentState.selectedCell?.second == col) {
                    null // Deselectează (la dublu click sau apăsare pe aceeași celulă)
                } else {
                    Pair(row, col) // Selectează noua celulă
                }
            currentState.copy(selectedCell = newSelectedCell)
        }
    }

    /**
     * Setează litera introdusă de utilizator în celula selectată.
     */

    fun setLetter(letter: String) {
        _state.update { currentState ->
            val selected = currentState.selectedCell ?: return@update currentState
            val (r, c) = selected

            // 1. Creăm noua grilă cu litera nouă
            val newGrid = currentState.grid.mapIndexed { rowIdx, rowList ->
                rowList.mapIndexed { colIdx, cell ->
                    if (rowIdx == r && colIdx == c) {
                        cell.copy(userInput = letter.uppercase())
                    } else {
                        cell
                    }
                }
            }

            // 2. Verificăm victoria imediat după ce am creat newGrid
            val rebusCurent = _selectedRebus.value
            if (rebusCurent != null) {
                // Dacă checkVictory returnează true, ridicăm flag-ul
                if (checkVictory(newGrid, rebusCurent.matriceVerificare)) {
                    _isGameWon.value = true
                }
            }

            // 3. Returnăm starea cu noua grilă
            currentState.copy(grid = newGrid)
        }
    }

    private fun checkVictory(currentGrid: List<List<Cell>>, solution: List<List<String>>): Boolean {
        currentGrid.forEachIndexed { r, row ->
            row.forEachIndexed { c, cell ->
                // Verificăm doar celulele care nu sunt negre
                if (!cell.isBlack) {
                    val correctChar = solution.getOrNull(r)?.getOrNull(c) ?: ""
                    // Dacă o singură celulă nu coincide sau e goală, returnăm false
                    if (!cell.userInput.equals(correctChar, ignoreCase = true)) {
                        return false
                    }
                }
            }
        }
        return true // Dacă am trecut prin toate și sunt corecte, returnăm true

    }
}