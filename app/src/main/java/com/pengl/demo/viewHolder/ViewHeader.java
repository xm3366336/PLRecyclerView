package com.pengl.demo.viewHolder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pengl.recyclerview.SectionItem;
import com.pengl.demo.R;

import androidx.appcompat.widget.AppCompatImageView;

/**
 *
 */
public class ViewHeader implements SectionItem {

    private AppCompatImageView imageView;

    @Override
    public View createView(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header, parent, false);
        imageView = view.findViewById(R.id.image);
        return view;
    }

    @Override
    public void onBind() {
        imageView.setImageResource(R.mipmap.mclaren_1);
    }
}