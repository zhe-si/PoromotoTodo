package com.bitteam.pomodorotodo.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bitteam.pomodorotodo.R;
import com.bitteam.pomodorotodo.mvp.model.HistoryPomodoroListModel;
import com.bitteam.pomodorotodo.ui.activity.MainActivity;
import com.bitteam.pomodorotodo.ui.adaptor.DoneRecyclerViewAdapter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class HomeFragment extends Fragment {

    private HistoryPomodoroListModel hPListModel;

    private View rootView;
    private RecyclerView recyclerView;
    private DoneRecyclerViewAdapter doneRecyclerViewAdapter;
    
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        initView();
        return rootView;
    }

    private void initView() {

        hPListModel = new HistoryPomodoroListModel(getActivity());

        View todoStudy = rootView.findViewById(R.id.todo_home);
        recyclerView = todoStudy.findViewById(R.id.todo_recycler_view);

        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        doneRecyclerViewAdapter = new DoneRecyclerViewAdapter(getActivity(), hPListModel);
        recyclerView.setAdapter(doneRecyclerViewAdapter);

        TextView todoCount = todoStudy.findViewById(R.id.item_count);

        TextView itemListTitle = todoStudy.findViewById(R.id.item_list_title);
        itemListTitle.setText("历史事项");

        // 更新日期
        CalendarView calendarView = rootView.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                LocalDateTime startDate = LocalDateTime.of(year, month + 1, dayOfMonth, 0, 0);
                LocalDateTime endDate = LocalDateTime.of(year, month + 1, dayOfMonth, 23, 59, 59);

                hPListModel.setStartTime(Date.from(startDate.atZone(ZoneId.systemDefault()).toInstant()));
                hPListModel.setEndTime(Date.from(endDate.atZone(ZoneId.systemDefault()).toInstant()));

                todoCount.setText(Integer.toString(hPListModel.getHistoryPomodoroList().size()));
                doneRecyclerViewAdapter.notifyDataSetChanged();
            }
        });

        Date date = new Date(calendarView.getDate());
        LocalDateTime startDate = LocalDateTime.of(date.getYear() + 1900, date.getMonth() + 1, date.getDate(), 0, 0);
        LocalDateTime endDate = LocalDateTime.of(date.getYear() + 1900, date.getMonth() + 1, date.getDate(), 23, 59, 59);

        hPListModel.setStartTime(Date.from(startDate.atZone(ZoneId.systemDefault()).toInstant()));
        hPListModel.setEndTime(Date.from(endDate.atZone(ZoneId.systemDefault()).toInstant()));

        todoCount.setText(Integer.toString(hPListModel.getHistoryPomodoroList().size()));
    }
}
