package com.shahareinisim.tzachiapp.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.text.LineBreaker;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.shahareinisim.tzachiapp.Models.Note;
import com.shahareinisim.tzachiapp.Models.TfilahPart;
import com.shahareinisim.tzachiapp.Views.ViewHolderTPart;
import com.shahareinisim.tzachiapp.databinding.ItemTfilahPartBinding;

import java.util.ArrayList;

public class TfilahAdapter extends RecyclerView.Adapter<ViewHolderTPart> {

    OnItemClickListener onItemClickListener;
    ArrayList<TfilahPart> tfilahParts;
    Context context;

    public static final float[][] textTypes = {{18,21,16},{22,25,20},{25,29,22},{30,34,27}};
    public int textSize;
    public int font;
    public boolean justifyAlignment;

    public TfilahAdapter(ArrayList<TfilahPart> tfilahParts, int textSize, int font, boolean justifyAlignment) {
        this.tfilahParts = tfilahParts;
        this.textSize = (textSize > textTypes.length-1 || textSize < 0) ? 0 : textSize;
        this.font = font;
        this.justifyAlignment = justifyAlignment;
    }

    @NonNull
    @Override
    public ViewHolderTPart onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ViewHolderTPart(ItemTfilahPartBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderTPart holder, int position) {

        TfilahPart tfilahPart = tfilahParts.get(position);

        holder.binding.text.setText(tfilahPart.getPart());

        holder.binding.text.setTypeface(ResourcesCompat.getFont(context, font));


        SpannableString wordToSpan;

        switch (tfilahPart.getPrimaryKey()) {
            case TITLE:
                titleDesign(holder);
                break;
            case NOTE:
                noteDesign(holder);
                break;
            case INSIDE_NOTE:
                regularDesign(holder, true);
                break;
            case ADDON:
                if (tfilahPart.isTitle()) titleDesign(holder);
                else holidayDesign(holder, true);
                break;
            case EMPTY:
                emptyLineDesign(holder);
                break;
            default:
                regularDesign(holder, false);
        }

        if (tfilahPart.isInsideNote()) {
            wordToSpan = new SpannableString(holder.binding.text.getText());
            for (Note note : tfilahPart.getNotes()) {
                setTextToComment(wordToSpan, note);
            }
            holder.binding.text.setText(wordToSpan, TextView.BufferType.SPANNABLE);
        }

        holder.binding.getRoot().setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onClick();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            holder.binding.text.setJustificationMode(justifyAlignment ? LineBreaker.JUSTIFICATION_MODE_INTER_WORD : LineBreaker.JUSTIFICATION_MODE_NONE);
        }
    }

    private void emptyLineDesign(ViewHolderTPart part) {
        part.binding.text.setTextColor(Color.RED);
        part.binding.text.setTextSize(0);
        part.binding.text.setGravity(View.FOCUS_RIGHT);
        part.binding.getRoot().setPadding(convertToPX(0), convertToPX(7), convertToPX(0), convertToPX(7));
        part.binding.indicator.setVisibility(View.GONE);
    }

    public void regularDesign(ViewHolderTPart part, boolean insideNote) {
        part.binding.text.setTextColor(Color.BLACK);
        part.binding.text.setTextSize(textTypes[textSize][0]);
        part.binding.text.setGravity(View.FOCUS_RIGHT);
        part.binding.getRoot().setPadding(convertToPX(20), convertToPX(insideNote ? 0 : 3), convertToPX(20), convertToPX(insideNote ? 0 : 3));
        part.binding.indicator.setVisibility(View.GONE);
    }

    private void holidayDesign(ViewHolderTPart part, boolean insideNote) {
        part.binding.text.setTextColor(Color.RED);
        part.binding.text.setTextSize(textTypes[textSize][0]);
        part.binding.text.setGravity(View.FOCUS_RIGHT);
        part.binding.getRoot().setPadding(convertToPX(20), convertToPX(insideNote ? 0 : 3), convertToPX(20), convertToPX(insideNote ? 0 : 3));
        part.binding.indicator.setVisibility(View.GONE);
    }

    public void titleDesign(ViewHolderTPart part) {
        part.binding.text.setTextColor(Color.BLUE);
        part.binding.text.setTextSize(textTypes[textSize][1]);
        part.binding.text.setGravity(View.FOCUS_RIGHT);
        part.binding.getRoot().setPadding(convertToPX(20), convertToPX(30), convertToPX(0), convertToPX(15));
        part.binding.indicator.setVisibility(View.VISIBLE);
    }

    private void noteDesign(ViewHolderTPart part) {
        part.binding.text.setTextColor(Color.GRAY);
        part.binding.text.setTextSize(textTypes[textSize][2]);
        part.binding.text.setGravity(View.FOCUS_RIGHT);
        part.binding.getRoot().setPadding(convertToPX(20), convertToPX(5), convertToPX(20), convertToPX(0));
        part.binding.indicator.setVisibility(View.GONE);
    }

    public void setTextToComment(SpannableString wordToSpan, Note note) {
        if (note.getEndIndex() > wordToSpan.length()/* || note.getStartIndex() > 0*/) {
            Log.d("# Note #", String.format("setHighLightedText: error: endIndex = %s, length = %s", note.getEndIndex(), wordToSpan.length()));
            return;
        }
        wordToSpan.setSpan(new RelativeSizeSpan(0.9f), note.getStartIndex()-1, note.getEndIndex(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordToSpan.setSpan(new ForegroundColorSpan(Color.GRAY), note.getStartIndex()-1, note.getEndIndex(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
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

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onClick();
    }
}
