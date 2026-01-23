package com.example.bettr.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bettr.R;

import java.util.ArrayList;
import java.util.Objects;

public class AdapterIndex extends RecyclerView.Adapter<AdapterIndex.MyViewHolder>{ // TODO crear la clase Usuario,la forma de inyectar usuarios, clase post,
	// TODO crear celda para los post, igual crear celda para la barra de dias de streak, completar hábitos.
	ArrayList<Objects> usuarios = new ArrayList<>();

	public AdapterIndex(ArrayList<Objects> usuarios){
		this.usuarios = usuarios;
	}

	@NonNull
	@Override
	public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View elemento= LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_index, // TODO Crear celda
				parent, false);
		MyViewHolder mvh = new MyViewHolder(elemento);
		return mvh ;
	}

	@Override
	public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
		Object usuario = this.usuarios.get(position);
	}

	@Override
	public int getItemCount() {
		return usuarios.size();
	}

	public class MyViewHolder extends RecyclerView.ViewHolder {
		public MyViewHolder(View viewElemento) {
			super(viewElemento);
		}
	}
}
