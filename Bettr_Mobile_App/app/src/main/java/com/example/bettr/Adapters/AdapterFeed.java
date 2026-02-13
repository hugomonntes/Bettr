package com.example.bettr.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bettr.Publicaciones.Publicaciones;
import com.example.bettr.R;

import java.util.ArrayList;

public class AdapterFeed extends RecyclerView.Adapter<AdapterFeed.MyViewHolder> {
    private ArrayList<Publicaciones> listaPublicaciones;

    public AdapterFeed(ArrayList<Publicaciones> listaPublicaciones) {
        this.listaPublicaciones = listaPublicaciones;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Publicaciones post = listaPublicaciones.get(position);
        holder.tvUserName.setText(post.getNombreUsuario());
        holder.tvPostInfo.setText(post.getInfo());
        holder.tvDescription.setText(post.getDescripcion());
        holder.tvLikes.setText(String.valueOf(post.getLikes()));
        holder.tvStreak.setText("🔥 " + post.getStreak());

        // Por ahora usamos el logo como placeholder. 
        // Si tienes una URL real, aquí es donde usarías Glide.with(context).load(post.getImageUrl()).into(holder.ivPostImage);
        holder.ivPostImage.setImageResource(R.drawable.logobettr);
    }

    @Override
    public int getItemCount() {
        return listaPublicaciones.size();
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
