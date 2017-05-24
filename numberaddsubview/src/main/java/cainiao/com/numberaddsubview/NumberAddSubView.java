package cainiao.com.numberaddsubview;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/3/8.
 */

public class NumberAddSubView extends LinearLayout implements View.OnClickListener {
    private Button btn_sub;
    private TextView tv_value;
    private Button btn_add;
    private int value=1;
    private int minValue=1;
    private int maxValue=10;

    public int getValue() {
        String valueStr = tv_value.getText().toString().trim();
        if(!TextUtils.isEmpty(valueStr)){
            value= Integer.valueOf(valueStr);
        }
        
        return value;
    }

    public void setValue(int value) {
        this.value = value;
        tv_value.setText(value+"");
    }

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public NumberAddSubView(Context context) {
        this(context,null);
    }

    public NumberAddSubView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public NumberAddSubView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context,R.layout.number_add_sub_view,this);
        btn_sub= (Button) findViewById(R.id.btn_sub);
        tv_value= (TextView) findViewById(R.id.tv_value);
        btn_add= (Button) findViewById(R.id.btn_add);
        getValue();
        //设置点击事件
        btn_sub.setOnClickListener(this);
        btn_add.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sub:
                subNumber();
                if(mListener!=null){
                    mListener.onButtonSub(v,value);
                }

                break;
            case R.id.btn_add:
                addNumber();
                if(mListener!=null){
                    mListener.onButtonAdd(v,value);
                }


                break;

            default:
                break;
        }
    }

    private void addNumber() {//加
        if (value<maxValue){
            value+=1;

        }
        setValue(value);

    }

    private void subNumber() {//减
        if(value>minValue){
            value-=1;
        }
        setValue(value);

    }
    public interface OnNumberClickListener{
        /**
         * 当减少按钮被点击时回调
         * @param v
         * @param value
         */

        public void onButtonSub(View v,int value);
        /**
         * 当增加按钮被点击时回调
         * @param v
         * @param value
         */

        public void onButtonAdd(View v,int value);
    }

    /**
     * 设置监听数字按钮
     * @param listener
     */

    public void setOnNumberClickListener(OnNumberClickListener listener) {
        mListener = listener;
    }

    private OnNumberClickListener mListener;


}
