package com.bitteam.pomodorotodo.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bitteam.pomodorotodo.R;
import com.bitteam.pomodorotodo.ui.popup.AddPomodoroPopup;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.lxj.xpopup.XPopup;

import java.lang.ref.WeakReference;

import lombok.Getter;

import static androidx.navigation.ui.NavigationUI.onNavDestinationSelected;


public class MainActivity extends AppCompatActivity {

    private static MainActivity mainActivity = null;

    public static MainActivity getInstance() {
        return mainActivity;
    }

    @Getter
    private View forcedMenuItem = null;
    @Getter
    private String forcedMenuTag = null;
    @Getter
    private String userName = null;

    private BottomNavigationView navView;
    private NavController navController;
    private AppBarConfiguration appBarConfiguration;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        mainActivity = this;
        userName = getIntent().getStringExtra("user_name");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);
        menu.findItem(R.id.add_menu_item).setVisible(false);
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        updateMenuForcedItem();

        switch (item.getItemId()) {
            case R.id.room_item:
                Intent roomIntent = new Intent(MainActivity.this, RoomActivity.class);
                startActivity(roomIntent);
                break;

            case R.id.data_statistics:
                Intent intent = new Intent(MainActivity.this, StatisticActivity.class);
                startActivity(intent);
                break;

//            case R.id.add_menu_item_standard:
            case R.id.add_menu_item:
                // TODO:此处，可能会因为type类型不对而导致无用的sql操作
                new XPopup.Builder(this).asCustom(new AddPomodoroPopup(this)
                    .withPorodoroType(forcedMenuTag).withMenuItem(forcedMenuItem)).show();
                break;

//            case R.id.add_menu_item_habit:
//                new XPopup.Builder(this).asCustom(new AddPomodoroPopup(this)
//                        .withPorodoroType(forcedMenuTag).withMenuItem(forcedMenuItem)).show();
//                break;
//
//            case R.id.add_menu_item_goal:
//                new XPopup.Builder(this).asCustom(new AddPomodoroPopup(this)
//                        .withPorodoroType(forcedMenuTag).withMenuItem(forcedMenuItem)).show();
//                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateMenuForcedItem() {
        forcedMenuItem = getMenuItem(navView.getSelectedItemId());
        forcedMenuTag = getMenuTag(navView.getSelectedItemId());
    }

    private String getMenuTag(int id) {

        switch (id) {
            case R.id.navigation_home:
                return getString(R.string.title_home);
            case R.id.navigation_study:
                return getString(R.string.title_study);
            case R.id.navigation_work:
                return getString(R.string.title_work);
            case R.id.navigation_health:
                return getString(R.string.title_health);
            case R.id.navigation_setting:
                return getString(R.string.title_setting);
            default:
                return null;
        }
    }

    private View getMenuItem(int id) {

        switch (id) {
            case R.id.navigation_home:
                return navView.findViewById(R.id.navigation_home);
            case R.id.navigation_study:
                return navView.findViewById(R.id.navigation_study);
            case R.id.navigation_work:
                return navView.findViewById(R.id.navigation_work);
            case R.id.navigation_health:
                return navView.findViewById(R.id.navigation_health);
            case R.id.navigation_setting:
                return navView.findViewById(R.id.navigation_setting);
            default:
                return null;
        }
    }

    private void initView() {

        navView = findViewById(R.id.nav_view);

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        setupWithNavController(navView, navController);

        // 控制 ActionBar 是否与 navigation 同步
        appBarConfiguration = new AppBarConfiguration.Builder().build();

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
    }

    private void setupWithNavController(
        @NonNull final BottomNavigationView bottomNavigationView,
        @NonNull final NavController navController) {
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            final int homeId = bottomNavigationView.getMenu().getItem(0).getItemId();
            final int otherId = bottomNavigationView.getMenu().getItem(4).getItemId();
            if (item.getItemId() == homeId || item.getItemId() == otherId) {
                menu.findItem(R.id.add_menu_item).setVisible(false);
            } else {
                menu.findItem(R.id.add_menu_item).setVisible(true);
            }
            return onNavDestinationSelected(item, navController);
        });
        final WeakReference<BottomNavigationView> weakReference =
            new WeakReference<>(bottomNavigationView);
        navController.addOnDestinationChangedListener(
            new NavController.OnDestinationChangedListener() {
                @Override
                public void onDestinationChanged(@NonNull NavController controller,
                                                 @NonNull NavDestination destination, @Nullable Bundle arguments) {
                    BottomNavigationView view = weakReference.get();
                    if (view == null) {
                        navController.removeOnDestinationChangedListener(this);
                        return;
                    }
                    Menu menu = view.getMenu();
                    for (int h = 0, size = menu.size(); h < size; h++) {
                        MenuItem item = menu.getItem(h);
                        if (matchDestination(destination, item.getItemId())) {
                            item.setChecked(true);
                        }
                    }
                }
            });
    }

    static boolean matchDestination(@NonNull NavDestination destination,
                                    @IdRes int destId) {
        NavDestination currentDestination = destination;
        while (currentDestination.getId() != destId && currentDestination.getParent() != null) {
            currentDestination = currentDestination.getParent();
        }
        return currentDestination.getId() == destId;
    }
}
