/*package sandpa.vafex.rebus_ro.data
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

/**
 * Repository care simulează încărcarea rebusului dintr-o sursă de date.
 */
class RebusRepository {

    // Datele Rebusului (Exemplu 5x5)
    private val sampleRebus = Rebus(
        id = "1",
        titlu = "TEST 5X5",
        solutii = emptyMap(), // Lăsăm gol, deoarece nu le folosim încă
        grila = listOf(
            listOf("1V", "2V", "A", "B", "A"),
            listOf("B", "B", "3H", "B", "A"),
            listOf("4H", "A", "A", "B", "A"),
            listOf("B", "B", "A", "5V", "A"),
            listOf("6H", "A", "A", "A", "A")
        ),
        definitiiContainer = DefinitiiContainer(
            orizontal = listOf("4H: Cuvânt de 3 litere", "6H: Cuvânt de 5 litere"),
            vertical = listOf("1V: Cuvânt de 4 litere", "5V: Cuvânt de 2 litere")
        )
    )

    /**
     * Simulează o întârziere (delay) pentru a imita încărcarea din rețea/bază de date.
     */
    suspend fun getRebusList(): List<Rebus> = withContext(Dispatchers.IO) {
        delay(500) // Pauză de 0.5 secunde
        return@withContext listOf(sampleRebus)
    }
}*/
/*package sandpa.vafex.rebus_ro.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

/**
 * Repository care simulează încărcarea rebusului dintr-o sursă de date.
 * Datele sunt hardcodate aici, folosind "0" pentru celulele goale.
 */
class RebusRepository {

    // Datele Rebusului (Exemplu 5x5)
    private val sampleRebus = Rebus(
        id = "1",
        titlu = "TEST 5X5",
        solutii = emptyMap(),
        grila = listOf(
            // ⚠️ FOLOSIM "0" PENTRU CELULELE GOALE
            listOf("1V", "2V", "0", "B", "0"),
            listOf("B", "B", "3H", "B", "0"),
            listOf("4H", "0", "0", "B", "0"),
            listOf("B", "B", "0", "5V", "0"),
            listOf("6H", "0", "0", "0", "0")
        ),
        definitiiContainer = DefinitiiContainer(
            orizontal = listOf("4H: Cuvânt de 3 litere", "6H: Cuvânt de 5 litere"),
            vertical = listOf("1V: Cuvânt de 4 litere", "5V: Cuvânt de 2 litere")
        )
    )

    /**
     * Simulează o întârziere (delay) pentru a imita încărcarea din rețea/bază de date.
     */
    suspend fun getRebusList(): List<Rebus> = withContext(Dispatchers.IO) {
        delay(500) // Pauză de 0.5 secunde
        return@withContext listOf(sampleRebus)
    }
}*/
package sandpa.vafex.rebus_ro.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import sandpa.vafex.rebus_ro.network.RebusApi
import java.io.IOException

/**
 * Repository care încarcă rebusul folosind Retrofit și serviciul API.
 */
class RebusRepository {

    // Injectăm (sau referim) serviciul Retrofit
    private val rebusService = RebusApi.retrofitService

    /**
     * Prelucrează cererea de la ViewModel, gestionează excepțiile și returnează datele.
     */
    suspend fun getRebusList(): List<Rebus> = withContext(Dispatchers.IO) {
        try {
            // Apel direct către serviciul Retrofit
            return@withContext rebusService.getRebusList()
        } catch (e: IOException) {
            // Eroare de rețea (fără conexiune, timeout, etc.)
            println("Eroare de rețea/IO la preluarea rebusului: ${e.message}")
            return@withContext emptyList()
        } catch (e: Exception) {
            // Eroare de parsing JSON (Moshi) sau altă eroare de execuție
            println("Eroare la procesarea datelor rebusului: ${e.message}")
            return@withContext emptyList()
        }
    }
}