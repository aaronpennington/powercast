package com.penningtonb.powercast;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

class SearchResultListAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

    private ArrayList<Podcast> titlesList;
    private String uid;
    Context mContext;

    public SearchResultListAdapter(ArrayList<Podcast> titlesList, String uid) {
        this.titlesList = titlesList;
        this.uid = uid;
    }

    @Override
    public int getItemViewType(final int position) {
        return R.layout.search_row;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        mContext = parent.getContext();
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, final int position) {
        holder.getRowText().setText(titlesList.get(position).getTitle_original());
        holder.getRowButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentJump(titlesList.get(position).getId());
            }
        });
        Picasso.get().load(titlesList.get(position).getImage()).into(holder.getRowImage());
    }

    private void fragmentJump(String podcast_id) {
        PodcastDirectoryFragment pdFragment = PodcastDirectoryFragment.newInstance(uid, podcast_id, false);
        switchContent(R.id.search_container, pdFragment, podcast_id);
    }

    public void switchContent(int id, Fragment fragment, String podcast_id) {
        if (mContext == null) {
            Log.d("SEARCHRESULTLISTADAPTER", "Null context.");
            return;
        }
        if (mContext instanceof MainActivity) {
            Log.d("SEARCHRESULTLISTADAPTER", "Switching fragments...");
            MainActivity mainActivity = (MainActivity) mContext;
            Fragment frag = fragment;
            mainActivity.switchContent(id, frag, true);
        }
    }

    @Override
    public int getItemCount() {
        if (titlesList == null)
            return 0;
        else
            return titlesList.size();
    }
}
