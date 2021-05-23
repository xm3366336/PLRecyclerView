package com.pengl.demo.viewHolder;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pengl.recyclerview.SectionItem;
import com.pengl.demo.BuildConfig;
import com.pengl.demo.R;

/**
 *
 */
public class ViewFooter implements SectionItem {

    private Context mContext;
    private TextView tv_version;

    @Override
    public View createView(ViewGroup parent) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_footer, parent, false);
        tv_version = view.findViewById(R.id.tv_version);
        final TextView tv_copyright = view.findViewById(R.id.tv_copyright);
        view.setOnClickListener(view1 -> {
            Uri uri = Uri.parse(tv_copyright.getText().toString());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            mContext.startActivity(intent);
        });
        return view;
    }

    @Override
    public void onBind() {
        tv_version.setText(mContext.getString(R.string.version, BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE));
    }

}