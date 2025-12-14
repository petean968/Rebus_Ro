package sandpa.vafex.rebus_ro.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import sandpa.vafex.rebus_ro.data.Rebus

// URL-ul de bază ar trebui să fie cel al domeniului (gist.githubusercontent.com)
// dar pentru a evita o listă de URL-uri complicate, setăm doar domeniul comun
private const val BASE_URL =
    "https://gist.githubusercontent.com/"
// Tot ce e până la gh-ul utilizatorului

/**
 * Interfața care definește cererile HTTP.
 * Retrofit va implementa această interfață.
 */
interface RebusApiService {
    @GET("petean968/1a511d63553b380db7cc432cba5801ab/raw/8b30b5a2a7ca920d3442d5b28aa03b700cd17d57/rebusuri.json")
    // ATENȚIE: JSON-ul este o Listă de obiecte Rebus!
    suspend fun getRebusList(): List<Rebus>
}

/**
 * Obiectul Singleton care oferă o singură instanță de Retrofit și RebusApiService.
 */
object RebusApi {
    // 1. Interceptor pentru logare - util pentru debugging
    private val logging = HttpLoggingInterceptor().apply {
        setLevel(HttpLoggingInterceptor.Level.BODY) // Arată corpul răspunsului în logcat
    }

    // 2. Client OkHttpClient cu interceptorul adăugat
    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    // 3. Instanța Moshi cu adapterul Kotlin pentru a suporta proprietăți nule, etc.
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    // 4. Instanța Retrofit
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(httpClient)
        // Folosim Moshi pentru a converti JSON în obiecte Kotlin
        .addConverterFactory(MoshiConverterFactory.create(moshi).asLenient())
        .build()

    // 5. Instanța serviciului API (interfața implementată de Retrofit)
    val retrofitService: RebusApiService by lazy {
        retrofit.create(RebusApiService::class.java)
    }
}