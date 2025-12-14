
package sandpa.vafex.rebus_ro.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import sandpa.vafex.rebus_ro.data.Rebus

// ======================================================
// 1. ECRAN PRINCIPAL
// ======================================================

@Composable
fun RebusScreen(
    viewModel: RebusViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        when (val state = uiState) {
            is RebusUiState.Loading -> LoadingScreen()
            is RebusUiState.Error -> ErrorScreen(state.message)
            is RebusUiState.Success -> {
                val rebus = state.rebusList.firstOrNull()
                if (rebus != null) {
                    RebusContent(rebus)
                } else {
                    ErrorScreen("Nu există rebusuri.")
                }

            }
            else -> Unit
        }
    }
}

// ======================================================
// 2. CONTENT
// ======================================================
// ======================================================
// 1. REBUS CONTENT (MODIFICAT PENTRU STATE ȘI VIZIBILITATE TASTATURĂ)
// ======================================================


@Composable
private fun RebusContent(rebus: Rebus) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = rebus.titlu,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        RebusGrid(rebus.grila)

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f) // ← AICI weight funcționează NORMAL
                .verticalScroll(rememberScrollState())
        ) {
            Text("Definiții orizontale", fontWeight = FontWeight.Bold)
            rebus.definitiiContainer.orizontal.forEach {
                Text("• $it")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text("Definiții verticale", fontWeight = FontWeight.Bold)
            rebus.definitiiContainer.vertical.forEach {
                Text("• $it")
            }
        }

        KeyboardInput()
    }
}

// ======================================================
// 3. GRID
// ======================================================


// ======================================================
// 3. GRID (CORECTAT PENTRU AFIȘAREA INDICILOR ȘI SPAȚIILOR LIBERE)
// ======================================================
// ======================================================
// 3. GRID (CU INDICI DE RAND ȘI COLOANĂ)
// ======================================================

@Composable
private fun RebusGrid(grid: List<List<String>>) {
    val size = grid.size // Presupunem 5x5
    val cellSize = 40.dp
    val indexSize = 25.dp // Spațiu necesar pentru indicii 1, 2, 3...

    Column {
        // 1. RÂNDUL CU INDICI DE COLOANĂ (1, 2, 3, 4, 5)
        Row(
            // Asigurăm spațiu pentru indicele de rând (colțul din stânga sus)
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

        // 2. GRILA PROPRIU-ZISĂ (Rânduri cu indici)
        grid.forEachIndexed { r, row ->
            Row {
                // INDICELE DE RÂND (1, 2, 3, 4, 5)
                Box(
                    modifier = Modifier.size(indexSize, cellSize),
                    contentAlignment = Alignment.CenterEnd // Aliniat la dreapta
                ) {
                    Text(
                        text = (r + 1).toString(),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray
                    )
                }

                // CELULELE DIN RÂND
                row.forEachIndexed { c, cell ->
                    val isBlackCell = cell == "B"

                    // Indicele de start mic ("1H", "2V")
                    val hasIndex = cell.length > 1 || cell.contains("H") || cell.contains("V")

                    Box(
                        modifier = Modifier
                            .size(cellSize)
                            .border(BorderStroke(1.dp, Color.Gray))
                            .clickable(enabled = !isBlackCell) {
                                // Aici va veni logica de selectare a celulei
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = if (isBlackCell) Color.Black else Color.White
                        ) {
                            if (!isBlackCell) {
                                Box(modifier = Modifier.fillMaxSize()) {

                                    // 1. INDICELE DE START (Mic, sus-stânga)
                                    if (hasIndex) {
                                        Text(
                                            text = "",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.DarkGray,
                                            modifier = Modifier
                                                .align(Alignment.TopStart)
                                                .padding(2.dp)
                                        )
                                    }

                                    // 2. CONȚINUTUL CELULEI (Mare, Centru)
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        // ⚠️ AICI VINE LITERA UTILIZATORULUI (dar deocamdată rămâne gol)
                                        Text(
                                            text = "",
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
/*@Composable
private fun RebusGrid(grid: List<List<String>>) {
    // Definirea dimensiunii celulei
    val cellSize = 40.dp

    Column {
        grid.forEachIndexed { r, row ->
            Row {
                row.forEachIndexed { c, cell ->
                    val isBlackCell = cell == "B"

                    // Verifică dacă celula conține un indice de start (ex: "1H", "2V")
                    val hasIndex = cell.length > 1 || cell.contains("H") || cell.contains("V")

                    Box(
                        modifier = Modifier
                            .size(cellSize)
                            .border(BorderStroke(1.dp, Color.Gray))
                            .clickable(enabled = !isBlackCell) {
                                // Aici va veni logica de selectare (la pasul următor)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = if (isBlackCell) Color.Black else Color.White
                        ) {
                            if (!isBlackCell) {
                                Box(modifier = Modifier.fillMaxSize()) {

                                    // 1. INDICELE (Mic, sus-stânga)
                                    if (hasIndex) {
                                        Text(
                                            text = "",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.DarkGray,
                                            modifier = Modifier
                                                .align(Alignment.TopStart)
                                                .padding(2.dp)
                                        )
                                    }

                                    // 2. CONȚINUTUL CELULEI (Mare, Centru)
                                    // Aici se va afișa litera introdusă de user (deocamdată, este gol)
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        // Textul este gol pentru că nu am integrat încă userGrid
                                        Text(
                                            text = "",
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}*/

// ======================================================
// 4. KEYBOARD INPUT – FĂRĂ ERORI
// ======================================================

@Composable
private fun KeyboardInput() {
    var text by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = { text = it.take(1).uppercase() },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Characters

            ),

            textStyle = TextStyle(
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.width(80.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Button(
            onClick = { text = "" },
            enabled = text.isNotEmpty()
        ) {
            Text("OK")
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
        Text(msg, color = MaterialTheme.colorScheme.error)
    }
}
