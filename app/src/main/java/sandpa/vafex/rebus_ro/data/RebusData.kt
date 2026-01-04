package sandpa.vafex.rebus_ro.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
//Reprezintă modelul de date principal pentru un Rebus.
//Permite Moshi să genereze codul de parsare
//la compilare pentru performanță optimă.
@JsonClass(generateAdapter = true)
data class Rebus(
    @Json(name = "id") val id: String,
    @Json(name = "titlu") val titlu: String, // <-- Adăugat

    // Mapare pentru soluții: Cheia este coordonata (ex: "1H"), valoarea este litera corectă.
    @Json(name = "solutii") val solutii: Map<String, String>, // <-- Map<String, String>

    // Definițiile sunt acum sub-obiecte, pe direcțiile (Orizontal/Vertical).
    @Json(name = "definitii") val definitiiContainer: DefinitiiContainer, // <-- Container
    // Structura grilei de joc reprezentată ca matrice de caractere/String-uri.
    @Json(name = "grila") val grila: List<List<String>>,
    // Matrice de referință utilizată pentru validarea corectitudinii răspunsurilor.
    @Json(name = "matrice_verificare") val matriceVerificare: List<List<String>>
)
//Structură de date pentru organizarea definițiilor jocului.
@JsonClass(generateAdapter = true)
data class DefinitiiContainer(
    @Json(name = "orizontal") val orizontal: List<String>, // <-- Listă simplă de String
    @Json(name = "vertical") val vertical: List<String>    // <-- Listă simplă de String
)