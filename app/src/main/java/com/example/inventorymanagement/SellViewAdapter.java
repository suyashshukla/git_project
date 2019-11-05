package com.example.inventorymanagement;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class SellViewAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<Model> sellRecordList;

    public SellViewAdapter(Context context, int layout, ArrayList<Model> sellRecordList) {
        this.context = context;
        this.layout = layout;
        this.sellRecordList = sellRecordList;
    }

    @Override
    public int getCount() {
        return sellRecordList.size();
    }

    @Override
    public Object getItem(int position) {
        return sellRecordList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder{
        TextView txtName,txtQuantity,txtPrice,txtTotal;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowSellCard =convertView;
        ViewHolder holder =new ViewHolder();
        if (rowSellCard == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowSellCard = inflater.inflate(layout,null);
            holder.txtName = rowSellCard.findViewById(R.id.txtName);
            holder.txtQuantity = rowSellCard.findViewById(R.id.txtQuantity);
            holder.txtPrice = rowSellCard.findViewById(R.id.txtPrice);
            holder.txtTotal = rowSellCard.findViewById(R.id.txtTotal);

            rowSellCard.setTag(holder);
        }
        else {
            holder = (ViewHolder) rowSellCard.getTag();
        }

        Model model = sellRecordList.get(position);

        holder.txtName.setText(model.getProductName());
        holder.txtQuantity.setText(""+model.getProductQuantity());
        holder.txtPrice.setText(""+model.getProductPrice());
        holder.txtTotal.setText(""+model.getTotal());

        return rowSellCard;
    }
}
