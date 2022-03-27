package com.example.todo.ui.home;

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

import com.example.todo.databinding.FragmentHomeBinding;
import com.example.todo.model.Tache;
import com.example.todo.ui.FormulaireTacheFragment;
import com.example.todo.ui.TacheListAdapter;

import com.example.todo.R;

import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private TacheListAdapter adapter;

    private FormulaireTacheFragment formTacheFragment;

    //Crée la vue du fragments des tâches disponibles.
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}