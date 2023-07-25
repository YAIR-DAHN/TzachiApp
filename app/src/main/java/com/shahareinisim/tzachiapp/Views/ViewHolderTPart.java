package com.shahareinisim.tzachiapp.Views;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shahareinisim.tzachiapp.R;

public class ViewHolderTPart extends RecyclerView.ViewHolder {

    public TextView text;
    public ImageView indicator;
    public ViewHolderTPart(@NonNull View itemView) {
        super(itemView);

        text = itemView.findViewById(R.id.text);
        indicator = itemView.findViewById(R.id.indicator);
    }
}
