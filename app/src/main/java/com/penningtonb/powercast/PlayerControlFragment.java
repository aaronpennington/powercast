package com.penningtonb.powercast;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.util.Util;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlayerControlFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlayerControlFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "audio_link";

    // TODO: Rename and change types of parameters
    private String audio_link;

    private PlayerView playerView;
    private SimpleExoPlayer player;

    private boolean playWhenReady = false;
    private int currentWindow = 0;
    private long playbackPosition = 0;

    public PlayerControlFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment PlayerControlFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlayerControlFragment newInstance(String param1) {
        PlayerControlFragment fragment = new PlayerControlFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            audio_link = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_player_control, container, false);

        playerView = rootView.findViewById(R.id.fragment_player_control);

        // prevent the controls from disappearing
        playerView.setControllerShowTimeoutMs(0);
        playerView.setControllerHideOnTouch(false);

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT >= 24) {
            initializePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT >= 24) {
            releasePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT < 24) {
            releasePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //hideSystemUi();
        if ((Util.SDK_INT < 24 || player == null)) {
            initializePlayer();
        }
    }

    /* This makes the app fullscreen, which would be great for a video playing app, but since
       mine is meant for audio, I'm not going to have this enabled right now.
     */
    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    private void releasePlayer() {
        if (player != null) {
            playWhenReady = player.getPlayWhenReady();
            playbackPosition = player.getContentPosition();
            currentWindow = player.getCurrentWindowIndex();
            player.release();
            player = null;
        }
    }

    private void initializePlayer() {
        // Streaming setup
//    if (player == null) {
//      DefaultTrackSelector trackSelector = new DefaultTrackSelector(this);
//      trackSelector.setParameters(trackSelector.buildUponParameters().setMaxVideoSizeSd());
//      player = new SimpleExoPlayer.Builder(this).setTrackSelector(trackSelector).build();
//    }

        player = new SimpleExoPlayer.Builder(getActivity()).build();
        playerView.setPlayer(player);

        // Add media item
        if (!audio_link.equals("null")) {
            Uri myUri = Uri.parse(audio_link);
            MediaItem mediaItem = MediaItem.fromUri(myUri);
            //MediaItem mediaItem = new MediaItem.Builder().setUri(getString(R.string.media_url_dash)).setMimeType(MimeTypes.APPLICATION_MPD).build();
            player.setMediaItem(mediaItem);
            playWhenReady = true;
        }


        // Supply saved state information to the player
        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playbackPosition);
        player.prepare();
    }
}