package sandpa.vafex.rebus_ro.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okio.buffer
import okio.source
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
//instantiem un "Singleton", un singur obiect/instalator in toata casa
//cuvantul cheie : object ; De ce? Iti trebuie un singur networkModule.
object NetworkModule {
    // Adresa de bază corectă, depozitul de rebusuri
    private const val BASE_URL =
        "https://gist.githubusercontent.com/petean968/1a511d63553b380db7cc432cba5801ab/raw /"

    // Construim Moshi/Translatorul de date (JSON) in obiecte Kotlin
    private val moshi = Moshi.Builder()//creeaza : DefinitiiContainerJsonAdapter,RebusJsonAdapter
        .addLast(KotlinJsonAdapterFactory()) // Dictionar.Adaugă Moshi pentru clasele Kotlin
        .build()

    // Configurăm retrofit, "camionul" care aduce date
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        // AICI VOM FOLOSI CONVERTERUL CARE SETEAZĂ MODUL LENIENT
        .addConverterFactory(
            MoshiConverterFactory.create(moshi).asLenient() // asLenient() "fi mai ingaduitor"
        )
        .build()//asambleaza !

    // Serviciul
    val rebusService: RebusApiService =
        retrofit.create(RebusApiService::class.java)
    // Retrofit creează automat toată logica prin care doar ceri :  "rebusService.getRebus()"
    // și el face toată munca grea în spate.
}
//BASE_URL : izvorul de date
//Retrofit : conducta de captare
//Moshi : Filtrul
//asLenient() : daca in filtru e o frunza mare, o inlatura
//rebusService : robinetul de la bucatarie