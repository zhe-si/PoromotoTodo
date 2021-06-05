package com.bitteam.pomodorotodo.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bitteam.pomodorotodo.R;
import com.bitteam.pomodorotodo.mvp.model.StandardPomodoroListModel;
import com.bitteam.pomodorotodo.ui.adaptor.TodoRecyclerViewAdapter;


public class HealthFragment extends Fragment {

    private StandardPomodoroListModel healthSPListModel;

    private View rootView;
    private RecyclerView todoView;
    private TodoRecyclerViewAdapter todoRecyclerViewAdapter;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_exercise, container, false);
        initView();
        return rootView;
    }
    
    private void initView() {

        healthSPListModel = new StandardPomodoroListModel(getContext(), getString(R.string.title_health));

        View todoExercise = rootView.findViewById(R.id.todo_exercise);
        
        todoView = todoExercise.findViewById(R.id.todo_recycler_view);

        todoView.setHasFixedSize(true);
        todoView.setNestedScrollingEnabled(false);
        
        todoView.setLayoutManager(new LinearLayoutManager(getActivity()));

        todoRecyclerViewAdapter = new TodoRecyclerViewAdapter(getContext(), getString(R.string.title_health), healthSPListModel);
        todoView.setAdapter(todoRecyclerViewAdapter);

        TextView todoCount = todoExercise.findViewById(R.id.item_count);
        todoCount.setText(String.valueOf(healthSPListModel.getStanderdPomodoroList().size()));
    }
}
