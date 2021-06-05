package com.bitteam.pomodorotodo.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bitteam.pomodorotodo.R;
import com.bitteam.pomodorotodo.mvp.model.bean.HistoryPomodoroBean;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.bitteam.pomodorotodo.mvp.model.HistoryPomodoroListModel;

public class StatisticActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);
        initView();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    private void initView() {
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setHomeButtonEnabled(true);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        initChart();
    }

    public static int diffDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        int day1 = cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);

        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        if (year1 != year2) { //同一年
            int timeDistance = 0;
            for (int i = year1; i < year2; i++) {
                if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) { // 闰年 
                    timeDistance += 366;
                } else { // 不是闰年
                    timeDistance += 365;
                }
            }

            return timeDistance + (day1 - day2);
        } else { // 不同年
            return day1 - day2;
        }
    }

    private void initChart() {
        final int days = 7;

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);

        String[] dates = new String[days];
        SimpleDateFormat formatter = new SimpleDateFormat("M-d");
        for (int i = 0; i < days; ++i) {
            dates[days - 1 - i] = formatter.format(cal.getTime());
            cal.add(Calendar.DATE, -1);
        }

        int[] studyTime = new int[days];
        int[] workTime = new int[days];
        int[] exerciseTime = new int[days];
        int todayCount = 0;
        int totalCount = 0;
        int todayTime = 0;
        int totalTime = 0;

        HistoryPomodoroListModel historyModel = new HistoryPomodoroListModel(this);
        List<HistoryPomodoroBean> historyList = historyModel.getHistoryPomodoroList();
        for (HistoryPomodoroBean historyBean : historyList) {
            Date date = historyBean.getStartTime();
            int time = historyBean.getTimeLength();
            int i = diffDay(date, cal.getTime()) - 1;
            if (i >= 0 && i < days) {
                String tag = historyBean.getTag();
                switch (tag) {
                    case "学习":
                        studyTime[i] += time;
                        break;
                    case "工作":
                        workTime[i] += time;
                        break;
                    case "健康":
                        exerciseTime[i] += time;
                        break;
                }
                if (i == days -1) {
                    todayTime += time;
                    todayCount += 1;
                }
            }
            totalTime += time;
            totalCount += 1;
        }
        initCard(todayCount, todayTime, totalCount, totalTime);
        initBarChart(studyTime, workTime, exerciseTime, dates);
        initPieChart(Arrays.stream(studyTime).sum(), Arrays.stream(workTime).sum(), Arrays.stream(exerciseTime).sum());
    }

    private void initCard(int todayCount, int todayTime, int weekCount, int weekTime) {
        TextView todayTimeView = findViewById(R.id.statistic_today_durations);
        TextView todayCountView = findViewById(R.id.statistic_today_times);
        TextView weekTimeView = findViewById(R.id.schedule_amount_durations);
        TextView weekCountView = findViewById(R.id.schedule_amount_times);
        todayTimeView.setText(Integer.toString(todayTime));
        todayCountView.setText(Integer.toString(todayCount));
        weekTimeView.setText(Integer.toString(weekTime));
        weekCountView.setText(Integer.toString(weekCount));
    }
    private void initBarChart(int[] studyTime, int[] workTime, int[] exerciseTime, String[] dates) {
        final int days = studyTime.length;

        List<BarEntry> studyData = new ArrayList<>();
        List<BarEntry> workData = new ArrayList<>();
        List<BarEntry> exerciseData = new ArrayList<>();
        for (int i = 0; i < days; ++i) {
            studyData.add(new BarEntry(i, studyTime[i]));
            workData.add(new BarEntry(i, workTime[i]));
            exerciseData.add(new BarEntry(i, exerciseTime[i]));
        }

        BarDataSet studyDataSet = new BarDataSet(studyData, "学习");
        BarDataSet workDataSet = new BarDataSet(workData, "工作");
        BarDataSet exerciseDataSet = new BarDataSet(exerciseData, "锻炼");
        studyDataSet.setColor(Color.parseColor("#EEEE00"));
        workDataSet.setColor(Color.parseColor("#00EE76"));
        exerciseDataSet.setColor(Color.parseColor("#00BFFF"));

        BarData barData = new BarData(studyDataSet, workDataSet, exerciseDataSet);
        barData.setBarWidth(0.2f);
        barData.groupBars(0f, 0.6f, 0);

        BarChart barChart = (BarChart) findViewById(R.id.statistic_chart);
        barChart.setData(barData);
        barChart.getDescription().setEnabled(false);
        barChart.getAxisRight().setEnabled(false);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setCenterAxisLabels(true);
        xAxis.setAxisMinimum(0);
        xAxis.setAxisMaximum(days + 2);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                int index = (int) value;
                if (index >= 0 && index < dates.length) {
                    return dates[index];
                }
                return "";
            }
        });
    }

    private void initPieChart(int studyTime, int workTime, int exerciseTime) {
        if (studyTime == 0 && workTime == 0 && exerciseTime == 0) {
            studyTime = 1;
            workTime = 1;
            exerciseTime = 1;
        }
        PieChart pieChart = findViewById(R.id.statistic_pie_chart);
        List<PieEntry> strings = new ArrayList<>();
        strings.add(new PieEntry(studyTime, "学习"));
        strings.add(new PieEntry(workTime, "工作"));
        strings.add(new PieEntry(exerciseTime, "锻炼"));

        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(Color.parseColor("#EEEE00"));
        colors.add(Color.parseColor("#00EE76"));
        colors.add(Color.parseColor("#00BFFF"));

        PieDataSet dataSet = new PieDataSet(strings, "Label");
        dataSet.setColors(colors);

        PieData pieData = new PieData(dataSet);
        pieData.setDrawValues(true);
        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.invalidate();
    }
}
