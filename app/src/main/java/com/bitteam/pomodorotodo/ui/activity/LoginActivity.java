package com.bitteam.pomodorotodo.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.bitteam.pomodorotodo.R;
import com.bitteam.pomodorotodo.mvp.model.UserInformationListModel;
import com.bitteam.pomodorotodo.mvp.model.bean.UserInformationBean;

public class LoginActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
    }

    private EditText registerName;
    private EditText registerPwd;
    private CheckBox isSavePwdCB;
    private UserInformationListModel userInformationListModel;

    private static final String NAME = "name";
    private static final String PASS_TOKEN = "password";
    private static final String CACHE_PATH = "userDataCache";

    private void initView() {

        registerName = findViewById(R.id.input_login_name);
        registerPwd = findViewById(R.id.input_login_pwd);
        isSavePwdCB = findViewById(R.id.login_remember);

        SharedPreferences sharedPreferences = getSharedPreferences(CACHE_PATH, MODE_PRIVATE);

        String nameCache = sharedPreferences.getString(NAME, null);
        String passwordCache = sharedPreferences.getString(PASS_TOKEN, null);

        if (nameCache != null) registerName.setText(nameCache);
        if (passwordCache != null) registerPwd.setText(passwordCache);

        userInformationListModel = new UserInformationListModel(this);

        Button loginButton = findViewById(R.id.btn_login);

        loginButton.setOnClickListener(view -> {

            for(UserInformationBean bean:userInformationListModel.getUserInformationList()){

                if (registerPwd == null || registerName == null) return;
                else {
                    if(bean.getUsername().equals(registerName.getText().toString()) && bean.getPassword().equals(registerPwd.getText().toString())){
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("user_name", bean.getUsername());
                        startActivity(intent);

                        if (!bean.getUsername().equals(nameCache)) {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(PASS_TOKEN, null);
                            editor.apply();
                        }

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(NAME, bean.getUsername());
                        if (isSavePwdCB.isChecked()) editor.putString(PASS_TOKEN, bean.getPassword());
                        editor.apply();

                        return;
                    }
                }
            }
            Toast.makeText(LoginActivity.this,"账号/密码不匹配或不存在",Toast.LENGTH_SHORT).show();
        });

        TextView skipView = findViewById(R.id.skip_login);
        skipView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        TextView register = findViewById(R.id.sign_in);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}
