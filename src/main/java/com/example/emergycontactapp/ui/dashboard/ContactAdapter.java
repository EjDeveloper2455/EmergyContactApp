package com.example.emergycontactapp.ui.dashboard;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.emergycontactapp.Entities.EmergyContact;
import com.example.emergycontactapp.databinding.ContactItemBinding;
import com.example.emergycontactapp.databinding.EmergyContactItemBinding;
import com.example.emergycontactapp.ui.Contact;
import com.example.emergycontactapp.ui.OnItemClickListener;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    private List<Contact> dataset;
    private OnItemClickListener<Contact> listener;
    public ContactAdapter(List<Contact> dataset, OnItemClickListener<Contact> listener){
        this.dataset = dataset;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ContactItemBinding binding = ContactItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Contact contact = dataset.get(position);
        holder.getBinding().tvContactName.setText(contact.getNombre());
        holder.getBinding().tvContactPhone.setText(contact.getTelefono());
        holder.setOnclickListener(contact,listener);
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
    public void setItems(List<Contact> dataset){
        this.dataset = dataset;
        notifyDataSetChanged();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private ContactItemBinding binding;
        public ViewHolder(@NonNull ContactItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        public void setOnclickListener(Contact emergyContact, OnItemClickListener<Contact> listener){
            binding.cardContact.setOnClickListener(e -> listener.onItemClick(emergyContact));
        }
        public ContactItemBinding getBinding(){
            return binding;
        }
    }
}
