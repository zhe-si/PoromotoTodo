package com.bitteam.pomodorotodo.mvp.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bitteam.pomodorotodo.Exception.ArgsException;
import com.bitteam.pomodorotodo.mvp.model.DataBase.PomodoroTodoDB;
import com.bitteam.pomodorotodo.mvp.model.bean.TimePomodoroBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Cleanup;
import lombok.NonNull;
import lombok.Setter;

/**
 * 日程番茄时钟列表
 * 负责管理所有与日程有关的番茄时钟（未完成的日程）
 */
public class TimePomodoroListModel {

    private final PomodoroTodoDB pomodoroTodoDB;

    /**
     * 查看日程表的范围
     */
    private Date startTime = null;
    private Date endTime = null;
    private boolean hasChanged = false;

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
        this.hasChanged = true;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
        this.hasChanged = true;
    }

    public TimePomodoroListModel(Context context) {

        this.pomodoroTodoDB = PomodoroTodoDB.getInstance(context);
    }

    @Setter
    private List<TimePomodoroBean> timePomodoroList;

    public synchronized List<TimePomodoroBean> getTimePomodoroList() {

        if (this.timePomodoroList == null || this.hasChanged) {
            updateList();
        }

        return timePomodoroList;
    }

    private boolean isTimeInRange(@NonNull Date startTime, @NonNull Date endTime) {

        if (this.startTime == null && this.endTime == null) return true;
        else if (this.startTime == null) return endTime.before(this.endTime);
        else if (this.endTime == null) return startTime.after(this.startTime);
        else return startTime.after(this.startTime) && endTime.before(this.endTime);
    }

    /**
     * 从数据源更新数据
     */
    public void updateList() {

        this.setTimePomodoroList(new ArrayList<TimePomodoroBean>());
        @Cleanup SQLiteDatabase db = this.pomodoroTodoDB.getReadableDatabase();
        @Cleanup Cursor c = db.query(PomodoroTodoDB.TimePomodoroTable.TABLE_NAME, null,
                PomodoroTodoDB.TimePomodoroTable.START_TIME + ">=? AND " + PomodoroTodoDB.TimePomodoroTable.END_TIME + "<=?",
                new String[]{"" + startTime.getTime(), "" + endTime.getTime()}, null, null, PomodoroTodoDB.TimePomodoroTable.START_TIME);

        while (c.moveToNext()) {
            this.timePomodoroList.add(new TimePomodoroBean(c));
        }
        this.hasChanged = false;
    }

    public void addNewTimePomodoro(@NonNull TimePomodoroBean bean) {

        ContentValues values = new ContentValues();
        values.put(PomodoroTodoDB.TimePomodoroTable.NAME, bean.getName());
        values.put(PomodoroTodoDB.TimePomodoroTable.TAG, bean.getTag());
        values.put(PomodoroTodoDB.TimePomodoroTable.TIME_LENGTH, bean.getTimeLength());
        values.put(PomodoroTodoDB.TimePomodoroTable.DESCRIPTION, bean.getDescription());
        values.put(PomodoroTodoDB.TimePomodoroTable.IS_STRICT, bean.isStrict());
        values.put(PomodoroTodoDB.TimePomodoroTable.PICTURES, bean.getPictures());
        values.put(PomodoroTodoDB.TimePomodoroTable.START_TIME, bean.getStartTime().getTime());
        values.put(PomodoroTodoDB.TimePomodoroTable.END_TIME, bean.getEndTime().getTime());

        @Cleanup SQLiteDatabase db = this.pomodoroTodoDB.getWritableDatabase();
        db.insert(PomodoroTodoDB.TimePomodoroTable.TABLE_NAME, null, values);

        if (isTimeInRange(bean.getStartTime(), bean.getEndTime())) {
            this.hasChanged = true;
        }
    }

    public void deleteTimePomodoro(int _id) {

        getTimePomodoroList().remove(new TimePomodoroBean(_id));

        @Cleanup SQLiteDatabase db = this.pomodoroTodoDB.getWritableDatabase();
        db.delete(PomodoroTodoDB.TimePomodoroTable.TABLE_NAME,
                PomodoroTodoDB.TimePomodoroTable.ID + "=?", new String[]{"" + _id});
    }

    public void editTimePomodoro(int _id, @NonNull String[] args, @NonNull String[] values) {

        if (args.length > values.length) throw new ArgsException("args is longer than values");

        int index = getTimePomodoroList().indexOf(new TimePomodoroBean(_id));
        TimePomodoroBean bean = null;
        if (index >= 0) bean = getTimePomodoroList().get(index);

        @Cleanup SQLiteDatabase db = this.pomodoroTodoDB.getWritableDatabase();
        ContentValues v = new ContentValues();
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case PomodoroTodoDB.TimePomodoroTable.NAME:
                    v.put(args[i], values[i]);
                    if (bean != null) bean.setName(values[i]);
                    break;

                case PomodoroTodoDB.TimePomodoroTable.TIME_LENGTH:
                    v.put(args[i], Integer.valueOf(values[i]));
                    if (bean != null) bean.setTimeLength(Integer.parseInt(values[i]));
                    break;

                case PomodoroTodoDB.TimePomodoroTable.DESCRIPTION:
                    v.put(args[i], values[i]);
                    if (bean != null) bean.setDescription(values[i]);
                    break;

                case PomodoroTodoDB.TimePomodoroTable.IS_STRICT:
                    v.put(args[i], Boolean.valueOf(values[i]));
                    if (bean != null) bean.setStrict(Boolean.parseBoolean(values[i]));
                    break;

                case PomodoroTodoDB.TimePomodoroTable.PICTURES:
                    v.put(args[i], values[i]);
                    if (bean != null) bean.setPictures(values[i]);
                    break;

                case PomodoroTodoDB.TimePomodoroTable.START_TIME:
                    v.put(args[i], Long.valueOf(values[i]));
                    if (bean != null) bean.setStartTime(new Date(Long.parseLong(values[i])));
                    break;

                case PomodoroTodoDB.TimePomodoroTable.END_TIME:
                    v.put(args[i], Long.valueOf(values[i]));
                    if (bean != null) bean.setEndTime(new Date(Long.parseLong(values[i])));
                    break;

                default:
                    break;
            }
        }
        db.update(PomodoroTodoDB.TimePomodoroTable.TABLE_NAME, v,
                PomodoroTodoDB.TimePomodoroTable.ID + "=?", new String[]{"" + _id});
    }
}
