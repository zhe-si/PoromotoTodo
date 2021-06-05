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
import com.bitteam.pomodorotodo.mvp.model.HabitPomodoroListModel;
import com.bitteam.pomodorotodo.mvp.model.bean.HabitPomodoroBean;
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

public class AddHabitPomodoroPopup extends CenterPopupView {

    private AppCompatEditText habitPomodoroNameET;
    private AppCompatSpinner habitPomodoroFrequency;
    private AppCompatEditText habitPomodoroTimes;//规定频率下的次数
    private AppCompatEditText habitPomodoroClock;
    private SwitchCompat habitPomodoroStrictSwi;
    private AppCompatEditText habitPomodoroDescriptionET;
    private AppCompatImageView habitPomodoroImageView;
    private AppCompatButton habitPomodoroCreate;
    private AppCompatButton habitPomodoroClose;

    private HabitPomodoroListModel habitPomodoroListModel;

    private String habitPomodoroType = null;

    @Setter
    private String habitPomodoroPictureURI = null;

    @Setter
    private boolean habitPomodoroIsStrict = false;

    public AddHabitPomodoroPopup withHabitPomodoroType(@lombok.NonNull String type) {
        this.habitPomodoroType = type;
        return this;
    }

    /**
     * 构造器
     * @param context
     */
    public AddHabitPomodoroPopup(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        habitPomodoroNameET = findViewById(R.id.add_habit_popup_input_name);
        habitPomodoroFrequency = findViewById(R.id.add_habit_popup_input_frequency);
        habitPomodoroTimes = findViewById(R.id.add_habit_popup_input_times);
        habitPomodoroClock = findViewById(R.id.add_habit_popup_input_clock);
        habitPomodoroStrictSwi = findViewById(R.id.add_habit_popup_switch_strict);
        habitPomodoroDescriptionET = findViewById(R.id.add_habit_popup_input_description);
        habitPomodoroImageView = findViewById(R.id.add_habit_popup_input_image);
        habitPomodoroClose = findViewById(R.id.add_habit_popup_close);
        habitPomodoroCreate = findViewById(R.id.add_habit_popup_create);

        if (habitPomodoroType == null) throw new UIException("habit pomodoro type is not set");

        habitPomodoroListModel = new HabitPomodoroListModel(getContext(), habitPomodoroType);

        habitPomodoroCreate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                HabitPomodoroBean bean = HabitPomodoroBean.builder()
                        .name(habitPomodoroNameET.getText() == null ? "新的养习惯番茄钟" :
                                habitPomodoroNameET.getText().toString())
                        .description(habitPomodoroDescriptionET.getText() == null ? "这是一个新的养习惯番茄钟" :
                                habitPomodoroDescriptionET.getText().toString())
                        .frequency(String.valueOf(habitPomodoroFrequency))
                        .tag(null)
                        .number_of_times(String.valueOf(habitPomodoroTimes))
                        .each_pomodoro_time(Integer.parseInt(String.valueOf(habitPomodoroClock)))
                        .isStrict(habitPomodoroIsStrict)
                        .hpicture(habitPomodoroPictureURI)
                        .build();
                habitPomodoroListModel.addNewHabitPomodoro(bean);

                dismiss();
            }
        });

        habitPomodoroClose.setOnClickListener(v -> dismiss());

        habitPomodoroImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setPictureWithSelector();
            }
        });

        habitPomodoroStrictSwi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setHabitPomodoroIsStrict(isChecked);
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
                    setHabitPomodoroPictureURI(resultList.get(0).getPhotoPath());

                    if (habitPomodoroPictureURI == null) {
                        habitPomodoroImageView.setImageResource(R.drawable.ic_pic);
                    } else {
                        habitPomodoroImageView.setImageURI(Uri.parse(habitPomodoroPictureURI));
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
        return R.layout.popup_add_habit;
    }
}
