package com.example.todo.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tache_table")
public class Tache {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "titre_col")
    private String titre;

    @ColumnInfo(name = "description_col")
    private String description;

    @NonNull
    @ColumnInfo(name = "num_image_col")
    private int num_image;

    @NonNull
    @ColumnInfo(name = "bool_chosen_col")
    private boolean is_chosen;

    public Tache(@NonNull String titre, String description, @NonNull int num_image, @NonNull boolean is_chosen){
        this.titre = titre;
        this.description = description;
        this.num_image = num_image;
        this.is_chosen = is_chosen;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }
    public void setTitre(@NonNull String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public int getNum_image() {
        return num_image;
    }
    public void setNum_image(@NonNull int num_image) {
        this.num_image = num_image;
    }

    public boolean getIs_chosen() {
        return is_chosen;
    }
    public void getIs_chosen(@NonNull boolean is_chosen) {
        this.is_chosen = is_chosen;
    }
}
