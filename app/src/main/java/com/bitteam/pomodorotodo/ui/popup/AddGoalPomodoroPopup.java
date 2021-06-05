package com.bitteam.pomodorotodo.ui.popup;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.SwitchCompat;

import com.bitteam.pomodorotodo.Exception.UIException;
import com.bitteam.pomodorotodo.R;
import com.bitteam.pomodorotodo.mvp.model.GoalPomodoroListModel;
import com.bitteam.pomodorotodo.mvp.model.bean.GoalPomodoroBean;
import com.bitteam.pomodorotodo.ui.popup.Tools.GlideImageLoader;
import com.lxj.xpopup.core.CenterPopupView;

import java.util.List;

import cn.finalteam.galleryfinal.BuildConfig;
import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ImageLoader;
import cn.finalteam.galleryfinal.ThemeConfig;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import lombok.Setter;

public class AddGoalPomodoroPopup extends CenterPopupView {

    private AppCompatEditText goalPomodoroNameET;
    private SwitchCompat goalPomodoroStrictSwi;
    private AppCompatEditText goalPomodoroDescriptionET;
    private AppCompatImageView goalPomodoroImageView;
    private AppCompatButton goalPomodoroClose;
    private AppCompatButton goalPomodoroCreate;
    private AppCompatSpinner goalPomodoroDeadline;
    private AppCompatEditText goalPomodoroTotalTime;
    private AppCompatEditText goalPomodoroClock;

    private GoalPomodoroListModel goalPomodoroListModel;

    private String goalPomodoroType = null;

    @Setter
    private String goalPomodoroPictureURI = null;

    @Setter
    private boolean goalPomodoroIsStrict = false;

    public AddGoalPomodoroPopup withGoalPomodoroType(@lombok.NonNull String type) {
        this.goalPomodoroType = type;
        return this;
    }

    /**
     * 构造器
     * @param context
     */
    public AddGoalPomodoroPopup(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        goalPomodoroNameET = findViewById(R.id.add_goal_popup_input_name);
        goalPomodoroStrictSwi = findViewById(R.id.add_goal_popup_switch_strict);
        goalPomodoroDescriptionET = findViewById(R.id.add_goal_popup_input_description);
        goalPomodoroImageView = findViewById(R.id.add_goal_popup_input_image);
        goalPomodoroClose = findViewById(R.id.add_goal_popup_close);
        goalPomodoroCreate = findViewById(R.id.add_goal_popup_create);
        goalPomodoroDeadline = findViewById(R.id.add_goal_popup_input_deadline);
        goalPomodoroTotalTime = findViewById(R.id.add_goal_popup_input_totalTime);
        goalPomodoroClock = findViewById(R.id.add_goal_popup_input_clock);

        if (goalPomodoroType == null) throw new UIException("goal pomodoro type is not set");

        goalPomodoroListModel = new GoalPomodoroListModel(getContext(), goalPomodoroType);

        goalPomodoroCreate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                GoalPomodoroBean bean = GoalPomodoroBean.builder()
                        .name(goalPomodoroNameET.getText() == null?"新的定目标时钟":
                                goalPomodoroNameET.getText().toString())
                        .tag(null)
                        .description(goalPomodoroDescriptionET.getText() == null ? "这是一个新的番茄钟" :
                                goalPomodoroDescriptionET.getText().toString())
                        .isStrict(goalPomodoroIsStrict)
                        .dealine(Long.parseLong(String.valueOf(goalPomodoroDeadline)))
                        .total_time(Long.parseLong(String.valueOf(goalPomodoroTotalTime)))
                        .each_pomodoro_time(Integer.parseInt(String.valueOf(goalPomodoroClock)))
                        .gpicture(goalPomodoroPictureURI)
                        .build();
                goalPomodoroListModel.addNewGoalPomodoro(bean);

                dismiss();
            }
        });

        goalPomodoroClose.setOnClickListener(v -> dismiss());

        goalPomodoroImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setPictureWithSelector();
            }
        });

        goalPomodoroStrictSwi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setGoalPomodoroIsStrict(isChecked);
            }
        });
    }

    private void setPictureWithSelector() {
        ThemeConfig theme = new ThemeConfig.Builder().build();

        FunctionConfig functionConfig = new FunctionConfig.Builder()
                .setEnableCamera(true)
                .setEnableEdit(true)
                .setEnableCrop(true)
                .setEnableRotate(true)
                .setCropSquare(true)
                .setEnablePreview(true)
                .build();

        ImageLoader imageloader = new GlideImageLoader();

        CoreConfig coreConfig = new CoreConfig.Builder(getContext(), imageloader, theme)
                .setDebug(BuildConfig.DEBUG)
                .setFunctionConfig(functionConfig)
                .build();

        GalleryFinal.init(coreConfig);

        GalleryFinal.openGallerySingle(1, new GalleryFinal.OnHanlderResultCallback() {
            @Override
            public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                if (!resultList.isEmpty()) {
                    setGoalPomodoroPictureURI(resultList.get(0).getPhotoPath());

                    if (goalPomodoroPictureURI == null) {
                        goalPomodoroImageView.setImageResource(R.drawable.ic_pic);
                    } else {
                        goalPomodoroImageView.setImageURI(Uri.parse(goalPomodoroPictureURI));
                    }
                }
            }

            @Override
            public void onHanlderFailure(int requestCode, String errorMsg) {
                Log.println(Log.WARN, "Pic", "picture select failure: " + errorMsg);
            }
        });
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.popup_add_goal;
    }
}
