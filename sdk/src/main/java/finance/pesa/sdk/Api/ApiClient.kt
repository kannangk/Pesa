package finance.pesa.sdk.Api

import finance.pesa.sdk.Model.*
import finance.pesa.sdk.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


object ApiClient {

    private var servicesApiInterface: ServicesApiInterface? = null

    fun build(): ServicesApiInterface? {

        var builder: Retrofit.Builder = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())

        var httpClient: OkHttpClient.Builder = OkHttpClient.Builder()
        httpClient.addInterceptor(interceptor())

        var retrofit: Retrofit = builder.client(httpClient.build()).build()
        servicesApiInterface = retrofit.create(
            ServicesApiInterface::class.java
        )
        return servicesApiInterface as ServicesApiInterface
    }

    private fun interceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }

    interface ServicesApiInterface {

        //POST

        @Headers("Content-Type: application/json")
        @POST("/api/device/findDevice")
        fun findDevice(@Body body: RequestBody): Call<AvailableDevice>

        @Headers("Content-Type: application/json")
        @POST("/api/device/status")
        fun getDeviceStatus(@Body body: RequestBody, @HeaderMap headers: Map<String, String>): Call<DeviceStatus>

        @Headers("Content-Type: application/json")
        @POST("/api/device/register")
        fun registerDevice(@Body body: RequestBody, @HeaderMap headers: Map<String, String>): Call<CredentialStatus>

        @Headers("Content-Type: application/json")
        @POST("/api/device/auth")
        fun loginDevice(@Body body: RequestBody, @HeaderMap headers: Map<String, String>): Call<CredentialStatus>

        @Headers("Content-Type: application/json")
        @POST("/api/pesa_min/markets")
        fun getAccountSummary(@Body body: RequestBody, @HeaderMap headers: Map<String, String>): Call<InvestDash>

        @Headers("Content-Type: application/json")
        @POST("/api/message/accept_to_block")
        fun blockContact(@Body body: RequestBody, @HeaderMap headers: Map<String, String>): Call<ResponseBody>

        @Headers("Content-Type: application/json")
        @POST("/api/message/unscreen_accept")
        fun setAcceptScreen(@Body body: RequestBody, @HeaderMap headers: Map<String, String>): Call<ResponseBody>

        @Headers("Content-Type: application/json")
        @POST("/api/message/unscreen_decline")
        fun setDeclineScreen(@Body body: RequestBody, @HeaderMap headers: Map<String, String>): Call<ResponseBody>

        @Headers("Content-Type: application/json")
        @POST("/api/EPN/checkEPNAuthorizedStatus")
        fun checkEPNAuthStatus(@Body body: RequestBody, @HeaderMap headers: Map<String, String>): Call<EPNAuthData>

        @Headers("Content-Type: application/json")
        @POST("/api/EPN/verifyEPN")
        fun verifyEPN(@Body body: RequestBody, @HeaderMap headers: Map<String, String>): Call<ResponseStatus>

        @Headers("Content-Type: application/json")
        @POST("/api/device/send_otp")
        fun sendOTP(@Body body: RequestBody, @HeaderMap headers: Map<String, String>): Call<ResponseStatus>

        @Headers("Content-Type: application/json")
        @POST("/api/device/verifiy_otp")
        fun verifyOTP(@Body body: RequestBody, @HeaderMap headers: Map<String, String>): Call<ResponseStatus>

        @Headers("Content-Type: application/json")
        @POST("/api/EPN/createEPN")
        fun createEPNNumber(@Body body: RequestBody, @HeaderMap headers: Map<String, String>): Call<EPNCreateStatus>

        @Headers("Content-Type: application/json")
        @POST("/api/paymentGateway/creteTransaction")
        fun createTransaction(@Body body: RequestBody, @HeaderMap headers: Map<String, String>): Call<ResponseStatus>

        @Headers("Content-Type: application/json")
        @POST("/api/paymentGateway/updateTransaction")
        fun updateTransaction(@Body body: RequestBody, @HeaderMap headers: Map<String, String>): Call<ResponseStatus>


        //GET
        /* @Headers("Content-Type: application/json")
         @GET("/data/price?fsym=ETH&tsyms=USD")
         fun getUSDFromETH(): Call<ETHTOUSD>*/

        @Headers("Content-Type: application/json")
        @GET("/api/getMarkets")
        fun getAllMarkets(@HeaderMap headers: Map<String, String>): Call<BasicMarkets>

        @Headers("Content-Type: application/json")
        @GET("/api/paymentGateway/getTransaction")
        fun getAllActivities(@HeaderMap headers: Map<String, String>): Call<ActivitiesData>

    }
}