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
class TypeOneViewHolder extends AbstractViewHolder<TypeOneBean> {

    private Context mContext;
    private TextView mText;

    TypeOneViewHolder(ViewGroup parent) {
        super(parent, R.layout.type_one_item);
        mContext = parent.getContext();
        mText = itemView.findViewById(R.id.text);
    }

    @Override
    public void setData(TypeOneBean data) {
        mText.setText(data.text);
        mText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, mText.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}