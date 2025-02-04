package com.shahareinisim.tzachiapp.Views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;

import com.google.android.material.card.MaterialCardView;
import com.shahareinisim.tzachiapp.databinding.ItemTfilonBinding;

@SuppressLint("ViewConstructor")
public class TfilonItem extends MaterialCardView {

    ItemTfilonBinding binding;
    int id;

    @SuppressLint("UseCompatLoadingForDrawables")
    public TfilonItem(int id, String title, Context context) {
        super(context);
        this.id = id;
        binding = ItemTfilonBinding.inflate(LayoutInflater.from(context), this, true);

        binding.bannerTitle.setText(title);
    }

}
