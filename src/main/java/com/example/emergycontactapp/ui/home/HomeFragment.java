package com.example.emergycontactapp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.emergycontactapp.Entities.EmergyContact;
import com.example.emergycontactapp.databinding.FragmentHomeBinding;
import com.example.emergycontactapp.ui.OnItemClickListener;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements OnItemClickListener<EmergyContact> {

    private FragmentHomeBinding binding;
    private EmergyContactAdapter adapter;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        adapter = new EmergyContactAdapter(new ArrayList<>(),this);

        homeViewModel.getDataset().observe(getViewLifecycleOwner(), emergyContacts -> {
            if(emergyContacts.isEmpty()){
                Snackbar.make(binding.rvEmergyContact,"No hay Contactos de emergencia creados", Snackbar.LENGTH_LONG).show();
            }else{
                adapter.setItems(emergyContacts);
            }
        });

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        setupRecyclerView();

        return root;
    }

    private void setupRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        binding.rvEmergyContact.setLayoutManager(linearLayoutManager);
        binding.rvEmergyContact.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemClick(EmergyContact data) {
        Snackbar.make(binding.rvEmergyContact,data.getNombre(),Snackbar.LENGTH_LONG).show();
    }
}