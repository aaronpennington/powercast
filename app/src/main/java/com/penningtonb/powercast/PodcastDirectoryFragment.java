package com.penningtonb.powercast;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PodcastDirectoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class PodcastDirectoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private DirectoryResultListAdapter resultListAdapter;
    private TextView titleText;
    private TextView descText;
    private ArrayList<DirectoryEpisode> episode_titles;
    private ImageView podcastImage;
    private FloatingActionButton subscribeButton;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "uid";
    private static final String ARG_PARAM2 = "podcast_id";

    // TODO: Rename and change types of parameters
    private String uid;
    private String podcast_id;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param uid User's unique id from Firebase.
     * @param podcast_id Parameter 2.
     * @return A new instance of fragment PodcastDirectoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PodcastDirectoryFragment newInstance(String uid, String podcast_id) {
        PodcastDirectoryFragment fragment = new PodcastDirectoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, uid);
        args.putString(ARG_PARAM2, podcast_id);
        fragment.setArguments(args);
        return fragment;
    }

    public PodcastDirectoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            uid = getArguments().getString(ARG_PARAM1);
            podcast_id = getArguments().getString(ARG_PARAM2);
        }

        episode_titles = new ArrayList<DirectoryEpisode>();

        resultListAdapter = new DirectoryResultListAdapter(episode_titles);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_podcast_directory, container, false);

        // create the recycler view and populate with data
        recyclerView = view.findViewById(R.id.pDirEpisodesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(resultListAdapter);

        titleText = (TextView) view.findViewById(R.id.pDirTitleTextView);
        descText = (TextView) view.findViewById(R.id.pDirDescriptionTextView);
        podcastImage = (ImageView) view.findViewById(R.id.pDirImageView);
        subscribeButton = (FloatingActionButton) view.findViewById(R.id.subscribeButton);

        // make Volley request
        getPodcast(podcast_id);

        // set click listener for subscribe button
        subscribeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subscribeToPodcast(podcast_id);
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    public void getPodcast(String podcast_id) {

        // encode podcast_id (may be redundant??)
        String query = null;
        try {
            query = URLEncoder.encode(podcast_id, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // use Volley to make a network request to our middleware, then parse the JSON result
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = "https://powercast-server.herokuapp.com/podcast/" + query;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                DirectoryResponse dirResponse = gson.fromJson(response, DirectoryResponse.class);
                String podcast_title = dirResponse.getBody().getTitle();
                String podcast_desc = dirResponse.getBody().getDescription();

                titleText.setText(podcast_title);
                descText.setText(podcast_desc);
                Picasso.get().load(dirResponse.getBody().getImage()).into(podcastImage);

                for (int i = 0; i < 10; i++) {
                    episode_titles.add(dirResponse.getBody().getEpisodes().get(i));
                }

                // Update the RecyclerView
                resultListAdapter.notifyDataSetChanged();
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

    private void subscribeToPodcast(final String podcast_id) {
        //make a Volley call to our API
        // use Volley to make a network request to our middleware, then parse the JSON result
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = "https://powercast-server.herokuapp.com/subscribe/";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // make a Toast here? or some feedback indication
                Context context = getContext();
                CharSequence text = "Subscribed!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
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
                params.put("uid", uid);
                params.put("pid", podcast_id);
                return params;
            }
        };

        queue.add(stringRequest);
    }
}