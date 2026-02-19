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

import com.example.bettr.Dao.User;
import com.example.bettr.R;

import java.util.ArrayList;

public class AdapterFollowers extends RecyclerView.Adapter<AdapterFollowers.ViewHolder> {

    private ArrayList<User> users;
    private OnUserClickListener listener;

    public interface OnUserClickListener {
        void onUserClick(User user, int position);
    }

    public AdapterFollowers(ArrayList<User> users, OnUserClickListener listener) {
        this.users = users;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_follower, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = users.get(position);
        
        holder.tvUserName.setText("@" + user.getUsername());
        
        holder.tvFullName.setText(user.getName());
        
        if (user.getAvatarUrl() != null && !user.getAvatarUrl().isEmpty()) {
            Bitmap avatar = decodeBase64(user.getAvatarUrl());
            if (avatar != null) {
                holder.ivAvatar.setImageBitmap(avatar);
            } else {
                holder.ivAvatar.setImageResource(R.drawable.logobettr);
            }
        } else {
            holder.ivAvatar.setImageResource(R.drawable.logobettr);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onUserClick(user, position);
            }
        });
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
        return users != null ? users.size() : 0;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivAvatar;
        TextView tvUserName, tvFullName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.ivUserAvatar);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvFullName = itemView.findViewById(R.id.tvFullName);
        }
    }
}

