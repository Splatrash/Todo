package com.example.todo.ui.dashboard;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.todo.data.TacheDatabase;
import com.example.todo.model.Tache;

import java.util.List;

public class DashboardViewModel extends AndroidViewModel {

    private LiveData<List<Tache>> tachesChoisies;
    private TacheDatabase tacheDB;

    public DashboardViewModel(Application application) {
        super(application);
        tacheDB = TacheDatabase.getDatabase(application);
        tachesChoisies = tacheDB.todoDao().getAllChosenTache(true);
    }

    public LiveData<List<Tache>> getAllTachesChoisie() {
        return tachesChoisies;
    }
}