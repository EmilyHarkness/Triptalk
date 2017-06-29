package com.example.emily.triptalk.Login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.emily.triptalk.GitHubService;
import com.example.emily.triptalk.R;
import com.example.emily.triptalk.SpinnerOnItemSelectedListener;
import com.example.emily.triptalk.User;
import com.example.emily.triptalk.UserAdapter;
import com.example.emily.triptalk.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
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
    Spinner spinner;
    int sortType;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.users_list, null);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setVisibility(ProgressBar.VISIBLE);
        spinner = (Spinner) getActivity().findViewById(R.id.spinner);
        sortType = spinner.getSelectedItemPosition();

        userAdapter = new UserAdapter(getActivity(), new ArrayList<User>());
        final ListView listView = (ListView) view.findViewById(R.id.usersList);
        listView.setAdapter(userAdapter);

        GitHubService gitHubService = GitHubService.retrofit.create(GitHubService.class);
        Call<List<User>> call = gitHubService.getUsers();
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) { //ok
                    users = response.body();
                    switch (sortType) {
                        case 1: //A-Z
                            Collections.sort(users, new Comparator<User>() {
                                @Override
                                public int compare(User o1, User o2) {
                                    return o1.getLogin().compareTo(o2.getLogin());
                                }
                            });
                            break;
                        case 2: //Z-A
                            Collections.sort(users, new Comparator<User>() {
                                @Override
                                public int compare(User o1, User o2) {
                                    return o2.getLogin().compareTo(o1.getLogin());
                                }
                            });
                            break;
                        default: //without
                            break;
                    }
                    userAdapter.clear();
                    userAdapter.addAll(users);
                } else { //bad request
                }
                progressBar.setVisibility(ProgressBar.GONE);
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) { //No network
                progressBar.setVisibility(ProgressBar.GONE);
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 1: //A-Z
                        Collections.sort(users, new Comparator<User>() {
                            @Override
                            public int compare(User o1, User o2) {
                                return o1.getLogin().compareTo(o2.getLogin());
                            }
                        });
                        break;
                    case 2: //Z-A
                        Collections.sort(users, new Comparator<User>() {
                            @Override
                            public int compare(User o1, User o2) {
                                return o2.getLogin().compareTo(o1.getLogin());
                            }
                        });
                        break;
                    default: //without
                        Collections.sort(users, new Comparator<User>() {
                            @Override
                            public int compare(User o1, User o2) {
                                if (o1.getId() < o2.getId())
                                    return -1;
                                else
                                    return 1;
                            }
                        });
                        break;
                }
                userAdapter.clear();
                userAdapter.addAll(users);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeList);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (sortType != 0) {
                    spinner.setSelection(0);
                    Collections.sort(users, new Comparator<User>() {
                        @Override
                        public int compare(User o1, User o2) {
                            if (o1.getId() < o2.getId())
                                return -1;
                            else
                                return 1;
                        }
                    });
                    userAdapter.clear();
                    userAdapter.addAll(users);
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (listView.getLastVisiblePosition() == listView.getAdapter().getCount() - 1 &&
                        listView.getChildAt(listView.getChildCount() - 1).getBottom() <= listView.getHeight()) {
                    progressBar.setVisibility(ProgressBar.VISIBLE);

                    int maxID = 0;
                    for (int i = 0; i < users.size(); i++) {
                        if (users.get(i).getId() > maxID)
                            maxID = users.get(i).getId();
                    }
                    GitHubService gitHubService = GitHubService.retrofit.create(GitHubService.class);
                    Call<List<User>> call = gitHubService.getMoreUsers(maxID);
                    call.enqueue(new Callback<List<User>>() {
                        @Override
                        public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                            if (response.isSuccessful()) {
                                users.addAll(response.body());
                                userAdapter.addAll(response.body());
                            } else {
                            }
                            progressBar.setVisibility(ProgressBar.GONE);
                        }

                        @Override
                        public void onFailure(Call<List<User>> call, Throwable t) {
                            progressBar.setVisibility(ProgressBar.GONE);
                        }
                    });
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), UserDetailsActivity.class);
                intent.putExtra("login", users.get(position).getLogin());
                startActivity(intent);
            }
        });

        return view;
    }
}