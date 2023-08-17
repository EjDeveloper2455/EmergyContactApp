package com.example.emergycontactapp.ui.historial;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.emergycontactapp.Entities.Insidencia;
import com.example.emergycontactapp.R;
import com.example.emergycontactapp.databinding.FragmentHistorialBinding;
import com.example.emergycontactapp.databinding.ModalAppsBinding;
import com.example.emergycontactapp.databinding.ModalInsidenciaBinding;
import com.example.emergycontactapp.databinding.ModalInsidenciasBinding;
import com.example.emergycontactapp.ui.OnItemClickListener;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class HistorialFragment extends Fragment implements OnItemClickListener<Insidencia> {

    private FragmentHistorialBinding binding;
    private InsidenciaAdapter adapter;
    private HistorialViewModel historialViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHistorialBinding.inflate(getLayoutInflater());

        historialViewModel =
                new ViewModelProvider(this).get(HistorialViewModel.class);

        adapter = new InsidenciaAdapter(new ArrayList<>(),this);
        historialViewModel.getDataset().observe(getViewLifecycleOwner(), emergyContacts -> {
            if(emergyContacts.isEmpty()){
                binding.tvNoHayInsicencia.setVisibility(View.VISIBLE);
                binding.rvInsidencia.setVisibility(View.GONE);
            }else{
                binding.tvNoHayInsicencia.setVisibility(View.GONE);
                binding.rvInsidencia.setVisibility(View.VISIBLE);
                adapter.setItems(emergyContacts);
            }
        });
        setupRecyclerView();

        return binding.getRoot();
    }
    private void setupRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        binding.rvInsidencia.setLayoutManager(linearLayoutManager);
        binding.rvInsidencia.setAdapter(adapter);
    }
    @Override
    public void onItemClick(Insidencia data) {
        mostrarModalMensajeApp(data);
    }
    public void mostrarModalMensajeApp(Insidencia insidencia){
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle(getString(R.string.insidencia_no)+insidencia.getId())
                .setNegativeButton(getString(R.string.cerrar), null);;

        ModalInsidenciaBinding binding1 = ModalInsidenciaBinding.inflate(getLayoutInflater());
        builder.setView(binding1.getRoot());

        binding1.tvTipoIns.setText(insidencia.getTitulo());
        binding1.tvmensajeIns.setText(insidencia.getDescripcion());
        binding1.tvLatitudIns.setText(insidencia.getLatitud()+"");
        binding1.tvLogintudIns.setText(insidencia.getLongitud()+"");
        binding1.tvFechaIns.setText(insidencia.getFecha());
        AlertDialog alertDialogMensaje = builder.create();
        alertDialogMensaje.show();
    }
}