package com.bitteam.pomodorotodo.mvp.model.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.bitteam.pomodorotodo.R;

import lombok.NonNull;

/**
 * 数据库管理类
 */
public class PomodoroTodoDB extends SQLiteOpenHelper {

    private final Context context;

    /**
     * 构造方法
     * @param context app的context对象
     * @param name 数据库名
     * @param factory 创建Cursor的工厂，一般为null
     * @param version 数据库版本
     */
    private PomodoroTodoDB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    private static PomodoroTodoDB instance = null;
    public static synchronized PomodoroTodoDB getInstance(@NonNull Context context) {
        if (instance == null) {
            instance = new PomodoroTodoDB(context, context.getString(R.string.database_name),
                    null, Integer.parseInt(context.getString(R.string.database_version)));
        }

        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(new StandardPomodoroTable().CREATE_TABLE);
        db.execSQL(new TimePomodoroTable().CREATE_TABLE);
        db.execSQL(new HistoryPomodoroTable().CREATE_TABLE);
        db.execSQL(new WhiteItemTable().CREATE_TABLE);
        db.execSQL(new UserInformationTable().CREATE_TABLE);
        db.execSQL(new GoalPomodoroTable().CREATE_TABLE);
        db.execSQL(new HabitPomodoroTable().CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // 更新版本，可以执行删除并重建表操作（注意数据保存）
    }


    private static final String CREATE_TABLE_SQL = "CREATE TABLE";
    private static final String INT_PRI_INCREMENT = "INTEGER PRIMARY KEY AUTOINCREMENT";
    private static final String NOT_NULL = "NOT NULL";
    private static final String DEFAULT_NULL = "DEFAULT NULL";

    /**
     * 标准番茄时钟数据库表
     */
    public class StandardPomodoroTable {

        public static final String TABLE_NAME = "standard_pomodoro";

        public static final String ID = "_id";
        public static final String NAME = "name";
        public static final String TAG = "tag";
        public static final String TIME_LENGTH = "time_length";
        public static final String DESCRIPTION = "description";
        public static final String IS_STRICT = "is_strict";
        public static final String PICTURES = "pictures";

        protected final String tStudy = context.getResources().getString(R.string.title_study);
        protected final String tWork = context.getResources().getString(R.string.title_work);
        protected final String tHealth = context.getResources().getString(R.string.title_health);

        private final String CREATE_TABLE = CREATE_TABLE_SQL + " " + TABLE_NAME + " (" +
                ID + " " + INT_PRI_INCREMENT + "," +
                NAME + " TEXT " + NOT_NULL + "," +
                TAG + " TEXT " + NOT_NULL + " CHECK(" + TAG + " IN ('" + tStudy + "', '" + tWork + "', '" + tHealth + "'))," +
                TIME_LENGTH + " INTEGER " + NOT_NULL + "," +
                DESCRIPTION + " TEXT DEFAULT ''," +
                IS_STRICT + " BOOLEAN DEFAULT 0," +
                PICTURES + " TEXT " + DEFAULT_NULL +
                ");";
    }

    /**
     * 日程番茄时钟数据库表
     */
    public class TimePomodoroTable extends StandardPomodoroTable {

        public static final String TABLE_NAME = "time_pomodoro";

        public static final String START_TIME = "start_time";
        public static final String END_TIME = "end_time";

        private final String CREATE_TABLE = CREATE_TABLE_SQL + " " + TABLE_NAME + " (" +
                ID + " " + INT_PRI_INCREMENT + "," +
                NAME + " TEXT " + NOT_NULL + "," +
                TAG + " TEXT " + NOT_NULL + " CHECK(" + TAG + " IN ('" + tStudy + "', '" + tWork + "', '" + tHealth + "'))," +
                TIME_LENGTH + " INTEGER " + NOT_NULL + "," +
                DESCRIPTION + " TEXT DEFAULT ''," +
                IS_STRICT + " BOOLEAN DEFAULT 0," +
                PICTURES + " TEXT " + DEFAULT_NULL + "," +
                START_TIME + " BIGINT " + NOT_NULL + "," +
                END_TIME + " BIGINT " + NOT_NULL +
                ");";
    }

    /**
     * 历史番茄钟日程数据库表
     */
    public class HistoryPomodoroTable extends TimePomodoroTable {

        public static final String TABLE_NAME = "history_pomodoro";

        public static final String SUMMARY = "summary";
        public static final String POMODORO_ID = "pomodoro_id"; // 对应番茄钟的_ID
        public static final String POMODORO_TYPE = "pomodoro_type";

        public static final String POMODORO_TYPE_STANDARD = "番茄钟";
        public static final String POMODORO_TYPE_GOAL = "定目标";
        public static final String POMODORO_TYPE_HABIT = "养习惯";
        public static final String POMODORO_TYPE_OTHER = "其他";

        private final String CREATE_TABLE = CREATE_TABLE_SQL + " " + TABLE_NAME + " (" +
                ID + " " + INT_PRI_INCREMENT + "," +
                NAME + " TEXT " + NOT_NULL + "," +
                TAG + " TEXT " + NOT_NULL + " CHECK(" + TAG + " IN ('" + tStudy + "', '" + tWork + "', '" + tHealth + "'))," +
                TIME_LENGTH + " INTEGER " + NOT_NULL + "," +
                DESCRIPTION + " TEXT " + DEFAULT_NULL + "," +
                IS_STRICT + " BOOLEAN DEFAULT 0," +
                PICTURES + " TEXT " + DEFAULT_NULL + "," +
                START_TIME + " BIGINT " + NOT_NULL + "," +
                END_TIME + " BIGINT " + NOT_NULL + "," +
                SUMMARY + " TEXT " + DEFAULT_NULL + "," +
//                POMODORO_TYPE + " TEXT " + NOT_NULL + " CHECK(" + POMODORO_TYPE + " IN ('" + POMODORO_TYPE_STANDARD + "', '" + POMODORO_TYPE_GOAL + "', '" + POMODORO_TYPE_HABIT + "', '" + POMODORO_TYPE_OTHER + "'))," +
                POMODORO_TYPE + " TEXT " + DEFAULT_NULL + "," +
//                POMODORO_ID + " INTEGER " + NOT_NULL + "" +
                POMODORO_ID + " INTEGER " + DEFAULT_NULL +
                ");";
    }

    /**
     * 白名单数据库表
     */
    public class WhiteItemTable{

        public static final String TABLE_NAME = "white_list";

        public static final String ID = "_id";
        public static final String APPLICATION_ID = "application_id";
        public static final String NAME = "name";

        public final String CREATE_TABLE = CREATE_TABLE_SQL + " " + TABLE_NAME +" ("+
                ID + " " + INT_PRI_INCREMENT + "," +
                APPLICATION_ID + "TEXT " + NOT_NULL + "," +
                NAME + "TEXT " + NOT_NULL +
                ");";
    }

    /**
     * 用户信息数据库表
     */
    public class UserInformationTable {

        public static final String TABLE_NAME = "user_information";

        public static final String ID = "_id";
        public static final String ACCOUNT_NUMBER = "account_number";
        public static final String USERNAME = "username";
        public static final String PASS_TOKEN = "password";
        public static final String DESCRIPTION = "description";
        public static final String HEAD_PORTRAIT = "head_portrait";

        private final String CREATE_TABLE = CREATE_TABLE_SQL + " " + TABLE_NAME + " (" +
                ID + " " + INT_PRI_INCREMENT + "," +
                ACCOUNT_NUMBER + " TEXT " + DEFAULT_NULL + "," +
                USERNAME + " TEXT " + NOT_NULL + "," +
                PASS_TOKEN + " TEXT " + NOT_NULL + "," +
                DESCRIPTION + " TEXT " + DEFAULT_NULL + "," +
                HEAD_PORTRAIT+ " TEXT " + DEFAULT_NULL + "" +
                ");";
    }

    /**
     * 目标的数据库表
     */
    public class GoalPomodoroTable{
        public static final String TABLE_NAME = "goal_pomodoro";

        public static final String ID = "_id";
        public static final String NAME = "name";
        public static final String TAG = "tag";
        public static final String DESCRIPTION = "description";
        public static final String IS_STRICT = "is_strict";
        public static final String BEGIN_TIME = "begin_time";//在begin_time之后某个时间开始
        public static final String DEADLINE = "deadline";//在deadline之前某个时间完成
        public static final String TOTAL_TIME = "total_time";//总共的时间=不可改变时间段的和,单位min
        public static final String EACH_POMODORO_TIME ="each_pomodoro_time";
        public static final String GPICTURE = "gpicture";

        protected final String tStudy = context.getResources().getString(R.string.title_study);
        protected final String tWork = context.getResources().getString(R.string.title_work);
        protected final String tHealth = context.getResources().getString(R.string.title_health);

        private final String CREATE_TABLE = CREATE_TABLE_SQL + " " + TABLE_NAME + " (" +
                ID + " " + INT_PRI_INCREMENT + "," +
                NAME + " TEXT " + NOT_NULL + "," +
                TAG + " TEXT " + NOT_NULL + " CHECK(" + TAG + " IN ('" + tStudy + "', '" + tWork + "', '" + tHealth + "'))," +
                DESCRIPTION + " TEXT DEFAULT ''," +
                IS_STRICT + " BOOLEAN DEFAULT 0," +
                BEGIN_TIME + " LONG " + NOT_NULL + "," +
                DEADLINE + " LONG " + NOT_NULL + "," +
                TOTAL_TIME + "LONG " + NOT_NULL + "," +
                EACH_POMODORO_TIME + " INREGER " + NOT_NULL + "," +
                GPICTURE + "TEXT " + DEFAULT_NULL + "" +
                ");";
    }

    /**
     * 习惯数据库表
     */
    public class HabitPomodoroTable{
        public static final String TABLE_NAME = "habit_pomodoro";

        public static final String ID = "_id";
        public static final String NAME = "name";
        public static final String TAG = "tag";
        public static final String DESCRIPTION = "description";
        public static final String FREQUENCY="frequency";//频率：每天，每周，每月。
        public static final String NUMBER_OF_TIMES="number_of_times";//确定频率下的次数，如：每月三次
        public static final String EACH_POMODORO_TIME ="each_pomodoro_time";
        public static final String IS_STRICT = "is_strict";
        public static final String HPICTURE = "hpicture";

        protected final String tStudy = context.getResources().getString(R.string.title_study);
        protected final String tWork = context.getResources().getString(R.string.title_work);
        protected final String tHealth = context.getResources().getString(R.string.title_health);

        private final String CREATE_TABLE = CREATE_TABLE_SQL + " " + TABLE_NAME + " (" +
                ID + " " + INT_PRI_INCREMENT + "," +
                NAME + " TEXT " + NOT_NULL + "," +
                TAG + " TEXT " + NOT_NULL + " CHECK(" + TAG + " IN ('" + tStudy + "', '" + tWork + "', '" + tHealth + "'))," +
                DESCRIPTION + " TEXT DEFAULT ''," +
                FREQUENCY + " TEXT " + NOT_NULL + " CHECK(" + FREQUENCY + " IN ('每天','每周','每月'))," +
                NUMBER_OF_TIMES+ " TEXT " + NOT_NULL + ","+
                EACH_POMODORO_TIME + " INREGER " + NOT_NULL + ","+
                IS_STRICT + " BOOLEAN DEFAULT 0," +
                HPICTURE + " TEXT " + DEFAULT_NULL +
                ");";
    }
}
