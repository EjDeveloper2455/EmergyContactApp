package com.example.emergycontactapp.ui.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.emergycontactapp.Daos.EmergyContactDao;
import com.example.emergycontactapp.Entities.EmergyContact;
import com.example.emergycontactapp.Repositories.EmergyContactRepository;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {

    private LiveData<List<EmergyContact>> dataset;
    private EmergyContactRepository repository;
    public HomeViewModel(@NonNull Application app) {
        super(app);
        repository = new EmergyContactRepository(app);
        dataset = repository.getDataset();
    }

    public LiveData<List<EmergyContact>> getDataset() {
        return dataset;
    }
}