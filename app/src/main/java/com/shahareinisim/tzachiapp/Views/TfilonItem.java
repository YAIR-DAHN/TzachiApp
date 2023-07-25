package com.shahareinisim.tzachiapp.Views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.palette.graphics.Palette;

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

    public int getItemId() {
        return id;
    }

}
