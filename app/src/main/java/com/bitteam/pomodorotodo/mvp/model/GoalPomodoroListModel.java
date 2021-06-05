package com.bitteam.pomodorotodo.mvp.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bitteam.pomodorotodo.Exception.ArgsException;
import com.bitteam.pomodorotodo.Exception.DataException;
import com.bitteam.pomodorotodo.mvp.model.DataBase.PomodoroTodoDB;
import com.bitteam.pomodorotodo.mvp.model.bean.GoalPomodoroBean;

import java.util.ArrayList;
import java.util.List;

import lombok.Cleanup;
import lombok.NonNull;
import lombok.Setter;

/**
 * 设置目标，有开始时间和结束时间，有tag，以及是否完成目标
 */
public class GoalPomodoroListModel {
    private final String pomodoroType;
    private final PomodoroTodoDB pomodoroTodoDB;

    public GoalPomodoroListModel(Context context, String pomodoroType) {
        this.pomodoroType = pomodoroType;
        this.pomodoroTodoDB = PomodoroTodoDB.getInstance(context);
    }

    @Setter
    private List<GoalPomodoroBean> goalPomodoroList;

    public synchronized List<GoalPomodoroBean> getGoalPomodoroList() {

        if (this.goalPomodoroList == null) {
            updateList();
        }

        return goalPomodoroList;
    }
    /**
     * 从数据源更新数据
     */
    public void updateList() {

        this.setGoalPomodoroList(new ArrayList<GoalPomodoroBean>());
        @Cleanup SQLiteDatabase db = this.pomodoroTodoDB.getReadableDatabase();
        @Cleanup Cursor c = db.query(PomodoroTodoDB.GoalPomodoroTable.TABLE_NAME, null,
                PomodoroTodoDB.GoalPomodoroTable.TAG + "=?", new String[]{this.pomodoroType},
                null, null, PomodoroTodoDB.GoalPomodoroTable.ID);

        while (c.moveToNext()) {
            this.goalPomodoroList.add(new GoalPomodoroBean(c));
        }
    }

    public void addNewGoalPomodoro(@NonNull GoalPomodoroBean bean) {

        getGoalPomodoroList().add(bean);

        ContentValues values = new ContentValues();
        values.put(PomodoroTodoDB.GoalPomodoroTable.NAME, bean.getName());
        values.put(PomodoroTodoDB.GoalPomodoroTable.TAG, this.pomodoroType);
        values.put(PomodoroTodoDB.GoalPomodoroTable.DESCRIPTION, bean.getDescription());
        values.put(PomodoroTodoDB.GoalPomodoroTable.IS_STRICT, bean.isStrict());
        values.put(PomodoroTodoDB.GoalPomodoroTable.BEGIN_TIME,bean.getBegin_time());
        values.put(PomodoroTodoDB.GoalPomodoroTable.DEADLINE,bean.getDealine());
        values.put(PomodoroTodoDB.GoalPomodoroTable.TOTAL_TIME,bean.getTotal_time());
        values.put(PomodoroTodoDB.GoalPomodoroTable.EACH_POMODORO_TIME,bean.getEach_pomodoro_time());
        values.put(PomodoroTodoDB.GoalPomodoroTable.GPICTURE, bean.getGpicture());
        @Cleanup SQLiteDatabase db = this.pomodoroTodoDB.getWritableDatabase();
        db.insert(PomodoroTodoDB.GoalPomodoroTable.TABLE_NAME, null, values);
    }

    public void deleteGoalPomodoro(int _id) {

        getGoalPomodoroList().remove(GoalPomodoroBean.builder()._id(_id).build());

        @Cleanup SQLiteDatabase db = this.pomodoroTodoDB.getWritableDatabase();
        db.delete(PomodoroTodoDB.GoalPomodoroTable.TABLE_NAME,
                PomodoroTodoDB.GoalPomodoroTable.ID + "=?", new String[]{"" + _id});
    }

    public void editGoalPomodoro(int _id, @NonNull String[] args, @NonNull String[] values) {

        if (args.length > values.length) throw new ArgsException("args is longer than values");

        int index = getGoalPomodoroList().indexOf(GoalPomodoroBean.builder()._id(_id).build());
        if (index < 0) throw new DataException("don't have this data item");
        GoalPomodoroBean bean = getGoalPomodoroList().get(index);

        @Cleanup SQLiteDatabase db = this.pomodoroTodoDB.getWritableDatabase();
        ContentValues v = new ContentValues();
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case PomodoroTodoDB.GoalPomodoroTable.NAME:
                    v.put(args[i], values[i]);
                    bean.setName(values[i]);
                    break;

                case PomodoroTodoDB.GoalPomodoroTable.BEGIN_TIME:
                    v.put(args[i], Long.parseLong(values[i]));
                    bean.setBegin_time(Long.parseLong(values[i]));
                    break;

                case PomodoroTodoDB.GoalPomodoroTable.DEADLINE:
                    v.put(args[i], Long.parseLong(values[i]));
                    bean.setDealine(Long.parseLong(values[i]));
                    break;

                case PomodoroTodoDB.GoalPomodoroTable.DESCRIPTION:
                    v.put(args[i], values[i]);
                    bean.setDescription(values[i]);
                    break;

                case PomodoroTodoDB.GoalPomodoroTable.IS_STRICT:
                    v.put(args[i], Boolean.valueOf(values[i]));
                    bean.setStrict(Boolean.parseBoolean(values[i]));
                    break;

                case PomodoroTodoDB.GoalPomodoroTable.EACH_POMODORO_TIME:
                    v.put(args[i], Integer.parseInt(values[i]));
                    bean.setEach_pomodoro_time(Integer.parseInt(values[i]));
                    break;

                case PomodoroTodoDB.GoalPomodoroTable.TOTAL_TIME:
                    v.put(args[i], Long.parseLong(values[i]));
                    bean.setTotal_time(Long.parseLong(values[i]));
                    break;

                case PomodoroTodoDB.GoalPomodoroTable.GPICTURE:
                    v.put(args[i], values[i]);
                    bean.setGpicture(values[i]);
                    break;

                default:
                    break;
            }
        }
        db.update(PomodoroTodoDB.GoalPomodoroTable.TABLE_NAME, v,
                PomodoroTodoDB.GoalPomodoroTable.ID + "=? AND " + PomodoroTodoDB.GoalPomodoroTable.TAG + "=?",
                new String[]{"" + _id, this.pomodoroType});
    }
}
