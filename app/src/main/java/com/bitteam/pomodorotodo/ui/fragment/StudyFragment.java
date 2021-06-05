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


public class StudyFragment extends Fragment {

    StandardPomodoroListModel studySPListModel;

    private View rootView;
    private RecyclerView todoView;
    private TodoRecyclerViewAdapter todoRecyclerViewAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_study, container, false);
        initView();
        return rootView;
    }

    private void initView() {

        studySPListModel = new StandardPomodoroListModel(getContext(), getString(R.string.title_study));

        View todoStudy = rootView.findViewById(R.id.todo_study);
        todoView = todoStudy.findViewById(R.id.todo_recycler_view);
        todoView.setLayoutManager(new LinearLayoutManager(getActivity()));
        todoRecyclerViewAdapter = new TodoRecyclerViewAdapter(getContext(), getString(R.string.title_study), studySPListModel);
        todoView.setAdapter(todoRecyclerViewAdapter);

        TextView todoCount = todoStudy.findViewById(R.id.item_count);
        todoCount.setText(String.valueOf(studySPListModel.getStanderdPomodoroList().size()));
    }
}
