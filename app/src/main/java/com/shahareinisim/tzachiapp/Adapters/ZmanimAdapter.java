package com.shahareinisim.tzachiapp.Adapters;

import android.annotation.SuppressLint;
import android.icu.text.SimpleDateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shahareinisim.tzachiapp.Models.Zman;
import com.shahareinisim.tzachiapp.R;

import java.util.ArrayList;

public class ZmanimAdapter extends RecyclerView.Adapter<ZmanimAdapter.ViewHolder> {

    ArrayList <Zman> zmanim;

    public ZmanimAdapter(ArrayList<Zman> zmanim) {
        this.zmanim = zmanim;
    }

    @NonNull
    @Override
    public ZmanimAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_zmanim, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ZmanimAdapter.ViewHolder holder, int position) {
        Zman zman = zmanim.get(position);
        @SuppressLint("SimpleDateFormat")
        String time = new SimpleDateFormat("HH:mm:ss").format(zman.getZman());
        holder.label.setText(zman.getLabel());
        holder.time.setText(time);
    }

    @Override
    public int getItemCount() {
        if (zmanim != null) return zmanim.size();
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView label, time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            label = itemView.findViewById(R.id.label);
            time = itemView.findViewById(R.id.time);
        }
    }
}
