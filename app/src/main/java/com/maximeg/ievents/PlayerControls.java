package com.maximeg.ievents;

import android.content.Context;

import androidx.leanback.media.MediaPlayerAdapter;
import androidx.leanback.media.PlaybackTransportControlGlue;
import androidx.leanback.widget.Action;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.PlaybackControlsRow;

public class PlayerControls extends PlaybackTransportControlGlue {
    private PlaybackControlsRow.SkipPreviousAction skipPreviousAction;
    private PlaybackControlsRow.FastForwardAction fastForwardAction;
    private PlaybackControlsRow.RewindAction rewindAction;

    private MediaPlayerAdapter mediaPlayerAdapter;

    private String eventState;

    public PlayerControls(Context context, MediaPlayerAdapter impl, String eventState) {
        super(context, impl);

        mediaPlayerAdapter = impl;

        this.eventState = eventState;

        rewindAction = new PlaybackControlsRow.RewindAction(getContext());
        fastForwardAction = new PlaybackControlsRow.FastForwardAction(getContext());
        skipPreviousAction = new PlaybackControlsRow.SkipPreviousAction(getContext());
    }

    @Override
    protected void onCreatePrimaryActions(ArrayObjectAdapter primaryActionsAdapter) {
        super.onCreatePrimaryActions(primaryActionsAdapter);

        if(!eventState.equals(getContext().getString(R.string.state_live))){
            primaryActionsAdapter.add(skipPreviousAction);
            primaryActionsAdapter.add(rewindAction);
            primaryActionsAdapter.add(fastForwardAction);
        }
    }

    @Override
    protected void onCreateSecondaryActions(ArrayObjectAdapter adapter) {
        super.onCreateSecondaryActions(adapter);
    }

    @Override
    public void onActionClicked(Action action) {
        if (action == rewindAction) {
            mediaPlayerAdapter.seekTo(getCurrentPosition() - 20 * 1000);
        } else if (action == fastForwardAction ) {
            mediaPlayerAdapter.seekTo(getCurrentPosition() + 20 * 1000);
        } else if (action == skipPreviousAction){
            mediaPlayerAdapter.seekTo(0);
            mediaPlayerAdapter.play();
        } else {
            super.onActionClicked(action);
        }
    }

}
