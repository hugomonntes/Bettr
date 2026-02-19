package com.example.bettr.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bettr.ApiRest.Api_Inserts;
import com.example.bettr.Feed;
import com.example.bettr.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CameraFragment extends Fragment {

    private ImageView ivPreview;
    private EditText etDescription;
    private AutoCompleteTextView spinnerHabitType;
    private Button btnCamera, btnGallery, btnPost;
    private Bitmap selectedBitmap;
    private Api_Inserts apiInserts;
    private FrameLayout loadingOverlay;

    // Habit types matching the web app
    private final String[] habitTypes = {
            "Ejercicio",
            "Lectura",
            "Meditación",
            "Estudio",
            "Saludable",
            "Otro"
    };

    private final String[] habitTypeEmojis = {
            "💪 Ejercicio",
            "📚 Lectura",
            "🧘 Meditación",
            "📖 Estudio",
            "🥗 Comida Saludable",
            "⭐ Otro"
    };

    private final ActivityResultLauncher<Intent> cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Bundle extras = result.getData().getExtras();
                    selectedBitmap = (Bitmap) extras.get("data");
                    ivPreview.setImageBitmap(selectedBitmap);
                    ivPreview.setImageTintList(null);
                }
            }
    );

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
        spinnerHabitType = view.findViewById(R.id.spinnerHabitType);
        btnCamera = view.findViewById(R.id.btnCamera);
        btnGallery = view.findViewById(R.id.btnGallery);
        btnPost = view.findViewById(R.id.btnPost);
        loadingOverlay = view.findViewById(R.id.camera_loading_overlay);

        // Setup habit type dropdown
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                habitTypeEmojis
        );
        spinnerHabitType.setAdapter(adapter);
        spinnerHabitType.setText(habitTypeEmojis[0], false);

        if (btnCamera != null) {
            btnCamera.setOnClickListener(v -> {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraLauncher.launch(intent);
            });
        }

        if (btnGallery != null) {
            btnGallery.setOnClickListener(v -> {
                galleryLauncher.launch("image/*");
            });
        }

        if (btnPost != null) {
            btnPost.setOnClickListener(v -> {
                postHabit();
            });
        }

        return view;
    }

    private void postHabit() {
        if (etDescription == null || spinnerHabitType == null) {
            return;
        }
        
        String description = etDescription.getText().toString().trim();
        String selectedType = spinnerHabitType.getText().toString();
        
        // Get plain habit type without emoji
        String habitType = selectedType;
        for (int i = 0; i < habitTypeEmojis.length; i++) {
            if (selectedType.equals(habitTypeEmojis[i])) {
                habitType = habitTypes[i];
                break;
            }
        }
        
        if (selectedBitmap == null || description.isEmpty()) {
            Toast.makeText(getContext(), "Añade una imagen y descripción", Toast.LENGTH_SHORT).show();
            return;
        }

        showLoading();

        SharedPreferences prefs = requireActivity().getSharedPreferences("BettrPrefs", Context.MODE_PRIVATE);
        int userId = prefs.getInt("userId", -1);

        if (userId == -1) {
            hideLoading();
            return;
        }

        String imageBase64 = encodeImageToBase64(selectedBitmap);

        apiInserts.addHabit(userId, description, imageBase64, habitType, success -> {
            if (isAdded() && getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    hideLoading();
                    if (success) {
                        Toast.makeText(getContext(), "¡Hábito compartido!", Toast.LENGTH_SHORT).show();
                        // Clear form
                        etDescription.setText("");
                        ivPreview.setImageResource(android.R.drawable.ic_menu_camera);
                        selectedBitmap = null;
                        
                        // Navigate to home
                        if (getActivity() instanceof Feed) {
                            ((Feed) getActivity()).navigateToHome();
                        }
                    } else {
                        Toast.makeText(getContext(), "Error al compartir hábito", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private String encodeImageToBase64(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream);
        byte[] byteArray = outputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
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
