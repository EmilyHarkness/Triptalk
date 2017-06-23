package com.example.emily.triptalk.Login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.emily.triptalk.GitHubService;
import com.example.emily.triptalk.R;
import com.example.emily.triptalk.User;
import com.example.emily.triptalk.UserAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnItemClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

/**
 * Created by emily on 22.06.2017.
 */

public class UsersRecyclerFragment extends AppCompatActivity {
    List<User> users = new ArrayList<User>();
    UserAdapter userAdapter;

    //    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.users_recycler, null);
//    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.users_recycler);
        ButterKnife.bind(this);
    }

    /*public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView login;
        public TextView url;

        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item, parent, false));
            //avatar = (ImageView) itemView.findViewById(R.id.image);
            login = (TextView) itemView.findViewById(R.id.login_text_view);
        }
    }
*/
}