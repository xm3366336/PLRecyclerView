package com.pengl.demo.expand;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.pengl.PLRecyclerView.AbstractViewHolder;
import com.pengl.demo.R;

/**
 *
 */
class ChildViewHolder extends AbstractViewHolder<ChildBean> {

    TextView mText;

    private Context mContext;

    ChildViewHolder(ViewGroup parent) {
        super(parent, R.layout.child_item);
        mContext = parent.getContext();
        mText = itemView.findViewById(R.id.text);
        mText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "i am child view", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void setData(ChildBean data) {
        mText.setText(String.valueOf(data.text));
    }

}
