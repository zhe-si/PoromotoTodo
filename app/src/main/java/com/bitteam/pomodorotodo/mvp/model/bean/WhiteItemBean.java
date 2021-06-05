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
public class WhiteItemBean {

    public WhiteItemBean(int _id) {
        this._id = _id;
    }

    public WhiteItemBean(@NonNull Cursor cursor) {
        set_id(cursor.getInt(cursor.getColumnIndex(PomodoroTodoDB.WhiteItemTable.ID)));
        setApplication_id(cursor.getString(cursor.getColumnIndex(PomodoroTodoDB.WhiteItemTable.APPLICATION_ID)));
        setName(cursor.getString(cursor.getColumnIndex(PomodoroTodoDB.WhiteItemTable.NAME)));
    }

    @EqualsAndHashCode.Include
    private int _id;

    private String application_id;
    private String name;
}
