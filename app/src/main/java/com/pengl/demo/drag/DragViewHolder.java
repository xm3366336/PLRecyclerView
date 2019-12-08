package com.pengl.demo.drag;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pengl.PLRecyclerView.AbstractViewHolder;
import com.pengl.demo.R;

/**
 *
 */
class DragViewHolder extends AbstractViewHolder<DragBean> implements View.OnClickListener {

    private TextView mText;
    private ImageView mReorder;
    private Context mContext;

    DragViewHolder(ViewGroup parent) {
        super(parent, R.layout.drag_item);
        mContext = parent.getContext();
        mText = itemView.findViewById(R.id.text);
        mReorder = itemView.findViewById(R.id.reorder);
        mText.setOnClickListener(this);

        //触摸拖拽
        mReorder.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ItemTouchHelperProvider.getInstance().startDrag(DragViewHolder.this);
                return true;
            }
        });

        //长按拖拽
        //        mText.setOnLongClickListener(new View.OnLongClickListener() {
        //            @Override
        //            public boolean onLongClick(View v) {
        //                ItemTouchHelperProvider.getInstance().startDrag(DragViewHolder.this);
        //                return true;
        //            }
        //        });
    }

    @Override
    public void setData(DragBean data) {
        mText.setText(data.text);
        if (data.status) {
            mReorder.setVisibility(View.VISIBLE);
        } else {
            mReorder.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(mContext, mText.getText().toString(), Toast.LENGTH_SHORT).show();
    }
}
