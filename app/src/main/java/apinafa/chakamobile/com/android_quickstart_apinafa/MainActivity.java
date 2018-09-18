package apinafa.chakamobile.com.android_quickstart_apinafa;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.ResponseBody;

import java.util.Random;

import apinafa.chakamobile.com.android_quickstart_apinafa.rest.GetAuthorizationService;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

import static apinafa.chakamobile.com.android_quickstart_apinafa.App.payMobileAdapter;
import static apinafa.chakamobile.com.android_quickstart_apinafa.tools.OkHttpUtils.getUnsafeOkHttpClient;

public class MainActivity extends AppCompatActivity {


    GetAuthorizationService getAuthService;


    private ProgressDialog mProgressDialog;

    private Callback<Response> initCallback = new Callback<Response>() {

        @Override
        public void onResponse(Response<Response> response, Retrofit retrofit) {

            cancelProgress();
        }

        @Override
        public void onFailure(Throwable t) {

            cancelProgress();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Retrofit payMobileAdapter1 = new Retrofit.Builder()
                .baseUrl(BuildConfig.URL_APINAFA)
                .client(getUnsafeOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final TextView txt_ref=(TextView)findViewById(R.id.ref_price);
        final TextView txt_price=(TextView)findViewById(R.id.amount_price);
        setSupportActionBar(toolbar);
        CreateAFakeReference();

        getAuthService = payMobileAdapter1.create(GetAuthorizationService.class);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int choice) {
                switch (choice) {
                    case DialogInterface.BUTTON_NEUTRAL:
                        Log.d(Const.GENERAL_TAG, "onClick: BUTTON_NEUTRAL ..Closing Dialog");
                        break;
                }
            }
        };

        ImageButton btn_pay =(ImageButton)findViewById(R.id.btn_pay);
        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(Const.GENERAL_TAG, "onClick: btn_pay ");


                //Extends the webview and return true or onCheckIsTextEditor
                final WebView webView = new WebView(getApplicationContext()) {
                    @Override
                    public boolean onCheckIsTextEditor() {
                        return true;
                    }
                };

                WebSettings settings = webView.getSettings();
                settings.setJavaScriptEnabled(true);
                settings.setDomStorageEnabled(true);
                webView.setFocusableInTouchMode(true);
                //webView.requestFocus();
                webView.requestFocus(View.FOCUS_DOWN);
                webView.setFocusable(true);





                webView.setWebViewClient(new WebViewClient(){

                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        webView.loadUrl(url);
                        return true;
                    }

                    @Override
                    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                        // DOT CALL SUPER METHOD
                        //super.onReceivedSslError(view, handler, error);
                        handler.proceed();
                    }
                });


                //BUIL URL TO GO FOR BGFI USER AUTHENTIFICATION
                String urltogo=BuildConfig.URL_APINAFA+"/webapps/authorize?" +
                        "response_type=code" +
                        "&redirect_uri="+Const.Redirect_URI +
                        "&state="+txt_ref.getText().toString() +
                        "&client_id="+Const.CLIENT_ID +
                        "&scope="+txt_price.getText().toString();

                Log.d(Const.GENERAL_TAG+"GO", "urltogo : "+urltogo);



                webView.loadUrl(urltogo);
                webView.requestFocusFromTouch();

                // display the WebView in an AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setTitle("Paiement BGFI MOBILE")
                        .setView(webView)
                        .setOnCancelListener(new AlertDialog.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialogInterface) {
                                //Log.d(TAG, "onCancel: ");
                                //Toast.makeText(getApplicationContext(), "Hell", Toast.LENGTH_LONG).show();
                                CreateAFakeReference();


                            }
                        })
                        .setOnDismissListener(new AlertDialog.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                //Toast.makeText(getApplicationContext(), "Hell", Toast.LENGTH_LONG).show();
                                CreateAFakeReference();
                            }
                        })


                        //.setNeutralButton("Annulez le paiement", dialogClickListener)


                        .show();






                //showProgress("Chargement en cours...");
                //doGettingAuthorisation("code",Const.Redirect_URI, (String) txt_ref.getText(),Const.CLIENT_ID, String.valueOf(txt_price.getText()));


            }
        });


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void CreateAFakeReference() {
        TextView tv1 = (TextView) findViewById(R.id.ref_price);
        TextView amount_price = (TextView) findViewById(R.id.amount_price);
        final Random random = new Random();
        int rand_ref = random.nextInt(20000)+1;
        int rand_price = random.nextInt(10000)+1;
        tv1.setText("REF"+rand_ref);
        amount_price.setText(""+rand_price);
    }


    //Get Authorisation
    private void doGettingAuthorisation(String response_type, String redirect_uri,String state,String client_id,String scope) {
        Log.d(Const.GENERAL_TAG, "### doGettingAuthorisation ### ");
        Log.d(Const.GENERAL_TAG, "response_type :"+response_type);
        Log.d(Const.GENERAL_TAG, "redirect_uri :"+redirect_uri);
        Log.d(Const.GENERAL_TAG, "state :"+state);
        Log.d(Const.GENERAL_TAG, "client_id :"+client_id);
        Log.d(Const.GENERAL_TAG, "scope :"+scope);
        Log.d(Const.GENERAL_TAG, "### doGettingAuthorisation ### ");

        Call<ResponseBody> call = getAuthService.getAuthorisationCode(response_type,redirect_uri,state,client_id,scope);

        call.enqueue(new Callback<ResponseBody>() {


            @Override
            public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                Log.d(Const.GENERAL_TAG, "Response: Body "+response.body().toString());
                Log.d(Const.GENERAL_TAG, "Response: Code : "+response.code());
                Log.d(Const.GENERAL_TAG, "Response: Header : "+response.headers());

                WebView webView = new WebView(MainActivity.this);

                webView.loadUrl("https://cg-api.bgfimobileservices.com/webapps/authorize?");

                // display the WebView in an AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Paiement BGFI MOBILE")
                        .setView(webView)
                        //.setNeutralButton("Annulez le paiement", dialogClickListener)
                        .show();

                cancelProgress();



            }

            @Override
            public void onFailure(Throwable t) {
                Log.d(Const.GENERAL_TAG, "Error");
                cancelProgress();

            }
        });
        //call.enqueue(Re);
        //init.enqueu
    }

    private void showProgress(String message) {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(message);
        mProgressDialog.setCancelable(true);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();
    }

    private void cancelProgress() {
        if (mProgressDialog != null) {
            mProgressDialog.cancel();
        }
    }


}
