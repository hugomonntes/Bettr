package com.example.bettr;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bettr.Adapters.AdapterFeed;
import com.example.bettr.ApiRest.Api_Gets;
import com.example.bettr.Publicaciones.Publicaciones;

import java.util.ArrayList;

public class Feed extends AppCompatActivity {

    private RecyclerView rvFeed;
    private AdapterFeed adapter;
    private ArrayList<Publicaciones> listaPublicaciones = new ArrayList<>();
    private Api_Gets apiGets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_feed);

        apiGets = new Api_Gets();
        rvFeed = findViewById(R.id.rvFeed);
        rvFeed.setLayoutManager(new LinearLayoutManager(this));

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loadPosts();
    }

    private void loadPosts() {
        apiGets.getPublicaciones(posts -> {
            runOnUiThread(() -> {
                if (posts != null && !posts.isEmpty()) {
                    listaPublicaciones.clear();
                    listaPublicaciones.addAll(posts);
                    adapter = new AdapterFeed(listaPublicaciones);
                    rvFeed.setAdapter(adapter);
                } else {
                    Log.i("Error al cargar", "No se pudieron cargar las publicaciones");;
                    // Datos de prueba por si la API falla o está vacía
                    setupData();
                }
            });
        });
    }

    private void setupData() {
        listaPublicaciones.add(new Publicaciones("Diego Costa", "", "Soy el mejor.", "Entrenamiento Futbol • 1h", 234, 45));
        listaPublicaciones.add(new Publicaciones("Carlos Alberto", "", "Programando", "Programación • 2h", 120, 12));
        listaPublicaciones.add(new Publicaciones("Iago Doval 99", "", "Bajando de nivel", "Padel • 1h", 99, 22));
        adapter = new AdapterFeed(listaPublicaciones);
        rvFeed.setAdapter(adapter);
    }
}
