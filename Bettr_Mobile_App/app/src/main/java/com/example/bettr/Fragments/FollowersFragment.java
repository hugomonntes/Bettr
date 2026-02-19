package com.example.bettr.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bettr.Adapters.AdapterFollowers;
import com.example.bettr.ApiRest.Api_Gets;
import com.example.bettr.Dao.User;
import com.example.bettr.R;

import java.util.ArrayList;

public class FollowersFragment extends Fragment {

    private RecyclerView rvUsers;
    private AdapterFollowers adapter;
    private final ArrayList<User> usersList = new ArrayList<>();
    private Api_Gets apiGets;
    private int userId;
    private TextView tvEmpty;
    private String fragmentType;

    public static FollowersFragment newInstance(int userId, String type) {
        FollowersFragment fragment = new FollowersFragment();
        Bundle args = new Bundle();
        args.putInt("userId", userId);
        args.putString("type", type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getInt("userId", -1);
            fragmentType = getArguments().getString("type", "followers");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_followers, container, false);

        apiGets = new Api_Gets();
        rvUsers = view.findViewById(R.id.rvUsers);
        tvEmpty = view.findViewById(R.id.tvEmpty);

        rvUsers.setLayoutManager(new LinearLayoutManager(getContext()));

        loadUsers();

        return view;
    }

    private void loadUsers() {
        if (fragmentType.equals("followers")) {
            apiGets.getFollowers(userId, users -> {
                displayUsers(users);
            });
        } else {
            apiGets.getFollowing(userId, users -> {
                displayUsers(users);
            });
        }
    }

    private void displayUsers(ArrayList<User> users) {
        if (isAdded() && getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                if (users != null && !users.isEmpty()) {
                    usersList.clear();
                    usersList.addAll(users);
                    adapter = new AdapterFollowers(usersList, (user, position) -> {
                    });
                    rvUsers.setAdapter(adapter);
                    rvUsers.setVisibility(View.VISIBLE);
                    tvEmpty.setVisibility(View.GONE);
                } else {
                    rvUsers.setVisibility(View.GONE);
                    tvEmpty.setVisibility(View.VISIBLE);
                    if (fragmentType.equals("followers")) {
                        tvEmpty.setText("No hay seguidores aún");
                    } else {
                        tvEmpty.setText("No sigues a nadie");
                    }
                }
            });
        }
    }
}

