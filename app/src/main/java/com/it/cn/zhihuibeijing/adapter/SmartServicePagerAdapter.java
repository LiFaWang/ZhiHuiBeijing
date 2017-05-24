package com.it.cn.zhihuibeijing.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.it.cn.zhihuibeijing.R;
import com.it.cn.zhihuibeijing.domain.SmartServicePagerBean;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.BitmapCallback;

import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 商城热卖的适配器
 * Created by Administrator on 2017/2/26.
 */

public class SmartServicePagerAdapter extends RecyclerView.Adapter<SmartServicePagerAdapter.ViewHolder> {
    private final Context context;
    private final List<SmartServicePagerBean.ListBean> datas;

    public SmartServicePagerAdapter (Context context, List<SmartServicePagerBean.ListBean> datas){
        this.context=context;
        this.datas=datas;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView=View.inflate(context, R.layout.item_smart_service_pager,null);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        //根据位置得到对应的数据
        final SmartServicePagerBean.ListBean listBean = datas.get(position);
        //绑定数据
        OkGo.get(listBean.getImgUrl())
                .tag(this)
                .execute(new BitmapCallback() {
                    @Override
                    public void onSuccess(Bitmap bitmap, Call call, Response response) {
                        holder.iv_product.setImageBitmap(bitmap);
                        holder.tv_name.setText(listBean.getName());
                        holder.tv_price.setText("￥"+listBean.getPrice()+"");
//
                    }
                });


    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    /**
     * 清空适配器
     */
    public void clearData() {
        datas.clear();
        notifyItemRangeChanged(0,datas.size());
    }

    public void addData(int position, List<SmartServicePagerBean.ListBean> data) {
        datas.addAll(position,data);
        notifyItemRangeChanged(0,datas.size());
    }

    public int getDataCount() {
        return datas.size();

    }


    class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView iv_product;
        private TextView tv_name;
        private TextView tv_price;
        private Button btn_buy;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_product= (ImageView)itemView.findViewById(R.id.iv_product);
            tv_name= (TextView) itemView.findViewById(R.id.tv_name);
            tv_price= (TextView) itemView.findViewById(R.id.tv_price);
            btn_buy= (Button) itemView.findViewById(R.id.btn_buy);
       btn_buy.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                System.out.println("被点击了"+v.toString());
                            }
                        });

        }

    }
}
