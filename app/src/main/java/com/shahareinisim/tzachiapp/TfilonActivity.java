package com.shahareinisim.tzachiapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.shahareinisim.tzachiapp.Fragments.TfilahFragment;
import com.shahareinisim.tzachiapp.Views.MainItem;
import com.shahareinisim.tzachiapp.Views.TfilonItem;

public class TfilonActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tfilon);

        initCardView("שחרית", view -> tfilahFragment(TfilahFragment.Tfilah.SHACHRIT));
        initCardView("מנחה", view -> tfilahFragment(TfilahFragment.Tfilah.MINCHA));
        initCardView("ערבית", view -> tfilahFragment(TfilahFragment.Tfilah.HARVIT));
        initCardView("ברכת המזון", view -> tfilahFragment(TfilahFragment.Tfilah.BIRCAT_HAMAZON));
        initCardView("תפילת הדרך", view -> tfilahFragment(TfilahFragment.Tfilah.TFILAT_HADEREH));
        initCardView("קריאת שמע על המיטה", view -> tfilahFragment(TfilahFragment.Tfilah.KRIAT_SHMAA));
        initCardView("ברכת הלבנה", view -> tfilahFragment(TfilahFragment.Tfilah.BIRCAT_HALEVANA));
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