package com.penningtonb.powercast;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

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

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserFragment extends Fragment {

    Activity mActivity;
    Button logoutButton;
    Button deleteUserButton;
    Button popupConfirm;
    Button popupCancel;
    TextView currentUser;
    View mView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "uid";
    private static final String ARG_PARAM2 = "email";

    // TODO: Rename and change types of parameters
    private String uid;
    private String email;

    public UserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param uid Parameter 1.
     * @param email Parameter 2.
     * @return A new instance of fragment UserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserFragment newInstance(String uid, String email) {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, uid);
        args.putString(ARG_PARAM2, email);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            uid = getArguments().getString(ARG_PARAM1);
            email = getArguments().getString(ARG_PARAM2);
        }

        mActivity = this.getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_user, container, false);

        currentUser = mView.findViewById(R.id.user_current_text);
        currentUser.setText(email);
        logoutButton = mView.findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout(mView);
            }
        });

        deleteUserButton = mView.findViewById(R.id.deleteUserButton);
        deleteUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteUser(view);
            }
        });

        return mView;
    }

    public void logout(View view) {

        // store logged in status in sharedprefs
        SharedPreferences sharedPref = mActivity.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(getString(R.string.is_logged_in), false);
        editor.putString(getString(R.string.current_uid), null);
        editor.putString(getString(R.string.current_user_email), null);
        editor.apply();

        // call /logout with Volley
//        RequestQueue queue = Volley.newRequestQueue(view.getContext());
//        String url = "https://powercast-server.herokuapp.com/logout";
//        final String finalUsername = email;
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                System.out.println(response);
//
//                // Throw error if needed
//                if (response.equals("error")) {
//                    // error handling here
//                    System.out.println("Error: Unable to logout");
//                }
//
//                // Log user out, refresh sharedPrefs
//                else {
//                    // store logged in status in sharedprefs
//                    SharedPreferences sharedPref = mActivity.getSharedPreferences("prefs", Context.MODE_PRIVATE);
//                    SharedPreferences.Editor editor = sharedPref.edit();
//                    editor.putBoolean(getString(R.string.is_logged_in), false);
//                    editor.putString(getString(R.string.current_uid), null);
//                    editor.putString(getString(R.string.current_user_email), null);
//                    editor.apply();
//
//                    // go to login activity now
//                    Intent intent = new Intent(mView.getContext(), LoginActivity.class);
//                    startActivity(intent);
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.d("USER ACTIVITY", "Request failed!");
//            }
//        }) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("username", finalUsername);
//                return params;
//            }
//        };
//
//        queue.add(stringRequest);

        // send user to LoginActivity
        Intent intent = new Intent(this.getContext(), LoginActivity.class);
        startActivity(intent);
    }

    public void deleteUser(View view) {
        // display confirmation dialog ("Are you sure you want to delete your account? This action is irreversable. Y/N")
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.delete_user_popup, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);


        popupConfirm = popupView.findViewById(R.id.deleteUserYesButton);
        popupCancel = popupView.findViewById(R.id.deleteUserNoButton);

        // show the popup window
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        // dismiss the popup window when cancel
        popupCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });

        // call /delete_user with Volley
        popupConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                // use Volley to make a network request to our middleware, then parse the JSON result
                RequestQueue queue = Volley.newRequestQueue(view.getContext());
                String url = "https://powercast-server.herokuapp.com/delete_user";

                final String finalUid = uid;
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);

                        // Throw error if needed
                        if (response.equals("error")) {
                            // error handling here
                            System.out.println("Error: Unable to delete account");
                        }

                        // Log user out, refresh sharedPrefs
                        else {
                            // store logged in status in sharedprefs
                            SharedPreferences sharedPref = mActivity.getSharedPreferences("prefs", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putBoolean(getString(R.string.is_logged_in), false);
                            editor.putString(getString(R.string.current_uid), null);
                            editor.putString(getString(R.string.current_user_email), null);
                            editor.apply();

                            popupWindow.dismiss();

                            // go to login activity now
                            Intent intent = new Intent(mView.getContext(), LoginActivity.class);
                            startActivity(intent);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("USER ACTIVITY", "Request failed!");
                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("uid", finalUid);
                        return params;
                    }
                };

                queue.add(stringRequest);
            }
        });



    }
}