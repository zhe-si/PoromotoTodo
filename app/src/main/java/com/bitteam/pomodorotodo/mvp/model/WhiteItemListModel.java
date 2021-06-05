package com.bitteam.pomodorotodo.mvp.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bitteam.pomodorotodo.Exception.ArgsException;
import com.bitteam.pomodorotodo.Exception.DataException;
import com.bitteam.pomodorotodo.mvp.model.DataBase.PomodoroTodoDB;
import com.bitteam.pomodorotodo.mvp.model.bean.WhiteItemBean;

import java.util.ArrayList;
import java.util.List;

import lombok.Cleanup;
import lombok.NonNull;
import lombok.Setter;

/**
 * 白名单列表
 * 负责管理所有白名单条款信息
 */
public class WhiteItemListModel {

    private final PomodoroTodoDB pomodoroTodoDB;

    public WhiteItemListModel(Context context) {

        this.pomodoroTodoDB = PomodoroTodoDB.getInstance(context);
    }

    @Setter
    private List<WhiteItemBean> whiteItemList;

    public synchronized List<WhiteItemBean> getWhiteItemList() {

        if (this.whiteItemList == null) {
            updateList();
        }

        return whiteItemList;
    }

    /**
     * 从数据源更新数据
     */
    public void updateList() {

        this.setWhiteItemList(new ArrayList<WhiteItemBean>());
        @Cleanup SQLiteDatabase db = this.pomodoroTodoDB.getReadableDatabase();
        @Cleanup Cursor c = db.query(PomodoroTodoDB.WhiteItemTable.TABLE_NAME, null,
                null, null, null, null, PomodoroTodoDB.WhiteItemTable.ID);

        while (c.moveToNext()) {
            this.whiteItemList.add(new WhiteItemBean(c));
        }
    }

    public void addNewWhiteItem(@NonNull WhiteItemBean bean) {

        getWhiteItemList().add(bean);

        ContentValues values = new ContentValues();
        values.put(PomodoroTodoDB.WhiteItemTable.APPLICATION_ID, bean.getApplication_id());
        values.put(PomodoroTodoDB.WhiteItemTable.NAME, bean.getName());

        @Cleanup SQLiteDatabase db = this.pomodoroTodoDB.getWritableDatabase();
        db.insert(PomodoroTodoDB.WhiteItemTable.TABLE_NAME, null, values);
    }

    public void deleteWhiteItem(int _id) {

        getWhiteItemList().remove(WhiteItemBean.builder()._id(_id).build());

        @Cleanup SQLiteDatabase db = this.pomodoroTodoDB.getWritableDatabase();
        db.delete(PomodoroTodoDB.WhiteItemTable.TABLE_NAME,
                PomodoroTodoDB.WhiteItemTable.ID + "=?", new String[]{"" + _id});
    }

    public void editWhiteItem(int _id, @NonNull String[] args, @NonNull String[] values) {

        if (args.length > values.length) throw new ArgsException("args is longer than values");

        int index = getWhiteItemList().indexOf(WhiteItemBean.builder()._id(_id).build());
        if (index < 0) throw new DataException("don't have this data item");
        WhiteItemBean bean = getWhiteItemList().get(index);

        @Cleanup SQLiteDatabase db = this.pomodoroTodoDB.getWritableDatabase();
        ContentValues v = new ContentValues();
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case PomodoroTodoDB.WhiteItemTable.APPLICATION_ID:
                    v.put(args[i], values[i]);
                    bean.setApplication_id(values[i]);
                    break;

                case PomodoroTodoDB.WhiteItemTable.NAME:
                    v.put(args[i], values[i]);
                    bean.setName(values[i]);
                    break;

                default:
                    break;
            }
        }
        db.update(PomodoroTodoDB.WhiteItemTable.TABLE_NAME, v,
                PomodoroTodoDB.WhiteItemTable.ID + "=?",
                new String[]{"" + _id});
    }
}
