package com.example.bettr.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bettr.R;

import java.util.ArrayList;
import java.util.Objects;

public class AdapterIndex extends RecyclerView.Adapter<AdapterIndex.MyViewHolder>{ 
	ArrayList<Objects> usuarios = new ArrayList<>();

	public AdapterIndex(ArrayList<Objects> usuarios){
		this.usuarios = usuarios;
	}

	@NonNull
	@Override
	public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View elemento = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
		return new MyViewHolder(elemento);
	}

	@Override
	public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
		Object usuario = this.usuarios.get(position);
	}

	@Override
	public int getItemCount() {
		return usuarios.size();
	}

	public static class MyViewHolder extends RecyclerView.ViewHolder {
		public MyViewHolder(View viewElemento) {
			super(viewElemento);
		}
	}
}
