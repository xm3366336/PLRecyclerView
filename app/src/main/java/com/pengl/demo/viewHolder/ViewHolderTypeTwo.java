package com.pengl.demo.viewHolder;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.pengl.recyclerview.AbstractViewHolder;
import com.pengl.demo.R;
import com.pengl.demo.model.BeanTypeTwo;

/**
 *
 */
public class ViewHolderTypeTwo extends AbstractViewHolder<BeanTypeTwo> {

    TextView mText;
    private Context mContext;

    public ViewHolderTypeTwo(ViewGroup parent) {
        super(parent, R.layout.item_type_two);
        mContext = parent.getContext();
        mText = itemView.findViewById(R.id.text);
    }

    @Override
    public void setData(BeanTypeTwo data) {
        mText.setText(data.getText());
        mText.setOnClickListener(v -> Toast.makeText(mContext, mText.getText(), Toast.LENGTH_SHORT).show());
    }

}
