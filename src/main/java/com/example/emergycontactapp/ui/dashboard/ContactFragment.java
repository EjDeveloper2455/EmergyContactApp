package com.example.emergycontactapp.ui.dashboard;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.emergycontactapp.databinding.FragmentContactsBinding;
import com.example.emergycontactapp.ui.Contact;
import com.example.emergycontactapp.ui.OnItemClickListener;
import com.example.emergycontactapp.ui.home.EmergyContactAdapter;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class ContactFragment extends Fragment implements OnItemClickListener<Contact> {

    private FragmentContactsBinding binding;
    DashboardViewModel dashboardViewModel;
    private ContactAdapter adapter;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        adapter = new ContactAdapter(new ArrayList<>(),this);

        binding = FragmentContactsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        buscarContactos();

        binding.imgBusqueda.setOnClickListener(e -> buscarContactos());

        setupRecyclerView();
        return root;
    }

    public void buscarContactos(){
        dashboardViewModel.getDataset(this,this.getContext(),binding).observe(getViewLifecycleOwner(), emergyContacts -> {
            if(emergyContacts.isEmpty()){
                Snackbar.make(binding.rvContact,"No hay Contactos de emergencia creados", Snackbar.LENGTH_LONG).show();
            }else{
                adapter.setItems(emergyContacts);
            }
        });
    }
    private void setupRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        binding.rvContact.setLayoutManager(linearLayoutManager);
        binding.rvContact.setAdapter(adapter);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemClick(Contact data) {
        Snackbar.make(binding.tilBuscar,data.getNombre(),Snackbar.LENGTH_LONG).show();
    }
}