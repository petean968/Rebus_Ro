
package sandpa.vafex.rebus_ro.ui
import sandpa.vafex.rebus_ro.network.RebusApi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import sandpa.vafex.rebus_ro.data.Rebus
import sandpa.vafex.rebus_ro.network.NetworkModule // Asigură-te că acest import este corect
import java.io.IOException

// ==========================================================
// 1. STRUCTURI DE STARE
// ==========================================================

// Stări posibile pentru interfața utilizator
sealed interface RebusUiState {
    object Loading : RebusUiState
    data class Success(
        val rebusList: List<Rebus>,
        val gameState: GameState // <--- NOU
    ) : RebusUiState
    data class Error(val message: String) : RebusUiState
}

// Stare nouă pentru a gestiona logica jocului
data class GameState(
    // Matricea cu răspunsurile utilizatorului
    val userGrid: List<List<String>>,
    // Coordonatele celulei selectate (rând, coloană)
    val selectedCell: Pair<Int, Int>? = null,
    // Direcția curentă de scriere (true = Orizontal, false = Vertical)
    val isHorizontal: Boolean = true
)


// ==========================================================
// 2. VIEW MODEL ȘI LOGICA DE DATE
// ==========================================================

class RebusViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<RebusUiState>(RebusUiState.Loading)
    val uiState: StateFlow<RebusUiState> = _uiState

    init {
        getRebusuri() // Apelăm funcția la inițializarea ViewModel-ului
    }

    // Funcția care lipsea: PREIA DATELE ȘI INIȚIALIZEAZĂ GameState
    private fun getRebusuri() {
        viewModelScope.launch {
            _uiState.value = RebusUiState.Loading
            try {
                //val result: List<Rebus> = NetworkModule.rebusService.getRebusuri()
                val result: List<Rebus> = RebusApi.retrofitService.getRebusList()

                // Inițializează GameState pe baza grilei primului rebus
                val initialGrid = result.firstOrNull()?.grila

                if (initialGrid != null) {
                    // Creează userGrid: o listă de liste, inițializată cu "" (gol) pentru celulele "0"
                    val userGrid = initialGrid.map { row ->
                        row.map { if (it == "0") "" else it }
                    }

                    val initialState = GameState(userGrid = userGrid)

                    // Setează starea de succes cu datele și starea inițială a jocului
                    _uiState.value = RebusUiState.Success(result, initialState)
                } else {
                    _uiState.value = RebusUiState.Error("Nu s-au găsit rebusuri.")
                }
            } catch (e: IOException) {
                _uiState.value = RebusUiState.Error("Eroare de rețea: ${e.message}")
            } catch (e: Exception) {
                _uiState.value = RebusUiState.Error("Eroare de procesare: ${e.message}")
            }
        }
    }

    // ==========================================================
    // 3. LOGICA DE JOC
    // ==========================================================

    /**
     * Setează celula selectată și schimbă direcția la dublu click
     */
    fun selectCell(row: Int, col: Int) {
        val currentState = _uiState.value

        if (currentState is RebusUiState.Success) {
            val currentSelected = currentState.gameState.selectedCell
            val currentDirection = currentState.gameState.isHorizontal

            // Verifică dacă este un bloc negru. Dacă da, ignoră.
            if (currentState.rebusList.first().grila[row][col] == "B") {
                return
            }

            // Logica dublu-click: dacă celula este aceeași, schimbă direcția
            val newDirection = if (currentSelected == Pair(row, col)) {
                !currentDirection
            } else {
                currentDirection
            }

            // Aplică noua stare
            _uiState.update {
                RebusUiState.Success(
                    rebusList = currentState.rebusList,
                    gameState = currentState.gameState.copy(
                        selectedCell = Pair(row, col),
                        isHorizontal = newDirection
                    )
                )
            }
        }
    }

    /**
     * Actualizează litera din celula selectată.
     */
    fun onLetterTyped(letter: String) {
        val currentState = _uiState.value

        if (currentState is RebusUiState.Success) {
            val selected = currentState.gameState.selectedCell

            if (selected != null) {
                val (r, c) = selected

                // Ne asigurăm că nu modificăm indicii numerici sau blocurile negre
                if (currentState.rebusList.first().grila[r][c] == "0") {

                    // Clonează lista pentru a asigura imutabilitatea Flow-ului
                    val newUserGrid = currentState.gameState.userGrid.map { it.toMutableList() }.toMutableList()

                    // Punem litera nouă în celula selectată
                    newUserGrid[r][c] = letter.uppercase().take(1)

                    // Aplicăm noua stare
                    _uiState.update {
                        RebusUiState.Success(
                            rebusList = currentState.rebusList,
                            gameState = currentState.gameState.copy(
                                userGrid = newUserGrid.map { it.toList() } // Converteste înapoi la List<List<String>>
                            )
                        )
                    }
                }
            }
        }
    }
}