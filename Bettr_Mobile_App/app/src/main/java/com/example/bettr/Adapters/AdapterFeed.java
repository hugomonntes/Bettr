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

        String imageData = habit.getImageUrl();
        if (imageData != null && !imageData.isEmpty()) {
            try {
                byte[] decodedString = Base64.decode(imageData, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                if (decodedByte != null) {
                    holder.ivPostImage.setImageBitmap(decodedByte);
                } else {
                    holder.ivPostImage.setImageResource(R.drawable.logobettr);
                }
            } catch (Exception e) {
                holder.ivPostImage.setImageResource(R.drawable.logobettr);
            }
        } else {
            holder.ivPostImage.setImageResource(R.drawable.logobettr);
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
