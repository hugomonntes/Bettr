package com.example.bettr.Adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bettr.Dao.User;
import com.example.bettr.R;

import java.util.ArrayList;

public class AdapterUsers extends RecyclerView.Adapter<AdapterUsers.ViewHolder> {

    private ArrayList<User> users;
    private OnFollowClickListener listener;

    public interface OnFollowClickListener {
        void onFollowClick(User user, int position);
    }

    public AdapterUsers(ArrayList<User> users, OnFollowClickListener listener) {
        this.users = users;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = users.get(position);
        holder.tvUserName.setText("@" + user.getUsername());
        holder.tvFullName.setText(user.getName());
        
        holder.ivAvatar.setImageResource(R.drawable.logobettr);

        // Cambiamos el estado del botón según si ya lo seguimos
        if (user.isFollowing()) {
            holder.btnFollow.setText("Siguiendo");
            holder.btnFollow.setBackgroundColor(Color.parseColor("#374151")); // Gris oscuro
            holder.btnFollow.setTextColor(Color.WHITE);
        } else {
            holder.btnFollow.setText("Seguir");
            holder.btnFollow.setBackgroundColor(Color.parseColor("#FACC15")); // Amarillo Bettr
            holder.btnFollow.setTextColor(Color.parseColor("#1C1F22"));
        }

        holder.btnFollow.setOnClickListener(v -> {
            if (listener != null) {
                listener.onFollowClick(user, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivAvatar;
        TextView tvUserName, tvFullName;
        Button btnFollow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.ivUserAvatar);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvFullName = itemView.findViewById(R.id.tvFullName);
            btnFollow = itemView.findViewById(R.id.btnFollow);
        }
    }
}
