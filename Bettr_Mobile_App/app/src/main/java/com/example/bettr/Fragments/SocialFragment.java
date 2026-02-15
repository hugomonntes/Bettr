package com.example.bettr.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bettr.Adapters.AdapterUsers;
import com.example.bettr.ApiRest.Api_Gets;
import com.example.bettr.ApiRest.Api_Inserts;
import com.example.bettr.Dao.User;
import com.example.bettr.Feed;
import com.example.bettr.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class SocialFragment extends Fragment {

    private RecyclerView rvUsers;
    private AdapterUsers adapter;
    private ArrayList<User> allUsers = new ArrayList<>();
    private Api_Gets apiGets;
    private Api_Inserts apiInserts;
    private int myUserId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_social, container, false);

        apiGets = new Api_Gets();
        apiInserts = new Api_Inserts();
        
        SharedPreferences prefs = requireActivity().getSharedPreferences("BettrPrefs", Context.MODE_PRIVATE);
        myUserId = prefs.getInt("userId", -1);

        rvUsers = view.findViewById(R.id.rvUsers);
        rvUsers.setLayoutManager(new LinearLayoutManager(getContext()));

        TextInputEditText etSearch = view.findViewById(R.id.etSearch);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterUsers(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        loadUsers();

        return view;
    }

    private void loadUsers() {
        if (getActivity() instanceof Feed) ((Feed) getActivity()).showLoading();
        
        apiGets.searchUsers("", users -> {
            if (isAdded() && getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    if (getActivity() instanceof Feed) ((Feed) getActivity()).hideLoading();
                    allUsers.clear();
                    for (User u : users) {
                        if (u.getId() != myUserId) allUsers.add(u);
                    }
                    adapter = new AdapterUsers(new ArrayList<>(allUsers), this::followUser);
                    rvUsers.setAdapter(adapter);
                });
            }
        });
    }

    private void filterUsers(String text) {
        ArrayList<User> filteredList = new ArrayList<>();
        for (User user : allUsers) {
            if (user.getUsername().toLowerCase().contains(text.toLowerCase()) || 
                user.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(user);
            }
        }
        if (adapter != null) {
            adapter.setUsers(filteredList);
        }
    }

    private void followUser(User userToFollow) {
        if (myUserId == -1) return;
        
        if (getActivity() instanceof Feed) ((Feed) getActivity()).showLoading();
        
        apiInserts.followUser(myUserId, userToFollow.getId(), success -> {
            if (isAdded() && getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    if (getActivity() instanceof Feed){
                        ((Feed) getActivity()).hideLoading();
                    }
                });
            }
        });
    }
}
