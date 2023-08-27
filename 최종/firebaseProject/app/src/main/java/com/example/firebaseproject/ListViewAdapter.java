package com.example.firebaseproject;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {
    ArrayList<ListViewItem>itemlist = new ArrayList<ListViewItem>();
    @Override
    public int getCount() {
        return itemlist.size();
    }

    @Override
    public Object getItem(int position) {
        return itemlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int posi = position;
        final Context c = parent.getContext();

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item,parent,false);
        }
        //객체 생성
        TextView temp = (TextView)convertView.findViewById(R.id.tempText);
        TextView humi = (TextView)convertView.findViewById(R.id.humiText);
        TextView time = (TextView)convertView.findViewById(R.id.timeText);
        TextView index = (TextView)convertView.findViewById(R.id.indexText);

        //리스트뷰에 값을 뿌림
        ListViewItem listitem = itemlist.get(position);

        temp.setText(listitem.getTemp());
        humi.setText(listitem.getHumi());
        time.setText(listitem.getTime());
        index.setText(listitem.getIndex());

        return convertView;
    }
    //값을 추가하는 메서드
    public void addItem(String temp, String humi, String time, String index){
        ListViewItem item = new ListViewItem();
        item.setTemp(temp);
        item.setHumi(humi);
        item.setTime(time);
        item.setIndex(index);

        itemlist.add(item);
    }
}
