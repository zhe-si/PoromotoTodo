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
public class StandardPomodoroBean {

    public StandardPomodoroBean() {
    }

    public StandardPomodoroBean(int _id) {
        this._id = _id;
    }

    public StandardPomodoroBean(@NonNull Cursor cursor) {
        set_id(cursor.getInt(cursor.getColumnIndex(PomodoroTodoDB.StandardPomodoroTable.ID)));
        setName(cursor.getString(cursor.getColumnIndex(PomodoroTodoDB.StandardPomodoroTable.NAME)));
        setTag(cursor.getString(cursor.getColumnIndex(PomodoroTodoDB.StandardPomodoroTable.TAG)));
        setTimeLength(cursor.getInt(cursor.getColumnIndex(PomodoroTodoDB.StandardPomodoroTable.TIME_LENGTH)));
        setDescription(cursor.getString(cursor.getColumnIndex(PomodoroTodoDB.StandardPomodoroTable.DESCRIPTION)));
        setStrict(cursor.getInt(cursor.getColumnIndex(PomodoroTodoDB.StandardPomodoroTable.IS_STRICT)) != 0);
        setPictures(cursor.getString(cursor.getColumnIndex(PomodoroTodoDB.StandardPomodoroTable.PICTURES)));
    }

    @EqualsAndHashCode.Include
    private int _id;

    @EqualsAndHashCode.Exclude
    private String name;
    @EqualsAndHashCode.Exclude
    private String tag;
    @EqualsAndHashCode.Exclude
    private int timeLength;
    @EqualsAndHashCode.Exclude
    private String description;
    @EqualsAndHashCode.Exclude
    private boolean isStrict;
    @EqualsAndHashCode.Exclude
    private String pictures;
}
