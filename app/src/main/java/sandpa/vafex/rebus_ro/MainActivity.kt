
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

                    // 1. creez VIEWMODEL-ul AICI (O singură dată, deasupra NavHost)
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
                                viewModel = sharedViewModel, // 3. Îl pasez pe ACELAȘI aici
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