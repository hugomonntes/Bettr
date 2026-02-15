package com.example.bettr.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bettr.Adapters.AdapterFeed;
import com.example.bettr.ApiRest.Api_Gets;
import com.example.bettr.Publicaciones.Habit;
import com.example.bettr.R;

import java.util.ArrayList;

public class FeedFragment extends Fragment {

    private RecyclerView rvFeed;
    private AdapterFeed adapter;
    private final ArrayList<Habit> listaHabitos = new ArrayList<>();
    private Api_Gets apiGets;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        apiGets = new Api_Gets();
        rvFeed = view.findViewById(R.id.rvFeed);
        rvFeed.setLayoutManager(new LinearLayoutManager(getContext()));

        loadPosts();

        return view;
    }

    private void loadPosts() {
        apiGets.getHabits(habits -> {
            if (isAdded() && getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    if (habits != null && !habits.isEmpty()) {
                        listaHabitos.clear();
                        listaHabitos.addAll(habits);
                        adapter = new AdapterFeed(listaHabitos);
                        rvFeed.setAdapter(adapter);
                    } else {
                        setupDummyData();
                    }
                });
            }
        });
    }

    private void setupDummyData() {
        listaHabitos.clear();
        listaHabitos.add(new Habit("Diego Costa", "", "Todos unos matados, putos perroflas", "Entrenamiento Futbol • 2h", 234, 45));
        listaHabitos.add(new Habit("Carlos Italiani", "", "Embaraja", "Cartas • 22h", 120, 12));
        listaHabitos.add(new Habit("Iago Doval", "", "Costa espabila", "Clase • 5min", 1120, 12));
        adapter = new AdapterFeed(listaHabitos);
        rvFeed.setAdapter(adapter);
    }
}
