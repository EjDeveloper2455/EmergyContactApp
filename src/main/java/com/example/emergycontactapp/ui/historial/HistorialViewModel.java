package com.example.emergycontactapp.ui.historial;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.emergycontactapp.Entities.Insidencia;
import com.example.emergycontactapp.Repositories.InsidenciaRepository;

import java.util.List;

public class HistorialViewModel extends AndroidViewModel {
    private LiveData<List<Insidencia>> dataset;
    private InsidenciaRepository repository;
    public HistorialViewModel(Application app) {
        super(app);
        repository = new InsidenciaRepository(app);
        dataset = repository.getDataset();
    }

    public LiveData<List<Insidencia>> getDataset() {
        return dataset;
    }
}