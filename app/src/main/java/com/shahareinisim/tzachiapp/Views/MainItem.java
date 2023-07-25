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
public class MainItem extends MaterialCardView {
    
    int id;

    @SuppressLint("UseCompatLoadingForDrawables")
    public MainItem(int id, String title, int bannerId, Context context) {
        super(context);
        inflate(context, R.layout.item_main, this);

        this.id = id;

        ImageView banner = findViewById(R.id.banner);
        TextView bannerTitle = findViewById(R.id.banner_title);

        bannerTitle.setText(title);

        Bitmap bitmap = drawableToBitmap(getResources().getDrawable(bannerId));
        banner.setImageBitmap(bitmap);

        Palette.from(bitmap).generate(p -> {
            if (p != null && p.getVibrantSwatch() != null) {
                findViewById(R.id.main_item).setBackgroundColor(p.getVibrantSwatch().getRgb());
                bannerTitle.setTextColor(p.getVibrantSwatch().getTitleTextColor());
            }
        });
    }

    public int getItemId() {
        return id;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap;
        if (drawable instanceof BitmapDrawable) {
            bitmap = ((BitmapDrawable) drawable).getBitmap();
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
        }
        return bitmap;
    }

}
