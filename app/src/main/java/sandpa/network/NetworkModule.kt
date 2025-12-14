package sandpa.vafex.rebus_ro.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okio.buffer
import okio.source
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object NetworkModule {
    // Adresa de bază corectă
    private const val BASE_URL =
        "https://gist.githubusercontent.com/petean968/1a511d63553b380db7cc432cba5801ab/raw/8b30b5a2a7ca920d3442d5b28aa03b700cd17d57/"

    // Construim Moshi
    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory()) // Adaugă Moshi pentru clasele Kotlin
        .build()

    // Configurăm Retrofit
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        // AICI VOM FOLOSI CONVERTERUL CARE SETEAZĂ MODUL LENIENT
        .addConverterFactory(
            MoshiConverterFactory.create(moshi).asLenient() // <-- Aceasta este metoda corectă
        )
        .build()

    // Serviciul
    val rebusService: RebusApiService =
        retrofit.create(RebusApiService::class.java)
}

