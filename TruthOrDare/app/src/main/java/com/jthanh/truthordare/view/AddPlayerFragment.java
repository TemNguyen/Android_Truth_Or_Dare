package com.jthanh.truthordare.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jthanh.truthordare.R;
import com.jthanh.truthordare.databinding.FragmentAddPlayerBinding;
import com.jthanh.truthordare.model.Player;
import com.jthanh.truthordare.viewmodel.AddPlayerAdapter;

import java.util.ArrayList;

public class AddPlayerFragment extends Fragment {
    private FragmentAddPlayerBinding binding;
    private ArrayList<Player> players;
    private AddPlayerAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        players = new ArrayList<>();
        players.add(new Player(0, "Thanh"));
        players.add(new Player(1, "Loc"));
        adapter = new AddPlayerAdapter(players);
        binding.rvPlayers.setAdapter(adapter);

        binding.btnAddPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                players.add(new Player(3, ""));
                adapter.notifyDataSetChanged();
            }
        });

        binding.btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (Player player : players) {
                    if (player.getName().isEmpty()) {
                        return;
                    }
                }
                Bundle bundle = new Bundle();
                bundle.putSerializable("listPlayer", players);
                Navigation.findNavController(view).navigate(R.id.gameFragment, bundle);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddPlayerBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}