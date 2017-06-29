package com.example.emily.triptalk.Login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emily.triptalk.GitHubService;
import com.example.emily.triptalk.R;
import com.example.emily.triptalk.RecyclerViewOnItemClickListener;
import com.example.emily.triptalk.User;
import com.example.emily.triptalk.UserAdapter;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

public class UsersRecyclerFragment extends Fragment {
    List<User> users = new ArrayList<User>();
    UserRecyclerAdapter userAdapter;
    ProgressBar progressBar;
    Spinner spinner;
    SwipeRefreshLayout swipeRefreshLayout;
    int sortType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.users_recycler, null);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setVisibility(ProgressBar.VISIBLE);
        spinner = (Spinner) getActivity().findViewById(R.id.spinner);
        sortType = spinner.getSelectedItemPosition();

        userAdapter = new UserRecyclerAdapter(getActivity(), new ArrayList<User>(), new RecyclerViewOnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent intent = new Intent(getActivity(), UserDetailsActivity.class);
                intent.putExtra("login", users.get(position).getLogin());
                startActivity(intent);
            }
        });
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.usersRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(userAdapter);

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
                    userAdapter.users.clear();
                    userAdapter.users.addAll(users);
                    userAdapter.notifyDataSetChanged();
                } else { //bad request
                }
                progressBar.setVisibility(ProgressBar.GONE);
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) { //no network
                progressBar.setVisibility(ProgressBar.GONE);
            }
        });

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRecycler);
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
                    userAdapter.users.clear();
                    userAdapter.users.addAll(users);
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (users.size() > 0) {
                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    int itemCount = layoutManager.getItemCount();
                    int lastCompletelyVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition();
                    if (itemCount - lastCompletelyVisibleItemPosition == 1) {
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
                                if (response.isSuccessful()) { //ok
                                    users.addAll(response.body());
                                    userAdapter.users.addAll(response.body());
                                    userAdapter.notifyDataSetChanged();
                                } else { //bad request
                                }
                                progressBar.setVisibility(ProgressBar.GONE);
                            }

                            @Override
                            public void onFailure(Call<List<User>> call, Throwable t) { //no network
                                progressBar.setVisibility(ProgressBar.GONE);
                            }
                        });
                    }
                }
            }
        });

        return view;
    }

    public class UserRecyclerAdapter extends RecyclerView.Adapter<UserRecyclerAdapter.UserViewHolder> {

        LayoutInflater layoutInflater;
        List<User> users;
        RecyclerViewOnItemClickListener listener;

        public UserRecyclerAdapter(Context context, List<User> users, RecyclerViewOnItemClickListener listener) {
            layoutInflater = LayoutInflater.from(context);
            this.users = users;
            this.listener = listener;
        }

        @Override
        public UserRecyclerAdapter.UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = layoutInflater.inflate(R.layout.list_item, parent, false);
            final UserViewHolder userViewHolder = new UserViewHolder(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(v, userViewHolder.getPosition());
                }
            });
            return userViewHolder;
        }

        @Override
        public void onBindViewHolder(UserRecyclerAdapter.UserViewHolder holder, int position) {
            User currentUser = users.get(position);
            Picasso.with(getContext()).load(currentUser.getAvatar_url()).into(holder.imageView);
            holder.textView.setText(currentUser.getLogin());
        }

        @Override
        public int getItemCount() {
            return users.size();
        }

        class UserViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            TextView textView;

            public UserViewHolder(View itemView) {
                super(itemView);
                imageView = (ImageView) itemView.findViewById(R.id.image);
                textView = (TextView) itemView.findViewById(R.id.login_text_view);
            }
        }
    }
}