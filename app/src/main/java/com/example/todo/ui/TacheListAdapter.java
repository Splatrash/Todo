package com.example.todo.ui;

import android.content.Context;

import android.util.Log;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.example.todo.MainActivity;
import com.example.todo.R;
import com.example.todo.data.AppExecutors;
import com.example.todo.data.TacheDatabase;
import com.example.todo.model.Tache;

public class TacheListAdapter extends RecyclerView.Adapter<TacheListAdapter.TacheViewHolder> {

    private List<Tache> tachesList;
    private TacheDatabase mDb;

    private onMenuItemClickListener mListener;

    private Context mContext;

    public interface onMenuItemClickListener {
        void onContextMenuModifierClick(int position);
    }

    public void setOnMenuItemClickListener(onMenuItemClickListener listener) {
        this.mListener = listener;
    }

    @NonNull
    @Override
    public TacheViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.tache_one_line, viewGroup, false);
        mDb = TacheDatabase.getDatabase(viewGroup.getContext());
        return new TacheListAdapter.TacheViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TacheViewHolder tacheViewHolder, int position) {

        Tache current = tachesList.get(position);

        tacheViewHolder.tvTitre.setText(current.getTitre());
        tacheViewHolder.tvDescription.setText(current.getDescription());

        int[] icon_ressource_number = {R.drawable.icon1, R.drawable.icon2,
                R.drawable.icon3, R.drawable.icon4, R.drawable.icon5};
        tacheViewHolder.imgTache.setImageResource(icon_ressource_number[current.getNum_image()]);

        //Écouter un clic simple sur une tâche
        tacheViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("TAG", "onBindViewHolder: " + current.getId());

                        // A revoir, car enlè
                        mDb.todoDao().setIfChosen(current.getId(), true);

                    }
                });
            }
        });

        //Écouter un clic long sur une tâche pour le menu contextuel
        tacheViewHolder.itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                MenuItem modifier = contextMenu.add(0, view.getId(), 0, R.string.edit_tache_menu);
                MenuItem supprimer = contextMenu.add(0, view.getId(), 1, R.string.delete_tache_menu);

                //Empêche le menu contextuel d'apparaitre si l'utilisateur n'est pas un admin
                if(!MainActivity.isAdmin)
                    contextMenu.clear();

                else
                    contextMenu.hasVisibleItems();

                //Lorsque l'option modifier du menu contextuel est sélectionnée
                modifier.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        AppExecutors.getInstance().diskIO().execute(new Runnable() {
                            @Override
                            public void run() {

                                //Faire la gestion de la modification d'une tâche ici

                                Tache tacheModifier = current;

                                //mDb.todoDao().updateTache();
                            }
                        });
                        return false;
                    }
                });

                //Lorsque l'option supprimer du menu contextuel est sélectionnée
                supprimer.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        AppExecutors.getInstance().diskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                //Suppression de l'item ici
                                mDb.todoDao().deleteTache(current);
                            }
                        });
                        return false;
                    }
                });
            }
        });
    }

    public void setTaches(List<Tache> taches) {
        this.tachesList = taches;
    }

    public List<Tache> getTaches() {
        return tachesList;
    }

    @Override
    public int getItemCount() {
        if (tachesList != null)
            return tachesList.size();
        else return 0;
    }

    public class TacheViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTitre, tvDescription, tvId;
        ImageView imgTache;
        public TacheViewHolder(@NonNull View itemView, final onMenuItemClickListener listener) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tv_id);
            tvTitre = itemView.findViewById(R.id.tv_titre);
            tvDescription = itemView.findViewById(R.id.tv_description);
            imgTache = itemView.findViewById(R.id.img_tache);
        }
    }
}
