package com.bitteam.pomodorotodo.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bitteam.pomodorotodo.R;
import com.bitteam.pomodorotodo.mvp.model.UserInformationListModel;
import com.bitteam.pomodorotodo.ui.activity.BackupActivity;
import com.bitteam.pomodorotodo.ui.activity.LoginActivity;
import com.bitteam.pomodorotodo.ui.activity.MainActivity;
import com.bitteam.pomodorotodo.ui.activity.RegisterActivity;
import com.bitteam.pomodorotodo.ui.activity.SettingActivity;
import com.bitteam.pomodorotodo.ui.activity.StatisticActivity;

import org.w3c.dom.Text;

public class OtherFragment extends Fragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_other, container, false);

        TextView userNameTV = rootView.findViewById(R.id.nick_name);
        String userName = MainActivity.getInstance().getUserName();
        if (userName != null) userNameTV.setText(userName);

        TextView otherSetting = rootView.findViewById(R.id.other_setting);
        otherSetting.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), SettingActivity.class);
            startActivity(intent);
        });

        TextView statisticData = rootView.findViewById(R.id.statistic_data);
        statisticData.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), StatisticActivity.class);
            startActivity(intent);
        });

        TextView backup = rootView.findViewById(R.id.backup);
        backup.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), BackupActivity.class);
            startActivity(intent);
        });

        Button bntQuitButton = rootView.findViewById(R.id.btn_quit);
        bntQuitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }
}
