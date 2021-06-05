package com.bitteam.pomodorotodo.mvp.model.bean;

import android.database.Cursor;

import com.bitteam.pomodorotodo.mvp.model.DataBase.PomodoroTodoDB;

import java.util.Date;
import java.util.Objects;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class TimePomodoroBean extends StandardPomodoroBean {

    public TimePomodoroBean() {
    }

    public TimePomodoroBean(int _id) {
        super(_id);
    }

    public TimePomodoroBean(@NonNull Cursor cursor) {
        super(cursor);

        setStartTime(new Date(cursor.getLong(cursor.getColumnIndex(PomodoroTodoDB.TimePomodoroTable.START_TIME))));
        setEndTime(new Date(cursor.getLong(cursor.getColumnIndex(PomodoroTodoDB.TimePomodoroTable.END_TIME))));
    }

    private Date startTime = null; // LocalDataTime API过高，Android api 26 版本才支持
    private Date endTime = null;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TimePomodoroBean)) return false;
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
