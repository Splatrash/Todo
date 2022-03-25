package com.example.todo.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.MainActivity;
import com.example.todo.databinding.FragmentHomeBinding;
import com.example.todo.model.Tache;
import com.example.todo.ui.TacheListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.example.todo.R;

import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private TacheListAdapter adapter;

    EditText etTitre;
    EditText etDescription;
    TextView tvTitrePopup;
    TextView tvIdTache ;
    Button btnAjouterPopup;
    RelativeLayout rlPopupGestionTaches;
    FloatingActionButton fabAjouterTache;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView recyclerView =root.findViewById(R.id.rv_tache);
        Context context = recyclerView.getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setHasFixedSize(true);
        adapter = new TacheListAdapter();
        recyclerView.setAdapter(adapter);

        homeViewModel.getAllTaches().observe(getViewLifecycleOwner(), new Observer<List<Tache>>() {
            @Override
            public void onChanged(List<Tache> taches) {
                adapter.setTaches(taches);
                adapter.notifyDataSetChanged();

            }
        });



        return root;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        tvTitrePopup = view.findViewById(R.id.tv_titre_popup);
        tvIdTache = view.findViewById(R.id.tv_id);
        btnAjouterPopup = view.findViewById(R.id.btn_ajouter_modifier);

        fabAjouterTache = view.findViewById(R.id.fab_add_tache);
        rlPopupGestionTaches = view.findViewById(R.id.rl_popup_gestion_taches);
        //Cache le boutton pour ajouter une tâche
        fabAjouterTache.hide();
        // Cache le formulaire de gestion des tâches
        if (rlPopupGestionTaches.getVisibility() == View.VISIBLE) {
            rlPopupGestionTaches.setVisibility(View.GONE);
        }
        Button btnAjouterModifierPopup = view.findViewById(R.id.btn_ajouter_modifier);
        //Gestion du boutton flottant ajout tâche
        fabAjouterTache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               popupGestionTache("ajouter", "","");
            }
        });


        EditText etTitre = view.findViewById(R.id.et_titre);
        EditText etDescription = view.findViewById(R.id.et_description);
        TextView tvId = view.findViewById(R.id.tv_id);

        btnAjouterModifierPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String titre = etTitre.getText().toString().trim();
                String description = etDescription.getText().toString().trim();

                if (titre != ""){
                    if (btnAjouterModifierPopup.getText() == getString(R.string.btn_popup_tache_ajouter)){
                        ((MainActivity)getActivity()).AjouterUneTache(titre, description);
                    }
                    else if(btnAjouterModifierPopup.getText() == getString(R.string.btn_popup_tache_modifier)){
                        //((MainActivity)getActivity()).ModifierUneTache(titre, description);
                    }

                    if (rlPopupGestionTaches.getVisibility() == View.VISIBLE) {
                        rlPopupGestionTaches.setVisibility(View.GONE);

                        //Pour ne pas garder le texte d'un ajout précédant
                        etTitre.setText("");
                        etDescription.setText("");

                    }
                }
            }
        });


        Button btnAnnulerPopup = view.findViewById(R.id.btn_annuler);
        btnAnnulerPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rlPopupGestionTaches.getVisibility() == View.VISIBLE) {
                    rlPopupGestionTaches.setVisibility(View.GONE);

                    //Pour ne pas garder le texte d'un ajout précédant
                    etTitre.setText("");
                    etDescription.setText("");
                }
            }
        });

    }

    public void popupGestionTache(String type, String titre, String description){

        rlPopupGestionTaches.setVisibility(View.VISIBLE);
        if(type == "ajouter"){

            tvTitrePopup.setText(R.string.tv_popup_ajouter);
            btnAjouterPopup.setText(R.string.btn_popup_tache_ajouter);
        }
        if(type == "modifier"){
            tvTitrePopup.setText(R.string.tv_popup_modifier);
            btnAjouterPopup.setText(R.string.btn_popup_tache_modifier);
            etTitre.setText(titre);
            etDescription.setText(description);
        }
    }

    public void test(){
        Log.d("STATE", "test");
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}