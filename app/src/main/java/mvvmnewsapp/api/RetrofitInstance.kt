package mvvmnewsapp.api

import mvvmnewsapp.utils.Constants.Companion.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {
    companion object{

        private val retrofit by lazy {
            // Create a logging interceptor
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY) // See the body of the response

           // Create a network client with the logging interceptor
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()

            // pass the client to retrofit instance
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()) // convert to kotlin object
                .client(client)
                .build()
        }

        // API instance
        val api by lazy {
            retrofit.create(NewsAPI::class.java)
        }
    }

}