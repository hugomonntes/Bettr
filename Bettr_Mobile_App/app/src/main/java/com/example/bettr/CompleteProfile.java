package com.example.bettr;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bettr.ApiRest.Api_Gets;
import com.example.bettr.ApiRest.Api_Inserts;
import com.google.android.material.textfield.TextInputEditText;

public class CompleteProfile extends AppCompatActivity {
    TextInputEditText etBio, etUserName;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_complete_profile);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etBio = findViewById(R.id.etBio);
        etUserName = findViewById(R.id.etUserName);

//        int id = 0;
//        Api_Gets apiGets = null;
//        apiGets.getUserByUsername(etUserName.getText().toString(), success -> {
//            runOnUiThread(()->{
//                if (success) {
//                    // id
//                }
//            });
//        });

//        Api_Inserts apiInserts = null;
//        apiInserts.insertDescription(id,etBio.getText().toString(),success -> { // TODO me falta meter id
//            runOnUiThread(() -> {
//                if (success) {
//                    Toast.makeText(CompleteProfile.this, "Perfil Completado", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(CompleteProfile.this, CompleteProfile.class);
//                    startActivity(intent);
//                    finish();
//                } else {
//                    Toast.makeText(CompleteProfile.this, "Error al completar el perfil", Toast.LENGTH_SHORT).show();
//                }
//            });
//        });

//        Button btnFinish = findViewById(R.id.btnFinishProfile);
//        btnFinish.setOnClickListener(v -> {
//            Toast.makeText(this, "Perfil completado", Toast.LENGTH_SHORT).show();
//            // Aquí tengo que meter el Feed principal
//            finish();
//        });
    }
}
