package com.pengl.demo.viewHolder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pengl.recyclerview.AbstractViewHolder;
import com.pengl.demo.R;
import com.pengl.demo.utils.ItemTouchHelperProvider;
import com.pengl.demo.model.BeanDrag;

/**
 *
 */
public class ViewHolderDrag extends AbstractViewHolder<BeanDrag> implements View.OnClickListener {

    private TextView mText;
    private ImageView mReorder;
    private Context mContext;

    @SuppressLint("ClickableViewAccessibility")
    public ViewHolderDrag(ViewGroup parent) {
        super(parent, R.layout.item_drag);
        mContext = parent.getContext();
        mText = itemView.findViewById(R.id.text);
        mReorder = itemView.findViewById(R.id.reorder);
        mText.setOnClickListener(this);

        //触摸拖拽
        mReorder.setOnTouchListener((v, event) -> {
            ItemTouchHelperProvider.getInstance().startDrag(ViewHolderDrag.this);
            return true;
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
    public void setData(BeanDrag data) {
        mText.setText(data.getText());
        if (data.isStatus()) {
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
