package com.example.emergycontactapp.ui.home;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.emergycontactapp.Entities.EmergyContact;
import com.example.emergycontactapp.databinding.EmergyContactItemBinding;
import com.example.emergycontactapp.ui.OnItemClickListener;

import java.util.List;

public class EmergyContactAdapter extends RecyclerView.Adapter<EmergyContactAdapter.ViewHolder> {
    private List<EmergyContact> dataset;
    private OnItemClickListener<EmergyContact> listener;
    public EmergyContactAdapter(List<EmergyContact> dataset, OnItemClickListener<EmergyContact> listener){
        this.dataset = dataset;
        this.listener = listener;
    }

    @NonNull
    @Override
    public EmergyContactAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        EmergyContactItemBinding binding = EmergyContactItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull EmergyContactAdapter.ViewHolder holder, int position) {
        EmergyContact emergyContact = dataset.get(position);
        holder.getBinding().tvNombre.setText(emergyContact.getNombre());
        holder.getBinding().tvPhone.setText(emergyContact.getTelefono());
        holder.setOnclickListener(emergyContact,listener);
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
    public void setItems(List<EmergyContact> dataset){
        this.dataset = dataset;
        notifyDataSetChanged();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private EmergyContactItemBinding binding;
        public ViewHolder(@NonNull EmergyContactItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        public void setOnclickListener(EmergyContact emergyContact, OnItemClickListener<EmergyContact> listener){
            binding.imgEditEmergyContact.setOnClickListener(e -> listener.onItemClick(emergyContact));
        }
        public EmergyContactItemBinding getBinding(){
            return binding;
        }
    }
}
