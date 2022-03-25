package com.example.todo.data;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.todo.model.Tache;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Database(entities = Tache.class, exportSchema = true, version = 1)
public abstract class TacheDatabase extends RoomDatabase {

    public static TacheDatabase INSTANCE;

    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    // DAO
    public abstract TodoDao todoDao();

    public static TacheDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            // Crée la BDD
            synchronized (TacheDatabase.class) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        TacheDatabase.class, "todo_database").addCallback(sRoomDatabaseCallback).build();
            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback() {
                @Override
                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    super.onCreate(db);
                    Log.d("TAG", "onCreate: ");

                    databaseWriteExecutor.execute(() -> {

                        TodoDao dao = INSTANCE.todoDao();

                        Tache tache = new Tache("Tache automatique 1", "Description Tache 1", 0, false);
                        dao.insertTache(tache);
                        tache = new Tache("Tache automatique 2", "Description Tache 2", 1, false);
                        dao.insertTache(tache);
                        tache = new Tache("Tache automatique 3", "Description Tache 3", 2, false);
                        dao.insertTache(tache);

                    });

                }
            };

}
