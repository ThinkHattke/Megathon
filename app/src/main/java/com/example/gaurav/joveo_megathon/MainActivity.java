package com.example.gaurav.joveo_megathon;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.linkedin.platform.APIHelper;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;

public class MainActivity extends AppCompatActivity {

    Button LinkedIn;
    TextView skip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinkedIn = findViewById(R.id.linkedin);

        LinkedIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                init();
            }
        });

        skip = findViewById(R.id.skip);

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ChatBot.class);
                startActivity(i);
                finish();
            }
        });


//
//        computePakageHash(); //Have upfated on linkedin developer portal.
        init();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Add this line to your existing onActivityResult() method
        LISessionManager.getInstance(getApplicationContext()).onActivityResult(this, requestCode, resultCode, data);
    }

    // Build the list of member permissions our LinkedIn session requires
    private static Scope buildScope() {
        return Scope.build(Scope.R_BASICPROFILE, Scope.W_SHARE, Scope.R_EMAILADDRESS);
    }

    private void init() {
        print("Already logged In");
        handleLogin();
    }

    private void handleLogin() {
        LISessionManager.getInstance(getApplicationContext()).init(this, buildScope(), new AuthListener() {
            @Override
            public void onAuthSuccess() {
                // Authentication was successful.  You can now do
                // other calls with the SDK.


                fetchPersonalInfo();
            }

            @Override
            public void onAuthError(LIAuthError error) {
                Log.e("GAURAV", error.toString());
            }
        }, true);
    }

    private void handleLogout(){
        LISessionManager.getInstance(getApplicationContext()).clearSession();
    }

    private void fetchPersonalInfo() {
        String url = "https://api.linkedin.com/v1/people/~:(id,first-name,last-name,picture-url,email-Address)?format=json";

        final APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
        apiHelper.getRequest(this, url, new ApiListener() {
            @Override
            public void onApiSuccess(ApiResponse apiResponse) {
                // Success!
                try {
                    JSONObject jsonObject = apiResponse.getResponseDataAsJson();
                    String firstName = jsonObject.getString("firstName");
                    String lastName = jsonObject.getString("lastName");
                    String pictureURL = jsonObject.getString("pictureUrl");
                    String emailAddress = jsonObject.getString("emailAddress");

                    //Picasso.with(getApplicationContext()).load(pictureURL).into(piv);

                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Resume - ");
                    stringBuilder.append("\n\n");
                    stringBuilder.append("First Name: " +firstName);
                    stringBuilder.append("\n\n");
                    stringBuilder.append("Last Name: " +lastName);
                    stringBuilder.append("\n\n");
                    stringBuilder.append("Email Address: " +emailAddress);
                    stringBuilder.append("\n\n");
                    stringBuilder.append("Education Details: " +emailAddress);

                    String message = stringBuilder.toString();

                    //txtDetails.setText(stringBuilder);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onApiError(LIApiError liApiError) {
                // Error making GET request!
                Log.e("XYZ", liApiError.getMessage());
            }
        });
    }


    public void print(String s) {
        Toast.makeText(this,s, Toast.LENGTH_LONG).show();
    }


}
