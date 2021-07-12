package com.penningtonb.powercast;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class ResetPassword extends AppCompatActivity {

    private EditText emailPrompt;
    private Button confirm1;
    private Button cancel1;
    private Button confirm2;
    private Button cancel2;
    private EditText codePrompt;
    private EditText newPassword1;
    private EditText newPassword2;
    private TextView emailSentMessage;
    private TextView errorMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        View view = findViewById(R.id.resetPasswordView);

        emailPrompt = (EditText) findViewById(R.id.emailPromptText);
        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String message = intent.getStringExtra(LoginActivity.EXTRA_MESSAGE);
        emailPrompt.setText(message);

        confirm1 = (Button) findViewById(R.id.confirmButton1);
        cancel1 = (Button) findViewById(R.id.cancelButton1);
        confirm2 = (Button) findViewById(R.id.confirmButton2);
        cancel2 = (Button) findViewById(R.id.cancelButton2);
        codePrompt = (EditText) findViewById(R.id.verifyCodePromptText);
        newPassword1 = (EditText) findViewById(R.id.newPasswordPrompt1);
        newPassword2 = (EditText) findViewById(R.id.newPasswordPrompt2);
        emailSentMessage = (TextView) findViewById(R.id.emailSentMessage);
        errorMessage = (TextView) findViewById(R.id.passwordResetErrorText);

        // This is the "Confirm" button that will send an email to the users address.
        confirm1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // volley request to /request_password_reset
                RequestQueue queue = Volley.newRequestQueue(view.getContext());
                String url = "https://powercast-server.herokuapp.com/request_password_reset";

                final String finalUsername = emailPrompt.getText().toString();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        if (response.equals("error")) {
                            // display appropriate error message
                            System.out.println("INVALID EMAIL");
                            errorMessage.setVisibility(View.VISIBLE);
                            errorMessage.setText("ERROR: Invalid email.");
                        }
                        else {
                            // make visible the rest of the form
                            emailSentMessage.setVisibility(View.VISIBLE);
                            newPassword1.setVisibility(View.VISIBLE);
                            newPassword2.setVisibility(View.VISIBLE);
                            codePrompt.setVisibility(View.VISIBLE);
                            cancel2.setVisibility(View.VISIBLE);
                            confirm2.setVisibility(View.VISIBLE);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("RESET PASSWORD ACTIVITY", "Request failed!");
                        errorMessage.setVisibility(View.VISIBLE);
                        errorMessage.setText("ERROR: Network error. Please try again.");
                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("email", finalUsername);
                        return params;
                    }
                };

                queue.add(stringRequest);
            }
        });

    }

    // compare two strings to make sure they match
    private boolean passwordsMatch(String password, String confirm) {
        return password.equals(confirm);
    }
}