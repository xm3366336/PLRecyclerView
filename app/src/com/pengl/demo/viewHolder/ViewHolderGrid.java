package com.pengl.demo.viewHolder;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.pengl.recyclerview.AbstractViewHolder;
import com.pengl.demo.R;
import com.pengl.demo.model.BeanGrid;

/**
 *
 */
public class ViewHolderGrid extends AbstractViewHolder<BeanGrid> {

    private TextView mText;
    private Context mContext;

    public ViewHolderGrid(ViewGroup parent) {
        super(parent, R.layout.item_grid);
        mContext = parent.getContext();
        mText = itemView.findViewById(R.id.text);
        mText.setOnClickListener(v -> Toast.makeText(mContext, mText.getText().toString(), Toast.LENGTH_SHORT).show());
    }

    @Override
    public void setData(BeanGrid data) {
        mText.setText(data.getText());
    }

}
