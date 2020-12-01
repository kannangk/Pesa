package finance.pesa.sdk.Api

import finance.pesa.sdk.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers

object ConversfinancenApi {

    private var servicesApiInterface: finance.pesa.sdk.Api.ConversfinancenApi.ServicesApiInterface? = null

    fun build(): finance.pesa.sdk.Api.ConversfinancenApi.ServicesApiInterface? {

        var builder: Retrofit.Builder = Retrofit.Builder()
            .baseUrl(Constants.CONVERSION_URL)
            .addConverterFactory(GsonConverterFactory.create())

        var httpClient: OkHttpClient.Builder = OkHttpClient.Builder()
        httpClient.addInterceptor(finance.pesa.sdk.Api.ConversfinancenApi.interceptor())

        var retrofit: Retrofit = builder.client(httpClient.build()).build()
        finance.pesa.sdk.Api.ConversfinancenApi.servicesApiInterface = retrofit.create(
            finance.pesa.sdk.Api.ConversfinancenApi.ServicesApiInterface::class.java
        )
        return finance.pesa.sdk.Api.ConversfinancenApi.servicesApiInterface as finance.pesa.sdk.Api.ConversfinancenApi.ServicesApiInterface
    }

    private fun interceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }

    interface ServicesApiInterface {
        //GET
       /* @Headers("Content-Type: applicatfinancen/json")
        @GET("/data/price?fsym=ETH&tsyms=USD")
        fun getUSDFromETH(): Call<ETHTOUSD>*/


    }
}