package com.example.emergycontactapp.ui.contact;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.emergycontactapp.Entities.EmergyContact;
import com.example.emergycontactapp.R;
import com.example.emergycontactapp.databinding.ActivityContactBinding;
import com.example.emergycontactapp.ui.Contact;
import com.example.emergycontactapp.ui.home.EmergyContactViewModel;


public class ContactActivity extends AppCompatActivity {
    ActivityContactBinding binding;
    EmergyContact emergyContact;
    private EmergyContactViewModel emergyContactViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityContactBinding.inflate(getLayoutInflater());
        getSupportActionBar().hide();
        setContentView(binding.getRoot());

        emergyContactViewModel = new ViewModelProvider(this).get(EmergyContactViewModel.class);

        emergyContact = null;
        Intent getIntent = getIntent();

        if(getIntent.hasExtra("EmergyContact")){
            emergyContact = (EmergyContact) getIntent.getSerializableExtra("EmergyContact");
            binding.tilNombre.getEditText().setText(emergyContact.getNombre());
            binding.tilTelefono.getEditText().setText(emergyContact.getTelefono());
            binding.tilEmail.getEditText().setText(emergyContact.getCorreo());

            if(emergyContact.getId() == -1)binding.btnEliminar.setVisibility(View.INVISIBLE);
            else{
                binding.toolbarContact.setTitle(R.string.edit_contacto);
            }
        }else {
            binding.btnEliminar.setVisibility(View.INVISIBLE);
        }

        binding.btnLimpiar.setOnClickListener(e -> {
            binding.tilNombre.getEditText().setText(null);
            binding.tilTelefono.getEditText().setText(null);
            binding.tilEmail.getEditText().setText(null);
        });
        binding.btnGuardar.setOnClickListener(e ->{
            if(emergyContact != null && !validar()){
                EmergyContact newEmergyContact = new EmergyContact(binding.tilNombre.getEditText().getText().toString(),
                        binding.tilTelefono.getEditText().getText().toString(),binding.tilEmail.getEditText().getText().toString());
                if(emergyContact.getId() == -1)emergyContactViewModel.insert(newEmergyContact);
                else{
                    newEmergyContact.setId(emergyContact.getId());
                    emergyContactViewModel.update(newEmergyContact);
                }
                Intent intent = new Intent();
                setResult(RESULT_OK,intent);
                finish();
            }
        });

        binding.btnEliminar.setOnClickListener(e -> {
            emergyContactViewModel.delete(emergyContact);
            finish();
        });
    }

    private boolean validar(){
        if(binding.tilNombre.getEditText().getText().toString().isEmpty()){
            binding.tilNombre.setError(getString(R.string.este_campo_es_obligatorio));
            return true;
        }else if(binding.tilTelefono.getEditText().getText().toString().isEmpty()){
            binding.tilTelefono.setError(getString(R.string.este_campo_es_obligatorio));
            return true;
        }
        return false;
    }
}