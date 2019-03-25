package com.maximeg.ievents;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.leanback.widget.ImageCardView;
import androidx.leanback.widget.Presenter;
import androidx.core.content.ContextCompat;

import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

public class CardPresenter extends Presenter {
    private static final int CARD_WIDTH = 500;
    private static final int CARD_HEIGHT = 250;
    private static int sSelectedBackgroundColor;
    private static int sDefaultBackgroundColor;
    private Activity mActivity;

    public CardPresenter(Activity mActivity){
        this.mActivity = mActivity;
    }

    private static void updateCardBackgroundColor(ImageCardView view, boolean selected) {
        int color = selected ? sSelectedBackgroundColor : sDefaultBackgroundColor;
        view.setBackgroundColor(color);
        view.findViewById(R.id.info_field).setBackgroundColor(color);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        sDefaultBackgroundColor = ContextCompat.getColor(parent.getContext(), R.color.default_background);
        sSelectedBackgroundColor = ContextCompat.getColor(parent.getContext(), R.color.selected_background);

        ImageCardView cardView = new ImageCardView(parent.getContext()) {
            @Override
            public void setSelected(boolean selected) {
                updateCardBackgroundColor(this, selected);
                super.setSelected(selected);
            }
        };

        cardView.setFocusable(true);
        cardView.setFocusableInTouchMode(true);
        updateCardBackgroundColor(cardView, false);
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object item) {
        Event event = (Event) item;
        ImageCardView cardView = (ImageCardView) viewHolder.view;

        cardView.setOnClickListener((View view) -> {
            if(!event.getState().equals(mActivity.getString(R.string.state_live)) && !event.getState().equals(mActivity.getString(R.string.state_replay))){
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder.setMessage(mActivity.getString(R.string.state_unknown_message));
                builder.setCancelable(true);
                builder.setPositiveButton(mActivity.getString(R.string.ok), null);
                mActivity.runOnUiThread(() -> {
                    builder.show().getButton(DialogInterface.BUTTON_POSITIVE).requestFocus();
                });
            }
            else{
                Intent intent = new Intent(mActivity, PlaybackActivity.class);
                intent.putExtra(mActivity.getString(R.string.intent_event), event);
                mActivity.startActivity(intent);
            }
        });

        if (event.getImg() != null) {
            cardView.setTitleText(event.getDate());
            if(event.getState().equals(mActivity.getString(R.string.state_replay))){
                cardView.setContentText(mActivity.getString(R.string.title_replay));
            }
            else if(event.getState().equals(mActivity.getString(R.string.state_before))){
                cardView.setContentText(mActivity.getString(R.string.title_before));
            }
            else{
                cardView.setContentText(mActivity.getString(R.string.title_live));
            }

            cardView.setMainImageDimensions(CARD_WIDTH, CARD_HEIGHT);
            Glide.with(viewHolder.view.getContext())
                    .load(event.getImg())
                    .centerCrop()
                    .into(cardView.getMainImageView());
        }
    }

    @Override
    public void onUnbindViewHolder(Presenter.ViewHolder viewHolder) {
        ImageCardView cardView = (ImageCardView) viewHolder.view;
        cardView.setBadgeImage(null);
        cardView.setMainImage(null);
    }
}
