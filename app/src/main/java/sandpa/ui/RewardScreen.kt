

package sandpa.vafex.rebus_ro.ui

import android.media.MediaPlayer
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import sandpa.vafex.rebus_ro.R // Importul tău pentru resurse

@Composable
fun RewardScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val mediaPlayer = remember {
        MediaPlayer.create(context, R.raw.solo_chitara_rebus_3)
    }

    // Asta pornește muzica automat (solo-ul tău de chitară)
    LaunchedEffect(Unit) {
        mediaPlayer.start()
    }

    // Asta oprește muzica dacă bunicul închide pagina mai repede
    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer.release()
        }
    }

    // Containerul principal care adaugă fundalul crem și bordura populară
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFDF5E6)) // Fundalul crem
            .padding(8.dp) // Spațiu exterior
            // 1. Roșu (Exterior)
            .border(4.dp, Color(0xFFCE1126))
            // 2. Galben (Mijloc) - lipit de roșu
            .padding(4.dp)
            .border(4.dp, Color(0xFFFAD905))
            // 3. Albastru (Interior) - lipit de galben
            .padding(4.dp)
            .border(4.dp, Color(0xFF002B7F))
    ) {
        // ... restul codului rămâne la fel
   /* Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFDF5E6)) // Fundal crem mat
            .padding(12.dp)
            .border(
                width = 4.dp,
                color = Color(0xFFCE1126), // Roșu tricolor (exterior)
                shape = RoundedCornerShape(4.dp)
            )
            .padding(4.dp)
            .border(
                width = 2.dp,
                color = Color(0xFF002B7F), // Albastru tricolor (interior)
                shape = RoundedCornerShape(2.dp)
            )*/

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Textul tău exact, stilizat mare
            Text(
                text = "FELICITĂRI!",
                style = MaterialTheme.typography.displayMedium,
                color = Color(0xFF8B0000), // Roșu închis (bordura)
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Motivul tău geometric
            Image(
                painter = painterResource(id = R.drawable.motiv_romanesc),
                contentDescription = "Motiv Popular",
                modifier = Modifier.size(200.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Butonul tău exact cum l-ai definit, cu onBack
            Button(
                onClick = onBack,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFCE1126)) // Roșu tricolor
            ) {
                Text("Înapoi la listă", color = Color.White)
            }

            // Semnătura ta muzicală
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Muzica: petean  - Etno-Rock Solo",
                fontSize = 12.sp,
                color = Color.DarkGray.copy(alpha = 0.6f)
            )
        }
    }
}

