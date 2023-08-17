package com.example.emergycontactapp.ui.home;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.emergycontactapp.Entities.Insidencia;
import com.example.emergycontactapp.Repositories.InsidenciaRepository;

import java.util.List;

public class InsidenciaViewModel extends AndroidViewModel {

    private LiveData<List<Insidencia>> dataset;
    private InsidenciaRepository repository;
    public InsidenciaViewModel(Application app) {
        super(app);
        this.repository  = new InsidenciaRepository(app);
        dataset = repository.getDataset();
    }

    public LiveData<List<Insidencia>> getDataset() {
        return dataset;
    }
    public void insert(Insidencia insidencia){
        repository.insert(insidencia);
    }
}