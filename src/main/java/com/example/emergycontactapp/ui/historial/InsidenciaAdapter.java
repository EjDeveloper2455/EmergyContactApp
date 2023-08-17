package com.example.emergycontactapp.ui.historial;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.emergycontactapp.Entities.Insidencia;
import com.example.emergycontactapp.databinding.ContactItemBinding;
import com.example.emergycontactapp.databinding.InsidenciaItemBinding;
import com.example.emergycontactapp.ui.Contact;
import com.example.emergycontactapp.ui.OnItemClickListener;
import com.example.emergycontactapp.ui.contact.ContactAdapter;

import java.util.List;

public class InsidenciaAdapter extends RecyclerView.Adapter<InsidenciaAdapter.ViewHolder>{

    private List<Insidencia> dataset;
    private OnItemClickListener<Insidencia> listener;
    public InsidenciaAdapter(List<Insidencia> dataset, OnItemClickListener<Insidencia> listener){
        this.dataset = dataset;
        this.listener = listener;
    }

    @NonNull
    @Override
    public InsidenciaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        InsidenciaItemBinding binding = InsidenciaItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new InsidenciaAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull InsidenciaAdapter.ViewHolder holder, int position) {
        Insidencia insidencia = dataset.get(position);
        holder.getBinding().tvInsidenciaFecha.setText(insidencia.getFecha());
        holder.getBinding().tvInsidenciaUbicacion.setText(insidencia.getLatitud()+","+insidencia.getLongitud());
        holder.setOnclickListener(insidencia,listener);
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
    public void setItems(List<Insidencia> dataset){
        this.dataset = dataset;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private InsidenciaItemBinding binding;
        public ViewHolder(@NonNull InsidenciaItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        public void setOnclickListener(Insidencia insidencia, OnItemClickListener<Insidencia> listener){
            binding.imgInsidenciaVer.setOnClickListener(e -> listener.onItemClick(insidencia));
        }
        public InsidenciaItemBinding getBinding(){
            return binding;
        }
    }
}
