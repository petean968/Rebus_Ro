package sandpa.vafex.rebus_ro.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import android.content.Intent
import android.net.Uri
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.graphics.Color


@Composable
fun SelectionScreen(
    viewModel: RebusViewModel = viewModel(),
    onRebusSelected: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val emailulMeu = "peteanpeteanpetean@yahoo.com"

    // Începem Column-ul care ține TOT ecranul
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        Text(text = "Alege un Rebus", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        // 1. Zona cu lista (folosim weight(1f) ca să împingem butonul jos)
        Box(modifier = Modifier.weight(1f)) {
            when (val state = uiState) {
                is RebusUiState.Loading -> CircularProgressIndicator()
                is RebusUiState.Success -> {
                    LazyColumn {
                        items(state.rebusList) { rebus ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable {
                                        viewModel.onRebusSelected(rebus)
                                        onRebusSelected()
                                    }
                            ) {
                                Text(
                                    text = rebus.titlu,
                                    modifier = Modifier.padding(16.dp),
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }
                    }
                }
                is RebusUiState.Error -> Text("Eroare: ${state.message}")
                else -> Unit
            }
        }

        // 2. Butonul de Email - Acum e fix la finalul Column-ului, deci va sta jos
        Button(
            onClick = {
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:")
                    putExtra(Intent.EXTRA_EMAIL, arrayOf(emailulMeu))
                    putExtra(Intent.EXTRA_SUBJECT, "Propunere Rebus Nou - Creator")
                    putExtra(Intent.EXTRA_TEXT, """
                        Salut! Vreau să propun un rebus pentru aplicație.
                        
                        Numele meu (autor): 
                        Tema rebusului: 
                        
                        GRILA (5x5):
                        _ _ _ _ _
                        _ _ _ _ _
                        _ _ _ _ _
                        _ _ _ _ _
                        _ _ _ _ _
                        
                        DEFINIȚII:
                        1H: 
                        2H: ...
                    """.trimIndent())
                }
                context.startActivity(intent)
            },
            modifier = Modifier
                .fillMaxWidth() // Un pic de spațiu față de listă
                .padding(top = 16.dp),
            colors =  ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))

        ) {
            Text(text = "✉️ Creeaza si tu un rebus 5x5, sa-l completam si pe al tau !",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium
                )
        }
    }
}