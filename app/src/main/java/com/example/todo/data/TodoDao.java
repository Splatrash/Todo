package com.example.todo.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import com.example.todo.model.Tache;


@Dao
public interface TodoDao {

    //Obtient toutes les tâches
    @Query("SELECT * FROM tache_table ORDER BY id DESC")
    LiveData<List<Tache>> getAllTodo();

    //Supprime toutes les tâches
    @Query("DELETE FROM tache_table")
    void deleteAllTaches();

    //Obtien toutes les tâches qui ont été choisie comme tâche à faire
    @Query("SELECT * FROM tache_table WHERE bool_chosen_col = :isChosen ORDER BY id DESC")
    LiveData<List<Tache>> getAllChosenTache(boolean isChosen);

    //Permet de mettre à jour si la tâche est choisie ou non
    @Query("Update tache_table SET bool_chosen_col = :isChosen WHERE id = :id")
    void setIfChosen(int id, boolean isChosen);

    @Insert
    void insertTache(Tache tache);

    @Update
    void updateTache(Tache tache);

    @Delete
    void deleteTache(Tache tache);

}
