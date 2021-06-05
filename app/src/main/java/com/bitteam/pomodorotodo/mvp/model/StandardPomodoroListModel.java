package com.bitteam.pomodorotodo.mvp.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bitteam.pomodorotodo.Exception.ArgsException;
import com.bitteam.pomodorotodo.Exception.DataException;
import com.bitteam.pomodorotodo.mvp.model.DataBase.PomodoroTodoDB;
import com.bitteam.pomodorotodo.mvp.model.bean.StandardPomodoroBean;

import java.util.ArrayList;
import java.util.List;

import lombok.Cleanup;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * 标准番茄时钟列表
 * 负责管理当前某类型(学习，工作，健康）所有标准番茄时钟
 */
public class StandardPomodoroListModel {

    @Getter
    private String pomodoroType;

    public void changePomodoroType(String pomodoroType) {

        this.pomodoroType = pomodoroType;
        standerdPomodoroList = null;
    }

    private final PomodoroTodoDB pomodoroTodoDB;

    public StandardPomodoroListModel(Context context, String pomodoroType) {

        this.pomodoroType = pomodoroType;
        this.pomodoroTodoDB = PomodoroTodoDB.getInstance(context);
    }

    @Setter
    private List<StandardPomodoroBean> standerdPomodoroList;

    public synchronized List<StandardPomodoroBean> getStanderdPomodoroList() {

        if (this.standerdPomodoroList == null) {
            updateList();
        }

        return standerdPomodoroList;
    }

    /**
     * 从数据源更新数据
     */
    public void updateList() {

        this.setStanderdPomodoroList(new ArrayList<StandardPomodoroBean>());
        @Cleanup SQLiteDatabase db = this.pomodoroTodoDB.getReadableDatabase();
        @Cleanup Cursor c = db.query(PomodoroTodoDB.StandardPomodoroTable.TABLE_NAME, null,
                PomodoroTodoDB.StandardPomodoroTable.TAG + "=?", new String[]{this.pomodoroType},
                null, null, PomodoroTodoDB.StandardPomodoroTable.ID);

        while (c.moveToNext()) {
            this.standerdPomodoroList.add(new StandardPomodoroBean(c));
        }
    }

    /**
     * 根据 _id 获得时钟类型
     * @param _id
     * @return
     */
    public String getPomodoroTypeById(int _id) {

        @Cleanup SQLiteDatabase db = this.pomodoroTodoDB.getReadableDatabase();
        @Cleanup Cursor c = db.query(PomodoroTodoDB.StandardPomodoroTable.TABLE_NAME,
                new String[]{PomodoroTodoDB.StandardPomodoroTable.TAG},
                PomodoroTodoDB.StandardPomodoroTable.ID + "=?", new String[]{String.valueOf(_id)},
                null, null, null);

        if (c.moveToNext()) {
            int i = c.getColumnIndex(PomodoroTodoDB.StandardPomodoroTable.ID);
            if (i >= 0) return c.getString(i);
        }
        return null;
    }

    /**
     * 根据 _id 获得bean，忽略tag限制
     * @param _id 时钟 id
     * @return 若无，返回null
     */
    public StandardPomodoroBean getPomodoroById(int _id) {

        @Cleanup SQLiteDatabase db = this.pomodoroTodoDB.getReadableDatabase();
        @Cleanup Cursor c = db.query(PomodoroTodoDB.StandardPomodoroTable.TABLE_NAME,
                null,
                PomodoroTodoDB.StandardPomodoroTable.ID + "=?", new String[]{String.valueOf(_id)},
                null, null, null);

        if (c.moveToNext()) return new StandardPomodoroBean(c);

        return null;
    }

    public void addNewStandardPomodoro(@NonNull StandardPomodoroBean bean) {

        getStanderdPomodoroList().add(bean);

        ContentValues values = new ContentValues();
        values.put(PomodoroTodoDB.StandardPomodoroTable.NAME, bean.getName());
        values.put(PomodoroTodoDB.StandardPomodoroTable.TAG, this.pomodoroType);
        values.put(PomodoroTodoDB.StandardPomodoroTable.TIME_LENGTH, bean.getTimeLength());
        values.put(PomodoroTodoDB.StandardPomodoroTable.DESCRIPTION, bean.getDescription());
        values.put(PomodoroTodoDB.StandardPomodoroTable.IS_STRICT, bean.isStrict());
        values.put(PomodoroTodoDB.StandardPomodoroTable.PICTURES, bean.getPictures());

        @Cleanup SQLiteDatabase db = this.pomodoroTodoDB.getWritableDatabase();
        db.insert(PomodoroTodoDB.StandardPomodoroTable.TABLE_NAME, null, values);
    }

    public void deleteStandardPomodoro(int _id) {

        getStanderdPomodoroList().remove(StandardPomodoroBean.builder()._id(_id).build());

        @Cleanup SQLiteDatabase db = this.pomodoroTodoDB.getWritableDatabase();
        db.delete(PomodoroTodoDB.StandardPomodoroTable.TABLE_NAME,
                PomodoroTodoDB.StandardPomodoroTable.ID + "=?", new String[]{"" + _id});
    }

    public void editStandardPomodoro(int _id, StandardPomodoroBean bean) {

        String[] args = new String[]{
                PomodoroTodoDB.StandardPomodoroTable.ID,
                PomodoroTodoDB.StandardPomodoroTable.NAME,
                PomodoroTodoDB.StandardPomodoroTable.TAG,
                PomodoroTodoDB.StandardPomodoroTable.TIME_LENGTH,
                PomodoroTodoDB.StandardPomodoroTable.DESCRIPTION,
                PomodoroTodoDB.StandardPomodoroTable.IS_STRICT,
                PomodoroTodoDB.StandardPomodoroTable.PICTURES};
        String[] values = new String[]{
                bean.get_id() + "",
                bean.getName(),
                bean.getTag(),
                bean.getTimeLength() + "",
                bean.getDescription(),
                String.valueOf(bean.isStrict()),
                bean.getPictures()
        };

        this.editStandardPomodoro(_id, args, values);
    }

    public void editStandardPomodoro(int _id, @NonNull String[] args, @NonNull String[] values) {

        if (args.length > values.length) throw new ArgsException("args is longer than values");

        int index = getStanderdPomodoroList().indexOf(StandardPomodoroBean.builder()._id(_id).build());
        if (index < 0) throw new DataException("don't have this data item");
        StandardPomodoroBean bean = getStanderdPomodoroList().get(index);

        @Cleanup SQLiteDatabase db = this.pomodoroTodoDB.getWritableDatabase();
        ContentValues v = new ContentValues();
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case PomodoroTodoDB.StandardPomodoroTable.NAME:
                    v.put(args[i], values[i]);
                    bean.setName(values[i]);
                    break;

                case PomodoroTodoDB.StandardPomodoroTable.TIME_LENGTH:
                    v.put(args[i], Integer.valueOf(values[i]));
                    bean.setTimeLength(Integer.parseInt(values[i]));
                    break;

                case PomodoroTodoDB.StandardPomodoroTable.DESCRIPTION:
                    v.put(args[i], values[i]);
                    bean.setDescription(values[i]);
                    break;

                case PomodoroTodoDB.StandardPomodoroTable.IS_STRICT:
                    v.put(args[i], Boolean.valueOf(values[i]));
                    bean.setStrict(Boolean.parseBoolean(values[i]));
                    break;

                case PomodoroTodoDB.StandardPomodoroTable.PICTURES:
                    v.put(args[i], values[i]);
                    bean.setPictures(values[i]);
                    break;

                default:
                    break;
            }
        }
        db.update(PomodoroTodoDB.StandardPomodoroTable.TABLE_NAME, v,
                PomodoroTodoDB.StandardPomodoroTable.ID + "=? AND " + PomodoroTodoDB.StandardPomodoroTable.TAG + "=?",
                new String[]{"" + _id, this.pomodoroType});
//        db.update(PomodoroTodoDB.StandardPomodoroTable.TABLE_NAME, v,
//                PomodoroTodoDB.StandardPomodoroTable._ID + "=?",
//                new String[]{"" + _id});
    }
}
