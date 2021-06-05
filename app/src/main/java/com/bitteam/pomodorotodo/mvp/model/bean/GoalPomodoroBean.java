package com.bitteam.pomodorotodo.mvp.model.bean;

import android.database.Cursor;

import com.bitteam.pomodorotodo.mvp.model.DataBase.PomodoroTodoDB;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@AllArgsConstructor
@Builder
public class GoalPomodoroBean {
    public GoalPomodoroBean(int _id){
        this._id=_id;
    }
    public GoalPomodoroBean(@NonNull Cursor cursor){
        set_id(cursor.getInt(cursor.getColumnIndex(PomodoroTodoDB.GoalPomodoroTable.ID)));
        setName(cursor.getString(cursor.getColumnIndex(PomodoroTodoDB.GoalPomodoroTable.NAME)));
        setTag(cursor.getString(cursor.getColumnIndex(PomodoroTodoDB.GoalPomodoroTable.TAG)));
        setDescription(cursor.getString(cursor.getColumnIndex(PomodoroTodoDB.GoalPomodoroTable.DESCRIPTION)));
        setStrict(cursor.getInt(cursor.getColumnIndex(PomodoroTodoDB.GoalPomodoroTable.IS_STRICT)) != 0);
        setBegin_time(cursor.getLong(cursor.getColumnIndex(PomodoroTodoDB.GoalPomodoroTable.BEGIN_TIME)));
        setDealine(cursor.getLong(cursor.getColumnIndex(PomodoroTodoDB.GoalPomodoroTable.DEADLINE)));
        setTotal_time(cursor.getLong(cursor.getColumnIndex(PomodoroTodoDB.GoalPomodoroTable.TOTAL_TIME)));
        setEach_pomodoro_time(cursor.getInt(cursor.getColumnIndex(PomodoroTodoDB.GoalPomodoroTable.EACH_POMODORO_TIME)));
        setGpicture(cursor.getString(cursor.getColumnIndex(PomodoroTodoDB.GoalPomodoroTable.GPICTURE)));
    }
    @EqualsAndHashCode.Include
    private int _id;

    private String name;
    private String tag;
    private String description;
    private boolean isStrict;
    private Long begin_time;//在begin_time之后某个时间开始
    private Long dealine;//在deadline之前某个时间完成
    private Long total_time;//总共的时间=不可改变时间段的和
    private int each_pomodoro_time;//每次时长，单位min
    private String gpicture;
}