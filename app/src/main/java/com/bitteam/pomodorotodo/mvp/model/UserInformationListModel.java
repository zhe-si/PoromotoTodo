package com.bitteam.pomodorotodo.mvp.model;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bitteam.pomodorotodo.Exception.ArgsException;
import com.bitteam.pomodorotodo.Exception.DataException;
import com.bitteam.pomodorotodo.mvp.model.DataBase.PomodoroTodoDB;
import com.bitteam.pomodorotodo.mvp.model.bean.UserInformationBean;

import java.util.ArrayList;
import java.util.List;

import lombok.Cleanup;
import lombok.NonNull;
import lombok.Setter;

/**
 * 用户信息列表
 * 负责管理用户信息
 */
public class UserInformationListModel {

    private final PomodoroTodoDB pomodoroTodoDB;

    public UserInformationListModel(Context context) {

        this.pomodoroTodoDB = PomodoroTodoDB.getInstance(context);
    }

    @Setter
    private List<UserInformationBean> userInformationList;

    public synchronized List<UserInformationBean> getUserInformationList() {

        if (this.userInformationList == null) {
            updateList();
        }

        return userInformationList;
    }

    /**
     * 从数据源更新数据
     */
    public void updateList() {

        this.setUserInformationList(new ArrayList<UserInformationBean>());
        @Cleanup SQLiteDatabase db = this.pomodoroTodoDB.getReadableDatabase();
        @Cleanup Cursor c = db.query(PomodoroTodoDB.UserInformationTable.TABLE_NAME, null,
                null, null,
                null, null, PomodoroTodoDB.UserInformationTable.ID);

        while (c.moveToNext()) {
            this.userInformationList.add(new UserInformationBean(c));
        }
    }

    public void addNewUserInformation(@NonNull UserInformationBean bean) {

        getUserInformationList().add(bean);

        ContentValues values = new ContentValues();
        values.put(PomodoroTodoDB.UserInformationTable.USERNAME, bean.getUsername());
        values.put(PomodoroTodoDB.UserInformationTable.PASS_TOKEN, bean.getPassword());
        values.put(PomodoroTodoDB.UserInformationTable.DESCRIPTION, bean.getDescription());
        values.put(PomodoroTodoDB.UserInformationTable.HEAD_PORTRAIT, bean.getHead_portrait());

        @Cleanup SQLiteDatabase db = this.pomodoroTodoDB.getWritableDatabase();
        db.insert(PomodoroTodoDB.UserInformationTable.TABLE_NAME, null, values);
    }

    public void deleteUserInformation(int _id) {

        getUserInformationList().remove(UserInformationBean.builder()._id(_id).build());

        @Cleanup SQLiteDatabase db = this.pomodoroTodoDB.getWritableDatabase();
        db.delete(PomodoroTodoDB.UserInformationTable.TABLE_NAME,
                PomodoroTodoDB.UserInformationTable.ID + "=?", new String[]{"" + _id});
    }

    public void editUserInformation(int _id, @NonNull String[] args, @NonNull String[] values) {

        if (args.length > values.length) throw new ArgsException("args is longer than values");

        int index = getUserInformationList().indexOf(UserInformationBean.builder()._id(_id).build());
        if (index < 0) throw new DataException("don't have this data item");
        UserInformationBean bean = getUserInformationList().get(index);

        @Cleanup SQLiteDatabase db = this.pomodoroTodoDB.getWritableDatabase();
        ContentValues v = new ContentValues();
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case PomodoroTodoDB.UserInformationTable.ACCOUNT_NUMBER:
                    v.put(args[i], values[i]);
                    bean.setAccount_number(values[i]);
                    break;

                case PomodoroTodoDB.UserInformationTable.USERNAME:
                    v.put(args[i], values[i]);
                    bean.setUsername(values[i]);
                    break;

                case PomodoroTodoDB.UserInformationTable.PASS_TOKEN:
                    v.put(args[i], values[i]);
                    bean.setPassword(values[i]);
                    break;

                case PomodoroTodoDB.UserInformationTable.DESCRIPTION:
                    v.put(args[i], values[i]);
                    bean.setDescription(values[i]);
                    break;

                case PomodoroTodoDB.UserInformationTable.HEAD_PORTRAIT:
                    v.put(args[i], values[i]);
                    bean.setHead_portrait(values[i]);
                    break;

                default:
                    break;
            }
        }
        db.update(PomodoroTodoDB.UserInformationTable.TABLE_NAME, v,
                PomodoroTodoDB.UserInformationTable.ID + "=?" ,
                new String[]{"" + _id});
    }
}