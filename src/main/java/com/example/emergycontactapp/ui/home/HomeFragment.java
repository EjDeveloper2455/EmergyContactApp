package com.example.emergycontactapp.ui.home;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;



import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.emergycontactapp.Entities.EmergyContact;
import com.example.emergycontactapp.Entities.Insidencia;
import com.example.emergycontactapp.Entities.Ubicacion;
import com.example.emergycontactapp.R;
import com.example.emergycontactapp.databinding.FragmentHomeBinding;
import com.example.emergycontactapp.databinding.ModalAppsBinding;
import com.example.emergycontactapp.databinding.ModalInsidenciasBinding;
import com.example.emergycontactapp.databinding.PopUpAppMensajeBinding;
import com.example.emergycontactapp.databinding.PopUpInsidenciaBinding;
import com.example.emergycontactapp.ui.OnItemClickListener;
import com.example.emergycontactapp.ui.contact.ContactActivity;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment implements OnItemClickListener<EmergyContact>, LocationListener {

    private FragmentHomeBinding binding;
    private EmergyContactAdapter adapter;

    private ActivityResultLauncher<Intent> launcher;
    private EmergyContactViewModel homeViewModel;
    private AlertDialog alertDialog,alertDialogMensaje;
    private Ubicacion ubicacion;
    private LocationManager locationManager;
    static int REQUEST_CODE_GPS = 12;
    private InsidenciaViewModel insidenciaViewModel;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(EmergyContactViewModel.class);

        insidenciaViewModel =
                new ViewModelProvider(this).get(InsidenciaViewModel.class);

        adapter = new EmergyContactAdapter(new ArrayList<>(),this);

        ubicacion = null;

        homeViewModel.getDataset().observe(getViewLifecycleOwner(), emergyContacts -> {
            if(emergyContacts.isEmpty()){
                binding.rvEmergyContact.setVisibility(View.GONE);
                binding.tvNoHayContactosEmergencia.setVisibility(View.VISIBLE);
            }else{
                adapter.setItems(emergyContacts);
                binding.rvEmergyContact.setVisibility(View.VISIBLE);
                binding.tvNoHayContactosEmergencia.setVisibility(View.GONE);
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

        solicitarPermisosGPS(this.getContext());

        binding.fabEmergyContact.setOnClickListener(e -> {
            Intent  intent = new Intent(this.getContext(), ContactActivity.class);
            EmergyContact nuevo = new EmergyContact("","","");
            nuevo.setId(-1);
            intent.putExtra("EmergyContact",nuevo);
            launcher.launch(intent);
        });

        setupRecyclerView();

        return root;
    }

    private void setupRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        binding.rvEmergyContact.setLayoutManager(linearLayoutManager);
        binding.rvEmergyContact.setAdapter(adapter);
    }

    private void solicitarPermisosGPS(Context contexto) {
        if(ContextCompat.checkSelfPermission(contexto, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            useFineLocation();
        }else{
            ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_GPS);
        }
    }
    @SuppressLint({"ServiceCast", "MissingPermission"})
    private void useCoarseLocation() {
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
    }
    @SuppressLint({"ServiceCast", "MissingPermission"})
    private void useFineLocation() {
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CODE_GPS){
            if(grantResults.length > 0){
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    useFineLocation();
                }else if(grantResults[1] == PackageManager.PERMISSION_GRANTED){
                    useCoarseLocation();
                }
            }else{
                Snackbar.make(binding.rvEmergyContact,"Ha ocurrido un error al obtener la ubicacion",Snackbar.LENGTH_LONG).show();
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        ubicacion = new Ubicacion(location.getLatitude(), location.getLongitude());
        locationManager.removeUpdates(this);
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
        solicitarPermisosGPS(this.getContext());
        if (ubicacion == null){
            Snackbar.make(binding.rvEmergyContact,getString(R.string.no_ubicacion),Snackbar.LENGTH_LONG).show();
            return;
        }
        // Crear un AlertDialog.Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle(R.string.tipo_de_insistence);

        PopUpInsidenciaBinding binding1 = PopUpInsidenciaBinding.inflate(getLayoutInflater());
        builder.setView(binding1.getRoot());

        builder.setPositiveButton(R.string.insidencia_rapida, (dialog, which) -> {
            mostrarModalMensajeApp(getString(R.string.insidencia_rapida),getString(R.string.mensaje_insidencia_rapida));
            dialog.dismiss(); //
        });

        builder.setNegativeButton(R.string.insidencia_especifica, (dialog, which) -> {
            mostrarModalMensaje();
            dialog.dismiss();
        });

        builder.create().show();
    }
    public void mostrarModalMensajeApp(String tipo,String mensaje){
        alertDialogMensaje = null;
        // Crear un AlertDialog.Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle(R.string.aplicaciones_de_mensajeria);

        ModalAppsBinding binding1 = ModalAppsBinding.inflate(getLayoutInflater());
        builder.setView(binding1.getRoot());

        binding1.iconWhatsapp.setOnClickListener(e -> {
            recorrerContantos(tipo,"Whatsapp",mensaje);
            alertDialogMensaje.dismiss(); //
        });
        binding1.iconEmail.setOnClickListener(e -> {
            recorrerContantos(tipo,"Email",mensaje);
            alertDialogMensaje.dismiss(); //
        });

        binding1.iconSMS.setOnClickListener(e -> {
            recorrerContantos(tipo,"SMS",mensaje);
            alertDialogMensaje.dismiss();
        });

        alertDialogMensaje = builder.create();
        alertDialogMensaje.show();
    }
    public void mostrarModalMensaje(){
        alertDialog = null;
        String tiposIncidencia[] = {getString(R.string.mensaje_insicencia0),
        getString(R.string.mensaje_insicencia2),
        getString(R.string.mensaje_insicencia3),
        getString(R.string.mensaje_insicencia4),
        getString(R.string.mensaje_insicencia5),
        getString(R.string.mensaje_insicencia6),
                getString(R.string.mensaje_insicencia7),
        getString(R.string.mensaje_insicencia8)
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        ModalInsidenciasBinding binding1 = ModalInsidenciasBinding.inflate(getLayoutInflater());

        Button button[] = {binding1.btnInsidecia0,binding1.btnInsidecia2,binding1.btnInsidecia3,
                binding1.btnInsidecia4,binding1.btnInsidecia5,binding1.btnInsidecia6,
                binding1.btnInsidecia7,binding1.btnInsidecia8};

        builder.setView(binding1.getRoot())
                .setNegativeButton(R.string.cancelar, null);

        for (int i= 0; i<button.length; i++){
            int finalI = i;
            button[i].setOnClickListener(e ->{
                mostrarModalMensajeApp(getString(R.string.insidencia_especifica),tiposIncidencia[finalI]);
                alertDialog.dismiss();
            });
        }

        binding1.btnInsidenciaDescriptiva.setOnClickListener(e -> {
            if (!binding1.tilInsidenciaDescriptiva.getEditText().getText().toString().isEmpty()){
                mostrarModalMensajeApp(getString(R.string.insidencia_especifica),binding1.tilInsidenciaDescriptiva.getEditText().getText().toString());
                alertDialog.dismiss();
            }else binding1.tilInsidenciaDescriptiva.setError(getString(R.string.este_campo_es_obligatorio));
        });

        alertDialog = builder.create();
        alertDialog.show();
    }

    public void recorrerContantos(String tipo,String appMsg,String mensaje){
        homeViewModel.getDataset().observe(getViewLifecycleOwner(), emergyContacts -> {
            if(emergyContacts.isEmpty()){
                Snackbar.make(binding.rvEmergyContact,getString(R.string.no_hay_contacto_emergencia), Snackbar.LENGTH_LONG).show();

            }else{
                for (EmergyContact contacto: emergyContacts) {
                    if (appMsg.equals("Whatsapp"))sendWhatsApp(contacto.getTelefono(),mensaje);
                    else if(appMsg.equals("Email"))if(!contacto.getCorreo().isEmpty())sendEmail(contacto.getCorreo(),mensaje);
                    else sendSMS(contacto.getTelefono(),mensaje);
                }
                Date date = new Date();
                SimpleDateFormat formatoFecha = new SimpleDateFormat("dd-MM-yyyy");
                SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm:ss");
                String fecha = formatoFecha.format(date)+" "+formatoHora.format(date);

                insidenciaViewModel.insert(new Insidencia(tipo,mensaje,ubicacion.getLatitud(),ubicacion.getLongitud(),fecha));
            }
        });
    }
    private void sendWhatsApp(String numero,String message) {
        String uriGeo = "geo:" + ubicacion.getLatitud() + "," + ubicacion.getLongitud();
        Uri locationUri = Uri.parse(uriGeo);

        Uri uri = Uri.parse("https://api.whatsapp.com/send?phone=" + numero + "&text=" + message+" "+locationUri.toString());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
    private void sendEmail(String destinatario,String body) {
        String[] recipients = {destinatario};
        String subject = "Mensaje de insidencia";

        String uriGeo = "geo:" + ubicacion.getLatitud() + "," + ubicacion.getLongitud();
        Uri locationUri = Uri.parse(uriGeo);

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, recipients);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, body+" "+locationUri.toString());

        startActivity(intent);

    }

    private void sendSMS(String numero,String message) {
        String uriGeo = "geo:" + ubicacion.getLatitud() + "," + ubicacion.getLongitud();
        Uri locationUri = Uri.parse(uriGeo);

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(numero, null, message+" "+locationUri.toString(), null, null);
    }

}