package com.example.emergycontactapp.Repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.emergycontactapp.Daos.InsidenciaDao;
import com.example.emergycontactapp.DataBase.DBApp;
import com.example.emergycontactapp.Entities.EmergyContact;
import com.example.emergycontactapp.Entities.Insidencia;

import java.util.List;

public class InsidenciaRepository {
    private InsidenciaDao dao;
    private LiveData<List<Insidencia>> dataset;

    public InsidenciaRepository(Application app) {
        DBApp db = DBApp.getDatabase(app);
        this.dao = db.insidenciaDao();
        this.dataset = dao.getInsidencias();
    }

    public LiveData<List<Insidencia>> getDataset() {
        return dataset;
    }

    public void insert(Insidencia nuevo){
        DBApp.databaseWriteExecutor.execute(() -> {
            dao.insert(nuevo);
        });
    }

    public void update(Insidencia actualizar){
        DBApp.databaseWriteExecutor.execute(() -> {
            dao.update(actualizar);
        });
    }

    public void delete(Insidencia borrar){
        DBApp.databaseWriteExecutor.execute(() -> {
            dao.delete(borrar);
        });
    }

    public void deleteAll(){
        DBApp.databaseWriteExecutor.execute(() -> {
            dao.deleteAll();
        });
    }
}
