package com.pengl.demo.staggered;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pengl.PLRecyclerView.AbstractViewHolder;
import com.pengl.demo.R;

/**
 *
 */
public class StaggerViewHolder extends AbstractViewHolder<StaggerBean> {

    TextView mText;

    public StaggerViewHolder(ViewGroup parent) {
        super(parent, R.layout.stagger_item);
        mText = itemView.findViewById(R.id.text);
    }

    @Override
    public void setData(StaggerBean data) {
        mText.setText(data.text);
        mText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        int height = (int) (100 + Math.random() * 300);
        ViewGroup.LayoutParams lp = itemView.getLayoutParams();
        lp.height = height;

        itemView.setLayoutParams(lp);
    }

}
