package com.example.bettr.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bettr.ApiRest.Api_Inserts;
import com.example.bettr.R;

import java.io.IOException;

public class CameraFragment extends Fragment {

    private ImageView ivPreview;
    private EditText etDescription;
    private Button btnCamera, btnGallery, btnPost;
    private Bitmap selectedBitmap;
    private Api_Inserts apiInserts;

    // Launcher para la Cámara
    private final ActivityResultLauncher<Intent> cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Bundle extras = result.getData().getExtras();
                    selectedBitmap = (Bitmap) extras.get("data");
                    ivPreview.setImageBitmap(selectedBitmap);
                    ivPreview.setImageTintList(null); // Quitar el tinte gris del icono
                }
            }
    );

    // Launcher para la Galería
    private final ActivityResultLauncher<String> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    try {
                        selectedBitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), uri);
                        ivPreview.setImageBitmap(selectedBitmap);
                        ivPreview.setImageTintList(null);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
    );

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);

        apiInserts = new Api_Inserts();
        ivPreview = view.findViewById(R.id.ivPreview);
        etDescription = view.findViewById(R.id.etDescription);
        btnCamera = view.findViewById(R.id.btnCamera);
        btnGallery = view.findViewById(R.id.btnGallery);
        btnPost = view.findViewById(R.id.btnPost);

        btnCamera.setOnClickListener(v -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraLauncher.launch(intent);
        });

        btnGallery.setOnClickListener(v -> galleryLauncher.launch("image/*"));

        btnPost.setOnClickListener(v -> postHabit());

        return view;
    }

    private void postHabit() {
        String description = etDescription.getText().toString().trim();
        
        if (selectedBitmap == null) {
            Toast.makeText(getContext(), "Please select or take a photo first", Toast.LENGTH_SHORT).show();
            return;
        }

        if (description.isEmpty()) {
            Toast.makeText(getContext(), "Please write a description", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO: En una app real, aquí subirías el Bitmap a un servidor (Cloudinary/Firebase)
        // y obtendrías una URL. Por ahora usaremos una URL de ejemplo.
        String dummyImageUrl = "https://bettr.app/sample_habit.jpg";

        // id_usuario fijo a 1 para pruebas, deberías usar el ID del usuario logueado
        apiInserts.addHabit(1, description, dummyImageUrl, "Daily Habit", success -> {
            if (isAdded()) {
                requireActivity().runOnUiThread(() -> {
                    if (success) {
                        Toast.makeText(getContext(), "Habit posted successfully!", Toast.LENGTH_SHORT).show();
                        etDescription.setText("");
                        ivPreview.setImageResource(android.R.drawable.ic_menu_camera);
                        selectedBitmap = null;
                    } else {
                        Toast.makeText(getContext(), "Error posting habit", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
