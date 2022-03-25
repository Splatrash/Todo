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

    @Query("SELECT * FROM tache_table ORDER BY id DESC")
    LiveData<List<Tache>> getAllTodo();

    @Query("DELETE FROM tache_table")
    void deleteAllTaches();

    @Query("SELECT * FROM tache_table WHERE bool_chosen_col = :isChosen ORDER BY id DESC")
    LiveData<List<Tache>> getAllChosenTache(boolean isChosen);

    @Query("Update tache_table SET bool_chosen_col = :isChosen WHERE titre_col = :titre")
    void setIfChosen(String titre, boolean isChosen);

    @Insert
    void insertTache(Tache tache);

    @Update
    void updateTache(Tache tache);

    @Delete
    void deleteTache(Tache tache);

}
