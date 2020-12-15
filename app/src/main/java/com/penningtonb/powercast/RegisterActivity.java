package com.penningtonb.powercast;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText emailText;
    EditText passwordText;
    EditText passwordConfirmText;
    Button registerButton;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        View view = findViewById(R.id.registerContainer);
        emailText = view.findViewById(R.id.email);
        passwordText = view.findViewById(R.id.passwordRegister);
        passwordConfirmText = view.findViewById(R.id.passwordConfirm);
        registerButton = view.findViewById(R.id.register2);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String message = intent.getStringExtra(LoginActivity.EXTRA_MESSAGE);
        emailText.setText(message);

        mContext = this;

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get user input
                String password = null;
                String username = emailText.getText().toString();
                if (passwordsMatch(passwordText.getText().toString(), passwordConfirmText.getText().toString()))
                    password = passwordText.getText().toString();

                // use Volley to make a network request to our middleware, then parse the JSON result
                RequestQueue queue = Volley.newRequestQueue(view.getContext());
                String url = "https://powercast-server.herokuapp.com/register";

                final String finalPassword = password;
                final String finalUsername = username;
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // if successful, login and switch to main activity
                        // send user to LoginActivity
                        Intent intent = new Intent(mContext, LoginActivity.class);
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("LOGIN ACTIVITY", "Request failed!");
                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("email", finalUsername);
                        params.put("password", finalPassword);
                        return params;
                    }
                };

                queue.add(stringRequest);
            }
        });
    }

    private boolean passwordsMatch(String password, String confirm) {
        return password.equals(confirm);
    }
}