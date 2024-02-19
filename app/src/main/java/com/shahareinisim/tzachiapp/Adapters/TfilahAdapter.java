package com.shahareinisim.tzachiapp.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shahareinisim.tzachiapp.Models.TfilahPart;
import com.shahareinisim.tzachiapp.R;
import com.shahareinisim.tzachiapp.Views.ViewHolderTPart;

import java.util.ArrayList;

public class TfilahAdapter extends RecyclerView.Adapter<ViewHolderTPart> {

    ArrayList<TfilahPart> tfilahParts;
    Context context;

    public static final float[][] textTypes = {{18,21,16},{22,25,20},{25,29,22},{30,34,27}};
    public int textSize;

    public TfilahAdapter(ArrayList<TfilahPart> tfilahParts, int textSize) {
        this.tfilahParts = tfilahParts;
        this.textSize = (textSize > textTypes.length-1 || textSize < 0) ? 0 : textSize;
    }

    @NonNull
    @Override
    public ViewHolderTPart onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ViewHolderTPart(LayoutInflater.from(parent.getContext()).inflate(R.layout.tfilah_part, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderTPart holder, int position) {

        TfilahPart tfilahPart = tfilahParts.get(position);

        holder.text.setText(tfilahPart.getPart());

        SpannableString wordToSpan;

        switch (tfilahPart.getPrimaryKey()) {
            case TITLE:
                titleDesign(holder);
                break;
            case NOTE:
                noteDesign(holder);
                break;
            case INSIDE_NOTE:
            case INLINE_NOTE:
                regularDesign(holder, true);
                wordToSpan = new SpannableString(holder.text.getText());
                for (String note : tfilahPart.getNotes()) {
                    setHighLightedText(wordToSpan, note);
                }
                holder.text.setText(wordToSpan, TextView.BufferType.SPANNABLE);
                break;
            case HOLIDAY:
                holidayDesign(holder, true);
                wordToSpan = new SpannableString(holder.text.getText());
                for (String note : tfilahPart.getNotes()) {
                    setHighLightedText(wordToSpan, note);
                }
                holder.text.setText(wordToSpan, TextView.BufferType.SPANNABLE);
                break;
            case EMPTY:
                emptyLineDesign(holder);
                break;
            default:
                regularDesign(holder, false);
        }
    }

    private void emptyLineDesign(ViewHolderTPart part) {
        part.text.setTextColor(Color.RED);
        part.text.setTextSize(0);
        part.text.setGravity(View.FOCUS_RIGHT);
        part.itemView.setPadding(convertToPX(0), convertToPX(7), convertToPX(0), convertToPX(7));
        part.indicator.setVisibility(View.GONE);
    }

    public void regularDesign(ViewHolderTPart part, boolean insideNote) {
        part.text.setTextColor(Color.BLACK);
        part.text.setTextSize(textTypes[textSize][0]);
        part.text.setGravity(View.FOCUS_RIGHT);
        part.itemView.setPadding(convertToPX(20), convertToPX(insideNote ? 0 : 3), convertToPX(20), convertToPX(insideNote ? 0 : 3));
        part.indicator.setVisibility(View.GONE);
    }

    private void holidayDesign(ViewHolderTPart part, boolean insideNote) {
        part.text.setTextColor(Color.RED);
        part.text.setTextSize(textTypes[textSize][0]);
        part.text.setGravity(View.FOCUS_RIGHT);
        part.itemView.setPadding(convertToPX(20), convertToPX(insideNote ? 0 : 3), convertToPX(20), convertToPX(insideNote ? 0 : 3));
        part.indicator.setVisibility(View.GONE);
    }

    public void titleDesign(ViewHolderTPart part) {
        part.text.setTextColor(Color.BLUE);
        part.text.setTextSize(textTypes[textSize][1]);
        part.text.setGravity(View.FOCUS_RIGHT);
        part.itemView.setPadding(convertToPX(20), convertToPX(30), convertToPX(0), convertToPX(15));
        part.indicator.setVisibility(View.VISIBLE);
    }

    private void noteDesign(ViewHolderTPart part) {
        part.text.setTextColor(Color.GRAY);
        part.text.setTextSize(textTypes[textSize][2]);
        part.text.setGravity(View.FOCUS_RIGHT);
        part.itemView.setPadding(convertToPX(20), convertToPX(5), convertToPX(20), convertToPX(0));
        part.indicator.setVisibility(View.GONE);
    }

    public void setHighLightedText(SpannableString wordToSpan, String textToHighlight) {
        String tvt = wordToSpan.toString().toLowerCase();
        int ofe = tvt.indexOf(textToHighlight);

        for (int ofs = 0; ofs < tvt.length() && ofe != -1; ofs = ofe + 1) {
            ofe = tvt.indexOf(textToHighlight, ofs);
            if (ofe == -1) break;
            else {
                wordToSpan.setSpan(new RelativeSizeSpan(0.9f), ofe, ofe + textToHighlight.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                wordToSpan.setSpan(new ForegroundColorSpan(Color.GRAY), ofe, ofe + textToHighlight.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

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
