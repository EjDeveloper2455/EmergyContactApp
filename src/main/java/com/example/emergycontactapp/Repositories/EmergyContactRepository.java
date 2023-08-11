package com.example.emergycontactapp.Repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.emergycontactapp.Daos.EmergyContactDao;
import com.example.emergycontactapp.DataBase.DBApp;
import com.example.emergycontactapp.Entities.EmergyContact;

import java.util.List;

public class EmergyContactRepository {
    private EmergyContactDao dao;
    private LiveData<List<EmergyContact>> dataset;

    public EmergyContactRepository(Application app) {
        DBApp db = DBApp.getDatabase(app);
        this.dao = db.emergyContactDao();
        this.dataset = dao.getEmergyContacts();
    }

    public LiveData<List<EmergyContact>> getDataset() {
        return dataset;
    }

    public void insert(EmergyContact nuevo){
        //INSERTANDO DE FORMA ASINCRONA, PARA NO AFECTAR LA INTERFAZ DE USUARIO
        DBApp.databaseWriteExecutor.execute(() -> {
            dao.insert(nuevo);
        });
    }

    public void update(EmergyContact actualizar){
        //ACTUALIZANDO DE FORMA ASINCRONA, PARA NO AFECTAR LA INTERFAZ DE USUARIO
        DBApp.databaseWriteExecutor.execute(() -> {
            dao.update(actualizar);
        });
    }

    public void delete(EmergyContact borrar){
        //BORRANDO UN REGISTRO DE FORMA ASINCRONA, PARA NO AFECTAR LA INTERFAZ DE USUARIO
        DBApp.databaseWriteExecutor.execute(() -> {
            dao.delete(borrar);
        });
    }

    public void deleteAll(){
        //BORRANDO TODOS LOS REGISTROS DE FORMA ASINCRONA, PARA NO AFECTAR LA INTERFAZ DE USUARIO
        DBApp.databaseWriteExecutor.execute(() -> {
            dao.deleteAll();
        });
    }
}
