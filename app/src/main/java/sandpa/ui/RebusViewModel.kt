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

    init {
        loadRebus()
    }

    private fun loadRebus() {
        viewModelScope.launch {
            try {
                // Obține lista de rebusuri
                val rebusList = repository.getRebusList()
                _uiState.value = RebusUiState.Success(rebusList)

                val rebus = rebusList.firstOrNull()
                if (rebus != null) {
                    // Inițializarea grilei de celule la încărcare
                    _state.update {
                        it.copy(grid = mapRebusToCells(rebus))
                    }
                }
            } catch (e: Exception) {
                _uiState.value = RebusUiState.Error("Eroare la încărcarea rebusului: ${e.localizedMessage}")
            }
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
            val newSelectedCell = if (currentState.selectedCell?.first == row && currentState.selectedCell?.second == col) {
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
            val selected = currentState.selectedCell

            // Verifică dacă există o celulă selectată. Dacă NU, returnează starea curentă.
            if (selected == null) {
                return@update currentState // ⚠️ Folosim return@update pentru a returna starea
            }

            val (r, c) = selected

            // 1. Creează o nouă listă (nouă grilă)
            val newGrid = currentState.grid.mapIndexed { row, rowList ->
                rowList.mapIndexed { col, cell ->
                    // 2. Modifică doar celula selectată
                    if (row == r && col == c) {
                        cell.copy(userInput = letter.uppercase()) // Actualizează userInput
                    } else {
                        cell
                    }
                }
            }
            // 3. Returnează noua stare
            currentState.copy(grid = newGrid) // ⚠️ Returnează RebusState
        }
    }

}