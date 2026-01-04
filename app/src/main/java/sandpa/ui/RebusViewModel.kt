package sandpa.vafex.rebus_ro.ui

import androidx.lifecycle.ViewModel//survival rotate
import androidx.lifecycle.viewModelScope//delegatie pentru "Dorel Coroutinas",aviz pozitiv
import kotlinx.coroutines.flow.MutableStateFlow//tabla de scris, se sterge
import kotlinx.coroutines.flow.StateFlow//se pune geamul pe ea, doar citim->UI
import kotlinx.coroutines.flow.asStateFlow//clemele de prins geamul pe tabla
import kotlinx.coroutines.flow.update//buretele care sterge o litera
import kotlinx.coroutines.launch//start Dorele!du-te la magazie
import sandpa.vafex.rebus_ro.data.Rebus//adu rebusul
import sandpa.vafex.rebus_ro.data.RebusRepository//magazia de rebusuri virtuala->JSON
//COROUTINAS ESTE MODUL DE A SCRIE FUNCTII SINCRONE

//CARE                  //ASINCRON
        //SE EXECUTA

/*LOGICA DE STARE*/
// ======================================================
// 1. DATA MODEL PENTRU UI
// ======================================================

// Celula individuală din grilă
// Celula individuală: E ca eticheta de inlocuit lipita pe rebus.
// Pe ea scriem unde stă (rând, coloană) și ce e în el.
data class Cell(
    val row: Int,
    val col: Int,
    val value: String, // Conținutul celulei (ex: "B", "1H", "2V", etc.)
    val index: Int? = null, // Indicele de start, nr. mic (dacă există)
    val userInput: String = "", // Litera introdusă de utilizator
    val isBlack: Boolean = false // Dacă este celulă neagră
)
//RebusState: E "Poza de ansamblu" a jocului în orice moment.
//Se schimba des, la litera
//Starea care gestionează grila și celula selectată
data class RebusState(
    val grid: List<List<Cell>> = emptyList(),// peretele rebus cu etichete
    val selectedCell: Pair<Int, Int>? = null // coordonatele (row, col) a celulei selectate
)
//RebusState se schimba doar la evenimente MARI
//Stări posibile pentru interfața utilizator (Încărcare, Succes, Eroare)
//meniu fix (sigilat = sealed)
sealed interface RebusUiState {

    object Loading : RebusUiState//Dorel cauta/aduce cheile de la magazia de rebusuri.
    data class Success(val rebusList: List<Rebus>) : RebusUiState//Am gasit marfa ,uite lista!
    data class Error(val message: String) : RebusUiState//Inundatie,avem o problema!
}


// ======================================================
// 2. VIEWMODEL (Șeful de șantier - creierul operațiunii)
// ======================================================
//fum->nas->creier->deschideGeam->nuMaiEfum->nas->creier->inchideGeam
class RebusViewModel(
    //Asigurare RebusRepository este accesibil
    //constructor : ( cumparam magazia : DeRebusuri ) ; constructor : (..)
    private val repository: RebusRepository = RebusRepository()
) : ViewModel() {
    //tabla de scriere (_state)=potiStergeAdauga->MutableStateFlow()
    //-state : starea jocului
    //StareInitialaMutabila(RebusState(obj:careu,litere))
    //MutableStateFlow(RebusState())-noi numa' scriem
    private val _state = MutableStateFlow(RebusState())
    //(state)read-only->Fragment,Compose
    //asStateFlow()-noi numa' citim
    val state: StateFlow<RebusState> = _state.asStateFlow()

    //-uiState : starea ecranului
    private val _uiState = MutableStateFlow<RebusUiState>(RebusUiState.Loading)//se incarca
    val uiState: StateFlow<RebusUiState> = _uiState.asStateFlow()

    private val _selectedRebus = MutableStateFlow<Rebus?>(null)
    val selectedRebus = _selectedRebus.asStateFlow()//userul l-a ales pe asta

    private val _isGameWon = MutableStateFlow(false)//becul victoriei:tabla de scris
    val isGameWon = _isGameWon.asStateFlow()//becul victoriei tabla de citit

    init {//cum a zis constructorul,se execută instantaneu când ViewModel-ul este creat în memorie.
          //Deschide usa magaziei de rebusuri
        fetchRebusList()//dupa ce te-ai nascut, cauta lista : init {fetchRebusList()}
    }

    private fun fetchRebusList() {
        viewModelScope.launch {//Dorele Sho Pa Ea !Ada rebus!
            try {
                val rebusList = repository.getRebusList()//asteptam,dar ecranul nu se stinge
                _uiState.value = RebusUiState.Success(rebusList)//a venit Dorel,cu lista
                                                                // scrie lista pe tabla
                //AM ȘTERS: Partea care făcea _state.update cu primul rebus
                //pentru ca acum am mai multe
            } catch (e: Exception) {
                _uiState.value = RebusUiState.Error("Eroare la încărcare: ${e.localizedMessage}")
            }
        }
    }
    //_uiState (cu underscore în față): Este monitorul pe care „bucătarul” (ViewModel)
    // are voie să scrie cu carioca, să șteargă, să modifice. E Mutable (se poate schimba).
    //ENCAPSULATE
    //UiState (U MARE): Este Nomenclatorul de Scrisori de la perete.
    //Scrisoarea poate fi de tip: Reclamație, Felicitare sau Cerere de Concediu).
    //uiState (u mic): este poza de pe tabla lui viewModel, dupa care UI-ul executa

    fun onRebusSelected(rebus: Rebus) {
        //_isGameWon (borcanul); .value dulceata de ... capsuni/zmeura
        _isGameWon.value = false//initiem becul victoriei=stins
        _selectedRebus.value = rebus//am ales capsuni
        //.update e ca și cum ai lua tabla de scris și ai redesena-o complet
        _state.update {
            it.copy(
                //Folosește funcția  existentă, transformand datele brute in
                //etichete de lipit pe borcan
                grid = mapRebusToCells(rebus),
                selectedCell = null//nici o celula selectata;nici o casuta apasata
            )
        }
    }
    fun resetVictory() {
        _isGameWon.value = false
    }



    //Funcție pentru a transforma grila de String-uri în grila de Cell-uri
    //muncitorul care aseaza casutele rebusului ales,dupa schema:rebus
    private fun mapRebusToCells(rebus: Rebus): List<List<Cell>> {
        //contorul pentru numerele mici (1,2,3,4,5)
        var indexCounter = 1
        //construim rebusul pe care se va ,(cu casute goale de scris)
        //mapIndexed() , Kotlin extension function
        //E definită în codul sursă al limbajului Kotlin,
        //pe care Google și JetBrains l-au scris pentru noi.
        //map : ia fiecare pruna,fierbe-o si pune-o in borcanul cu gem
        //Indexed : scrie si ce borcan e (nr.1;gem de prune)
        return rebus.grila.mapIndexed { r, row ->
            row.mapIndexed { c, value ->
                //neagra, blocata
                val isBlackCell = value == "B" //  Dacă e "B", este neagră

                // Indicele este calculat doar dacă valoarea conține V sau H (ex: "1V", "3H")
                val index = if (value.contains("H") || value.contains("V")) {
                    indexCounter++
                } else {
                    null
                }//tiparim etichetele, cu toate informatiile
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

     // Cand pun degetul pe casuta : TAP
     // Selectează o celulă la click. Deselectează dacă este deja selectată sau dacă este celulă neagră.

    fun selectCell(row: Int, col: Int) {
        val currentGrid = _state.value.grid

        // Asigură-te că celula nu este neagră, pentru nu face nimic, nu poti scrie...
        if (currentGrid.getOrNull(row)?.getOrNull(col)?.isBlack == true) {
            return
        }

        _state.update { currentState ->
            val newSelectedCell =
                if (currentState.selectedCell?.first == row && currentState.selectedCell?.second == col) {
                    null // Deselectează : (la dublu click sau apăsare pe aceeași celulă)
                } else {
                    Pair(row, col) // Selectează noua celulă
                }
            // Facem o copie a stării vechi, dar cu celula nouă selectată
            currentState.copy(selectedCell = newSelectedCell)
        }
    }

       //TASTATURA CUSTOM
       //Setează litera introdusă de utilizator în celula selectată.
       //Cand scrii o litera de la tastatura :


    fun setLetter(letter: String) {
        _state.update { currentState ->
            //n-avem casuta selectata cu galben, plecam la alta
            val selected = currentState.selectedCell ?: return@update currentState
            val (r, c) = selected

            //  Creăm noua grilă cu litera nouă
            //  SE CLONEAZA toata grila, NUMAI in patratica noastra punem litera scrisa
            val newGrid = currentState.grid.mapIndexed { rowIdx, rowList ->
                rowList.mapIndexed { colIdx, cell ->
                    if (rowIdx == r && colIdx == c) {
                        //majuscule, c-asa-i tastatura
                        cell.copy(userInput = letter.uppercase())
                    } else {
                        cell//sau : casutele raman la fel
                    }
                }
            }

            // 2. Verificăm victoria imediat după ce am creat newGrid
            val rebusCurent = _selectedRebus.value
            if (rebusCurent != null) {
                // Dacă checkVictory returnează true, ridicăm flag-ul
                if (checkVictory(newGrid, rebusCurent.matriceVerificare)) {
                    _isGameWon.value = true//s-a aprins becul victoriei
                }
            }

            // 3. Returnăm starea cu noua grilă
            //Trimitem noua poza a tablei cu ce-am scris pe ecran
            currentState.copy(grid = newGrid)
        }
    }
    //functia : profu' - verifica daca ce am scris este complet si corect
    private fun checkVictory(currentGrid: List<List<Cell>>, solution: List<List<String>>): Boolean {
        //fiecare casuta/patratica/cell
        currentGrid.forEachIndexed { r, row ->
            row.forEachIndexed { c, cell ->
                // Verificăm doar celulele care nu sunt negre
                if (!cell.isBlack) {
                    val correctChar = solution.getOrNull(r)?.getOrNull(c) ?: ""
                    // Dacă o singură celulă nu coincide sau e goală, returnăm false
                    // rebusul nu e complet
                    if (!cell.userInput.equals(correctChar, ignoreCase = true)) {
                        return false
                    }
                }
            }
        }
        return true // Dacă am trecut prin toate și sunt corecte, returnăm true

    }
}