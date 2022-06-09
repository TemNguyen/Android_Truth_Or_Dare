package com.jthanh.truthordare.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jthanh.truthordare.R;
import com.jthanh.truthordare.databinding.CustomActionBarBinding;
import com.jthanh.truthordare.databinding.FragmentAddPlayerBinding;
import com.jthanh.truthordare.model.AppDatabase;
import com.jthanh.truthordare.model.DatabaseDao;
import com.jthanh.truthordare.model.Player;
import com.jthanh.truthordare.viewmodel.AddPlayerAdapter;

import java.util.ArrayList;

public class AddPlayerFragment extends Fragment {
    private FragmentAddPlayerBinding binding;
    private CustomActionBarBinding actionBarBinding;
    private ArrayList<Player> players;
    private AddPlayerAdapter adapter;
    private AppDatabase appDatabase;
    private DatabaseDao databaseDao;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        actionBarBinding.tvTitle.setText("Thêm người chơi");
        actionBarBinding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).popBackStack();
            }
        });

        appDatabase = AppDatabase.getInstance(this.getContext());
        databaseDao = appDatabase.databaseDao();

        players = new ArrayList<>(databaseDao.getAllPlayer());
        if (players.size() == 0){
            players.add(new Player(""));
            players.add(new Player(""));
        }

        adapter = new AddPlayerAdapter(players);
        binding.rvPlayers.setAdapter(adapter);

        binding.btnAddPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                players.add(new Player(""));
                adapter.notifyDataSetChanged();
            }
        });

        binding.btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (players.size() < 2) {
                    return;
                }

                for (Player player : players) {
                    if (player.getName().isEmpty()) {
                        return;
                    }
                }
                
                databaseDao.deleteAllPlayer();
                databaseDao.insertAllPlayer(players);
                Bundle bundle = new Bundle();
                bundle.putSerializable("listPlayer", players);
                Navigation.findNavController(view).navigate(R.id.chooseQuestion, bundle);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddPlayerBinding.inflate(inflater, container, false);
        actionBarBinding = binding.actionBar;
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}