package com.bitteam.pomodorotodo.ui.adaptor;

import android.content.Context;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bitteam.pomodorotodo.R;
import com.bitteam.pomodorotodo.mvp.model.DataBase.PomodoroTodoDB;
import com.bitteam.pomodorotodo.mvp.model.HistoryPomodoroListModel;
import com.bitteam.pomodorotodo.mvp.model.bean.HistoryPomodoroBean;
import com.bitteam.pomodorotodo.ui.activity.FocusActivity;
import com.bitteam.pomodorotodo.ui.activity.MainActivity;
import com.bitteam.pomodorotodo.ui.popup.AddPomodoroPopup;
import com.github.vipulasri.timelineview.TimelineView;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Random;

import lombok.NonNull;

public class DoneRecyclerViewAdapter extends RecyclerView.Adapter<DoneRecyclerViewAdapter.ViewHolder> {

    static int[] image_resources = {R.drawable.c_img1, R.drawable.c_img2, R.drawable.c_img3, R.drawable.c_img4,
        R.drawable.c_img5, R.drawable.c_img6, R.drawable.c_img7};


    private HistoryPomodoroListModel historyPomodoroListModel;

    // 存储的数据
    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView hisTitle;
        TextView hisDescription;
        TextView hisStartEndTimeAndLength;
        TextView hisTag;
        TextView hisIsStrict;
        TimelineView timelineView;
        TextView doneId;
        CardView cardView;

        ImageView imageView;

        public ViewHolder(View itemView, @NonNull DoneRecyclerViewAdapter doneRVAdapter) {
            super(itemView);

            hisTitle = (TextView) itemView.findViewById(R.id.item_his_title);
            hisDescription = (TextView) itemView.findViewById(R.id.item_his_description);
            hisStartEndTimeAndLength = (TextView) itemView.findViewById(R.id.item_his_start_end_time_length);
            hisTag = (TextView) itemView.findViewById(R.id.item_his_tag);
            hisIsStrict = (TextView) itemView.findViewById(R.id.item_his_strict);
            timelineView = (TimelineView) itemView.findViewById(R.id.item_his_time_marker);
            imageView = itemView.findViewById(R.id.item_his_card_bg);
            doneId = itemView.findViewById(R.id.item_his_data_id);
            cardView = (CardView) itemView.findViewById(R.id.item_his_card_view);

            cardView.setOnLongClickListener((View.OnLongClickListener) v -> {

                String[] menuItems = new String[]{"删除"};

                new XPopup.Builder(doneRVAdapter.context)
                        .atView(v)
                        .asAttachList(menuItems, null, (OnSelectListener) (position, text) -> {

                            TextView doneID = (TextView) v.findViewById(R.id.item_his_data_id);
                            String doneIDSt = doneID.getText().toString();

                            MainActivity activity = MainActivity.getInstance();
                            activity.updateMenuForcedItem();

                            if (position == 0) {// 删除
                                doneRVAdapter.historyPomodoroListModel
                                        .deleteHistoryPomodoro(Integer.parseInt(doneIDSt));
                                activity.getForcedMenuItem().callOnClick();
                            }
                        })
                        .show();

                return true;
            });
        }
    }

    private Context context;

    public DoneRecyclerViewAdapter(Context context, HistoryPomodoroListModel model) {
        this.context = context;

        this.historyPomodoroListModel = model;
    }

    @androidx.annotation.NonNull
    @Override
    public ViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_history, viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view, this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        HistoryPomodoroBean historyPomodoroBean = historyPomodoroListModel.getHistoryPomodoroList().get(position);

        viewHolder.hisTitle.setText(historyPomodoroBean.getName());
        viewHolder.hisDescription.setText(historyPomodoroBean.getDescription());
        viewHolder.hisTag.setText(historyPomodoroBean.getTag());
        viewHolder.hisIsStrict.setText(historyPomodoroBean.isStrict() ? "严格模式" : "");

        String startTime = DateFormat.getPatternInstance(DateFormat.HOUR24_MINUTE).format(historyPomodoroBean.getStartTime());
        String endTime = DateFormat.getPatternInstance(DateFormat.HOUR24_MINUTE).format(historyPomodoroBean.getEndTime());
        viewHolder.hisStartEndTimeAndLength.setText(startTime + " - " + endTime + "    " + historyPomodoroBean.getTimeLength() + "min");

        viewHolder.doneId.setText(historyPomodoroBean.get_id() + "");

        SecureRandom random = new SecureRandom();
        viewHolder.imageView.setImageResource(image_resources[(random.nextInt() + position) % image_resources.length]);

        // 判断是否超时
        viewHolder.timelineView.setMarker(context.getResources().getDrawable(R.drawable.round));
    }

    @Override
    public int getItemCount() {

        return historyPomodoroListModel.getHistoryPomodoroList().size();
    }

}
