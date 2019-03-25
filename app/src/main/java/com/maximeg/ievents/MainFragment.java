package com.maximeg.ievents;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.leanback.app.RowsFragment;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.ListRowPresenter;
import androidx.leanback.widget.Presenter;
import androidx.core.content.ContextCompat;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainFragment extends RowsFragment {

    private static final int SETTINGS_BUTTON = 0;
    private static final int RELOAD_BUTTON = 1;

    private static final int GRID_ITEM_WIDTH = 350;
    private static final int GRID_ITEM_HEIGHT = 200;

    private ArrayObjectAdapter rowsAdapter;

    private List<Event> eventList = new ArrayList<>();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        rowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        setAdapter(rowsAdapter);

        loadSettings();
        loadVideos();
    }

    private void loadSettings() {
        HeaderItem gridHeader = new HeaderItem(1, getString(R.string.row_more));

        GridItemPresenter mGridPresenter = new GridItemPresenter();
        ArrayObjectAdapter gridRowAdapter = new ArrayObjectAdapter(mGridPresenter);
        gridRowAdapter.add(getResources().getDrawable(R.drawable.ic_settings));
        gridRowAdapter.add(getResources().getDrawable(R.drawable.ic_reload));
        rowsAdapter.add(new ListRow(gridHeader, gridRowAdapter));
    }

    private void loadVideos(){
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(getString(R.string.api_link)).build();

        Call call = client.newCall(request); // Async request
        call.enqueue(new Callback() {
            public void onResponse(Call call, Response response) {
                getActivity().runOnUiThread(() -> { // Prevents "RecyclerView is computing" error
                    try{
                        if(response.body() != null){
                            rowsAdapter.clear();
                            eventList.clear();

                            JSONArray jsonEventArray = new JSONArray(response.body().string());
                            for (int i = 0; i < jsonEventArray.length(); i++) {
                                eventList.add(new Event(jsonEventArray.getJSONObject(i)));
                            }

                            CardPresenter cardPresenter = new CardPresenter(getActivity());

                            ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(cardPresenter);
                            for (int j = 0; j < eventList.size(); j++) {
                                listRowAdapter.add(eventList.get(j));
                            }

                            HeaderItem header = new HeaderItem(0, getString(R.string.row_recents));

                            rowsAdapter.add(0, new ListRow(header, listRowAdapter));

                            loadSettings();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }

            public void onFailure(Call call, IOException e) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(getString(R.string.api_error_message));
                builder.setCancelable(true);
                builder.setPositiveButton(getString(R.string.ok), null);
                builder.setNegativeButton(getString(R.string.exit), (DialogInterface dialogInterface, int i) -> {
                    System.exit(1);
                });
                getActivity().runOnUiThread(() -> {
                    builder.show().getButton(DialogInterface.BUTTON_POSITIVE).requestFocus();
                });
            }
        });
    }

    private class GridItemPresenter extends Presenter {
        private int count = 0;

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent) {
            ImageView view = new ImageView(parent.getContext());

            view.setLayoutParams(new ViewGroup.LayoutParams(GRID_ITEM_WIDTH, GRID_ITEM_HEIGHT));
            view.setPadding(50,50,50,50);
            view.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            view.setAdjustViewBounds(true);
            view.setFocusable(true);
            view.setFocusableInTouchMode(true);
            view.setTag(count); // To know what's the button
            count += 1;
            view.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.default_background));
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, Object item) {
            ((ImageView) viewHolder.view).setImageDrawable((Drawable) item);

            viewHolder.view.setOnFocusChangeListener((View view, boolean b) -> {
                if(b){
                    view.setBackgroundColor(getResources().getColor(R.color.selected_background));
                }
                else{
                    view.setBackgroundColor(getResources().getColor(R.color.default_background));
                }
            });

            viewHolder.view.setOnClickListener((View view) -> {
                if(view.getTag().equals(SETTINGS_BUTTON)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(getString(R.string.settings_message));
                    builder.setCancelable(true);
                    builder.setPositiveButton(getString(R.string.ok), null);
                    getActivity().runOnUiThread(() -> {
                        builder.show().getButton(DialogInterface.BUTTON_POSITIVE).requestFocus();
                    });
                }
                else if(view.getTag().equals(RELOAD_BUTTON)){
                    loadVideos();
                }
            });
        }

        @Override
        public void onUnbindViewHolder(ViewHolder viewHolder) {
        }
    }

}
