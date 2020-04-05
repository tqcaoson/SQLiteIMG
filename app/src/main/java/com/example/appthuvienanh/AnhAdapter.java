package com.example.appthuvienanh;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class AnhAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private List<Anh> anhList;

    public AnhAdapter(Context context, int layout, List<Anh> anhList) {
        this.context = context;
        this.layout = layout;
        this.anhList = anhList;
    }

    @Override
    public int getCount() {
        return anhList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder{
        TextView txtTen, txtMoTa;
        ImageView imgHinh;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null){
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);
            holder.txtTen = (TextView) convertView.findViewById(R.id.textViewTenCustom);
            holder.txtMoTa = (TextView) convertView.findViewById(R.id.textViewMoTaCustom);
            holder.imgHinh = (ImageView) convertView.findViewById(R.id.imageHinhCustom);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        Anh anh = anhList.get(position);

        holder.txtTen.setText(anh.getTen());
        holder.txtMoTa.setText(anh.getMoTa());

        byte[] hinhAnh = anh.getHinh();
        Bitmap bitmap = BitmapFactory.decodeByteArray(hinhAnh, 0, hinhAnh.length);
        holder.imgHinh.setImageBitmap(bitmap);

        return convertView;
    }
}
