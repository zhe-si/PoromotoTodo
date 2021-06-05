package com.bitteam.pomodorotodo.ui.activity;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bitteam.pomodorotodo.Exception.DataException;
import com.bitteam.pomodorotodo.R;
import com.bitteam.pomodorotodo.mvp.model.DataBase.PomodoroTodoDB;
import com.bitteam.pomodorotodo.mvp.model.HistoryPomodoroListModel;
import com.bitteam.pomodorotodo.mvp.model.StandardPomodoroListModel;
import com.bitteam.pomodorotodo.mvp.model.bean.HistoryPomodoroBean;
import com.bitteam.pomodorotodo.mvp.model.bean.StandardPomodoroBean;
import com.bitteam.pomodorotodo.ui.widget.ClockView;

import java.util.Date;

/**
 * 锁屏界面
 */
public class FocusActivity extends AppCompatActivity {

    StandardPomodoroListModel sPModel = new StandardPomodoroListModel(this, null);
    HistoryPomodoroListModel hPModel = new HistoryPomodoroListModel(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_focus);
        initData();
        initView();
    }

    @Override
    public boolean onSupportNavigateUp() {

        // 更新历史数据
        hPBean.setEndTime(new Date());
        hPBean.setTimeLength(Math.abs(hPBean.getEndTime().compareTo(hPBean.getStartTime())));
        hPModel.addNewHistoryPomodoro(hPBean);

        finish();
        return super.onSupportNavigateUp();
    }

    private void initView() {

        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setHomeButtonEnabled(true);
        mActionBar.setDisplayHomeAsUpEnabled(true);

        clock = findViewById(R.id.clock);
        clock.setTime(sPBean.getTimeLength());

        Button startButton = findViewById(R.id.btn_start);
        clock.setStartButton(startButton);
        startButton.setOnClickListener(v -> {

            if (sPBean.getTimeLength() == 0) {

                startButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_play_arrow_white_24dp, 0, 0, 0);

                AlertDialog.Builder tipDialog = new AlertDialog.Builder(FocusActivity.this);
                tipDialog.setTitle("提示");
                tipDialog.setMessage("时间已经结束，请返回。");
                tipDialog.setPositiveButton("确定", (dialog, which) -> dialog.cancel());
                tipDialog.show();
                return;
            }

            if (!clock.isFinished()) {
                if (started) {
                    clock.stop();
                    startButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_play_arrow_white_24dp, 0, 0, 0);
                    started = false;
                } else {
                    clock.start();
                    startButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_pause_white_24dp, 0, 0, 0);
                    started = true;
                }
            } else {
                clock.finish();
                startButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_play_arrow_white_24dp, 0, 0, 0);
                started = false;

                AlertDialog.Builder tipDialog = new AlertDialog.Builder(FocusActivity.this);
                tipDialog.setTitle("提示");
                tipDialog.setMessage("时间已经结束，请返回。");
                tipDialog.setPositiveButton("确定", (dialog, which) -> dialog.cancel());
                tipDialog.show();
            }
        });
    }

    private void initData() {

        started = false;

        pomodoroId = getIntent().getIntExtra(PomodoroTodoDB.HistoryPomodoroTable.ID, -1);
        if (pomodoroId == -1) throw new DataException("can't find data with _id");

        pomodoroType = getIntent().getStringExtra(PomodoroTodoDB.HistoryPomodoroTable.POMODORO_TYPE);
//        if (pomodoroType == null) throw new DataException("can't find data pomodoro type");

//        sPModel.changePomodoroType(sPModel.getPomodoroTypeById(pomodoroId));

        sPBean = sPModel.getPomodoroById(pomodoroId);

        hPBean = new HistoryPomodoroBean();

        hPBean.setName(sPBean.getName());
        hPBean.setTag(sPBean.getTag());
        hPBean.setPomodoro_type(pomodoroType);
        hPBean.setPomodoroId(sPBean.get_id());
        hPBean.setDescription(sPBean.getDescription());
        hPBean.setPictures(sPBean.getPictures());
        hPBean.setStrict(sPBean.isStrict());
        // 启动历史事件计时
        hPBean.setStartTime(new Date());
    }

    private HistoryPomodoroBean hPBean = null;

    private StandardPomodoroBean sPBean = null;

    private int pomodoroId = -1;
    private String pomodoroType = null;

    private ClockView clock;
    private boolean started = false;
}
