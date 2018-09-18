package apinafa.chakamobile.com.android_quickstart_apinafa.rest;

import com.squareup.okhttp.ResponseBody;

import apinafa.chakamobile.com.android_quickstart_apinafa.rest.data.ResponseGettingAuthorisation;
import retrofit.Call;
import retrofit.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

public interface GetAuthorizationService {

    /*@FormUrlEncoded
    @POST("/webapps/authorize")
    Call<ResponseBase> init(@Body InitConnexionItem item);*/

    @FormUrlEncoded
    @POST("/webapps/authorize")
    Call<ResponseBody> getAuthorisationCode(
            @Field("response_type") String response_type,
            @Field("redirect_uri") String redirect_uri,
            @Field("state") String state,
            @Field("client_id") String client_id,
            @Field("scope") String scope
    );
}
