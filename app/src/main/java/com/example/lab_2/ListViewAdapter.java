package com.example.lab_2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

class Item {

    private int mPicture;
    private String mTitle;

    int getPicture() {
        return mPicture;
    }

    Item(int picture, String title) {
        mPicture = picture;
        mTitle = title;
    }

    String getTitle() {
        return mTitle;
    }
}

public class ListViewAdapter extends ArrayAdapter<Item> {

    ListViewAdapter(@NonNull Context context, ArrayList<Item> items) {
        super(context, 0, items);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        // Get the {@link Word} object located at this position in the list
        Item currentItem = getItem(position);

        ImageView picture = listItem.findViewById(R.id.IvPicture);
        picture.setBackgroundResource(currentItem.getPicture());

        TextView title = listItem.findViewById(R.id.tvTitle);
        title.setText(currentItem.getTitle());
        return listItem;
    }

}