package com.example.gaurav.joveo_megathon;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ibm.watson.developer_cloud.conversation.v1.ConversationService;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageRequest;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;
import com.ibm.watson.developer_cloud.http.ServiceCallback;
import com.linkedin.platform.APIHelper;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;

import org.json.JSONException;
import org.json.JSONObject;

public class ChatBot extends AppCompatActivity {

    String message="Hi";

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_bot);

        final ConversationService myCoversationServices = new ConversationService(                  //Initializing Chat-Bot
                "2018-08-08",
                getString(R.string.username),
                getString(R.string.password)
        );

        final TextView conversation = findViewById(R.id.coversation);
        final EditText userInput = findViewById(R.id.user_input);
        final ImageView buttonSend = findViewById(R.id.chat_send_btn);

        buttonSend.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final String inputText = userInput.getText().toString();
                if(inputText.equals("yes")|| inputText.equals("yup") || inputText.equals("sure")){
                    print("Loading...");


                }
                conversation.append(
                        android.text.Html.fromHtml("<p><Strong><font color=#cc0029>You:<Strong> " + inputText + "</font></p>")       //Extracting the User input
                );

                userInput.setText("");                                                       //Resetting the input area

                final MessageRequest request = new MessageRequest.Builder()                           //Starting connection with IBM Waston and passing the input.
                        .inputText(inputText)
                        .build();

                myCoversationServices
                        .message(getString(R.string.workspace), request)                        //Sending and receiving using Onresponse function.
                        .enqueue(new ServiceCallback<MessageResponse>() {
                            @Override
                            public void onResponse(MessageResponse response) {

                                final String outputText = response.getText().get(0);

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        conversation.append(

                                                android.text.Html.fromHtml("<p><Strong>Bot:</Strong> " + outputText + "</p>")    //Displaying the ouyput to user.
                                        );
                                    }
                                });

                                if (response.getEntities().get(0).getEntity().equals("surity") || response.getIntents().get(0).getIntent().equals("okay")){
                                    MessageRequest mrequest = new MessageRequest.Builder()                           //Starting connection with IBM Waston and passing the input.
                                            .inputText(outputText)
                                            .build();

                                    myCoversationServices
                                            .message(String.valueOf(R.string.workspace),mrequest)
                                            .enqueue(new ServiceCallback<MessageResponse>() {
                                                @Override
                                                public void onResponse(MessageResponse response) {
                                                    final String outputText = response.getText().get(0);
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            conversation.append(

                                                                    android.text.Html.fromHtml("<p><b>Bot:</b> " + outputText + "</p")    //Displaying the output to user.
                                                            );
                                                        }
                                                    });
                                                }

                                                @Override
                                                public void onFailure(Exception e) {

                                                }
                                            });
                                }

                            }

                            @Override
                            public void onFailure(Exception e) {}
                        });

                return false;
            }
        });

    }

    public void print(String s) {
        Toast.makeText(this,s, Toast.LENGTH_LONG).show();
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

                    message = stringBuilder.toString();

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


}
