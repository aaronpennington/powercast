package com.penningtonb.powercast;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    private RecyclerView recyclerView;
    private SearchResultListAdapter searchResultListAdapter;
    private ArrayList<Podcast> titlesList;
    private EditText searchQuery;
    private Button searchButton;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "uid";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String uid;
    private String mParam2;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param uid A unique user id from Firebase.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String uid, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, uid);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            uid = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        titlesList = new ArrayList<>();
        searchResultListAdapter = new SearchResultListAdapter(titlesList, uid);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        // create the recycler view and populate with data
        recyclerView = view.findViewById(R.id.searchResultsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(searchResultListAdapter);

        searchQuery = view.findViewById(R.id.editTextSearchQuery);
        searchButton = view.findViewById(R.id.buttonSearch);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get user input
                String query = null;
                try {
                    query = URLEncoder.encode(searchQuery.getText().toString(), "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                // use Volley to make a network request to our middleware, then parse the JSON result
                RequestQueue queue = Volley.newRequestQueue(getActivity());
                String url = "https://powercast-server.herokuapp.com/search/podcast?q=" + query;

                StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        titlesList.clear();
                        Gson gson = new Gson();
                        SearchResponse searchResult = gson.fromJson(response, SearchResponse.class);
                        searchResult.getBody().setSearchType("podcast");
                        // searchResult.getBody().setResults();
                        int myCount = searchResult.getBody().getCount();

                        for (int i = 0; i < myCount; i++) {
                            titlesList.add(searchResult.getBody().getResults().get(i));
                        }

                        // Update the RecyclerView
                        searchResultListAdapter.notifyDataSetChanged();

                        // Debug stuff!
                        Log.d("SEARCH FRAGMENT", "count = " + myCount);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("SEARCH FRAGMENT", "Request failed!");
                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();

                        return params;
                    }
                };

                queue.add(stringRequest);
            }
        });

        // Inflate the layout for this fragment
        return view;
    }
}