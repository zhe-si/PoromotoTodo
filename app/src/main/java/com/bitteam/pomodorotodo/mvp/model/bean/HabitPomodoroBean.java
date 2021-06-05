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
public class HabitPomodoroBean {
    public HabitPomodoroBean(int _id){
        this._id=_id;
    }
    public HabitPomodoroBean(@NonNull Cursor cursor){
        set_id(cursor.getInt(cursor.getColumnIndex(PomodoroTodoDB.HabitPomodoroTable.ID)));
        setName(cursor.getString(cursor.getColumnIndex(PomodoroTodoDB.HabitPomodoroTable.NAME)));
        setTag(cursor.getString(cursor.getColumnIndex(PomodoroTodoDB.HabitPomodoroTable.TAG)));
        setDescription(cursor.getString(cursor.getColumnIndex(PomodoroTodoDB.HabitPomodoroTable.DESCRIPTION)));
        setFrequency(cursor.getString(cursor.getColumnIndex(PomodoroTodoDB.HabitPomodoroTable.FREQUENCY)));
        setNumber_of_times(cursor.getString(cursor.getColumnIndex(PomodoroTodoDB.HabitPomodoroTable.NUMBER_OF_TIMES)));
        setEach_pomodoro_time(cursor.getInt(cursor.getColumnIndex(PomodoroTodoDB.HabitPomodoroTable.EACH_POMODORO_TIME)));
        setStrict(cursor.getInt(cursor.getColumnIndex(PomodoroTodoDB.HabitPomodoroTable.IS_STRICT)) != 0);
        setHpicture(cursor.getString(cursor.getColumnIndex(PomodoroTodoDB.HabitPomodoroTable.HPICTURE)));
    }
    @EqualsAndHashCode.Include
    private int _id;

    private String name;
    private String description;
    private String frequency;
    private String tag;
    private String number_of_times;
    private int each_pomodoro_time;//每次的时长，单位min
    private boolean isStrict;
    private String hpicture;

}
