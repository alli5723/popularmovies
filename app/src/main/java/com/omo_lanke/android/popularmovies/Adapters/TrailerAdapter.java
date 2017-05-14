package com.omo_lanke.android.popularmovies.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.omo_lanke.android.popularmovies.R;
import com.omo_lanke.android.popularmovies.model.TrailerDetails;

import java.util.List;

/**
 * Created by omo_lanke on 12/05/2017.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerAdapterViewHolder> {
    private Context context;
    private List<TrailerDetails> values;

    final private TrailerAdapterOnClickHandler mClickHandler;

    public interface TrailerAdapterOnClickHandler{
        void onClick(int index, TrailerDetails trailer);
    }

    public TrailerAdapter(@NonNull Context context, TrailerAdapterOnClickHandler clickHandler, List<TrailerDetails> t) {
        this.context = context;
        values = t;
        mClickHandler = clickHandler;
        Log.e("T-Adapter", "Trailer Adapter called");
    }

    public class TrailerAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name;
        public TextView size;
        public TextView type;
        public TextView language;
        TrailerAdapterViewHolder(View view){
            super(view);
            name = (TextView)view.findViewById(R.id.textViewName);
            size = (TextView)view.findViewById(R.id.textViewSize);
            type = (TextView)view.findViewById(R.id.textViewType);
            language = (TextView)view.findViewById(R.id.textViewLanguage);

            view.setOnClickListener(this);
        }

        public void onClick(View v){
            int pos = getAdapterPosition();
            //Captures the click from tis Adapter and sends to the activity
            mClickHandler.onClick(pos, values.get(pos));
        }
    }

    @Override
    public TrailerAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
        final Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        int layoutIdForList = R.layout.trailer_list_item;
        View view = inflater.inflate(layoutIdForList, viewGroup, shouldAttachToParentImmediately);
        view.setFocusable(true);
        return new TrailerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerAdapterViewHolder trailerAdapterViewHolder, int position) {
        trailerAdapterViewHolder.name.setText(values.get(position).getName());
        trailerAdapterViewHolder.size.setText(values.get(position).getSize());
        trailerAdapterViewHolder.type.setText(values.get(position).getType());
        trailerAdapterViewHolder.language.setText(values.get(position).getIso_639_1());
    }

    @Override
    public int getItemCount() {
        if(null == values) return 0;
        return values.size();
    }

    public void resetData(List<TrailerDetails> trailer) {
        values = trailer;
        notifyDataSetChanged();
    }
}
