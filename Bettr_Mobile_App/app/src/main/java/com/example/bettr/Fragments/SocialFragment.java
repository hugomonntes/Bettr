package com.example.appmovil.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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
import com.example.appmovil.Feed;
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
    private FrameLayout loadingOverlay;
    private View emptyState;

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
        tilSearch = view.findViewById(R.id.tilSearch);
        loadingOverlay = view.findViewById(R.id.loading_overlay);
        emptyState = view.findViewById(R.id.emptyState);

        if (etSearch != null) {
            etSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    // Debounce search
                    etSearch.removeCallbacks(searchRunnable);
                    etSearch.postDelayed(searchRunnable, 500);
                }
            });
        }

        if (tilSearch != null) {
            tilSearch.setEndIconOnClickListener(v -> {
                String query = etSearch.getText().toString().trim();
                searchUsers(query);
            });
        }

        loadInitialUsers();

        return view;
    }

    private final Runnable searchRunnable = new Runnable() {
        @Override
        public void run() {
            if (etSearch != null) {
                String query = etSearch.getText().toString().trim();
                searchUsers(query);
            }
        }
    };

    private void loadInitialUsers() {
        showLoading();
        
        apiGets.searchUsers("", users -> {
            if (isAdded() && getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    hideLoading();
                    updateList(users);
                });
            }
        });
    }

    private void searchUsers(String query) {
        showLoading();
        
        apiGets.searchUsers(query, users -> {
            if (isAdded() && getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    hideLoading();
                    updateList(users);
                });
            }
        });
    }

    private void updateList(ArrayList<User> users) {
        allUsersList.clear();
        if (users != null) {
            for (User u : users) {
                if (u.getId() != myUserId) {
                    allUsersList.add(u);
                }
            }
        }
        
        if (allUsersList.isEmpty()) {
            showEmptyState();
        } else {
            hideEmptyState();
            adapter = new AdapterUsers(allUsersList, (user, position) -> {
                followUser(user, position);
            });
            rvUsers.setAdapter(adapter);
        }
    }

    private void followUser(User userToFollow, int position) {
        if (myUserId == -1) {
            Toast.makeText(getContext(), "Error: Usuario no identificado", Toast.LENGTH_SHORT).show();
            return;
        }
        
        showLoading();
        
        if (userToFollow.isFollowing()) {
            apiInserts.unfollowUser(myUserId, userToFollow.getId(), success -> {
                if (isAdded() && getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        hideLoading();
                        if (success) {
                            userToFollow.setFollowing(false);
                            session.removeFollowing(userToFollow.getId());
                            if (adapter != null) {
                                adapter.notifyItemChanged(position);
                            }
                            Toast.makeText(getContext(), "Has dejado de seguir a @" + userToFollow.getUsername(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Error al dejar de seguir", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        } else {
            apiInserts.followUser(myUserId, userToFollow.getId(), success -> {
                if (isAdded() && getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        hideLoading();
                        if (success) {
                            userToFollow.setFollowing(true);
                            session.addFollowing(userToFollow.getId());
                            if (adapter != null) {
                                adapter.notifyItemChanged(position);
                            }
                            Toast.makeText(getContext(), "¡Ahora sigues a @" + userToFollow.getUsername(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Error al seguir usuario", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }

    private void showLoading() {
        if (getActivity() instanceof Feed) {
            ((Feed) getActivity()).showLoading();
        }
    }

    private void hideLoading() {
        if (getActivity() instanceof Feed) {
            ((Feed) getActivity()).hideLoading();
        }
    }

    private void showEmptyState() {
        if (rvUsers != null) rvUsers.setVisibility(View.GONE);
        if (emptyState != null) emptyState.setVisibility(View.VISIBLE);
    }

    private void hideEmptyState() {
        if (rvUsers != null) rvUsers.setVisibility(View.VISIBLE);
        if (emptyState != null) emptyState.setVisibility(View.GONE);
    }

    private TextInputLayout tilSearch;
}
