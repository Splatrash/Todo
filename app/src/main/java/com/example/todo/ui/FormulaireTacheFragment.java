package com.example.todo.ui;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;

import com.example.todo.MainActivity;
import com.example.todo.R;

//Classe qui contrôle le formulaire qui apparait lors de l'ajout ou la modification d'une tâche.
public class FormulaireTacheFragment extends Fragment {

    //Permet à la classe qui appel le fragment du formulaire d'indiquer quel type de formulaire elle veut.
    public static String formType;

    TextView tvTitrePopup;
    Button btnAjouterPopup;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_form_tache, container, false);

        tvTitrePopup = view.findViewById(R.id.tv_titre_popup);
        btnAjouterPopup = view.findViewById(R.id.btn_ajouter_modifier);

        //Check si c'est pour ajouter une tâche ou pour la modifier.
        if (formType == "ajouter"){
            popupGestionTache(formType, "", "");
        }
        /*else if(formType == "modifier"){
            popupGestionTache(formType, "", "");
        }*/

        Button btnAjouterModifierPopup = view.findViewById(R.id.btn_ajouter_modifier);

        EditText etTitre = view.findViewById(R.id.et_titre);
        EditText etDescription = view.findViewById(R.id.et_description);

        //Action lorsque le bouton ajouter ou modifier du formulaire est cliqué.
        btnAjouterModifierPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String titre = etTitre.getText().toString().trim();
                String description = etDescription.getText().toString().trim();

                if (!"".equals(titre))
                {
                    if (btnAjouterModifierPopup.getText() == getString(R.string.btn_popup_tache_ajouter)){
                        ((MainActivity)getActivity()).AjouterUneTache(titre, description);
                    }
                    /*else if(btnAjouterModifierPopup.getText() == getString(R.string.btn_popup_tache_modifier)){
                        ((MainActivity)getActivity()).ModifierUneTache(titre, description);
                    }*/
                    ((MainActivity)getActivity()).closeForm();
                }
            }
        });

        //Action lorsque le bouton annuler du formulaire est cliqué.
        Button btnAnnulerPopup = view.findViewById(R.id.btn_annuler);
        btnAnnulerPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((MainActivity)getActivity()).closeForm();
            }
        });
        return view;
    }

    //Change le contenu initial du formulaire selon sa fonction.
    public void popupGestionTache(String type, String titre, String description){

        if(type == "ajouter"){

            tvTitrePopup.setText(R.string.tv_popup_ajouter);
            btnAjouterPopup.setText(R.string.btn_popup_tache_ajouter);
        }
        /*if(type == "modifier"){
            tvTitrePopup.setText(R.string.tv_popup_modifier);
            btnAjouterPopup.setText(R.string.btn_popup_tache_modifier);
            etTitre.setText(titre);
            etDescription.setText(description);
        }*/
    }
}
