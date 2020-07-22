package com.pengl.demo.viewHolder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pengl.PLRecyclerView.AbstractAdapter;
import com.pengl.PLRecyclerView.AbstractViewHolder;
import com.pengl.demo.R;
import com.pengl.demo.adapter.AdapterExpand;
import com.pengl.demo.model.BeanExpandChild;
import com.pengl.demo.model.BeanExpandParent;

import java.util.List;

/**
 *
 */
public class ViewHolderParent extends AbstractViewHolder<BeanExpandParent> implements View.OnClickListener {

    private TextView mText;
    private ImageView mImageView;

    private BeanExpandParent parent;
    private List<BeanExpandChild> child;

    private AdapterExpand mAdapter;

    public ViewHolderParent(AbstractAdapter adapter, ViewGroup parent) {
        super(parent, R.layout.item_parent);
        mText = itemView.findViewById(R.id.text);
        mImageView = itemView.findViewById(R.id.image);
        mAdapter = (AdapterExpand) adapter;

        mText.setOnClickListener(this);
    }

    @Override
    public void setData(BeanExpandParent data) {
        mText.setText(String.valueOf(data.getText()));
        child = data.getChildList();
        parent = data;
    }

    @Override
    public void onClick(View v) {
        if (parent.isExpand()) {
            mAdapter.removeBack(getAdapterPosition(), child.size());
            parent.setExpand(false);
            mImageView.setImageResource(R.drawable.ic_keyboard_arrow_down);
        } else {
            mAdapter.insertAllBack(getAdapterPosition(), child);
            parent.setExpand(true);
            mImageView.setImageResource(R.drawable.ic_keyboard_arrow_up);
        }
    }
}
