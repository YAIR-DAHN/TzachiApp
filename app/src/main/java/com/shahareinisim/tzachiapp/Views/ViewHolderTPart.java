package com.shahareinisim.tzachiapp.Views;

import androidx.recyclerview.widget.RecyclerView;
import com.shahareinisim.tzachiapp.databinding.ItemTfilahPartBinding;

public class ViewHolderTPart extends RecyclerView.ViewHolder {

    public final ItemTfilahPartBinding binding;
    public ViewHolderTPart(ItemTfilahPartBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
}
