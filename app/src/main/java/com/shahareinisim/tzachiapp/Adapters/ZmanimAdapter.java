package com.shahareinisim.tzachiapp.Adapters;

import android.annotation.SuppressLint;
import android.icu.text.SimpleDateFormat;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shahareinisim.tzachiapp.Models.Zman;
import com.shahareinisim.tzachiapp.databinding.ItemZmanimBinding;

import java.util.ArrayList;

public class ZmanimAdapter extends RecyclerView.Adapter<ZmanimAdapter.ViewHolder> {

    ArrayList <Zman> zmanim;

    public ZmanimAdapter(ArrayList<Zman> zmanim) {
        this.zmanim = zmanim;
    }

    @NonNull
    @Override
    public ZmanimAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemZmanimBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ZmanimAdapter.ViewHolder holder, int position) {
        Zman zman = zmanim.get(position);
        @SuppressLint("SimpleDateFormat")
        String time = new SimpleDateFormat("HH:mm:ss").format(zman.getZman());
        holder.binding.label.setText(zman.getLabel());
        holder.binding.time.setText(time);
    }

    @Override
    public int getItemCount() {
        if (zmanim != null) return zmanim.size();
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemZmanimBinding binding;
        public ViewHolder(ItemZmanimBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
