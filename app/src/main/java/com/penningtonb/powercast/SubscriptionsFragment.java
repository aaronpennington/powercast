package com.penningtonb.powercast;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SubscriptionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SubscriptionsFragment extends Fragment {

    private RecyclerView recyclerView;
    private SubscriptionsListAdapter subscriptionsListAdapter;
    private ArrayList<DirectoryResponse> podcastList;
    private ArrayList<String> pidList;
    TextView emptySubText;
    private int spanCount;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "uid";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String uid;
    private String mParam2;

    public SubscriptionsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param uid Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SubscriptionsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SubscriptionsFragment newInstance(String uid, String param2) {
        SubscriptionsFragment fragment = new SubscriptionsFragment();
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

        pidList = new ArrayList<String>();
        podcastList = new ArrayList<DirectoryResponse>();
        spanCount = 3;
        subscriptionsListAdapter = new SubscriptionsListAdapter(uid, podcastList, spanCount);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_subscriptions, container, false);

        emptySubText = view.findViewById(R.id.emptySubsTextView);

        getUserSubscriptions(uid);


        // create the recycler view and populate with data
        recyclerView = view.findViewById(R.id.subRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), spanCount));
        recyclerView.setAdapter(subscriptionsListAdapter);

        return view;
    }

    private void getUserSubscriptions(final String uid) {
        // Volley call (/user_subs)
        // use Volley to make a network request to our middleware, then parse the JSON result
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = "https://powercast-server.herokuapp.com/user_subs";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // our response is a simple list of podcast ids.
                Gson gson = new Gson();
                Subscriptions subscriptions = gson.fromJson(response, Subscriptions.class);
                pidList.addAll(subscriptions.getPodcastList());
                pidToPodcast(pidList);
//                System.out.println(pidList.size());
                if (pidList.size() > 0) {
                    emptySubText.setVisibility(TextView.INVISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("SUBS FRAGMENT", "Request failed!");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("uid", uid);
                return params;
            }
        };

        queue.add(stringRequest);
    }

    public void pidToPodcast(ArrayList<String> pidList) {
        int n = pidList.size();
        for (int i = 0; i < n; i++) {
            // use Volley to make a network request to our middleware, then parse the JSON result
            RequestQueue queue = Volley.newRequestQueue(this.getContext());
            String url = "https://powercast-server.herokuapp.com/podcast/" + pidList.get(i);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    // our response is a simple list of podcast ids.
                    Gson gson = new Gson();
                    DirectoryResponse podcast = gson.fromJson(response, DirectoryResponse.class);
                    System.out.println(response);
                    System.out.println(podcast.getBody().getId());
                    podcastList.add(podcast);
                    // Update the RecyclerView
                    subscriptionsListAdapter.notifyDataSetChanged();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("SUBS FRAGMENT", "Request failed!");
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("uid", uid);

                    return params;
                }
            };

            queue.add(stringRequest);
        }
    }
}