package com.example.emergycontactapp.ui.contact;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.emergycontactapp.Entities.EmergyContact;
import com.example.emergycontactapp.R;
import com.example.emergycontactapp.databinding.FragmentContactsBinding;
import com.example.emergycontactapp.ui.Contact;
import com.example.emergycontactapp.ui.OnItemClickListener;
import com.example.emergycontactapp.ui.home.EmergyContactViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class ContactFragment extends Fragment implements OnItemClickListener<Contact> {

    private FragmentContactsBinding binding;
    ContactViewModel contactViewModel;
    private ContactAdapter adapter;
    private ActivityResultLauncher<Intent> launcher;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        contactViewModel =
                new ViewModelProvider(this).get(ContactViewModel.class);


        adapter = new ContactAdapter(new ArrayList<>(),this);

        binding = FragmentContactsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        buscarContactos();

        binding.imgBusqueda.setOnClickListener(e -> buscarContactos());

        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Snackbar.make(binding.cardView, R.string.savedContact,Snackbar.LENGTH_LONG).show();
                    } else {
                        Snackbar.make(binding.cardView, R.string.canceled,Snackbar.LENGTH_LONG).show();
                    }
                }
        );

        setupRecyclerView();
        return root;
    }

    public void buscarContactos(){
        contactViewModel.getDataset(this,this.getContext(),binding).observe(getViewLifecycleOwner(), emergyContacts -> {
            if(emergyContacts.isEmpty()){
                binding.rvContact.setVisibility(View.GONE);
                binding.tvNoHayContactos.setVisibility(View.VISIBLE);
            }else{
                adapter.setItems(emergyContacts);
                binding.rvContact.setVisibility(View.VISIBLE);
                binding.tvNoHayContactos.setVisibility(View.GONE);
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
        Intent  intent = new Intent(this.getContext(),ContactActivity.class);
        EmergyContact newEmergyContact = new EmergyContact(data.getNombre(),data.getTelefono(),data.getCorreo());
        newEmergyContact.setId(-1);
        intent.putExtra("EmergyContact",newEmergyContact);
        launcher.launch(intent);
    }
}