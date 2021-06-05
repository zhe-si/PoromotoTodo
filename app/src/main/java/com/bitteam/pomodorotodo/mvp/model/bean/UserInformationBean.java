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
public class UserInformationBean {

    public UserInformationBean(int _id) {
        this._id = _id;
    }

    public UserInformationBean(@NonNull Cursor cursor) {
        set_id(cursor.getInt(cursor.getColumnIndex(PomodoroTodoDB.UserInformationTable.ID)));
        setAccount_number(cursor.getString(cursor.getColumnIndex(PomodoroTodoDB.UserInformationTable.ACCOUNT_NUMBER)));
        setUsername(cursor.getString(cursor.getColumnIndex(PomodoroTodoDB.UserInformationTable.USERNAME)));
        setPassword(cursor.getString(cursor.getColumnIndex(PomodoroTodoDB.UserInformationTable.PASS_TOKEN)));
        setDescription(cursor.getString(cursor.getColumnIndex(PomodoroTodoDB.UserInformationTable.DESCRIPTION)));
        setHead_portrait(cursor.getString(cursor.getColumnIndex(PomodoroTodoDB.UserInformationTable.HEAD_PORTRAIT)));
    }

    @EqualsAndHashCode.Include
    private int _id;

    private String account_number;
    private String username;
    private String password;
    private String description;
    private String head_portrait;
}
