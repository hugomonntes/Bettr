package com.example.bettr.Fragments;

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

import com.example.bettr.Adapters.AdapterFeed;
import com.example.bettr.ApiRest.Api_Gets;
import com.example.bettr.Publicaciones.Publicaciones;
import com.example.bettr.R;

import java.util.ArrayList;

public class FeedFragment extends Fragment {

    private RecyclerView rvFeed;
    private AdapterFeed adapter;
    private ArrayList<Publicaciones> listaPublicaciones = new ArrayList<>();
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
        apiGets.getPublicaciones(posts -> {
            if (isAdded()) {
                getActivity().runOnUiThread(() -> {
                    if (posts != null && !posts.isEmpty()) {
                        listaPublicaciones.clear();
                        listaPublicaciones.addAll(posts);
                        adapter = new AdapterFeed(listaPublicaciones);
                        rvFeed.setAdapter(adapter);
                    } else {
                        setupDummyData();
                    }
                });
            }
        });
    }

    private void setupDummyData() {
        listaPublicaciones.clear();
        listaPublicaciones.add(new Publicaciones("Diego Costa", "", "Todos unos matados, putos perroflas", "Entrenamiento Futbol • 2h", 234, 45));
        listaPublicaciones.add(new Publicaciones("Carlos Italiani", "", "Embaraja", "Cartas • 22h", 120, 12));
        listaPublicaciones.add(new Publicaciones("Iago Doval", "", "Costa espabila", "Clase • 5min", 1120, 12));
        adapter = new AdapterFeed(listaPublicaciones);
        rvFeed.setAdapter(adapter);
    }
}
