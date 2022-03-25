package com.example.todo.ui;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.R;
import com.example.todo.data.AppExecutors;
import com.example.todo.data.TacheDatabase;
import com.example.todo.model.Tache;

import java.util.List;

public class TacheChoisieListAdapter extends RecyclerView.Adapter<TacheChoisieListAdapter.TacheChoisieViewHolder>{

    private List<Tache> tachesChoisiesList;
    private TacheDatabase mDb;

    @NonNull
    @Override
    public TacheChoisieListAdapter.TacheChoisieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.tache_one_line, viewGroup, false);
        mDb = TacheDatabase.getDatabase(viewGroup.getContext());
        return new TacheChoisieListAdapter.TacheChoisieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TacheChoisieListAdapter.TacheChoisieViewHolder tacheChoisieViewHolder, int position) {

        Tache current = tachesChoisiesList.get(position);

        tacheChoisieViewHolder.tvTitre.setText(current.getTitre());
        tacheChoisieViewHolder.tvDescription.setText(current.getDescription());


        int[] icon_ressource_number = {R.drawable.icon1, R.drawable.icon2,
                R.drawable.icon3, R.drawable.icon4, R.drawable.icon5};
        tacheChoisieViewHolder.imgTache.setImageResource(icon_ressource_number[current.getNum_image()]);

        //Écouter un clic simple sur une tâche
        tacheChoisieViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("TAG", "onBindViewHolder: " + current.getId());

                        // A revoir, car enlè
                        mDb.todoDao().setIfChosen(current.getTitre(), false);

                    }
                });
            }
        });
    }

    public void setTaches(List<Tache> taches) {
        this.tachesChoisiesList = taches;
    }

    @Override
    public int getItemCount() {
        if (tachesChoisiesList != null)
            return tachesChoisiesList.size();
        else return 0;
    }

    public class TacheChoisieViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTitre, tvDescription, tvId;
        ImageView imgTache;

        public TacheChoisieViewHolder (@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tv_id);
            tvTitre = itemView.findViewById(R.id.tv_titre);
            tvDescription = itemView.findViewById(R.id.tv_description);
            imgTache = itemView.findViewById(R.id.img_tache);

/*            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        // Bonne pratique pour s'assurer que l'élément clické est toujours présent
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            }); */
        }

    }
}
