/*package sandpa.vafex.rebus_ro.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

// 1. Clasa Rădăcină (ține lista de rebusuri)
/*@JsonClass(generateAdapter = true)
data class RebusList(
    @Json(name = "rebusuri") val rebusuri: List<Rebus>
)*/

// 2. Clasa care reprezintă un singur Puzzle
@JsonClass(generateAdapter = true)
data class Rebus(
    @Json(name = "id") val id: String,
    @Json(name = "nume") val nume: String,
    @Json(name = "autor") val autor: String,

    // Grila este o matrice de String-uri (Listă de Liste de String)
    @Json(name = "grila") val grila: List<List<String>>,

    @Json(name = "definitii_verticale") val definitiiVerticale: List<Definitie>,
    @Json(name = "definitii_orizontale") val definitiiOrizontale: List<Definitie>
)

// 3. Clasa care reprezintă o singură Definiție sau Indiciu
@JsonClass(generateAdapter = true)
data class Definitie(
    @Json(name = "id") val id: String,
    @Json(name = "definitie") val text: String, // Textul definiției
    @Json(name = "solutie") val solutie: String, // Soluția
    @Json(name = "lungime") val lungime: Int // Lungimea cuvântului
)*/
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

    @Json(name = "grila") val grila: List<List<String>>
)

@JsonClass(generateAdapter = true)
data class DefinitiiContainer(
    @Json(name = "orizontal") val orizontal: List<String>, // <-- Listă simplă de String
    @Json(name = "vertical") val vertical: List<String>    // <-- Listă simplă de String
)

// Clasa 'Definitie' NU mai este necesară, deoarece JSON-ul tău nu o folosește.
// Poți șterge clasa 'Definitie'