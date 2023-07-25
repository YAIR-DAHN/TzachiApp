package com.shahareinisim.tzachiapp.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shahareinisim.tzachiapp.Models.TfilahPart;
import com.shahareinisim.tzachiapp.R;
import com.shahareinisim.tzachiapp.Views.ViewHolderTPart;

import java.util.ArrayList;

public class TfilahAdapter extends RecyclerView.Adapter<ViewHolderTPart> {

    ArrayList<TfilahPart> tfilahParts;
    Context context;

    float[][] textTypes = {{22,25,20},{25,30,22},{30,38,25}};
    public int textSize;

    public TfilahAdapter(ArrayList<TfilahPart> tfilahParts, int textSize) {
        this.tfilahParts = tfilahParts;
        this.textSize = textSize;
    }

    @NonNull
    @Override
    public ViewHolderTPart onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ViewHolderTPart(LayoutInflater.from(parent.getContext()).inflate(R.layout.tfilah_part, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderTPart holder, int position) {


        holder.text.setText(tfilahParts.get(position).getPart());

        switch (tfilahParts.get(position).getKey()) {
            case "[t]":
                titleDesign(holder);
                break;
            case "[n]":
                noteDesign(holder);
                break;
            case "[e]":
                emptyLineDesign(holder);
                break;
            default:
                regularDesign(holder);
        }
    }

    private void emptyLineDesign(ViewHolderTPart part) {
        part.text.setTextColor(Color.BLACK);
        part.text.setTextSize(0);
        part.text.setGravity(View.FOCUS_RIGHT);
        part.itemView.setPadding(convertToPX(0), convertToPX(0), convertToPX(0), convertToPX(0));
        part.indicator.setVisibility(View.GONE);
    }

    public void regularDesign(ViewHolderTPart part) {
        part.text.setTextColor(Color.BLACK);
        part.text.setTextSize(textTypes[textSize][0]);
        part.text.setGravity(View.FOCUS_RIGHT);
        part.itemView.setPadding(convertToPX(20), convertToPX(0), convertToPX(20), convertToPX(20));
        part.indicator.setVisibility(View.GONE);
    }

    public void titleDesign(ViewHolderTPart part) {
        part.text.setTextColor(Color.BLUE);
        part.text.setTextSize(textTypes[textSize][1]);
        part.text.setGravity(View.FOCUS_RIGHT);
        part.itemView.setPadding(convertToPX(20), convertToPX(40), convertToPX(0), convertToPX(25));
        part.indicator.setVisibility(View.VISIBLE);
    }

    private void noteDesign(ViewHolderTPart part) {
        part.text.setTextColor(Color.GRAY);
        part.text.setTextSize(textTypes[textSize][2]);
        part.text.setGravity(View.FOCUS_RIGHT);
        part.itemView.setPadding(convertToPX(20), convertToPX(10), convertToPX(20), convertToPX(0));
        part.indicator.setVisibility(View.GONE);
    }

    public int convertToPX(int dp) {
        Resources r = context.getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp, r.getDisplayMetrics());
    }

    @Override
    public int getItemCount() {
//        if (tfilahParts == null) return 0;
        return tfilahParts.size();
    }
}
