
package sandpa.vafex.rebus_ro.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import sandpa.vafex.rebus_ro.R // Importul pentru resurse

@Composable
fun RewardScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White), // Acum Color.White va fi recunoscut
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "FELICITĂRI!",
            style = MaterialTheme.typography.displayMedium,
            color = Color(0xFF002B7F), // Albastru tricolor
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Ai completat corect rebusul.",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(32.dp))

        // AICI apare motivul tău geometric
        Image(
            painter = painterResource(id = R.drawable.motiv_romanesc),
            contentDescription = "Motiv Popular",
            modifier = Modifier.size(200.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onBack,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFCE1126)) // Roșu tricolor
        ) {
            Text("Înapoi la listă", color = Color.White)
        }
    }
}
