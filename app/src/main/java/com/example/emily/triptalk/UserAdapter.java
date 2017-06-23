package com.example.emily.triptalk;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by emily on 22.06.2017.
 */

public class UserAdapter extends ArrayAdapter<User> {
    public UserAdapter(Context context, ArrayList<User> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        User currentUser = getItem(position);

        ImageView imageView = (ImageView) listItemView.findViewById(R.id.image);
        Picasso.with(getContext()).load(currentUser.getAvatar_url()).into(imageView);
        TextView loginTextView = (TextView) listItemView.findViewById(R.id.login_text_view);
        loginTextView.setText(currentUser.getLogin());
        return listItemView;
    }
}
