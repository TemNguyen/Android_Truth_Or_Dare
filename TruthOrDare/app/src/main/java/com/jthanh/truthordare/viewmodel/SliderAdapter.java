package com.jthanh.truthordare.viewmodel;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.jthanh.truthordare.R;
import com.jthanh.truthordare.databinding.SliderItemBinding;
import com.jthanh.truthordare.dialogs.NotificationDialog;
import com.jthanh.truthordare.model.entities.PackageSelect;
import com.jthanh.truthordare.model.entities.Player;
import com.jthanh.truthordare.model.entities.Question;
import com.jthanh.truthordare.model.entities.QuestionPackage;
import com.jthanh.truthordare.model.rooms.AppDatabase;
import com.jthanh.truthordare.model.rooms.QuestionDao;
import com.jthanh.truthordare.model.rooms.QuestionPackageDao;

import java.util.ArrayList;
import java.util.Random;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder> {
    private Activity activity;

    private ArrayList<Player> sliderItems;
    private ViewPager2 viewPager2;
    private ArrayList<PackageSelect> packageSelected;

    private ArrayList<Question> listQuestion;
    private ArrayList<Question> listTruthQuestion;
    private ArrayList<Question> listDareQuestion;
    private int listQuestionSize;

    private AppDatabase appDatabase;
    private QuestionPackageDao questionPackageDao;
    private QuestionDao questionDao;

    private Random random;

    private NotificationDialog notificationDialog;

    public SliderAdapter(ArrayList<Player> sliderItems, ViewPager2 viewPager2,
                         ArrayList<PackageSelect> packageSelected, Activity activity) {
        this.activity = activity;
        this.sliderItems = sliderItems;
        this.viewPager2 = viewPager2;
        this.packageSelected = packageSelected;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SliderAdapter.SliderViewHolder(
                SliderItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {

        listQuestion = new ArrayList<>();
        listTruthQuestion = new ArrayList<>();
        listDareQuestion = new ArrayList<>();

        appDatabase = AppDatabase.getInstance(holder.itemView.getContext());
        questionDao = appDatabase.questionDao();
        questionPackageDao = appDatabase.questionPackageDao();
        random = new Random();

        notificationDialog = new NotificationDialog(activity);

        for (PackageSelect selected:
                packageSelected) {
            QuestionPackage questionPackage = selected.getQuestionPackage();
            listQuestion.addAll(questionDao.getQuestionByPackageId(questionPackage.getId()));
        }

        for (Question question:
             listQuestion) {
            if (question.getRule().equals("truth")) {
                listTruthQuestion.add(question);
            } else {
                listDareQuestion.add(question);
            }
        }

        holder.binding.tvPlayer.setText(sliderItems.get(position).getName());
        if (position == sliderItems.size() - 2) {
            viewPager2.post(runnable);
        }
        holder.binding.llTruth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listQuestionSize = listTruthQuestion.size();
                if (listQuestionSize > 0) {
                    Question rdQuestion = listTruthQuestion.get(random.nextInt(listQuestionSize));
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("question", rdQuestion);
                    bundle.putSerializable("player", sliderItems.get(holder.getAdapterPosition()));
                    bundle.putSerializable("type", "Thật");
                    Navigation.findNavController(holder.itemView).navigate(R.id.gameDetailFragment, bundle);
                } else {
                    showMessage("Không có câu hỏi thật nào :(", "sad");
                }
            }
        });
        holder.binding.llDare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listQuestionSize = listDareQuestion.size();
                if (listQuestionSize > 0) {
                    Bundle bundle = new Bundle();
                    Question rdQuestion = listDareQuestion.get(random.nextInt(listDareQuestion.size()));
                    bundle.putSerializable("question", rdQuestion);
                    bundle.putSerializable("player", sliderItems.get(holder.getAdapterPosition()));
                    bundle.putSerializable("type", "Thách");
                    Navigation.findNavController(holder.itemView).navigate(R.id.gameDetailFragment, bundle);
                } else {
                    showMessage("Không có câu hỏi thách nào :(", "sad");
                }
            }
        });
    }

    private void showMessage(String msg, String state) {
        notificationDialog.startDialog(msg, state);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                notificationDialog.dismissDialog();
            }
        }, 1000);
    }

    @Override
    public int getItemCount() {
        return sliderItems.size();
    }

    class SliderViewHolder extends RecyclerView.ViewHolder {
        private final SliderItemBinding binding;

        public SliderViewHolder(final SliderItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            sliderItems.addAll(sliderItems);
            notifyDataSetChanged();
        }
    };
}
