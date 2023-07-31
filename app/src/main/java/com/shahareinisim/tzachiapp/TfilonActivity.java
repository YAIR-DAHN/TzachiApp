package com.shahareinisim.tzachiapp;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.shahareinisim.tzachiapp.Fragments.TfilahFragment;
import com.shahareinisim.tzachiapp.Views.TfilonItem;

public class TfilonActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tfilon);

        initCardView(getString(R.string.shachrit), view -> tfilahFragment(TfilahFragment.Tfilah.SHACHRIT));
        initCardView(getString(R.string.mincha), view -> tfilahFragment(TfilahFragment.Tfilah.MINCHA));
        initCardView(getString(R.string.harvit), view -> tfilahFragment(TfilahFragment.Tfilah.HARVIT));
        initCardView(getString(R.string.bircat_hamazon), view -> tfilahFragment(TfilahFragment.Tfilah.BIRCAT_HAMAZON));
        initCardView(getString(R.string.tfilat_hadereh), view -> tfilahFragment(TfilahFragment.Tfilah.TFILAT_HADEREH));
        initCardView(getString(R.string.kriat_shema), view -> tfilahFragment(TfilahFragment.Tfilah.KRIAT_SHEMA));
        initCardView(getString(R.string.bircat_halevana), view -> tfilahFragment(TfilahFragment.Tfilah.BIRCAT_HALEVANA));
    }

    public void initCardView(String title, View.OnClickListener onClickListener) {

        TfilonItem tfilonItem = new TfilonItem(0, title, this);
        tfilonItem.setOnClickListener(onClickListener);
        ((LinearLayout) findViewById(R.id.dashboard)).addView(tfilonItem);

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) tfilonItem.getLayoutParams();
        params.setMargins(convertToPX(20), convertToPX(5), convertToPX(20), convertToPX(5));
    }

    public void tfilahFragment(TfilahFragment.Tfilah tfilah) {

        FragmentManager fm = getSupportFragmentManager();
        // create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        // replace the FrameLayout with new Fragment
        fragmentTransaction.add(R.id.fragment_container, new TfilahFragment(tfilah), "tag");
//        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.addToBackStack("");


        fragmentTransaction.commit();
    }
}