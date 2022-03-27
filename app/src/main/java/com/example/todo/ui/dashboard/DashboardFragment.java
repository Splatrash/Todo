package com.example.todo.ui.dashboard;

import android.content.Context;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.R;
import com.example.todo.databinding.FragmentDashboardBinding;
import com.example.todo.model.Tache;
import com.example.todo.ui.TacheChoisieListAdapter;

import java.util.List;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private DashboardViewModel dashboardViewModel;
    private TacheChoisieListAdapter adapter;

    //Crée la vue du fragment contenant la liste des tâches choisies.
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView recyclerView =root.findViewById(R.id.rv_tache_choisie);
        Context context = recyclerView.getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setHasFixedSize(true);
        adapter = new TacheChoisieListAdapter();
        recyclerView.setAdapter(adapter);

        dashboardViewModel.getAllTachesChoisie().observe(getViewLifecycleOwner(), new Observer<List<Tache>>() {
            @Override
            public void onChanged(List<Tache> taches) {
                adapter.setTaches(taches);
                adapter.notifyDataSetChanged();
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}