package com.bitteam.pomodorotodo.mvp.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bitteam.pomodorotodo.Exception.ArgsException;
import com.bitteam.pomodorotodo.Exception.DataException;
import com.bitteam.pomodorotodo.mvp.model.DataBase.PomodoroTodoDB;
import com.bitteam.pomodorotodo.mvp.model.bean.HabitPomodoroBean;

import java.util.ArrayList;
import java.util.List;

import lombok.Cleanup;
import lombok.NonNull;
import lombok.Setter;

/**
 * 设置目标，有开始时间和结束时间，有tag，以及是否完成目标
 */
public class HabitPomodoroListModel {
    private final String pomodoroType;
    private final PomodoroTodoDB pomodoroTodoDB;

    public HabitPomodoroListModel(Context context, String pomodoroType) {
        this.pomodoroType = pomodoroType;
        this.pomodoroTodoDB = PomodoroTodoDB.getInstance(context);
    }

    @Setter
    private List<HabitPomodoroBean> habitPomodoroList;

    public synchronized List<HabitPomodoroBean> getHabitPomodoroList() {

        if (this.habitPomodoroList == null) {
            updateList();
        }

        return habitPomodoroList;
    }
    /**
     * 从数据源更新数据
     */
    public void updateList() {

        this.setHabitPomodoroList(new ArrayList<HabitPomodoroBean>());
        @Cleanup SQLiteDatabase db = this.pomodoroTodoDB.getReadableDatabase();
        @Cleanup Cursor c = db.query(PomodoroTodoDB.HabitPomodoroTable.TABLE_NAME, null,
                PomodoroTodoDB.HabitPomodoroTable.TAG + "=?", new String[]{this.pomodoroType},
                null, null, PomodoroTodoDB.HabitPomodoroTable.ID);

        while (c.moveToNext()) {
            this.habitPomodoroList.add(new HabitPomodoroBean(c));
        }
    }

    public void addNewHabitPomodoro(@NonNull HabitPomodoroBean bean) {

        getHabitPomodoroList().add(bean);

        ContentValues values = new ContentValues();
        values.put(PomodoroTodoDB.HabitPomodoroTable.NAME, bean.getName());
        values.put(PomodoroTodoDB.HabitPomodoroTable.TAG, this.pomodoroType);
        values.put(PomodoroTodoDB.HabitPomodoroTable.DESCRIPTION, bean.getDescription());
        values.put(PomodoroTodoDB.HabitPomodoroTable.FREQUENCY,bean.getFrequency());
        values.put(PomodoroTodoDB.HabitPomodoroTable.NUMBER_OF_TIMES,bean.getNumber_of_times());
        values.put(PomodoroTodoDB.HabitPomodoroTable.EACH_POMODORO_TIME,bean.getEach_pomodoro_time());
        values.put(PomodoroTodoDB.HabitPomodoroTable.IS_STRICT, bean.isStrict());
        values.put(PomodoroTodoDB.HabitPomodoroTable.HPICTURE, bean.getHpicture());
        @Cleanup SQLiteDatabase db = this.pomodoroTodoDB.getWritableDatabase();
        db.insert(PomodoroTodoDB.HabitPomodoroTable.TABLE_NAME, null, values);
    }

    public void deleteHabitPomodoro(int _id) {

        getHabitPomodoroList().remove(HabitPomodoroBean.builder()._id(_id).build());

        @Cleanup SQLiteDatabase db = this.pomodoroTodoDB.getWritableDatabase();
        db.delete(PomodoroTodoDB.HabitPomodoroTable.TABLE_NAME,
                PomodoroTodoDB.HabitPomodoroTable.ID + "=?", new String[]{"" + _id});
    }

    public void editHabitPomodoro(int _id, @NonNull String[] args, @NonNull String[] values) {

        if (args.length > values.length) throw new ArgsException("args is longer than values");

        int index = getHabitPomodoroList().indexOf(HabitPomodoroBean.builder()._id(_id).build());
        if (index < 0) throw new DataException("don't have this data item");
        HabitPomodoroBean bean = getHabitPomodoroList().get(index);

        @Cleanup SQLiteDatabase db = this.pomodoroTodoDB.getWritableDatabase();
        ContentValues v = new ContentValues();
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case PomodoroTodoDB.HabitPomodoroTable.NAME:
                    v.put(args[i], values[i]);
                    bean.setName(values[i]);
                    break;

                case PomodoroTodoDB.HabitPomodoroTable.DESCRIPTION:
                    v.put(args[i], values[i]);
                    bean.setDescription(values[i]);
                    break;

                case PomodoroTodoDB.HabitPomodoroTable.EACH_POMODORO_TIME:
                    v.put(args[i], Integer.parseInt(values[i]));
                    bean.setEach_pomodoro_time(Integer.parseInt(values[i]));
                    break;

                case PomodoroTodoDB.HabitPomodoroTable.FREQUENCY:
                    v.put(args[i], values[i]);
                    bean.setFrequency(values[i]);
                    break;

                case PomodoroTodoDB.HabitPomodoroTable.NUMBER_OF_TIMES:
                    v.put(args[i], values[i]);
                    bean.setNumber_of_times(values[i]);
                    break;

                case PomodoroTodoDB.HabitPomodoroTable.IS_STRICT:
                    v.put(args[i], Boolean.valueOf(values[i]));
                    bean.setStrict(Boolean.parseBoolean(values[i]));
                    break;

                case PomodoroTodoDB.HabitPomodoroTable.HPICTURE:
                    v.put(args[i], values[i]);
                    bean.setHpicture(values[i]);
                    break;

                default:
                    break;
            }
        }
        db.update(PomodoroTodoDB.HabitPomodoroTable.TABLE_NAME, v,
                PomodoroTodoDB.HabitPomodoroTable.ID + "=? AND " + PomodoroTodoDB.HabitPomodoroTable.TAG + "=?",
                new String[]{"" + _id, this.pomodoroType});
    }
}
