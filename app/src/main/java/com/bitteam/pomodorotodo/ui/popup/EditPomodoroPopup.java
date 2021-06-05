package com.bitteam.pomodorotodo.ui.popup;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SwitchCompat;

import com.bitteam.pomodorotodo.Exception.UIException;
import com.bitteam.pomodorotodo.R;
import com.bitteam.pomodorotodo.mvp.model.StandardPomodoroListModel;
import com.bitteam.pomodorotodo.mvp.model.bean.StandardPomodoroBean;
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


public class EditPomodoroPopup extends CenterPopupView {

    private AppCompatEditText pomodoroNameET;
    private AppCompatEditText pomodoroLengthET;
    private SwitchCompat pomodoroStrictSwi;
    private AppCompatEditText pomodoroDescriptionET;
    private AppCompatImageView pomodoroImageView;
    private AppCompatButton pomodoroClose;
    private AppCompatButton pomodoroCreate;
    private AppCompatTextView pomodoroTopTitleTV;

    private StandardPomodoroListModel sPListModel;

    private String pomodoroType = null;

    private View menuItem = null;

    @Setter
    private String porodoroPictureURI = null;

    @Setter
    private boolean pomodoroIsStrict = false;

    private StandardPomodoroBean pomodoroOldBean = null;

    public EditPomodoroPopup withPomodoroType(@lombok.NonNull String type) {
        this.pomodoroType = type;
        return this;
    }

    public EditPomodoroPopup withMenuItem(@lombok.NonNull View menuItem) {
        this.menuItem = menuItem;
        return this;
    }

    public EditPomodoroPopup withPomodoroBean(@lombok.NonNull StandardPomodoroBean bean){
        this.pomodoroOldBean = bean;
        return this;
    }

    /**
     * 构造器
     * @param context Context
     */
    public EditPomodoroPopup(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        initComponent();

        if (pomodoroType == null) throw new UIException("porodoro type is not set");
        if (pomodoroOldBean == null) throw new UIException("porodoro old bean is not set");

        sPListModel = new StandardPomodoroListModel(getContext(), pomodoroType);

        initComponentData();

        pomodoroCreate.setOnClickListener(v -> {

            StandardPomodoroBean bean = StandardPomodoroBean.builder()
                    .name(pomodoroNameET.getText() == null ? "番茄钟" : pomodoroNameET.getText().toString())
                    .tag(null)
                    .timeLength(pomodoroLengthET.getText() == null ? 0 : Integer.parseInt(pomodoroLengthET.getText().toString()))
                    .description(pomodoroDescriptionET.getText() == null ? "这是一个番茄钟" : pomodoroDescriptionET.getText().toString())
                    .isStrict(pomodoroIsStrict)
                    .pictures(porodoroPictureURI)
                    .build();
            sPListModel.editStandardPomodoro(pomodoroOldBean.get_id(), bean);

            dismiss();

            if (menuItem != null) menuItem.callOnClick();
        });

        pomodoroClose.setOnClickListener(v -> dismiss());

        pomodoroImageView.setOnClickListener(v -> setPictureWithSelector());

        pomodoroStrictSwi.setOnCheckedChangeListener((buttonView, isChecked) -> setPomodoroIsStrict(isChecked));
    }

    private void initComponentData() {
        pomodoroTopTitleTV.setText("编辑番茄时钟");
        pomodoroNameET.setText(pomodoroOldBean.getName());
        pomodoroLengthET.setText(pomodoroOldBean.getTimeLength() + "");
        pomodoroDescriptionET.setText(changeStringWithoutNull(pomodoroOldBean.getDescription()));
        pomodoroStrictSwi.setChecked(pomodoroOldBean.isStrict());
        if (pomodoroOldBean.getPictures() == null) {
            pomodoroImageView.setImageResource(R.drawable.ic_pic);
        } else {
            pomodoroImageView.setImageURI(Uri.parse(pomodoroOldBean.getPictures()));
        }
    }

    private void initComponent() {

        pomodoroNameET = findViewById(R.id.add_pomodoro_popup_input_name);
        pomodoroLengthET = findViewById(R.id.add_pomodoro_popup_input_min);
        pomodoroStrictSwi = findViewById(R.id.add_pomodoro_popup_switch_strict);
        pomodoroDescriptionET = findViewById(R.id.add_pomodoro_popup_input_description);
        pomodoroImageView = findViewById(R.id.add_pomodoro_popup_input_image);
        pomodoroClose = findViewById(R.id.add_pomodoro_popup_close);
        pomodoroCreate = findViewById(R.id.add_pomodoro_popup_create);
        pomodoroTopTitleTV = findViewById(R.id.add_pomodoro_popup_top_title);
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
                    setPorodoroPictureURI(resultList.get(0).getPhotoPath());

                    if (porodoroPictureURI == null) {
                        pomodoroImageView.setImageResource(R.drawable.ic_pic);
                    } else {
                        pomodoroImageView.setImageURI(Uri.parse(porodoroPictureURI));
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
        return R.layout.popup_add_pomodoro;
    }

    private String changeStringWithoutNull(String s) {
        if (s == null) return "";
        return s;
    }
}