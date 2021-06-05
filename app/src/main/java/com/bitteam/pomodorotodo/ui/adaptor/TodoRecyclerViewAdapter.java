package com.bitteam.pomodorotodo.ui.adaptor;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bitteam.pomodorotodo.R;
import com.bitteam.pomodorotodo.mvp.model.DataBase.PomodoroTodoDB;
import com.bitteam.pomodorotodo.mvp.model.StandardPomodoroListModel;
import com.bitteam.pomodorotodo.mvp.model.bean.StandardPomodoroBean;
import com.bitteam.pomodorotodo.ui.activity.FocusActivity;
import com.bitteam.pomodorotodo.ui.activity.MainActivity;
import com.bitteam.pomodorotodo.ui.popup.EditPomodoroPopup;
import com.github.vipulasri.timelineview.TimelineView;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;

import java.security.SecureRandom;

import lombok.NonNull;


public class TodoRecyclerViewAdapter extends RecyclerView.Adapter<TodoRecyclerViewAdapter.PomodoroViewHolder> {

    static int[] image_resources = {R.drawable.c_img1, R.drawable.c_img2, R.drawable.c_img3, R.drawable.c_img4,
        R.drawable.c_img5, R.drawable.c_img6, R.drawable.c_img7};

    // 视图容器
    static class PomodoroViewHolder extends RecyclerView.ViewHolder {

        TextView todoTitle;
        TextView todoDescription;
        TextView todoLength;
        CardView cardView;
        TextView todoIsStrict;
        TimelineView timelineView;
        TextView todoId;
        ImageView imageView;

        public PomodoroViewHolder(View itemView, @NonNull TodoRecyclerViewAdapter todoRVAdapter) {
            super(itemView);

            todoTitle = (TextView) itemView.findViewById(R.id.item_todo_title);
            todoDescription = (TextView) itemView.findViewById(R.id.item_todo_description);
            todoLength = (TextView) itemView.findViewById(R.id.item_todo_length);
            cardView = (CardView) itemView.findViewById(R.id.item_todo_card_view);
            todoIsStrict = (TextView) itemView.findViewById(R.id.item_todo_strict);
            timelineView = (TimelineView) itemView.findViewById(R.id.item_todo_time_marker);
            todoId = (TextView) itemView.findViewById(R.id.item_todo_data_id);
            imageView = itemView.findViewById(R.id.item_todo_card_bg);

            cardView.setOnClickListener((View.OnClickListener) v -> {

                Intent focusIntent = new Intent(MainActivity.getInstance(), FocusActivity.class);
                TextView todoID = (TextView) v.findViewById(R.id.item_todo_data_id);
                String todoIDSt = todoID.getText().toString();
                focusIntent.putExtra(PomodoroTodoDB.HistoryPomodoroTable.ID, Integer.parseInt(todoIDSt));
                MainActivity.getInstance().startActivity(focusIntent);
            });

            cardView.setOnLongClickListener((View.OnLongClickListener) v -> {

                String[] menuItems = new String[]{"编辑", "删除"};

                new XPopup.Builder(todoRVAdapter.context)
                        .atView(v)
                        .asAttachList(menuItems, null, (OnSelectListener) (position, text) -> {

                            TextView todoID = (TextView) v.findViewById(R.id.item_todo_data_id);
                            int todoIDInt = Integer.parseInt(todoID.getText().toString());

                            MainActivity activity = MainActivity.getInstance();
                            activity.updateMenuForcedItem();

                            switch (position) {
                                case 0:
                                    StandardPomodoroBean bean = todoRVAdapter.standardPomodoroListModel.getPomodoroById(todoIDInt);
                                    new XPopup.Builder(activity).asCustom(new EditPomodoroPopup(activity)
                                            .withPomodoroType(todoRVAdapter.standardPomodoroListModel.getPomodoroType())
                                            .withMenuItem(activity.getForcedMenuItem())
                                            .withPomodoroBean(bean)).show();
                                    break;
                                case 1:
                                    todoRVAdapter.standardPomodoroListModel
                                            .deleteStandardPomodoro(todoIDInt);
                                    activity.getForcedMenuItem().callOnClick();
                                    break;
                            }
                        })
                        .show();

                return true;
            });
        }
    }


    private StandardPomodoroListModel standardPomodoroListModel;

    private final Context context;
    private final String pomodoroTag;

    public TodoRecyclerViewAdapter(Context context, String pomodoroTag,
                                   @NonNull StandardPomodoroListModel SPListModel) {
        this.context = context;
        this.pomodoroTag = pomodoroTag;

        this.standardPomodoroListModel = SPListModel;
    }

    @Override
    public PomodoroViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_todo, viewGroup,false);
        return new PomodoroViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(PomodoroViewHolder pomodoroViewHolder, int position) {

        StandardPomodoroBean standardPomodoroBean =
                standardPomodoroListModel.getStanderdPomodoroList().get(position);

        pomodoroViewHolder.todoTitle.setText(standardPomodoroBean.getName());
        pomodoroViewHolder.todoLength.setText(standardPomodoroBean.getTimeLength() + "分钟");
        pomodoroViewHolder.todoDescription.setText(standardPomodoroBean.getDescription());
        pomodoroViewHolder.todoIsStrict.setText(standardPomodoroBean.isStrict() ? "严格模式" : "");
        pomodoroViewHolder.todoId.setText(standardPomodoroBean.get_id() + "");

        SecureRandom random = new SecureRandom();
        pomodoroViewHolder.imageView.setImageResource(image_resources[(random.nextInt() - position) % image_resources.length]);

        // 判断是否超时
        pomodoroViewHolder.timelineView.setMarker(context.getResources().getDrawable(R.drawable.round));
    }

    @Override
    public int getItemCount() {
        return standardPomodoroListModel.getStanderdPomodoroList().size();
    }
}
