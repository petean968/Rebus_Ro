

package sandpa.vafex.rebus_ro.ui
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow // (Acesta ar trebui să existe deja)
// ...

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import sandpa.vafex.rebus_ro.data.Rebus
// NOTĂ: Clasa Cell, RebusState, RebusUiState și RebusViewModel
// sunt așteptate să fie importate sau definite corespunzător.

// ======================================================
// 1. ECRAN PRINCIPAL
// ======================================================


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RebusScreen(
    viewModel: RebusViewModel = viewModel()
) {
    // Colectează starea de încărcare/eroare (uiState) și starea grilei (state)
    val uiState by viewModel.uiState.collectAsState()
    val rebusState by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Rebus Android") })
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            when (val state = uiState) {
                is RebusUiState.Loading -> LoadingScreen()
                is RebusUiState.Error -> ErrorScreen(state.message)
                is RebusUiState.Success -> {
                    val rebus = state.rebusList.firstOrNull()
                    if (rebus != null) {
                        RebusContent(
                            rebus = rebus,
                            rebusState = rebusState,
                            viewModel = viewModel
                        )
                    } else {
                        ErrorScreen("Nu s-au găsit date de rebus valide.")
                    }
                }
                else->Unit
            }
        }
    }
}

// ======================================================
// 2. CONTENT (Grilă și Definiții)
// ======================================================


// RebusScreen.kt - funcția RebusContent (CORECTATĂ PENTRU CENTRARE VIZUALĂ ȘI DIMENSIUNI)

@Composable
private fun RebusContent(
    rebus: Rebus,
    rebusState: RebusState,
    viewModel: RebusViewModel
) {
    // ⚠️ ATENȚIE: Aceste valori trebuie să se potrivească cu cele din RebusGrid
    val cellSize = 50.dp
    val indexSize = 30.dp

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Titlul Rebusului
        Text(
            text = rebus.titlu,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // ----------------------------------------------------
        // GRILA CENTRATĂ VIZUAL
        // ----------------------------------------------------
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center // 1. Centrează conținutul
        ) {
            Row(verticalAlignment = Alignment.Top) {
                // 2. Grila (include indicii pe stânga)
                RebusGrid(
                    grid = rebusState.grid,
                    matriceVerificare = rebus.matriceVerificare,
                    selectedCell = rebusState.selectedCell,
                    onCellClick = viewModel::selectCell
                )

                // 3. Spacer de Compensare: Echilibrează spațiul indexSize din stânga
                Spacer(modifier = Modifier.width(indexSize + 1.dp))
            }
        }
        // ----------------------------------------------------

        Spacer(modifier = Modifier.height(16.dp))

        // Definițiile
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            Text("Definiții Orizontale", fontWeight = FontWeight.Bold)
            rebus.definitiiContainer.orizontal.forEach {
                Text("• $it")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text("Definiții Verticale", fontWeight = FontWeight.Bold)
            rebus.definitiiContainer.vertical.forEach {
                Text("• $it")
            }
        }

        // AFIȘEAZĂ TASTATURA NUMAI DACĂ O CELULĂ ESTE SELECTATĂ
        if (rebusState.selectedCell != null) {
            KeyboardInput(
                onLetterSubmit = viewModel::setLetter,
                // Logica de deselectare (pentru a închide tastatura)
                onDismiss = {
                    val (r, c) = rebusState.selectedCell ?: return@KeyboardInput
                    // Click pe aceeași celulă o deselectează (logica în ViewModel)
                    viewModel.selectCell(r, c)
                }
            )
        }
    }
}

// ======================================================
// 3. GRID & CELL
// ======================================================

// 3A. Celula individuală
@Composable
fun RebusCell(
    cell: Cell,
    matriceVerificare: List<List<String>>?,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    onCellClick: (row: Int, col: Int) -> Unit
) {
    // Folosim isBlack din Cell, care este setat în ViewModel
    val isBlackCell = cell.isBlack
    val cellSize = 50.dp
    val literaCorecta = matriceVerificare?.getOrNull(cell.row)?.getOrNull(cell.col)
    val esteCorect = !isBlackCell &&
            cell.userInput.isNotEmpty() &&
            cell.userInput.equals(literaCorecta, ignoreCase = true)
    val esteGresit = cell.userInput.isNotEmpty() &&
            !cell.userInput.equals(literaCorecta, ignoreCase = true)

    Box(
        modifier = modifier
            .size(cellSize)
            .clickable(enabled = !isBlackCell) {
                onCellClick(cell.row, cell.col)
            }
            .border(BorderStroke(1.dp, Color.Gray)),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = when {
                esteCorect -> Color(0xFF4CAF50)
                esteGresit -> Color(0xFFE53935)// VERDE dacă e corect!
                isSelected -> Color(0xFFFDD835) // Galben pentru selecție
                isBlackCell -> Color.Black
                else -> Color.White
            }
        ) {
            if (!isBlackCell) {
                Box(modifier = Modifier.fillMaxSize()) {



                    // 2. CONȚINUTUL CELULEI (Litera utilizatorului)
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = cell.userInput.uppercase(),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}

// 3B. Grila Container
@Composable
private fun RebusGrid(
    grid: List<List<Cell>>,
    matriceVerificare: List<List<String>>?,
    selectedCell: Pair<Int, Int>?,
    onCellClick: (row: Int, col: Int) -> Unit
) {
    if (grid.isEmpty()) return

    val size = grid.first().size // Presupunem grilă pătrată (5)
    val cellSize = 50.dp
    val indexSize = 30.dp

    Column {
        // Indicii de Coloană (1, 2, 3...)
        Row(
            modifier = Modifier.padding(start = indexSize)
        ) {
            (1..size).forEach { colIndex ->
                Box(
                    modifier = Modifier.size(cellSize),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Text(
                        text = colIndex.toString(),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray
                    )
                }
            }
        }

        // Grila propriu-zisă
        grid.forEachIndexed { r, row ->
            Row {
                // Indicele de Rând (1, 2, 3...)
                Box(
                    modifier = Modifier.size(indexSize, cellSize),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Text(
                        text = (r + 1).toString(),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray
                    )
                }

                // Celulele din rând
                row.forEachIndexed { c, cell ->
                    val isSelected = selectedCell?.let { it.first == r && it.second == c } ?: false

                    RebusCell(
                        cell = cell,
                        matriceVerificare = matriceVerificare,
                        isSelected = isSelected,
                        onCellClick = onCellClick
                    )
                }
            }
        }
    }
}


// ======================================================
// 4. KEYBOARD INPUT
// ======================================================
// RebusScreen.kt - funcția KeyboardInput (VERSIUNEA CUSTOM)
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun KeyboardInput(
    onLetterSubmit: (String) -> Unit,
    onDismiss: () -> Unit
) {
    // Definirea alfabetului (poate fi adaptată la ce litere doriți)
    val alphabet = ('A'..'Z').toList().map { it.toString() }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp)
            .background(Color(0xFFE0E0E0), shape = MaterialTheme.shapes.medium)
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // 1. GRID-ul de litere (4 coloane)
        // Am ales 4 coloane pentru a face butoanele destul de mari
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.Top)
        ) {
            alphabet.forEach { letter ->
                Button(
                    onClick = {
                        onLetterSubmit(letter)
                        // NOTĂ: Nu închidem automat, lăsăm utilizatorul să apese mai multe litere
                        // dacă am avea logica de avansare automată la următoarea celulă.
                        // Dacă vreți să se închidă automat, adăugați onDismiss() aici.
                    },
                    // Ajustăm dimensiunea butoanelor
                    modifier = Modifier
                        .widthIn(min = 40.dp)
                        .height(40.dp)
                        .weight(1f, fill = false), // Distribuie spațiul
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(text = letter, fontSize = 16.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 2. BUTONUL DE ÎNCHIDERE (OFF / X)
        Button(
            onClick = onDismiss, // Aceasta deselectează celula și închide tastatura
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
            modifier = Modifier.width(120.dp)
        ) {
            Text("OFF (X)", fontWeight = FontWeight.Bold)
        }
    }
}


// ======================================================
// 5. UTIL
// ======================================================

@Composable
private fun LoadingScreen() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorScreen(msg: String) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(msg, color = MaterialTheme.colorScheme.error, textAlign = TextAlign.Center)
    }
}