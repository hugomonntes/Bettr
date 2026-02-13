package com.example.bettr.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bettr.ApiRest.Api_Inserts;
import com.example.bettr.Feed;
import com.example.bettr.R;

import java.io.IOException;

public class CameraFragment extends Fragment {

    private ImageView ivPreview;
    private EditText etDescription;
    private Button btnCamera, btnGallery, btnPost;
    private Bitmap selectedBitmap;
    private Api_Inserts apiInserts;

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
        if (selectedBitmap == null || description.isEmpty()) return;

        if (getActivity() instanceof Feed) {
            ((Feed) getActivity()).showLoading();
        }

        // URL de ejemplo hasta integrar subida real de imagen
        String dummyImageUrl = "https://bettr-g5yv.onrender.com/uploads/sample.jpg";

        apiInserts.addHabit(1, description, dummyImageUrl, "Deporte", success -> {
            if (isAdded()) {
                requireActivity().runOnUiThread(() -> {
                    if (getActivity() instanceof Feed) {
                        ((Feed) getActivity()).hideLoading();
                        if (success) {
                            ((Feed) getActivity()).navigateToHome();
                        }
                    }
                });
            }
        });
    }
}
