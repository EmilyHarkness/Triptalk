package com.example.emily.triptalk.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.emily.triptalk.GitHubService;
import com.example.emily.triptalk.R;
import com.example.emily.triptalk.User;
import com.example.emily.triptalk.UserAdapter;
import com.example.emily.triptalk.UserDetails;

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
import retrofit2.http.Path;

/**
 * Created by emily on 22.06.2017.
 */

public class UsersListFragment extends AppCompatActivity {
    List<User> users = new ArrayList<User>();
    UserAdapter userAdapter;
    /*@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.users_list, null);
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.users_list);
        ButterKnife.bind(this);
        userAdapter = new UserAdapter(this, new ArrayList<User>());
        ListView listView = (ListView) findViewById(R.id.usersList);
        listView.setAdapter(userAdapter);

        GitHubService gitHubService = GitHubService.retrofit.create(GitHubService.class);
        Call<List<User>> call = gitHubService.getUsers();
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    //ok
                    users = response.body();
                    userAdapter.clear();
                    userAdapter.addAll(users);
                } else {
                    //bad request
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                //no network
            }
        });
    }

    @OnItemClick(R.id.usersList)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, UserDetailsActivity.class);
        intent.putExtra("login", users.get(position).getLogin());
        startActivity(intent);
    }

/*
    public static class ViewHolder extends RecyclerView.ViewHolder {
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