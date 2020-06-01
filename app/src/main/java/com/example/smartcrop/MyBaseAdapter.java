package com.example.smartcrop;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

class MyBaseAdapter extends BaseAdapter {

    Context context;
    ArrayList<Node> list;

    public MyBaseAdapter(Context context, ArrayList<Node> list) {

    this.context=context;
    this.list=list;
    }

    public void updateReceiptsList(List<Node> newlist) {
        list.clear();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView=inflater.inflate(R.layout.raw_cust_list,null);

        TextView tv_date = convertView.findViewById(R.id.date_val);
        TextView tv_time = convertView.findViewById(R.id.time_val);
        TextView tv_node1 = convertView.findViewById(R.id.node1_val);
        TextView tv_node2 = convertView.findViewById(R.id.node2_val);
        TextView tv_node3 = convertView.findViewById(R.id.node3_val);
        TextView tv_node4 = convertView.findViewById(R.id.node4_val);
        LinearLayout layout = convertView.findViewById(R.id.line);


        if (list.get(position).getN1().equals("OFF")){

            tv_node1.setTextColor(context.getResources().getColor(R.color.colorred));

        }else {
            tv_node1.setTextColor(context.getResources().getColor(R.color.colorgreen));

        }

        if (list.get(position).getN2().equals("OFF")){

            tv_node2.setTextColor(context.getResources().getColor(R.color.colorred));

        }else {
            tv_node2.setTextColor(context.getResources().getColor(R.color.colorgreen));
        }

        if (list.get(position).getN3().equals("OFF")){

            tv_node3.setTextColor(context.getResources().getColor(R.color.colorred));

        }else {
            tv_node3.setTextColor(context.getResources().getColor(R.color.colorgreen));
        }

        if (list.get(position).getN4().equals("OFF")){

            tv_node4.setTextColor(context.getResources().getColor(R.color.colorred));

        }else {
            tv_node4.setTextColor(context.getResources().getColor(R.color.colorgreen));
        }

        tv_date.setText(list.get(position).getDate());
        tv_time.setText(list.get(position).getTime());
        tv_node1.setText(list.get(position).getN1());
        tv_node2.setText(list.get(position).getN2());
        tv_node3.setText(list.get(position).getN3());
        tv_node4.setText(list.get(position).getN4());


        if(position%2==0)
        {
            layout.setBackgroundColor(Color.WHITE);
        }else{
            layout.setBackgroundColor(context.getResources().getColor(R.color.colorwhite));
        }

        return convertView;
    }
}
