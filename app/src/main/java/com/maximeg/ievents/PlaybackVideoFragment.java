package com.maximeg.ievents;

import android.net.Uri;
import android.os.Bundle;

import androidx.leanback.app.VideoSupportFragment;
import androidx.leanback.app.VideoSupportFragmentGlueHost;
import androidx.leanback.media.MediaPlayerAdapter;
import androidx.leanback.media.PlaybackTransportControlGlue;
import androidx.leanback.widget.PlaybackControlsRow;

public class PlaybackVideoFragment extends VideoSupportFragment {

    private PlaybackTransportControlGlue<MediaPlayerAdapter> mTransportControlGlue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getActivity() != null){
            Event event = (Event) getActivity().getIntent().getSerializableExtra(getString(R.string.intent_event));

            VideoSupportFragmentGlueHost glueHost = new VideoSupportFragmentGlueHost(PlaybackVideoFragment.this);

            MediaPlayerAdapter playerAdapter = new MediaPlayerAdapter(getActivity());
            playerAdapter.setRepeatAction(PlaybackControlsRow.RepeatAction.INDEX_NONE);

            mTransportControlGlue = new PlayerControls(getActivity(), playerAdapter, event.getState());
            mTransportControlGlue.setHost(glueHost);
            mTransportControlGlue.setTitle(event.getDate());
            mTransportControlGlue.setSubtitle(event.getDesc());
            mTransportControlGlue.playWhenPrepared();
            playerAdapter.setDataSource(Uri.parse(event.getLink()));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mTransportControlGlue != null) {
            mTransportControlGlue.pause();
        }
    }
}