package com.penningtonb.powercast;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class DirectoryResultListAdapter extends RecyclerView.Adapter<DirectoryResultListAdapter.mRecyclerViewHolder> {

    private ArrayList<DirectoryEpisode> episodeList;
    private Context mContext;

    public DirectoryResultListAdapter(ArrayList<DirectoryEpisode> episodeList) {
        // maybe make this a list of Episode
        this.episodeList = episodeList;
    }

    public class mRecyclerViewHolder extends RecyclerView.ViewHolder {

        Button rowButton;
        TextView rowText;
        private ConstraintLayout view;

        public mRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView.findViewById(R.id.fragDir);
            rowText = (TextView) view.findViewById(R.id.pDirEpisodeTextView);
            rowButton = (Button) view.findViewById(R.id.pDirListenButton);
        }

        public View getView(){
            return view;
        }
        public TextView getRowText() {return rowText;}
        public Button getRowButton() {return rowButton;}
    }

    @Override
    public int getItemViewType(final int position) {
        return R.layout.directory_row;
    }

    @NonNull
    @Override
    public mRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        mContext = parent.getContext();
        return new mRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull mRecyclerViewHolder holder, final int position) {
        holder.getRowText().setText(episodeList.get(position).getTitle());
        holder.getRowButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start a new PlayerControlFragment with the selected audio
                fragmentJump(episodeList.get(position).getAudio());
            }
        });
    }

    @Override
    public int getItemCount() {
        if (episodeList == null)
            return 0;
        else
            return episodeList.size();
    }

    private void fragmentJump(String audio_link) {
        PlayerControlFragment pcFragment = PlayerControlFragment.newInstance(audio_link);
        switchContent(R.id.control_container, pcFragment, audio_link);
    }

    public void switchContent(int id, Fragment fragment, String audio_link) {
        if (mContext == null) {
            Log.d("SEARCHRESULTLISTADAPTER", "Null context.");
            return;
        }
        if (mContext instanceof MainActivity) {
            Log.d("SEARCHRESULTLISTADAPTER", "Switching fragments...");
            MainActivity mainActivity = (MainActivity) mContext;
            Bundle bundle = new Bundle();
            bundle.putString("audio_link", audio_link);
            Fragment frag = fragment;
            frag.setArguments(bundle);
            mainActivity.switchContent(id, frag, false);
        }
    }
}
