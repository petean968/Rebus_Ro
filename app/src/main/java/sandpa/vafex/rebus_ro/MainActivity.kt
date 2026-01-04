
package sandpa.vafex.rebus_ro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import sandpa.vafex.rebus_ro.ui.RebusScreen
import sandpa.vafex.rebus_ro.ui.SelectionScreen
import sandpa.vafex.rebus_ro.ui.RewardScreen
import sandpa.vafex.rebus_ro.ui.RebusViewModel
import sandpa.vafex.rebus_ro.ui.theme.Rebus_RoTheme

class MainActivity : ComponentActivity() {//PORTALUL,APARITIA
    override fun onCreate(savedInstanceState: Bundle?) {//tap icoana->onCreate
        super.onCreate(savedInstanceState)//SE TREZESTE
        setContent {//XML_bye,desenam cu Kotlin
            Rebus_RoTheme {//paleta de haine si culori
                Surface(//modifier=eticheta de pe cutia "Surface" pe care scriu instructiuni
                    modifier = Modifier.fillMaxSize(),//Atlasul cu operatii "Modifier",
                                                        //din care aleg operatia "intinde-te
                                                        //pe tot ecranul
                    //Surface e o coala alba de hartie intinsa pe tot ecranul
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()//dispecerul : du-te la Vasile

                    //creez VIEWMODEL-ul AICI (O singură dată, deasupra NavHost)
                    //este CREIERUL COMUN,creat aici sus ca sa fie folosit si de lista de rebusuri
                    //si de jocul_rebusul propriuzis,de-l faceam inauntru se reseta
                    //folosim ktx extension(=viewModel()) pentru a-l păstra pe durata activității
                    val sharedViewModel: RebusViewModel = viewModel()
                    //shared, dosarul il citeste toata lumea
                    NavHost(navController = navController, startDestination = "lista") {
                        // NavHost este dispecerul ce are "Harta". Spunem aplicației:
                        // "Când pornești, du-te direct la destinația numită 'lista'".
                        // prima destinatie,lista de rebusuri
                        composable("lista") {
                            SelectionScreen(
                                viewModel = sharedViewModel, // 2. Îl pasăm pe cel comun
                                onRebusSelected = {
                                    navController.navigate("joc")
                                }// Când userul a ales un rebus, ghidul îl duce la "joc"
                            )
                        }
                        //a doua destinatie : jocul/rebusul propriuzis,ala ales
                        composable("joc") {
                            RebusScreen(
                                viewModel = sharedViewModel, // Îl pasez pe ACELAȘI aici
                                onWin = {                    //Folosește ACELAȘI creier (important!)
                                    navController.navigate("premiu")
                                }// Dacă ai câștigat, ghidul te duce(dispecerul te trimite)
                                 // la ecranul de premiu
                            )
                        }
                        //a treia destinatie : ecranul de premiu
                        composable("premiu") {
                            RewardScreen(onBack = {
                                navController.popBackStack("lista", inclusive = false)
                            })// Butonul de înapoi ne duce la listă, fără să închidă aplicația
                        }
                    }
                }
            }
        }
    }
}