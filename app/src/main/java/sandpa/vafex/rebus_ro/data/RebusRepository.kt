
package sandpa.vafex.rebus_ro.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import sandpa.vafex.rebus_ro.network.RebusApi
import java.io.IOException

  //Repository care încarcă rebusul folosind Retrofit și serviciul API.
  //"Administratorul de Date",decide de unde vin datele și să curețe  (erorile)

class RebusRepository {
    // Referință către robinetul nostru magic (Retrofit)
    // Injectăm (sau referim) serviciul Retrofit, Manual Dependency Injection
    // dependncies-lopata lui Dorel, si-o face singur :
    private val rebusService = RebusApi.retrofitService
    //DACA LUCRAI CU DAGGER(MAGAZIA DE SCULE)/HILT(NEA JAN DE LA SCULERIE),
    //AVEAM, in NetworkModule:

    /* @Module
    @InstallIn(SingletonComponent::class)
    object NetworkModule {

        @Provides
        @Singleton
        fun provideRebusService(): RebusApiService {
            return Retrofit.Builder()
                .baseUrl("https://...")
                .build()
                .create(RebusApiService::class.java)
      }
    }*/
    //SI AICI IN REBUS REPOSITORY :
    /* class RebusRepository @Inject constructor(
    private val rebusService: RebusApiService // Hilt o aduce aici automat!
) {
    // Fara sa scrii "=" nimic, Hilt știe de unde să o ia.
} */
// @Component și @Subcomponent -> E Dagger !Java,Server,Android.
// @HiltAndroidApp și @AndroidEntryPoint -> E Hilt.
    /* daca vrei Hilt, mai faci un pachet la nivel de MainActivity:

package sandpa.vafex.rebus_ro

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp    // Aceasta este "borna" care pornește Hilt în tot proiectul
class RebusApplication : Application() {
    // De obicei rămâne goală, dar e "creierul" care trăiește
    // cât timp aplicația e deschisă (chiar dacă schimbi ecranele)
    // o treci obligatoriu in (AndroidManifest.xml)

}
De ce am folosi un framework de Dependency Injection (DI) precum Hilt?",


Gestionarea Complexității (Separation of Concerns): Hilt automatizează ciclul de viață al dependențelor,
eliminând codul de tip 'boilerplate' necesar pentru instanțierea manuală a graficului de obiecte
(Network, Database, Repositories).

Facilitarea Testării Unitare (Testability): Prin DI, putem injecta implementări 'Mock' sau 'Fake'
în timpul testării, asigurându-ne că testele noastre sunt izolate și nu depind de surse de date
externe (precum un API real).

Mentenanță și Arhitectură Standardizată: Hilt impune o structură modulară și predictibilă,
facilitând colaborarea în echipe mari prin definirea clară a scopului (Scope) fiecărei dependențe.
*/

     // Prelucrează cererea de la ViewModel, gestionează excepțiile și returnează datele.

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