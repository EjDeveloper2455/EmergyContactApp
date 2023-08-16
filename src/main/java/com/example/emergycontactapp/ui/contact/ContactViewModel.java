package com.example.emergycontactapp.ui.contact;

import android.Manifest;
import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.emergycontactapp.databinding.FragmentContactsBinding;
import com.example.emergycontactapp.ui.Contact;

import java.util.ArrayList;
import java.util.List;

public class ContactViewModel extends AndroidViewModel {


    private static final int PERMISSION_REQUEST_READ_CONTACT = 100;
    private LiveData<List<Contact>> dataset;
    public ContactViewModel(@NonNull Application app) {
        super(app);
    }
    LiveData<List<Contact>> getDataset(Fragment fragment, Context context, FragmentContactsBinding binding){
        return solicitarPermisoContactos(fragment,context,binding);
    }
    private LiveData<List<Contact>> solicitarPermisoContactos(Fragment fragment, Context context, FragmentContactsBinding binding){
        //PREGUNTANDO SI YA TENGO UN DETERMINADO PERMISO
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            //ENTRA AQUI SI NO ME HAN DADO EL PERMISO, Y DEBO DE SOLICITARLO
            ActivityCompat.requestPermissions(fragment.getActivity(), new String[]{Manifest.permission.READ_CONTACTS}, PERMISSION_REQUEST_READ_CONTACT);
            return new MutableLiveData<>();
        }else{
            //ENTRA AQUI SI EL USUARIO YA ME OTORGÓ EL PERMISO ANTES, PUEDO HACER USO DE LA LECTURA DE CONTACTOS
            return getContacts(context,binding);
        }
    }

    private LiveData<List<Contact>> getContacts(Context context,FragmentContactsBinding binding) {
        List<Contact> contactos = new ArrayList<>();

        //String buscar = binding.tilSearch.getEditText().getText().toString();

        String charName = binding.tilBuscar.getEditText().getText().toString();
        if (charName.isEmpty()) charName = "A";

        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI,
                null, ContactsContract.Contacts.DISPLAY_NAME + " LIKE '"+charName+"%'", null, ContactsContract.Contacts.DISPLAY_NAME + " DESC");

        boolean continuar = true;
        if(cursor.getCount() > 0){
            while (cursor.moveToNext()){
                int idColumnIndex = Math.max(cursor.getColumnIndex(ContactsContract.Contacts._ID), 0);
                int nameColumnIndex = Math.max(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME), 0);
                int phoneColumnIndex = Math.max(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER), 0);//ME DICE SI TIENE O NO UN TELEFONO GUARDADO

                String id = cursor.getString(idColumnIndex);
                String name = cursor.getString(nameColumnIndex);
                if(Integer.parseInt(cursor.getString(phoneColumnIndex)) > 0){

                    //EL CONTACTO SI TIENE TELEFONO ALMACENADO
                    Cursor cursorPhone = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?", new String[]{id}, null);

                    String phone = "";
                    while (cursorPhone.moveToNext()){
                        int phoneCommonColumIndex = cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                        phone = cursorPhone.getString(phoneCommonColumIndex);

                        /*Contacto nuevo = new Contacto();
                        nuevo.setName(name);
                        nuevo.setPhone(phone);
                        contactos.add(nuevo);*/
                        continuar = false;
                    }

                    Cursor cursorCorreo = resolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                            null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + "=?", new String[]{id}, null);
                    String correo ="";
                    while (cursorCorreo.moveToNext()){
                        int emailCommonColumIndex = cursorCorreo.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA1);
                        correo = cursorCorreo.getString(emailCommonColumIndex);
                    }
                    Contact nuevo = new Contact(name,phone,correo);

                   contactos.add(nuevo);

                    cursorCorreo.close();
                    cursorPhone.close();
                }
            }
            cursor.close();
        }

        MutableLiveData mutableLiveData = new MutableLiveData<>();
        mutableLiveData.setValue(contactos);
        return mutableLiveData;
    }

}