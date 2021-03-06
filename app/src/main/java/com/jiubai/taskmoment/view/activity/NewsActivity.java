package com.jiubai.taskmoment.view.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jiubai.taskmoment.R;
import com.jiubai.taskmoment.adapter.NewsAdapter;
import com.jiubai.taskmoment.bean.News;
import com.jiubai.taskmoment.config.Config;
import com.jiubai.taskmoment.widget.SlidingLayout;
import com.jiubai.taskmoment.common.UtilBox;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 新消息页面
 */
public class NewsActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.tv_title)
    TextView tv_title;

    @Bind(R.id.lv_news)
    ListView lv;

    private ArrayList<News> newsList;
    private View footerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.aty_news);

        new SlidingLayout(this);

        UtilBox.setStatusBarTint(this, R.color.statusBar);

        ButterKnife.bind(this);

        newsList = (ArrayList<News>) getIntent().getSerializableExtra("newsList");

        initView();
    }

    /**
     * 初始化所有view
     */
    private void initView() {
        tv_title.setText("消息");

        footerView = LayoutInflater.from(this).inflate(R.layout.load_more_news, null);
        footerView.setOnClickListener(this);

        lv.addFooterView(footerView);

        NewsAdapter adapter = new NewsAdapter(this, newsList);
        lv.setAdapter(adapter);
    }

    /**
     * 加载更早的消息
     */
    private void loadMoreNews(){
        if (!Config.IS_CONNECTED) {
            Toast.makeText(this,
                    R.string.cant_access_network,
                    Toast.LENGTH_SHORT).show();
            return;
        }

        String request_time;
        if(newsList != null && !newsList.isEmpty()) {
            request_time = newsList.get(newsList.size() - 1).getTime();
        } else {
            request_time = Calendar.getInstance(Locale.CHINA).getTimeInMillis() / 1000 + "";
        }

        String[] key = {"len", "mid", "create_time"};
        String[] value = {"2", Config.MID, request_time.substring(0, 10) + ""};

//        VolleyUtil.requestWithCookie(Urls.LOAD_MORE_NEWS, key, value,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//
//                    }
//                });
    }

    @OnClick({R.id.iBtn_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iBtn_back:
                finish();
                overridePendingTransition(R.anim.scale_stay, R.anim.out_left_right);
                break;

            case R.id.rl_load_more_news:
                loadMoreNews();
                Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}