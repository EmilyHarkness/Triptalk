package com.example.emily.triptalk.Login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.users_recycler, null);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setVisibility(ProgressBar.VISIBLE);

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
                if (response.isSuccessful()) {
                    //ok
                    users = response.body();
                    userAdapter.users.clear();
                    userAdapter.users.addAll(users);
                    userAdapter.notifyDataSetChanged();
                } else {
                    //bad request
                }
                progressBar.setVisibility(ProgressBar.INVISIBLE);
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                //no network
                progressBar.setVisibility(ProgressBar.INVISIBLE);
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