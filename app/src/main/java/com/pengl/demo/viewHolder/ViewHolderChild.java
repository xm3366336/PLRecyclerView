package com.pengl.demo.viewHolder;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.pengl.recyclerview.AbstractViewHolder;
import com.pengl.demo.R;
import com.pengl.demo.model.BeanExpandChild;

/**
 *
 */
public class ViewHolderChild extends AbstractViewHolder<BeanExpandChild> {

    TextView mText;

    private Context mContext;

    public ViewHolderChild(ViewGroup parent) {
        super(parent, R.layout.item_child);
        mContext = parent.getContext();
        mText = itemView.findViewById(R.id.text);
        mText.setOnClickListener(v -> Toast.makeText(mContext, "i am child view", Toast.LENGTH_SHORT).show());
    }

    @Override
    public void setData(BeanExpandChild data) {
        mText.setText(String.valueOf(data.getText()));
    }

}
