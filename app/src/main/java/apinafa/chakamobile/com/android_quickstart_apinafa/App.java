package apinafa.chakamobile.com.android_quickstart_apinafa;

import android.app.Application;
import android.util.Log;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

import static apinafa.chakamobile.com.android_quickstart_apinafa.tools.OkHttpUtils.getUnsafeOkHttpClient;

public class App extends Application {

    public static Retrofit payMobileAdapter;


    @Override
    public void onCreate() {
        super.onCreate();

        //Log.d(Const.GENERAL_TAG, "URL_APINAFA : " +BuildConfig.URL_APINAFA);

        final OkHttpClient okHttpClient = new OkHttpClient();


        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();

        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpClient.interceptors().add(interceptor);

        okHttpClient.interceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                // Customize the request
                Request request = original.newBuilder()
                        .header("Cache-Control", "no-cache")
                        .header("Accept-Language", Locale.getDefault().getLanguage())
                        .method(original.method(), original.body())
                        .build();
                Response response = chain.proceed(request);
                return response;
            }
        });

        okHttpClient.setReadTimeout(30, TimeUnit.SECONDS);
        okHttpClient.setConnectTimeout(30, TimeUnit.SECONDS);



        payMobileAdapter = new Retrofit.Builder()
                .baseUrl(BuildConfig.URL_APINAFA)
                 // Ici désactive la verification du certificat ssl
                .client(getUnsafeOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }
}
