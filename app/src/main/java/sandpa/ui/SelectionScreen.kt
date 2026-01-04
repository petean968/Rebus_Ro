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
/*LOGICA DE EVENIMENT*/

@Composable
fun SelectionScreen(
    viewModel: RebusViewModel = viewModel(),
    onRebusSelected: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    //creem o variabila uiState care prin delegare "by" nu prin initiere "=",
    //"uiState" citeste ce scrie "_uiState"
    //"urmareste" sursa de date din viewModel(StateFlow)->si-l transforma in realitate UI:
    //collectAsState()->uiState(IMAGINEA)
    val context = LocalContext.current//da acces la Android OS;daca ar fi  pusa in viewModel
                    //ar ramane in viewModel in timp ce SelectionScreen dispare->memory leak
    val emailulMeu = "peteanpeteanpetean@yahoo.com"

    // Începem "Column" care ține TOT ecranul
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
    //frumos,in coloana, elementele;Catalogul de operatii Modifier:
        //umple toata foaia,da lasa margine(padding)
        Text(text = "Alege un Rebus", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))//elementele tre'sa respire,
                                                // nu sa se calce in picioare

        //  zona cu lista (folosesc weight(1f) ca să împing butonul jos)
        //la XML, trageai cutia
        Box(modifier = Modifier.weight(1f)) {
            when (val state = uiState) {//in functie de ce vede "ochiul collectAsState()
                is RebusUiState.Loading -> CircularProgressIndicator()//se incarca->cerculetul
                is RebusUiState.Success -> {//afiseaza lista
                    LazyColumn {//scroll->sterge ce iese,deseneaza ce intra
                                                //ReciclerView, cand am inceput eu Android...
                        items(state.rebusList) { rebus ->
                            Card(//items face cate un CARD, din fiecare rebus gasit in lista
                                //urmasul lui CardView din XML,e mai bun decat box:are "elevation"
                                //vezi ca se poate apasa/tap, din MatherialTheme
                                modifier = Modifier
                                    .fillMaxWidth()//umple
                                    .padding(vertical = 4.dp)//respira
                                    .clickable {//tap->rebusul ales
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
                else -> Unit//stai
            }
        }

        // 2. Butonul de Email - Acum e fix la finalul Column-ului, deci va sta jos
        Button(
            onClick = {
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    //Intent:formular/cerere;intent:cerere completata
                    //deci linia de mai sus, tradusa pentru un baiat oarecare:
                    //cerere=Formular(Formular.STAMPILATPENTRUPOSTA).scriipeEl{adresa,mesaj,subiect}
                    data = Uri.parse("mailto:")//data/ceFel=Uri/adresa;parse/tradupentru(email)
                    putExtra(Intent.EXTRA_EMAIL, arrayOf(emailulMeu))//aici scrii adresa destinatar
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
                    """.trimIndent())//aliniaza text
                }
                context.startActivity(intent)//send "cerere" to OS ;Android deschide "email app"
                                                //si completeaza campurile
            },
            modifier = Modifier
                .fillMaxWidth() // Un pic de spațiu față de listă
                .padding(top = 16.dp),
            colors =  ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))

        ) {
            Text(text = "✉️ Creeaza un rebus 5x5 (optional), numele creatorului este afisat doar cu acordul acestuia.La cerere, rebusul si numele pot fi sterse.",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium
                )
        }
    }
}