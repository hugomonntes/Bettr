package com.example.bettr.Adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bettr.Publicaciones.Habit;
import com.example.bettr.R;

import java.util.ArrayList;

public class AdapterFeed extends RecyclerView.Adapter<AdapterFeed.MyViewHolder> {
    private ArrayList<Habit> listaHabitos;

    public AdapterFeed(ArrayList<Habit> listaHabitos) {
        this.listaHabitos = listaHabitos;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Habit habit = listaHabitos.get(position);
        holder.tvUserName.setText(habit.getNombreUsuario());
        holder.tvPostInfo.setText(habit.getInfo());
        holder.tvDescription.setText(habit.getDescripcion());
        holder.tvLikes.setText(String.valueOf(habit.getLikes()));
        holder.tvStreak.setText("🔥 " + habit.getStreak());

        // Mostrar avatar del usuario
        if (habit.getUserAvatar() != null && !habit.getUserAvatar().isEmpty()) {
            Bitmap avatar = decodeBase64(habit.getUserAvatar());
            if (avatar != null) {
                holder.ivProfilePost.setImageBitmap(avatar);
            } else {
                holder.ivProfilePost.setImageResource(R.drawable.logobettr);
            }
        } else {
            holder.ivProfilePost.setImageResource(R.drawable.logobettr);
        }

        // Mostrar imagen del hábito (post)
        if (habit.getImageUrl() != null && !habit.getImageUrl().isEmpty()) {
            Bitmap postImage = decodeBase64(habit.getImageUrl());
            if (postImage != null) {
                holder.ivPostImage.setImageBitmap(postImage);
            } else {
                holder.ivPostImage.setImageResource(R.drawable.logobettr);
            }
        } else {
            holder.ivPostImage.setImageResource(R.drawable.logobettr);
        }
    }

    private Bitmap decodeBase64(String input) {
        try {
            byte[] decodedString = Base64.decode(input, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public int getItemCount() {
        return listaHabitos.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName, tvPostInfo, tvDescription, tvLikes, tvStreak;
        ImageView ivPostImage, ivProfilePost;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvPostInfo = itemView.findViewById(R.id.tvPostInfo);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            ivPostImage = itemView.findViewById(R.id.ivPostImage);
            ivProfilePost = itemView.findViewById(R.id.ivProfilePost);
            tvLikes = itemView.findViewById(R.id.tvLikes);
            tvStreak = itemView.findViewById(R.id.tvStreak);
        }
    }
}
