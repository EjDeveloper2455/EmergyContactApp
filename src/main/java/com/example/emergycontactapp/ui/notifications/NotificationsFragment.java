package com.example.emergycontactapp.ui.notifications;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.emergycontactapp.Entities.Ubicacion;
import com.example.emergycontactapp.R;
import com.example.emergycontactapp.databinding.FragmentNotificationsBinding;
import com.google.android.material.snackbar.Snackbar;

public class NotificationsFragment extends Fragment implements LocationListener {

    private static final int REQUEST_CODE_GPS = 50;
    private FragmentNotificationsBinding binding;
    private Ubicacion ubicacion;
    private LocationManager locationManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ubicacion = null;
        binding.btnShare.setEnabled(false);
        binding.btnOpenMap.setEnabled(false);

        binding.btnFindLocation.setOnClickListener(v -> {
            Snackbar.make(binding.btnFindLocation,"Solicitando ubicacion",Snackbar.LENGTH_LONG).show();
            solicitarPermisosGPS(this.getContext());
        });

        binding.btnOpenMap.setOnClickListener(v -> {
            abrirMapa();
        });

        binding.btnShare.setOnClickListener( v-> {
            compartirUbicacion();
        });


        return root;
    }

    private void compartirUbicacion() {
        if(ubicacion == null){
            Snackbar.make(binding.btnShare, R.string.no_ubicacion, Snackbar.LENGTH_LONG).show();
        }else{
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Mi ubicación Actual");
            shareIntent.putExtra(Intent.EXTRA_TEXT, ubicacion.toText());

            startActivity(Intent.createChooser(shareIntent, "Compartir Texto"));
        }
    }

    private void abrirMapa() {
        if(ubicacion == null){
            Snackbar.make(binding.btnOpenMap, R.string.no_ubicacion, Snackbar.LENGTH_LONG).show();
        }else{
            Uri mapLocation = Uri.parse("geo:"+ubicacion.toText()+"?z=14");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapLocation);
            startActivity(mapIntent);
        }
    }

    private void solicitarPermisosGPS(Context contexto) {
        if(ContextCompat.checkSelfPermission(contexto, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            //TENGO EL PERMISO, PUEDO UTILIZAR EL GPS
            useFineLocation();
        }else{
            ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_GPS);
        }
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
                Snackbar.make(binding.imageView5,"Ha ocurrido un error al obtener la ubicacion",Snackbar.LENGTH_LONG).show();
                binding.tilLatitud.setVisibility(View.INVISIBLE);
                binding.tilLongitud.setVisibility(View.INVISIBLE);
                binding.btnShare.setEnabled(false);
                binding.btnOpenMap.setEnabled(false);
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @SuppressLint({"ServiceCast", "MissingPermission"})
    private void useCoarseLocation() {
        //OBTIENE EL SERVICIO DE UBICACIÓN DEL DISPOSITIVO
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        //SOLICITAMOS ACTUALIZAR LA POSICIÓN GPS CON DETERMINADA APROXIMACIÓN (NETWORK_PROVIDER = COARSE_LOCATION = UBICACIÓN APROXIMADA)
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
    }
    @SuppressLint({"ServiceCast", "MissingPermission"})
    private void useFineLocation() {
        //OBTIENE EL SERVICIO DE UBICACIÓN DEL DISPOSITIVO
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        //SOLICITAMOS ACTUALIZAR LA POSICIÓN GPS CON DETERMINADA APROXIMACIÓN (GPS_PROVIDER = FINE_LOCATION = UBICACIÓN EXACTA)
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        ubicacion = new Ubicacion(location.getLatitude(), location.getLongitude());

        binding.tilLatitud.getEditText().setText(ubicacion.getLatitudeStr());
        binding.tilLongitud.getEditText().setText(ubicacion.getLongitudeStr());

        //binding.header.setText(R.string.header_location);
        //binding.cvData.setVisibility(View.VISIBLE);
        binding.btnShare.setEnabled(true);
        binding.btnOpenMap.setEnabled(true);

        Snackbar.make(binding.tilLatitud,location.getLatitude()+"",Snackbar.LENGTH_LONG).show();

        //DETENER ACTUALIZACION DE UBICACION PARA DEJARLO DE UN SOLO USO (SI SE QUIERE SEGUIMIENTO NO HACER ESTA PARTE)
        locationManager.removeUpdates(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}