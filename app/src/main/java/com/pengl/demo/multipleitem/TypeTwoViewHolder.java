package com.pengl.demo.multipleitem;

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
public class TypeTwoViewHolder extends AbstractViewHolder<TypeTwoBean> {

    TextView mText;
    private Context mContext;

    public TypeTwoViewHolder(ViewGroup parent) {
        super(parent, R.layout.type_two_item);
        mContext = parent.getContext();
        mText = itemView.findViewById(R.id.text);
    }

    @Override
    public void setData(TypeTwoBean data) {
        mText.setText(data.text);
        mText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, mText.getText(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
