package com.shahareinisim.tzachiapp.Adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shahareinisim.tzachiapp.Models.TfilahTitlePart;
import com.shahareinisim.tzachiapp.R;
import com.shahareinisim.tzachiapp.TfilonActivity;
import com.shahareinisim.tzachiapp.Views.ViewHolderTPart;

import java.util.ArrayList;

public class TitleAdapter extends RecyclerView.Adapter<ViewHolderTPart> {

    public final ArrayList<TfilahTitlePart> mDataSource;
    ClickListener clickListener;
    TfilonActivity activity;

    public TitleAdapter(TfilonActivity activity, ArrayList<TfilahTitlePart> dataSource) {
        this.activity = activity;
        this.mDataSource = dataSource;
    }

    @NonNull
    @Override
    public ViewHolderTPart onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderTPart(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tfilah_part, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderTPart holder, int position) {

        holder.itemView.setPadding(convertToPX(10), convertToPX(8), convertToPX(5), convertToPX(8));
        holder.text.setText(mDataSource.get(position).getTitle());

        holder.itemView.setOnClickListener(v -> clickListener.onPostClick(mDataSource.get(position).getPosition()));
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return mDataSource.size();
    }

    public interface ClickListener {
        void onPostClick(int position);
    }

    public void setPostClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public int convertToPX(int dp) {
        return activity.convertToPX(dp);
    }

}