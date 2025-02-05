package com.shahareinisim.tzachiapp.Adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shahareinisim.tzachiapp.Models.TfilahTitlePart;
import com.shahareinisim.tzachiapp.TfilonActivity;
import com.shahareinisim.tzachiapp.Views.ViewHolderTPart;
import com.shahareinisim.tzachiapp.databinding.ItemTfilahPartBinding;

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
        return new ViewHolderTPart(ItemTfilahPartBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderTPart holder, int position) {

        holder.binding.getRoot().setPadding(convertToPX(10), convertToPX(8), convertToPX(5), convertToPX(8));
        holder.binding.text.setText(mDataSource.get(position).getTitle());

        holder.binding.getRoot().setOnClickListener(v -> clickListener.onPostClick(mDataSource.get(position).getPosition()));
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