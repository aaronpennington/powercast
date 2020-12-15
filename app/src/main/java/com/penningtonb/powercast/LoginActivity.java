package com.penningtonb.powercast;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    Button loginButton;
    Button registerButton;
    EditText usernameText;
    EditText passwordText;
    public static String EXTRA_MESSAGE = "com.penningtonb.powercast.LOGIN";
    private String uid;
    private String user_email;
    Activity mContext;
    TextView errorMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        View view = findViewById(R.id.loginContainer);
        mContext = this;

        loginButton = view.findViewById(R.id.login);
        registerButton = view.findViewById(R.id.register);
        usernameText = view.findViewById(R.id.username);
        passwordText = view.findViewById(R.id.password);
        errorMessage = view.findViewById(R.id.login_error_message);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                // get user input
                String username = usernameText.getText().toString();
                String password = passwordText.getText().toString();

                // use Volley to make a network request to our middleware, then parse the JSON result
                RequestQueue queue = Volley.newRequestQueue(view.getContext());
                String url = "https://powercast-server.herokuapp.com/login";

                final String finalPassword = password;
                final String finalUsername = username;
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        Gson gson = new Gson();
                        User user = gson.fromJson(response, User.class);
                        if (user.getError() != null && user.getError().equals("auth/wrong-password")) {
                            // error handling here
                            System.out.println("WRONG PASSWORD");
                            errorMessage.setVisibility(View.VISIBLE);
                            errorMessage.setText("ERROR: Incorrect Password");
                        }
                        else if (user.getError() != null && user.getError().equals("auth/invalid-email")) {
                            // error handling here
                            System.out.println("INVALID EMAIL");
                            errorMessage.setVisibility(View.VISIBLE);
                            errorMessage.setText("ERROR: Invalid Email");
                        }
                        else if (user.getError() != null && user.getError().substring(0, 4).equals("auth")) {
                            // error handling here
                            System.out.println("MISC LOGIN ERROR");
                            errorMessage.setVisibility(View.VISIBLE);
                            errorMessage.setText("ERROR: Unable to login");
                        }
                        else {
                            uid = user.getUser().getUid();
                            user_email = user.getUser().getEmail();

                            // store logged in status in sharedprefs
                            SharedPreferences sharedPref = mContext.getSharedPreferences("prefs", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putBoolean(getString(R.string.is_logged_in), true);
                            editor.putString(getString(R.string.current_uid), uid);
                            editor.putString(getString(R.string.current_user_email), user_email);
                            editor.apply();

                            // go to main activity now
                            Intent intent = new Intent(view.getContext(), MainActivity.class);
                            intent.putExtra("USER_EMAIL", user_email);
                            intent.putExtra("USER_UID", uid);
                            startActivity(intent);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("LOGIN ACTIVITY", "Request failed!");
                        errorMessage.setVisibility(View.VISIBLE);
                        errorMessage.setText("ERROR: Network error. Please try again.");
                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("username", finalUsername);
                        params.put("password", finalPassword);
                        return params;
                    }
                };

                queue.add(stringRequest);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), RegisterActivity.class);
                String message = usernameText.getText().toString();
                intent.putExtra(EXTRA_MESSAGE, message);
                startActivity(intent);
            }
        });

        // Check if User is Already Logged In
        SharedPreferences sharedPref = this.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPref.getBoolean(getString(R.string.is_logged_in), false);
        System.out.println("USER IS LOGGED IN: " + isLoggedIn);
        if(isLoggedIn) {
            String current_uid = sharedPref.getString(getString(R.string.current_uid), "null");
            String current_user_email = sharedPref.getString(getString(R.string.current_user_email), "null");
            Intent intent = new Intent(view.getContext(), MainActivity.class);
            intent.putExtra("USER_EMAIL", current_user_email);
            intent.putExtra("USER_UID", current_uid);
            startActivity(intent);
        } else {
            view.setVisibility(View.VISIBLE);
        }

    }
}