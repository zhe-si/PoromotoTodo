package com.bitteam.pomodorotodo.mvp.contract;

import com.bitteam.pomodorotodo.mvp.base.BasePresenter;
import com.bitteam.pomodorotodo.mvp.base.BaseView;

/**
 * 首页协议(示例）
 * 每一个功能方向或页面定义一个
 * 用来协定显示和数据的交互
 */
public interface HomeContract {

    interface IView extends BaseView {}

    interface IPresenter extends BasePresenter {}

}
