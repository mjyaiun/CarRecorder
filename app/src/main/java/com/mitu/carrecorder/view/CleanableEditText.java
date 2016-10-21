package com.mitu.carrecorder.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.mitu.carrecorder.R;


/**
 * 说明：带删除按钮的EditTextView
 * 2016/4/29 0029
 */
public class CleanableEditText extends EditText {

    private Context mContext;
    private Drawable mDrawable;
    /**回调函数*/
    private TextWatcherCallBack mCallBack;

    public CleanableEditText(Context context) {
        super(context);
        this.mContext = context;
        init();
    }


    public CleanableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    public CleanableEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    private void init() {
        mDrawable = mContext.getResources().getDrawable(R.drawable.clean);
        mCallBack = null;
        TextWatcher textwatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //更新状态检查是否显示删除按钮
                updateCleanable(length(),true);
                //
                if(mCallBack != null )
                    mCallBack.handleMoreTextChanged();
            }
        };

        this.addTextChangedListener(textwatcher);
        this.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                updateCleanable(length(),hasFocus);
            }
        });

    }

    private void updateCleanable(int length,boolean hasFocus) {
        if (length > 0 && hasFocus){
            setCompoundDrawablesWithIntrinsicBounds(null,null,mDrawable,null);
        }else {
            setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
        }
    }

    //


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int DRAWABLE_RIGHT = 2;
        //可以获得上下左右四个drawable,右侧排第二
        Drawable rightIcon = getCompoundDrawables()[DRAWABLE_RIGHT];
        if (rightIcon != null && event.getAction() == MotionEvent.ACTION_UP){
            //检查点击位置是否是右侧的删除图标
            //注意使用getRwwX()是获取相对屏幕的位置，getX()可以获得相对父组件的位置
            int leftEdgeOfRightDrawable = getRight() - getPaddingRight() - rightIcon.getBounds().width();
            if (event.getRawX() >= leftEdgeOfRightDrawable){
                setText("");
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void finalize() throws Throwable{
        mDrawable = null;
        super.finalize();
    }

    public interface TextWatcherCallBack{
        public void  handleMoreTextChanged();
    }

    public void setCallBack(TextWatcherCallBack callBack){
        this.mCallBack = callBack;
    }

}
