package com.shahareinisim.tzachiapp.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shahareinisim.tzachiapp.Models.TfilahTitlePart;
import com.shahareinisim.tzachiapp.R;
import com.shahareinisim.tzachiapp.TfilonActivity;

import java.util.ArrayList;

public class TitleAdapter extends BaseAdapter {

    public final ArrayList<TfilahTitlePart> mDataSource;
    private final LayoutInflater layoutInflater;
    ClickListener clickListener;
    TfilonActivity activity;

    public TitleAdapter(TfilonActivity activity, ArrayList<TfilahTitlePart> dataSource) {
        this.activity = activity;
        this.mDataSource = dataSource;
        this.layoutInflater = activity.getLayoutInflater();
    }

    @Override
    public int getCount() {
        return mDataSource.size();
    }

    @Override
    public TfilahTitlePart getItem(int position) {
        return mDataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.tfilah_part, null);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.text);
            holder.tvTitle.setPadding(convertToPX(10), convertToPX(8), convertToPX(5), convertToPX(8));
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // bind data
        holder.tvTitle.setText(getItem(position).getTitle());

        holder.tvTitle.setOnClickListener(v -> clickListener.onPostClick(getItem(position).getPosition()));

        return convertView;
    }

    public static class ViewHolder {
        private TextView tvTitle;
    }

    public interface ClickListener {
        void onPostClick(int position);
    }

    public void setPostClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public int convertToPX(int dp) {
        return activity.convertToPX(dp);
    }

}