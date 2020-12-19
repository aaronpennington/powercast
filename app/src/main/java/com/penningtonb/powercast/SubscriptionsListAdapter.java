package com.penningtonb.powercast;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class SubscriptionsListAdapter extends RecyclerView.Adapter<SubscriptionsListAdapter.mRecyclerViewHolder> {

    private String uid;
    private ArrayList<DirectoryResponse> podcastList;
    Context mContext;

    public SubscriptionsListAdapter(String uid, ArrayList<DirectoryResponse> podcastList) {
        this.uid = uid;
        this.podcastList = podcastList;
    }

    @Override
    public int getItemViewType(final int position) {
        return R.layout.item_subscription;
    }

    @NonNull
    @Override
    public mRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        mContext = parent.getContext();
        return new mRecyclerViewHolder(view);
    }


    public class mRecyclerViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImage;
        private ConstraintLayout view;

        public mRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView.findViewById(R.id.itemSubs);
            itemImage = view.findViewById(R.id.subItemImage);
        }

        public ImageView getItemImage() { return itemImage; }
    }

    @Override
    public void onBindViewHolder(@NonNull mRecyclerViewHolder holder, final int position) {
        Picasso.get().load(podcastList.get(position).getBody().getImage()).into(holder.getItemImage());
        holder.getItemImage().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentJump(podcastList.get(position).getBody().getId());
            }
        });
    }

    private void fragmentJump(String podcast_id) {
        PodcastDirectoryFragment pdFragment = PodcastDirectoryFragment.newInstance(uid, podcast_id, true);
        switchContent(R.id.search_container, pdFragment, podcast_id);
    }

    public void switchContent(int id, Fragment fragment, String podcast_id) {
        if (mContext == null) {
            Log.d("SubsListAdapter", "Null context.");
            return;
        }
        if (mContext instanceof MainActivity) {
            Log.d("SubsListAdapter", "Switching fragments...");
            MainActivity mainActivity = (MainActivity) mContext;
            Fragment frag = fragment;
            mainActivity.switchContent(id, frag, true);
        }
    }

    @Override
    public int getItemCount() {
        if (podcastList == null)
            return 0;
        else
            return podcastList.size();
    }


}
