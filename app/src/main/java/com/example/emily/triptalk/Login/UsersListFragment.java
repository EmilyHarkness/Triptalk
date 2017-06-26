package com.example.emily.triptalk.Login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.emily.triptalk.GitHubService;
import com.example.emily.triptalk.R;
import com.example.emily.triptalk.User;
import com.example.emily.triptalk.UserAdapter;
import com.example.emily.triptalk.UserDetails;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by emily on 22.06.2017.
 */

public class UsersListFragment extends Fragment {
    List<User> users = new ArrayList<User>();
    UserAdapter userAdapter;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.users_list, null);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setVisibility(ProgressBar.VISIBLE);

        userAdapter = new UserAdapter(getActivity(), new ArrayList<User>());
        ListView listView = (ListView) view.findViewById(R.id.usersList);
        listView.setAdapter(userAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), UserDetailsActivity.class);
                intent.putExtra("login", users.get(position).getLogin());
                startActivity(intent);
            }
        });

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
                progressBar.setVisibility(ProgressBar.INVISIBLE);
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                //No network
                progressBar.setVisibility(ProgressBar.INVISIBLE);
            }
        });
        return view;
    }
}