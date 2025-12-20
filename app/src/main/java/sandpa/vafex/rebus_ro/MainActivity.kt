//ver GitHub 2

/*package sandpa.vafex.rebus_ro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import sandpa.vafex.rebus_ro.ui.RebusScreen
import sandpa.vafex.rebus_ro.ui.theme.Rebus_RoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Rebus_RoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RebusScreen()
                }
            }
        }
    }
}*/
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
import sandpa.vafex.rebus_ro.ui.SelectionScreen // Va trebui să creăm acest fișier
import sandpa.vafex.rebus_ro.ui.RewardScreen    // Va trebui să creăm acest fișier
import sandpa.vafex.rebus_ro.ui.RebusViewModel
import sandpa.vafex.rebus_ro.ui.theme.Rebus_RoTheme

/*class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Rebus_RoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // 1. Inițializăm Controllerul de Navigație
                    val navController = rememberNavController()

                    // 2. NavHost definește rutele aplicației
                    NavHost(navController = navController, startDestination = "lista") {

                        // Ruta pentru lista de selecție
                        composable("lista") {
                            SelectionScreen(onRebusSelected = {
                                navController.navigate("joc")
                            })
                        }

                        // Ruta pentru jocul de rebus propriu-zis
                        composable("joc") {
                            RebusScreen(onWin = {
                                navController.navigate("premiu")
                            })
                        }

                        // Ruta pentru afișarea premiului (motivul popular)
                        composable("premiu") {
                            RewardScreen(onBack = {
                                navController.popBackStack("lista", inclusive = false)
                            })
                        }
                    }
                }
            }
        }
    }
}*/
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Rebus_RoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    // 1. CREĂM VIEWMODEL-UL AICI (O singură dată, deasupra NavHost)
                    // Folosim ktx extension pentru a-l păstra pe durata activității
                    val sharedViewModel: RebusViewModel = viewModel()

                    NavHost(navController = navController, startDestination = "lista") {

                        composable("lista") {
                            SelectionScreen(
                                viewModel = sharedViewModel, // 2. Îl pasăm pe cel comun
                                onRebusSelected = {
                                    navController.navigate("joc")
                                }
                            )
                        }

                        composable("joc") {
                            RebusScreen(
                                viewModel = sharedViewModel, // 3. Îl pasăm pe ACELAȘI aici
                                onWin = {
                                    navController.navigate("premiu")
                                }
                            )
                        }

                        composable("premiu") {
                            RewardScreen(onBack = {
                                navController.popBackStack("lista", inclusive = false)
                            })
                        }
                    }
                }
            }
        }
    }
}