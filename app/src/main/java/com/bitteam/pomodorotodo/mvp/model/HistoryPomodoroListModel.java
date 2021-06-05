package com.bitteam.pomodorotodo.mvp.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bitteam.pomodorotodo.Exception.ArgsException;
import com.bitteam.pomodorotodo.mvp.model.DataBase.PomodoroTodoDB;
import com.bitteam.pomodorotodo.mvp.model.bean.HistoryPomodoroBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Cleanup;
import lombok.NonNull;
import lombok.Setter;

/**
 * 历史番茄钟日程列表
 * 负责维护所有的历史番茄日程，包括标准番茄时钟和日程番茄时钟，完成的番茄时钟日程会保存于此
 */
public class HistoryPomodoroListModel {

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

    public HistoryPomodoroListModel(Context context) {

        this.pomodoroTodoDB = PomodoroTodoDB.getInstance(context);
    }

    @Setter
    private List<HistoryPomodoroBean> histiryPomodoroList;

    public synchronized List<HistoryPomodoroBean> getHistoryPomodoroList() {

        if (this.histiryPomodoroList == null || this.hasChanged) {
            updateList();
        }

        return histiryPomodoroList;
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

        String startTimeSt, endTimeSt;

        if (startTime == null) startTimeSt = "0";
        else startTimeSt = startTime.getTime() + "";
        if (endTime == null) endTimeSt = Long.MAX_VALUE + "";
        else endTimeSt = endTime.getTime() + "";

        this.setHistiryPomodoroList(new ArrayList<HistoryPomodoroBean>());
        @Cleanup SQLiteDatabase db = this.pomodoroTodoDB.getReadableDatabase();
        @Cleanup Cursor c = db.query(PomodoroTodoDB.HistoryPomodoroTable.TABLE_NAME, null,
                PomodoroTodoDB.HistoryPomodoroTable.START_TIME + ">=? AND " + PomodoroTodoDB.HistoryPomodoroTable.END_TIME + "<=?",
                new String[]{"" + startTimeSt, "" + endTimeSt}, null, null, PomodoroTodoDB.HistoryPomodoroTable.START_TIME);

        while (c.moveToNext()) {
            this.histiryPomodoroList.add(new HistoryPomodoroBean(c));
        }
        this.hasChanged = false;
    }

    public void addNewHistoryPomodoro(@NonNull HistoryPomodoroBean bean) {

        ContentValues values = new ContentValues();
        values.put(PomodoroTodoDB.HistoryPomodoroTable.NAME, bean.getName());
        values.put(PomodoroTodoDB.HistoryPomodoroTable.TAG, bean.getTag());
        values.put(PomodoroTodoDB.HistoryPomodoroTable.TIME_LENGTH, bean.getTimeLength());
        values.put(PomodoroTodoDB.HistoryPomodoroTable.DESCRIPTION, bean.getDescription());
        values.put(PomodoroTodoDB.HistoryPomodoroTable.IS_STRICT, bean.isStrict());
        values.put(PomodoroTodoDB.HistoryPomodoroTable.PICTURES, bean.getPictures());
        values.put(PomodoroTodoDB.HistoryPomodoroTable.START_TIME, bean.getStartTime().getTime());
        values.put(PomodoroTodoDB.HistoryPomodoroTable.END_TIME, bean.getEndTime().getTime());
        values.put(PomodoroTodoDB.HistoryPomodoroTable.SUMMARY, bean.getSummary());
        values.put(PomodoroTodoDB.HistoryPomodoroTable.POMODORO_TYPE, bean.getPomodoro_type());
        values.put(PomodoroTodoDB.HistoryPomodoroTable.POMODORO_ID, bean.getPomodoroId());

        @Cleanup SQLiteDatabase db = this.pomodoroTodoDB.getWritableDatabase();
//        db.insert(PomodoroTodoDB.HistoryPomodoroTable.TABLE_NAME, null, values);
        db.insert("history_pomodoro", null, values);
        // TODO: 不知为什么，TABLE_NAME为null？

        if (isTimeInRange(bean.getStartTime(), bean.getEndTime())) {
            this.hasChanged = true;
        }
    }

    public void deleteHistoryPomodoro(int _id) {

        getHistoryPomodoroList().remove(new HistoryPomodoroBean(_id));

        @Cleanup SQLiteDatabase db = this.pomodoroTodoDB.getWritableDatabase();
        db.delete(PomodoroTodoDB.HistoryPomodoroTable.TABLE_NAME,
                PomodoroTodoDB.HistoryPomodoroTable.ID + "=?", new String[]{"" + _id});
    }

    public void editHistoryPomodoro(int _id, @NonNull String[] args, @NonNull String[] values) {

        if (args.length > values.length) throw new ArgsException("args is longer than values");

        int index = getHistoryPomodoroList().indexOf(new HistoryPomodoroBean(_id));
        HistoryPomodoroBean bean = null;
        if (index >= 0) bean = getHistoryPomodoroList().get(index);

        @Cleanup SQLiteDatabase db = this.pomodoroTodoDB.getWritableDatabase();
        ContentValues v = new ContentValues();
        for (int i = 0; i < args.length; i++) {
            checkArgs(args, values, bean, v, i);
        }
        db.update(PomodoroTodoDB.HistoryPomodoroTable.TABLE_NAME, v,
                PomodoroTodoDB.HistoryPomodoroTable.ID + "=?", new String[]{"" + _id});
    }

    private void checkArgs(@NonNull String[] args, @NonNull String[] values, HistoryPomodoroBean bean, ContentValues v, int i) {
        switch (args[i]) {
            case PomodoroTodoDB.HistoryPomodoroTable.NAME:
                v.put(args[i], values[i]);
                if (bean != null) bean.setName(values[i]);
                break;

            case PomodoroTodoDB.HistoryPomodoroTable.DESCRIPTION:
                v.put(args[i], values[i]);
                if (bean != null) bean.setDescription(values[i]);
                break;

            case PomodoroTodoDB.HistoryPomodoroTable.SUMMARY:
                v.put(args[i], values[i]);
                if (bean != null) bean.setSummary(values[i]);
                break;

            case PomodoroTodoDB.HistoryPomodoroTable.PICTURES:
                v.put(args[i], values[i]);
                if (bean != null) bean.setPictures(values[i]);
                break;

            case PomodoroTodoDB.HistoryPomodoroTable.IS_STRICT:
                v.put(args[i], Boolean.valueOf(values[i]));
                if (bean != null) bean.setStrict(Boolean.parseBoolean(values[i]));
                break;

            case PomodoroTodoDB.HistoryPomodoroTable.START_TIME:
                v.put(args[i], Long.valueOf(values[i]));
                if (bean != null) bean.setStartTime(new Date(Long.parseLong(values[i])));
                break;

            case PomodoroTodoDB.HistoryPomodoroTable.END_TIME:
                v.put(args[i], Long.valueOf(values[i]));
                if (bean != null) bean.setEndTime(new Date(Long.parseLong(values[i])));
                break;

            case PomodoroTodoDB.HistoryPomodoroTable.TIME_LENGTH:
                v.put(args[i], Integer.valueOf(values[i]));
                if (bean != null) bean.setTimeLength(Integer.parseInt(values[i]));
                break;

            case PomodoroTodoDB.HistoryPomodoroTable.POMODORO_ID:
                v.put(args[i], Integer.parseInt(values[i]));
                if (bean != null) bean.setPomodoroId(Integer.parseInt(values[i]));
                break;

            case PomodoroTodoDB.HistoryPomodoroTable.POMODORO_TYPE:
                v.put(args[i], values[i]);
                if(bean != null)bean.setPomodoro_type(values[i]);

            default:
                break;
        }
    }
}
