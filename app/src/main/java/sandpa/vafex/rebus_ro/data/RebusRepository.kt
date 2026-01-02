
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