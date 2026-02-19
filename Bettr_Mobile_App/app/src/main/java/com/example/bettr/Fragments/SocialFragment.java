package com.example.bettr.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import com.example.bettr.UserSession;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class SocialFragment extends Fragment {

    private RecyclerView rvUsers;
    private AdapterUsers adapter;
    private ArrayList<User> allUsersList = new ArrayList<>();
    private Api_Gets apiGets;
    private Api_Inserts apiInserts;
    private UserSession session;
    private int myUserId;
    private TextInputEditText etSearch;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_social, container, false);

        apiGets = new Api_Gets();
        apiInserts = new Api_Inserts();
        session = new UserSession(requireContext());
        
        SharedPreferences prefs = requireActivity().getSharedPreferences("BettrPrefs", Context.MODE_PRIVATE);
        myUserId = prefs.getInt("userId", -1);

        rvUsers = view.findViewById(R.id.rvUsers);
        rvUsers.setLayoutManager(new LinearLayoutManager(getContext()));

        etSearch = view.findViewById(R.id.etSearch);
        TextInputLayout tilSearch = view.findViewById(R.id.tilSearch);

        if (tilSearch != null) {
            tilSearch.setEndIconOnClickListener(v -> {
                String query = etSearch.getText().toString().trim();
                searchUsers(query);
            });
        }

        loadInitialUsers();

        return view;
    }

    private void loadInitialUsers() {
        if (getActivity() instanceof Feed) {
            ((Feed) getActivity()).showLoading();
        }
        
        apiGets.searchUsers("", users -> {
            if (isAdded() && getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    if (getActivity() instanceof Feed) {
                        ((Feed) getActivity()).hideLoading();
                    }
                    updateList(users);
                });
            }
        });
    }

    private void searchUsers(String query) {
        if (getActivity() instanceof Feed) {
            ((Feed) getActivity()).showLoading();
        }
        
        apiGets.searchUsers(query, users -> {
            if (isAdded() && getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    if (getActivity() instanceof Feed) {
                        ((Feed) getActivity()).hideLoading();
                    }
                    updateList(users);
                });
            }
        });
    }

    private void updateList(ArrayList<User> users) {
        allUsersList.clear();
        for (User u : users) {
            if (u.getId() != myUserId) {
                allUsersList.add(u);
            }
        }
        adapter = new AdapterUsers(allUsersList, (user, position) -> {
            followUser(user, position);
        });
        rvUsers.setAdapter(adapter);
    }

    private void followUser(User userToFollow, int position) {
        if (myUserId == -1 || userToFollow.isFollowing()) {
            return;
        }
        
        if (getActivity() instanceof Feed) {
            ((Feed) getActivity()).showLoading();
        }
        
        apiInserts.followUser(myUserId, userToFollow.getId(), success -> {
            if (isAdded() && getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    if (getActivity() instanceof Feed) {
                        ((Feed) getActivity()).hideLoading();
                    }
                    if (success) {
                        userToFollow.setFollowing(true);
                        session.addFollowing(userToFollow.getId());
                        if (adapter != null) {
                            adapter.notifyItemChanged(position);
                        }
                    }
                });
            }
        });
    }
}
