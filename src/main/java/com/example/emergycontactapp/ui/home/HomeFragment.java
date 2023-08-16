package com.example.emergycontactapp.ui.home;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.emergycontactapp.Entities.EmergyContact;
import com.example.emergycontactapp.R;
import com.example.emergycontactapp.databinding.FragmentHomeBinding;
import com.example.emergycontactapp.databinding.PopUpAppMensajeBinding;
import com.example.emergycontactapp.databinding.PopUpInsidenciaBinding;
import com.example.emergycontactapp.ui.OnItemClickListener;
import com.example.emergycontactapp.ui.contact.ContactActivity;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements OnItemClickListener<EmergyContact> {

    private FragmentHomeBinding binding;
    private EmergyContactAdapter adapter;

    private ActivityResultLauncher<Intent> launcher;
    private EmergyContactViewModel homeViewModel;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(EmergyContactViewModel.class);

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

        binding.btnReportar.setOnClickListener(e -> {
            mostrarPopup();
        });

        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Snackbar.make(binding.rvEmergyContact, R.string.savedContact,Snackbar.LENGTH_LONG).show();
                    } else {
                        Snackbar.make(binding.rvEmergyContact, R.string.canceled,Snackbar.LENGTH_LONG).show();
                    }
                }
        );

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
        Intent  intent = new Intent(this.getContext(), ContactActivity.class);
        intent.putExtra("EmergyContact",data);
        launcher.launch(intent);
    }

    public void mostrarPopup(){

        // Crear un AlertDialog.Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle(R.string.tipo_de_insistence);

        PopUpInsidenciaBinding binding1 = PopUpInsidenciaBinding.inflate(getLayoutInflater());
        builder.setView(binding1.getRoot());

        builder.setPositiveButton(R.string.insidencia_rapida, (dialog, which) -> {
            mostrarPopupAppMensaje();
            dialog.dismiss(); //
        });

        builder.setNegativeButton(R.string.insidencia_especifica, (dialog, which) -> {
            dialog.dismiss();
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    public void mostrarPopupAppMensaje(){

        // Crear un AlertDialog.Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle("Aplicaciónes de mensajería");

        PopUpAppMensajeBinding binding1 = PopUpAppMensajeBinding.inflate(getLayoutInflater());
        builder.setView(binding1.getRoot());

        builder.setPositiveButton("Whatsapp", (dialog, which) -> {
            recorrerContantos("Whatsapp");
            dialog.dismiss(); //
        });
        builder.setNeutralButton("Correo electrónico", (dialog, which) -> {
            recorrerContantos("Email");
            dialog.dismiss(); //
        });

        builder.setNegativeButton("SMS", (dialog, which) -> {
            recorrerContantos("SMS");
            dialog.dismiss();
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void recorrerContantos(String appMsg){
        homeViewModel.getDataset().observe(getViewLifecycleOwner(), emergyContacts -> {
            if(emergyContacts.isEmpty()){
                Snackbar.make(binding.rvEmergyContact,"No hay Contactos de emergencia creados", Snackbar.LENGTH_LONG).show();
            }else{
                for (EmergyContact contacto: emergyContacts) {
                    if (appMsg.equals("Whatsapp"))sendWhatsApp(contacto.getTelefono());
                    else if(appMsg.equals("Email"))if(!contacto.getCorreo().isEmpty())sendEmail(contacto.getCorreo());
                    else sendSMS(contacto.getTelefono());
                }
            }
        });
    }
    private void sendWhatsApp(String numero) {
        // Números de contacto (separados por coma) a los que deseas enviar el mensaje

        // Mensaje que deseas enviar
        String message = "Esta es una prueba para la aplicacion EmergyContantApp";

        // Crear un URI con el número y el mensaje
        Uri uri = Uri.parse("https://api.whatsapp.com/send?phone=" + numero + "&text=" + message);

        // Crear el intent para abrir WhatsApp
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
    private void sendEmail(String destinatario) {
        String[] recipients = {destinatario}; // Reemplaza con la dirección de correo electrónico del destinatario
        String subject = "Asunto del correo";
        String body = "Cuerpo del correo.";

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, recipients);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, body);


        startActivity(intent);

    }

    private void sendSMS(String numero) {
        String message = "Este es un mensaje de prueba para la aplicacion EmergyContactApp";

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(numero, null, message, null, null);
    }

}