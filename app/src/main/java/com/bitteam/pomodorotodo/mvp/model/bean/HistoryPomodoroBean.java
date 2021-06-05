package com.bitteam.pomodorotodo.mvp.model.bean;

import android.database.Cursor;

import com.bitteam.pomodorotodo.mvp.model.DataBase.PomodoroTodoDB;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * 历史番茄钟日程
 */
@Getter
@Setter
public class HistoryPomodoroBean extends TimePomodoroBean {

    public HistoryPomodoroBean() {
    }

    public HistoryPomodoroBean(int _id) {
        super(_id);
    }

    public HistoryPomodoroBean(@NonNull Cursor cursor) {
        super(cursor);

        setSummary(cursor.getString(cursor.getColumnIndex(PomodoroTodoDB.HistoryPomodoroTable.SUMMARY)));
        setPomodoro_type(cursor.getString(cursor.getColumnIndex(PomodoroTodoDB.HistoryPomodoroTable.POMODORO_TYPE)));
        setPomodoroId(cursor.getInt(cursor.getColumnIndex(PomodoroTodoDB.HistoryPomodoroTable.POMODORO_ID)));
    }

    private String summary;
    private String pomodoro_type;
    private int pomodoroId;
}
