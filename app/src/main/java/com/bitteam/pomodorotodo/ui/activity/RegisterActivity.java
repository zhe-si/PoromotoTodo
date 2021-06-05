package com.bitteam.pomodorotodo.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.SwitchCompat;

import com.bitteam.pomodorotodo.Exception.UIException;
import com.bitteam.pomodorotodo.R;
import com.bitteam.pomodorotodo.mvp.model.UserInformationListModel;
import com.bitteam.pomodorotodo.mvp.model.bean.UserInformationBean;

public class RegisterActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }

    private EditText registerName;
    private EditText registerPwd;
    private UserInformationListModel userInformationListModel;

    Button register_btn;

    private void initView() {

        userInformationListModel = new UserInformationListModel(this);
        register_btn = findViewById(R.id.btn_register);

        registerName = findViewById(R.id.register_name);
        registerPwd = findViewById(R.id.register_pwd);

        register_btn.setOnClickListener(v -> {
             if (registerName == null || registerPwd == null) throw new UIException("找不到名字框和密码框");

             for(UserInformationBean bean:userInformationListModel.getUserInformationList()){
                 if(bean.getUsername().equals(registerName.getText().toString())){
                     Toast.makeText(RegisterActivity.this,"账号已存在",Toast.LENGTH_SHORT).show();
                     return;
                 }
             }
            UserInformationBean bean = UserInformationBean.builder()
                    .account_number(null)
                    .username(registerName.getText().toString())
                    .password(registerPwd.getText().toString())
                    .description(null)
                    .head_portrait(null)
                    .build();
             userInformationListModel.addNewUserInformation(bean);

            Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        ImageView back = findViewById(R.id.back_login);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
