package com.elinz.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by sebastian on 05.06.14.
 */
public class MyCustomAdapter extends BaseAdapter {

    private ArrayList mListItems;
    private LayoutInflater mLayoutInflater;
    private int selectedIndex;

    public MyCustomAdapter(Context context, ArrayList arrayList){

        mListItems = arrayList;
        selectedIndex = -1;

        //get the layout inflater
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setSelectedIndex(int index) {
        this.selectedIndex = index;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public EStation getSelectedItem() {
        if (selectedIndex <= -1 || selectedIndex >= mListItems.size()) {
            return null;
        } else {
            return (EStation) mListItems.get(selectedIndex);
        }
    }

    @Override
    public int getCount() {
        //getCount() represents how many items are in the list
        return mListItems.size();
    }

    @Override
    //get the data of an item from a specific position
    //i represents the position of the item in the list
    public Object getItem(int i) {
        return null;
    }

    @Override
    //get the position id of the item from the list
    public long getItemId(int i) {
        return 0;
    }

    @Override

    public View getView(int position, View view, ViewGroup viewGroup) {

        // create a ViewHolder reference
        ViewHolder holder;

        //check to see if the reused view is null or not, if is not null then reuse it
        //if (view == null) {
            holder = new ViewHolder();

            view = mLayoutInflater.inflate(R.layout.list_item, null);
            holder.itemName = (TextView) view.findViewById(R.id.list_item_text_view);

            // the setTag is used to store the data within this view
       /* } else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (ViewHolder)view.getTag();
        }*/

        //get the string item from the position "position" from array list to put it on the TextView
        EStation listIt = (EStation) mListItems.get(position);
        String stringItem = listIt.getName();
        holder.itemName.setText(stringItem);
        /*if (stringItem != null) {
            if (holder.itemName != null) {
                //set the item name on the TextView
                holder.itemName.setText(stringItem);
            }
        }*/
        ViewHolder holder2 = new ViewHolder();
        holder2.itemName = (TextView) view.findViewById(R.id.list_item_text_view2);

        // the setTag is used to store the data within this view
        //view.setTag(holder2);
        String dist =  String.valueOf(listIt.getDistance()) + " km";
        holder2.itemName.setText(dist);
        view.setTag(R.id.list, listIt);
        /*if (dist != null) {
            if (holder.itemName != null) {
                //set the item name on the TextView
                holder.itemName.setText(dist);
            }
        }*/

        //this method must return the view corresponding to the data at the specified position.
        return view;

    }

    /**
     * Static class used to avoid the calling of "findViewById" every time the getView() method is called,
     * because this can impact to your application performance when your list is too big. The class is static so it
     * cache all the things inside once it's created.
     */
    private static class ViewHolder {

        protected TextView itemName;

    }
}
