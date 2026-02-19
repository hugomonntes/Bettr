package com.example.bettr.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bettr.Adapters.AdapterFeed;
import com.example.bettr.ApiRest.Api_Gets;
import com.example.bettr.ApiRest.Api_Inserts;
import com.example.bettr.Feed;
import com.example.bettr.Publicaciones.Habit;
import com.example.bettr.R;

import java.util.ArrayList;

public class HabitsFragment extends Fragment {

    private RecyclerView rvMyHabits;
    private AdapterFeed adapter;
    private final ArrayList<Habit> listaHabitos = new ArrayList<>();
    private Api_Gets apiGets;
    private Api_Inserts apiInserts;
    private int myUserId;
    private FrameLayout loadingOverlay;
    private LinearLayout emptyState;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_habits, container, false);

        apiGets = new Api_Gets();
        apiInserts = new Api_Inserts();
        
        rvMyHabits = view.findViewById(R.id.rvMyHabits);
        rvMyHabits.setLayoutManager(new LinearLayoutManager(getContext()));
        loadingOverlay = view.findViewById(R.id.loading_overlay);
        emptyState = view.findViewById(R.id.emptyState);

        SharedPreferences prefs = requireActivity().getSharedPreferences("BettrPrefs", Context.MODE_PRIVATE);
        myUserId = prefs.getInt("userId", -1);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadMyHabits();
    }

    private void loadMyHabits() {
        if (myUserId == -1) {
            showEmptyState();
            return;
        }

        showLoading();

        apiGets.getHabitsByUserId(myUserId, habits -> {
            if (isAdded() && getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    hideLoading();
                    if (habits != null && !habits.isEmpty()) {
                        listaHabitos.clear();
                        listaHabitos.addAll(habits);
                        adapter = new AdapterFeed(listaHabitos, myUserId, apiInserts, () -> loadMyHabits());
                        rvMyHabits.setAdapter(adapter);
                        rvMyHabits.setVisibility(View.VISIBLE);
                        emptyState.setVisibility(View.GONE);
                    } else {
                        showEmptyState();
                    }
                });
            }
        });
    }

    private void showEmptyState() {
        rvMyHabits.setVisibility(View.GONE);
        emptyState.setVisibility(View.VISIBLE);
    }

    private void showLoading() {
        if (loadingOverlay != null) {
            loadingOverlay.setVisibility(View.VISIBLE);
        }
    }

    private void hideLoading() {
        if (loadingOverlay != null) {
            loadingOverlay.setVisibility(View.GONE);
        }
    }
}
