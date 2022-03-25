package com.example.todo;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todo.data.TacheDatabase;
import com.example.todo.model.Tache;
import com.example.todo.ui.home.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.todo.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.todo.data.AppExecutors;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_CODE = 1;

    private ActivityMainBinding binding;
    private TacheDatabase tachesBD;

    public static boolean isAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.bt_admin:
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
                }
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    isAdmin = !isAdmin;

                    FloatingActionButton fabAjouterTache = findViewById(R.id.fab_add_tache);
                    if(isAdmin){

                        fabAjouterTache.show();
                    }
                    else {
                        fabAjouterTache.hide();
                    }
                    return true;
                }
                else {
                    Toast.makeText(this, "Vous devez donner la permission d'accéder à la caméra", Toast.LENGTH_LONG).show();
                    return false;
                }
            case R.id.bt_Suppression:
                SupprimerToutesTaches();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted. Continue the action or workflow
                // in your app.
                Toast.makeText(this, "Autorisation à la camera accordée", Toast.LENGTH_LONG).show();
            } else if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                        Manifest.permission.CAMERA)) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                    dialog.setTitle("Permission requise !");
                    dialog.setMessage("La permission à la camera est requise pour pouvoir administrer les tâches disponibles!");
                    dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
                        }
                    });
                    dialog.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(MainActivity.this, "Impossible d'accéder à l'appareil photo", Toast.LENGTH_SHORT).show();
                        }
                    });
                    dialog.show();
                }

            }
        }
    }

    public void AjouterUneTache(String titre, String description){
        tachesBD = TacheDatabase.getDatabase(this);
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {

                Random r = new Random();
                int nbrIconPossible = 5;
                Tache tache = new Tache(titre, description, r.nextInt(nbrIconPossible), false);
                tachesBD.todoDao().insertTache(tache);
            }
        });
    }

    public void ModifierUneTache(Tache tache){
        tachesBD = TacheDatabase.getDatabase(this);
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                HomeFragment homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.homeFragment);
            }
        });
    }

    public void SupprimerToutesTaches(){
        tachesBD = TacheDatabase.getDatabase(this);
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                tachesBD.todoDao().deleteAllTaches();
            }
        });
    }
}