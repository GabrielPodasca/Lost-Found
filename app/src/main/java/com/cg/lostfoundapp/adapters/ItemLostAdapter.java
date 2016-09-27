package com.cg.lostfoundapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cg.lostfoundapp.R;
import com.cg.lostfoundapp.activities.FoundItemActivity;
import com.cg.lostfoundapp.activities.LostItemActivity;
import com.cg.lostfoundapp.model.FoundItem;
import com.cg.lostfoundapp.model.Item;

import java.util.ArrayList;

/**
 * Created by Client2_2 on 9/27/2016.
 */
public class ItemLostAdapter extends BaseAdapter {
    private Context context; //context
    private ArrayList<Item> items; //data source of the list adapter

    public ItemLostAdapter(Context context, ArrayList<Item> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size(); //returns total of items in the list
    }

    @Override
    public Object getItem(int position) {
        return items.get(position); //returns list item at the specified position
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // inflate the layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.lost_item_listview, parent, false);
        }

        // get current item to be displayed
        Item currentItem = (Item) getItem(position);

        // get the TextView for item name and item description
        TextView txtName = (TextView)
                convertView.findViewById(R.id.txtName);
        TextView txtDateAndTime = (TextView)
                convertView.findViewById(R.id.txtDateAndTime);

        //sets the text for item name and item description from the current item object
        txtName.setText(currentItem.getName());
        txtDateAndTime.setText(currentItem.getWhen().toString());

        ImageView arrowImage = (ImageView) convertView.findViewById(R.id.arrowImage);
        arrowImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, LostItemActivity.class);
                context.startActivity(intent);
            }
        });

        return convertView;
    }
}