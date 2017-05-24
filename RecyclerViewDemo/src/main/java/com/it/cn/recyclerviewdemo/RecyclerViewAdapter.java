package com.it.cn.recyclerviewdemo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/2/24.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<String> datas;

    public RecyclerViewAdapter(Context context, ArrayList<String> datas) {
        this.context=context;
        this.datas=datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView=View.inflate(context,R.layout.item,null);
        return new ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //根据位置得到数据
        String data = datas.get(position);
        //绑定数据
        holder.tv_title.setText(data);


    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    /**
     * 添加数据
     * @param position
     * @param data
     */

    public void addData(int position, String data) {
        datas.add(position,data);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        datas.remove(position);
        //移除的刷新
        notifyItemRemoved(position);
    }

    public void removeAll() {
        datas.clear();
       notifyDataSetChanged();
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView iv_icon;
        private TextView tv_title;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_icon= (ImageView)itemView.findViewById(R.id.iv_icon);
            tv_title= (TextView)itemView.findViewById(R.id.tv_title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClickListener!=null){
                        onItemClickListener.onItemClick(v,getLayoutPosition(),datas.get(getAdapterPosition()));

                    }

                }
            });
        }
    }


    public interface OnItemClickListener {
        /**
         *
         * @param view
         * @param position
         * @param data
         */
        void onItemClick(View view,int position,String data);
    }
    private OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(OnItemClickListener listener){
        this.onItemClickListener=listener;
    }
}
