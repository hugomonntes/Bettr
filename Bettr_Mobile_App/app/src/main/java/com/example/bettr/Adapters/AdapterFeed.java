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

import com.example.bettr.ApiRest.Api_Inserts;
import com.example.bettr.Publicaciones.Habit;
import com.example.bettr.R;

import java.util.ArrayList;

public class AdapterFeed extends RecyclerView.Adapter<AdapterFeed.MyViewHolder> {
    private ArrayList<Habit> listaHabitos;
    private int userId;
    private Api_Inserts apiInserts;
    private Runnable onLikeChanged;

    public AdapterFeed(ArrayList<Habit> listaHabitos, int userId, Api_Inserts apiInserts, Runnable onLikeChanged) {
        this.listaHabitos = listaHabitos;
        this.userId = userId;
        this.apiInserts = apiInserts;
        this.onLikeChanged = onLikeChanged;
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
        
        holder.tvUserName.setText("@" + habit.getNombreUsuario());
        
        String habitType = habit.getHabitType();
        if (habitType != null && !habitType.isEmpty()) {
            String emoji = getHabitEmoji(habitType);
            holder.tvHabitType.setText(emoji + " " + habitType);
            holder.tvHabitType.setVisibility(View.VISIBLE);
        } else {
            holder.tvHabitType.setVisibility(View.GONE);
        }
        
        String timeAgo = getTimeAgo(habit.getCreatedAt());
        holder.tvPostInfo.setText("• " + timeAgo);
        
        if (habit.getDescripcion() != null && !habit.getDescripcion().isEmpty()) {
            holder.tvDescription.setText(habit.getDescripcion());
            holder.tvDescription.setVisibility(View.VISIBLE);
        } else {
            holder.tvDescription.setVisibility(View.GONE);
        }
        
        holder.tvLikes.setText(String.valueOf(habit.getLikes()));
        holder.tvStreak.setText("🔥 " + habit.getStreak());

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

        if (habit.getImageUrl() != null && !habit.getImageUrl().isEmpty()) {
            Bitmap postImage = decodeBase64(habit.getImageUrl());
            if (postImage != null) {
                holder.ivPostImage.setImageBitmap(postImage);
                holder.cvPostImage.setVisibility(View.VISIBLE);
            } else {
                holder.cvPostImage.setVisibility(View.GONE);
            }
        } else {
            holder.cvPostImage.setVisibility(View.GONE);
        }
        
        if (habit.getStreak() > 0) {
            holder.cvStreakCount.setVisibility(View.VISIBLE);
        } else {
            holder.cvStreakCount.setVisibility(View.GONE);
        }

        // Like button state
        if (habit.isLiked()) {
            holder.ivLike.setImageResource(android.R.drawable.btn_star_big_on);
            holder.ivLike.setColorFilter(0xFFFACC15);
        } else {
            holder.ivLike.setImageResource(android.R.drawable.btn_star_big_off);
            holder.ivLike.setColorFilter(0xFFFFFFFF);
        }

        // Like click listener
        holder.ivLike.setOnClickListener(v -> {
            int currentLikes = habit.getLikes();
            boolean currentlyLiked = habit.isLiked();
            
            // Toggle locally first for instant feedback
            habit.setLiked(!currentlyLiked);
            habit.setLikes(currentlyLiked ? currentLikes - 1 : currentLikes + 1);
            
            // Update UI
            holder.tvLikes.setText(String.valueOf(habit.getLikes()));
            if (habit.isLiked()) {
                holder.ivLike.setImageResource(android.R.drawable.btn_star_big_on);
                holder.ivLike.setColorFilter(0xFFFACC15);
            } else {
                holder.ivLike.setImageResource(android.R.drawable.btn_star_big_off);
                holder.ivLike.setColorFilter(0xFFFFFFFF);
            }

            // Call API
            if (currentlyLiked) {
                apiInserts.unlikeHabit(habit.getId(), userId, success -> {
                    if (!success) {
                        // Revert on failure
                        habit.setLiked(true);
                        habit.setLikes(currentLikes);
                    }
                });
            } else {
                apiInserts.likeHabit(habit.getId(), userId, success -> {
                    if (!success) {
                        // Revert on failure
                        habit.setLiked(false);
                        habit.setLikes(currentLikes);
                    }
                });
            }
        });
    }

    private String getHabitEmoji(String habitType) {
        switch (habitType) {
            case "Ejercicio": return "💪";
            case "Lectura": return "📚";
            case "Meditación": return "🧘";
            case "Estudio": return "📖";
            case "Saludable": return "🥗";
            default: return "⭐";
        }
    }

    private String getTimeAgo(String dateString) {
        if (dateString == null || dateString.isEmpty()) return "ahora";
        
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", java.util.Locale.getDefault());
            sdf.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
            java.util.Date date = sdf.parse(dateString.substring(0, Math.min(dateString.length(), 19)));
            
            if (date == null) return "ahora";
            
            long diff = System.currentTimeMillis() - date.getTime();
            
            if (diff < 60000) return "ahora";
            if (diff < 3600000) return Math.floor(diff / 60000) + "m";
            if (diff < 86400000) return Math.floor(diff / 3600000) + "h";
            if (diff < 604800000) return Math.floor(diff / 86400000) + "d";
            return Math.floor(diff / 604800000) + "sem";
        } catch (Exception e) {
            return "recientemente";
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
        TextView tvUserName, tvPostInfo, tvDescription, tvLikes, tvStreak, tvHabitType, tvComments;
        ImageView ivPostImage, ivProfilePost, ivLike, ivComment, ivShare;
        View cvPostImage, cvStreakCount;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvPostInfo = itemView.findViewById(R.id.tvPostInfo);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvLikes = itemView.findViewById(R.id.tvLikes);
            tvStreak = itemView.findViewById(R.id.tvStreak);
            tvHabitType = itemView.findViewById(R.id.tvHabitType);
            tvComments = itemView.findViewById(R.id.tvComments);
            ivPostImage = itemView.findViewById(R.id.ivPostImage);
            ivProfilePost = itemView.findViewById(R.id.ivProfilePost);
            ivLike = itemView.findViewById(R.id.ivLike);
            ivComment = itemView.findViewById(R.id.ivComment);
            ivShare = itemView.findViewById(R.id.ivShare);
            cvPostImage = itemView.findViewById(R.id.cvPostImage);
            cvStreakCount = itemView.findViewById(R.id.cvStreakCount);
        }
    }
}

