package com.pengl.demo.grid;

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
public class GridViewHolder extends AbstractViewHolder<GridBean> {

    private TextView mText;
    private Context mContext;

    public GridViewHolder(ViewGroup parent) {
        super(parent, R.layout.grid_item);
        mContext = parent.getContext();
        mText = itemView.findViewById(R.id.text);
        mText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, mText.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void setData(GridBean data) {
        mText.setText(data.text);
    }

}
