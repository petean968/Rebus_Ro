package sandpa.vafex.rebus_ro.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Rebus(
    @Json(name = "id") val id: String,
    @Json(name = "titlu") val titlu: String, // <-- Adăugat

    // Moshi va mapa cheile din "solutii" (ex: "1H") la String
    @Json(name = "solutii") val solutii: Map<String, String>, // <-- Map<String, String>

    // Definițiile sunt acum sub-obiecte
    @Json(name = "definitii") val definitiiContainer: DefinitiiContainer, // <-- Container

    @Json(name = "grila") val grila: List<List<String>>,
    @Json(name = "matrice_verificare") val matriceVerificare: List<List<String>>
)

@JsonClass(generateAdapter = true)
data class DefinitiiContainer(
    @Json(name = "orizontal") val orizontal: List<String>, // <-- Listă simplă de String
    @Json(name = "vertical") val vertical: List<String>    // <-- Listă simplă de String
)