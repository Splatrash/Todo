package com.example.todo.ui.home;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.todo.data.TacheDatabase;
import com.example.todo.model.Tache;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {

    private LiveData<List<Tache>> allTaches;
    private TacheDatabase tacheDB;

    public HomeViewModel(Application application) {
       super(application);
       tacheDB = TacheDatabase.getDatabase(application);
       allTaches = tacheDB.todoDao().getAllTodo();
    }

    public LiveData<List<Tache>> getAllTaches() { return allTaches; }

}