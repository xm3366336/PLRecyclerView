package com.pengl.demo.viewHolder;

import android.view.ViewGroup;
import android.widget.TextView;

import com.pengl.recyclerview.AbstractViewHolder;
import com.pengl.demo.R;
import com.pengl.demo.model.BeanStagger;

/**
 *
 */
public class ViewHolderStagger extends AbstractViewHolder<BeanStagger> {

    TextView mText;

    public ViewHolderStagger(ViewGroup parent) {
        super(parent, R.layout.item_stagger);
        mText = itemView.findViewById(R.id.text);
    }

    @Override
    public void setData(BeanStagger data) {
        mText.setText(data.getText());
        mText.setOnClickListener(v -> {

        });

        int height = (int) (100 + Math.random() * 300);
        ViewGroup.LayoutParams lp = itemView.getLayoutParams();
        lp.height = height;

        itemView.setLayoutParams(lp);
    }

}
