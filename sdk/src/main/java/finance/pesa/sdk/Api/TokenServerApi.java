package finance.pesa.sdk.Api;

import finance.pesa.sdk.Model.TokenServerResponse;
import finance.pesa.sdk.Model.VerifyModel;
import finance.pesa.sdk.utils.Constants;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

import static finance.pesa.sdk.utils.Constants.X_Authy_API_Key;


public interface TokenServerApi {

    //Twilio OTP verification
    @POST("/verify/token")
    @FormUrlEncoded
    Call<TokenServerResponse> getToken(@Field("phone_number") String phoneNumber);

    @POST("verification/start")
    @Headers("X-Authy-API-Key: "+X_Authy_API_Key)
    @FormUrlEncoded
    Call<TokenServerResponse> getOTP(@Field("via") String via,
                                     @Field("country_code") String country_code,
                                     @Field("code_length") String code_length,
                                     @Field("locale") String locale,
                                     @Field("phone_number") String phoneNumber);

    @GET("verification/check?a=a")
    @Headers("X-Authy-API-Key: "+X_Authy_API_Key)
    Call<VerifyModel> getVerify(@Query("country_code") String country_code,
                                @Query("verification_code") String verification_code,
                                @Query("phone_number") String phoneNumber);



}

