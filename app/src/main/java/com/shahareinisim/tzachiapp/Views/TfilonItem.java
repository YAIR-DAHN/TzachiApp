package com.shahareinisim.tzachiapp.Views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.shahareinisim.tzachiapp.R;

@SuppressLint("ViewConstructor")
public class TfilonItem extends MaterialCardView {

    int id;

    @SuppressLint("UseCompatLoadingForDrawables")
    public TfilonItem(int id, String title, Context context) {
        super(context);
        this.id = id;
        inflate(context, R.layout.item_tfilon, this);

       ((TextView) findViewById(R.id.banner_title)).setText(title);
    }

}
