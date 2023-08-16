package com.example.emergycontactapp.ui.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.emergycontactapp.Entities.EmergyContact;
import com.example.emergycontactapp.Repositories.EmergyContactRepository;

import java.util.List;

public class EmergyContactViewModel extends AndroidViewModel {

    private LiveData<List<EmergyContact>> dataset;
    private EmergyContactRepository repository;
    public EmergyContactViewModel(@NonNull Application app) {
        super(app);
        repository = new EmergyContactRepository(app);
        dataset = repository.getDataset();
    }

    public LiveData<List<EmergyContact>> getDataset() {
        return dataset;
    }
    public void insert(EmergyContact emergyContact){
        repository.insert(emergyContact);
    }
    public void update(EmergyContact emergyContact){
        repository.update(emergyContact);
    }
    public void delete(EmergyContact emergyContact){
        repository.delete(emergyContact);
    }

}