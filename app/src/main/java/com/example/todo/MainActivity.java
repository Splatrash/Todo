package com.example.todo;

import android.Manifest;

import android.content.DialogInterface;
import android.content.pm.PackageManager;

import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.widget.Toast;

import com.example.todo.data.TacheDatabase;
import com.example.todo.model.Tache;
import com.example.todo.ui.home.HomeFragment;
import com.example.todo.ui.FormulaireTacheFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
import com.example.todo.data.AppExecutors;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_CODE = 1;

    private ActivityMainBinding binding;
    private TacheDatabase tachesBD;

    FloatingActionButton fabAjouterTache;

    public static boolean isAdmin;
    private boolean formIsUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        fabAjouterTache = findViewById(R.id.fab_add_tache);

        //Cache le bouton pour ajouter une t??che ?? l'ouverture
        fabAjouterTache.hide();

        //V??rifie si le formulaire d'ajout d'une t??che est d??ja ouvert.
        formIsUp = false;

        //Gestion clique sur le bouton ajout d'une t??che, si le formulaire est d??ja ouvert, r??appuyer va le fermer
        fabAjouterTache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                formIsUp = !formIsUp;
                if (formIsUp){
                    FormulaireTacheFragment.formType = "ajouter";
                    getSupportFragmentManager().beginTransaction().replace(R.id.fl_fragment_form, new FormulaireTacheFragment(), "TAG")
                            .addToBackStack(null).commit();
                }
                else {
                    closeForm();
                }
            }
        });
    }

    public void closeForm(){
        formIsUp = false;
        FormulaireTacheFragment formulaireTacheFragmentRemove = (FormulaireTacheFragment) getSupportFragmentManager()
                .findFragmentByTag("TAG");
        getSupportFragmentManager()
                .beginTransaction()
                .remove(formulaireTacheFragmentRemove)
                .commit();
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
                //Demande ?? l'utilisateur de pouvoir acc??der ?? la camera
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
                }
                //V??rifie si la persmmission a ??t?? accord?? avant d'acc??der aux fonction d'administrateur.
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    isAdmin = !isAdmin;

                    FloatingActionButton fabAjouterTache = findViewById(R.id.fab_add_tache);
                    if(isAdmin)
                        fabAjouterTache.show();
                    else
                        fabAjouterTache.hide();

                    return true;
                }
                else {
                    Toast.makeText(this, "Vous devez donner la permission d'acc??der ?? la cam??ra", Toast.LENGTH_LONG).show();
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

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this, "Autorisation ?? la camera accord??e", Toast.LENGTH_LONG).show();

            else if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED)
            {
                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.CAMERA))
                {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                    dialog.setTitle("Permission requise !");
                    dialog.setMessage("La permission ?? la camera est requise pour pouvoir administrer les t??ches disponibles!");
                    dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
                        }
                    });
                    dialog.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(MainActivity.this, "Impossible d'acc??der ?? l'appareil photo", Toast.LENGTH_SHORT).show();
                        }
                    });
                    dialog.show();
                }

            }
        }
    }


    //Logique de l'ajout d'une t??che avec la mise ?? jour de le bd
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

    //Logique de la modification d'une t??che avec la mise ?? jour de le bd
    public void ModifierUneTache(Tache tache){
        tachesBD = TacheDatabase.getDatabase(this);
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                HomeFragment homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.homeFragment);
            }
        });
    }

    //Logique de la suppression d'une t??che avec la mise ?? jour de le bd
    public void SupprimerToutesTaches(){
        tachesBD = TacheDatabase.getDatabase(this);
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                tachesBD.todoDao().deleteAllTaches();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean("isAdmin", isAdmin);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);
        isAdmin = savedInstanceState.getBoolean("isAdmin");

        if(isAdmin){
            fabAjouterTache.show();
        }
        else {
            fabAjouterTache.hide();
        }

    }
}