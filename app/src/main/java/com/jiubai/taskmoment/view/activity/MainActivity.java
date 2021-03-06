package com.jiubai.taskmoment.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiubai.taskmoment.R;
import com.jiubai.taskmoment.adapter.TimelineAdapter;
import com.jiubai.taskmoment.config.Config;
import com.jiubai.taskmoment.config.Constants;
import com.jiubai.taskmoment.common.UtilBox;
import com.jiubai.taskmoment.receiver.UpdateViewReceiver;
import com.jiubai.taskmoment.view.fragment.MemberFragment;
import com.jiubai.taskmoment.view.fragment.PreferenceFragment;
import com.jiubai.taskmoment.view.fragment.TimelineFragment;
import com.jiubai.taskmoment.view.fragment.UserInfoFragment;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 主activity界面
 */
@SuppressLint("SetTextI18n")
public class MainActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.dw_main)
    DrawerLayout dw;

    @Bind(R.id.iBtn_back)
    ImageButton iBtn_back;

    @Bind(R.id.tv_title)
    TextView tv_title;

    @Bind(R.id.iBtn_tool)
    ImageButton iBtn_publish;

    @Bind(R.id.nv_main)
    NavigationView nv;

    private LinearLayout ll_nvHeader;
    private CircleImageView iv_navigation;
    private TextView tv_nickname;
    public static LinearLayout toolbar;

    private UpdateViewReceiver nicknameReceiver, portraitReceiver;
    private FragmentManager fragmentManager;
    private int currentItem = 0;
    private long doubleClickTime = 0;

    private TimelineFragment frag_timeline = new TimelineFragment();
    private MemberFragment _memberFragment = new MemberFragment();
    private UserInfoFragment frag_userInfo = new UserInfoFragment();
    private PreferenceFragment frag_preference = new PreferenceFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UtilBox.setStatusBarTint(this, R.color.statusBar);

        setContentView(R.layout.aty_main);

        ButterKnife.bind(this);

        initView();
    }

    /**
     * 初始化界面
     */
    private void initView() {
        toolbar = (LinearLayout) findViewById(R.id.toolBar);

        tv_title.setText(Config.COMPANY_NAME + "的" + getResources().getString(R.string.timeline));

        iBtn_back.setImageResource(R.drawable.navigation);

        iBtn_publish.setVisibility(View.VISIBLE);
        iBtn_publish.setImageResource(R.drawable.publish);

        // 默认显示任务圈
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frag_main, frag_timeline).commit();

        // 设置抽屉
        initNavigationHeader();

        // 设置昵称
        tv_nickname = (TextView) ll_nvHeader.findViewById(R.id.tv_navigation_nickname);
        if (!"".equals(Config.NICKNAME) && !"null".equals(Config.NICKNAME)) {
            tv_nickname.setText(Config.NICKNAME);
        }

        // 获取抽屉的头像
        iv_navigation = (CircleImageView) ll_nvHeader.findViewById(R.id.iv_navigation);
        if (Config.PORTRAIT != null) {
            ImageLoader.getInstance().displayImage(Config.PORTRAIT + "?t=" + Config.TIME, iv_navigation);
        } else {
            iv_navigation.setImageResource(R.drawable.portrait_default);
        }

        // 设置NavigationView
        initNavigationView();
    }

    /**
     * 初始化NavigationHeader
     */
    @SuppressLint("InflateParams")
    private void initNavigationHeader() {
        ll_nvHeader = (LinearLayout) LayoutInflater.from(this)
                .inflate(R.layout.navigation_header, null);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.height = UtilBox.dip2px(this, 212);
        ll_nvHeader.setLayoutParams(lp);
        ll_nvHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dw.closeDrawer(GravityCompat.START);

                if (currentItem == 2) {
                    return;
                }

                nv.getMenu().getItem(2).setChecked(true);
                tv_title.setText(R.string.userInfo);
                nv.getMenu().getItem(currentItem).setChecked(false);

                switchContent(frag_userInfo);

                tv_title.setOnClickListener(null);

                currentItem = 2;

                iBtn_publish.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 初始化NavigationView
     */
    private void initNavigationView() {
        nv.addHeaderView(ll_nvHeader);
        nv.getMenu().getItem(0).setChecked(true);
        nv.setItemTextColor(ColorStateList.valueOf(Color.parseColor("#212121")));
        nv.setItemIconTintList(null);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                dw.closeDrawer(GravityCompat.START);
                switch (menuItem.getItemId()) {
                    case R.id.navItem_timeLine:
                        if (currentItem == 0) {
                            break;
                        }
                        nv.getMenu().getItem(0).setChecked(true);
                        tv_title.setText(Config.COMPANY_NAME + "的" + getResources().getString(R.string.timeline));
                        nv.getMenu().getItem(currentItem).setChecked(false);

                        switchContent(frag_timeline);

                        tv_title.setOnClickListener(MainActivity.this);

                        currentItem = 0;

                        iBtn_publish.setVisibility(View.VISIBLE);
                        break;

                    case R.id.navItem_member:
                        if (currentItem == 1) {
                            break;
                        }
                        nv.getMenu().getItem(1).setChecked(true);
                        tv_title.setText(R.string.member);
                        nv.getMenu().getItem(currentItem).setChecked(false);

                        switchContent(_memberFragment);

                        tv_title.setOnClickListener(null);

                        currentItem = 1;

                        iBtn_publish.setVisibility(View.GONE);
                        break;

                    case R.id.navItem_userInfo:
                        if (currentItem == 2) {
                            break;
                        }
                        nv.getMenu().getItem(2).setChecked(true);
                        tv_title.setText(R.string.userInfo);
                        nv.getMenu().getItem(currentItem).setChecked(false);

                        switchContent(frag_userInfo);

                        tv_title.setOnClickListener(null);

                        currentItem = 2;

                        iBtn_publish.setVisibility(View.GONE);
                        break;

                    case R.id.navItem_preference:
                        if (currentItem == 3) {
                            break;
                        }
                        nv.getMenu().getItem(3).setChecked(true);
                        tv_title.setText(R.string.preference);
                        nv.getMenu().getItem(currentItem).setChecked(false);

                        switchContent(frag_preference);

                        tv_title.setOnClickListener(null);

                        currentItem = 3;

                        iBtn_publish.setVisibility(View.GONE);
                        break;

                    case R.id.navItem_chooseCompany:
                        Intent intent = new Intent(MainActivity.this, CompanyActivity.class);
                        intent.putExtra("hide", true);
                        startActivityForResult(intent, Constants.CODE_CHANGE_COMPANY);
                        overridePendingTransition(R.anim.in_right_left, R.anim.scale_stay);
                        break;
                }

                return true;
            }
        });
    }

    /**
     * 切换fragment
     *
     * @param to 需要切换到的fragment
     */
    private void switchContent(Fragment to) {
        Fragment from = null;
        switch (currentItem) {
            case 0:
                from = frag_timeline;
                break;

            case 1:
                from = _memberFragment;
                break;

            case 2:
                from = frag_userInfo;
                break;

            case 3:
                from = frag_preference;
                break;
        }

        @SuppressLint("CommitTransaction")
        FragmentTransaction transaction = fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.zoom_in, R.anim.zoom_out);

        // 先判断是否被add过
        if (!to.isAdded()) {
            // 隐藏当前的fragment，add下一个到Activity中
            transaction.hide(from).add(R.id.frag_main, to).commit();
        } else {
            transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
        }
    }


    @OnClick({R.id.iBtn_back, R.id.tv_title})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iBtn_back:
                dw.openDrawer(GravityCompat.START);
                break;

            case R.id.tv_title:
                if ((System.currentTimeMillis() - doubleClickTime) > 500) {
                    doubleClickTime = System.currentTimeMillis();
                } else {
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            if (TimelineAdapter.taskList != null
                                    && TimelineAdapter.taskList.size() > Constants.LOAD_NUM){
                                TimelineFragment.lv.setSelection(Constants.LOAD_NUM - 1);
                            }

                            TimelineFragment.lv.smoothScrollToPosition(0);
                        }
                    });
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case Constants.CODE_CHANGE_COMPANY:
                nv.getMenu().getItem(currentItem).setChecked(true);
                if (resultCode == RESULT_OK) {
                    recreate();
                }
                break;
        }
    }

    /**
     * 点击返回，回到桌面
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (dw.isDrawerOpen(GravityCompat.START)) {
                dw.closeDrawer(GravityCompat.START);
            } else if (currentItem == 0 && TimelineFragment.commentWindowIsShow) {
                UtilBox.setViewParams(TimelineFragment.space, 1, 0);
                TimelineFragment.ll_comment.setVisibility(View.GONE);
                TimelineFragment.commentWindowIsShow = false;
                UtilBox.toggleSoftInput(TimelineFragment.ll_comment, false);
            } else if ((currentItem == 0 && TimelineFragment.auditWindowIsShow)) {
                TimelineFragment.ll_audit.setVisibility(View.GONE);
                TimelineFragment.auditWindowIsShow = false;
            } else if (currentItem != 0) {
                nv.getMenu().getItem(0).setChecked(true);
                tv_title.setText(Config.COMPANY_NAME + "的" + getResources().getString(R.string.timeline));
                nv.getMenu().getItem(currentItem).setChecked(false);

                switchContent(frag_timeline);

                tv_title.setOnClickListener(MainActivity.this);

                currentItem = 0;

                iBtn_publish.setVisibility(View.VISIBLE);
            } else {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
            }

            return true;
        } else if (keyCode == KeyEvent.KEYCODE_MENU) {
            if (!dw.isDrawerOpen(GravityCompat.START)) {
                dw.openDrawer(GravityCompat.START);
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStart() {
        nicknameReceiver = new UpdateViewReceiver(this,
                new UpdateViewReceiver.UpdateCallBack() {
                    @Override
                    public void updateView(String msg, Object... objects) {
                        tv_nickname.setText(msg);
                        nv.removeHeaderView(ll_nvHeader);
                        nv.addHeaderView(ll_nvHeader);
                    }
                });
        nicknameReceiver.registerAction(Constants.ACTION_CHANGE_NICKNAME);

        portraitReceiver = new UpdateViewReceiver(this,
                new UpdateViewReceiver.UpdateCallBack() {
                    @Override
                    public void updateView(String msg, Object... objects) {
                        ImageLoader.getInstance().displayImage(
                                Config.PORTRAIT + "?t=" + Config.TIME, iv_navigation);
                        nv.removeHeaderView(ll_nvHeader);
                        nv.addHeaderView(ll_nvHeader);
                    }
                });
        portraitReceiver.registerAction(Constants.ACTION_CHANGE_PORTRAIT);

        super.onStart();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(nicknameReceiver);
        unregisterReceiver(portraitReceiver);

        super.onDestroy();
    }
}