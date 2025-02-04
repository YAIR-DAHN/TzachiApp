package com.shahareinisim.tzachiapp.Views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;

import androidx.core.content.ContextCompat;
import androidx.palette.graphics.Palette;

import com.google.android.material.card.MaterialCardView;
import com.shahareinisim.tzachiapp.databinding.ItemMainBinding;

@SuppressLint("ViewConstructor")
public class MainItem extends MaterialCardView {

    ItemMainBinding binding;
    int id;

    @SuppressLint("UseCompatLoadingForDrawables")
    public MainItem(int id, String title, int bannerDrawableId, Context context) {
        super(context);
        binding = ItemMainBinding.inflate(LayoutInflater.from(context), this, true);

        this.id = id;

        binding.bannerTitle.setText(title);

        Drawable bannerDrawable = ContextCompat.getDrawable(context, bannerDrawableId);
        // must convert to bitmap to apply palette
        Bitmap bitmap = drawableToBitmap(bannerDrawable);
        binding.banner.setImageBitmap(bitmap);

        Palette.from(bitmap).generate(palette -> {
            if (palette != null) applyPalette(palette);
            else applyDefaultColors();
        });
    }

    private void applyPalette(Palette palette) {
        Palette.Swatch vibrantSwatch = palette.getVibrantSwatch();
        if (vibrantSwatch != null) {
            binding.bannerTitle.setTextColor(vibrantSwatch.getTitleTextColor());
            binding.mainItem.setCardBackgroundColor(vibrantSwatch.getRgb());
        } else applyDefaultColors();
    }

    private void applyDefaultColors() {
        binding.bannerTitle.setTextColor(Color.BLACK);
        binding.mainItem.setCardBackgroundColor(Color.WHITE);
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
