package com.jiubai.taskmoment.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.jiubai.taskmoment.R;
import com.jiubai.taskmoment.common.UtilBox;
import com.jiubai.taskmoment.config.Constants;
import com.jiubai.taskmoment.view.activity.CheckPictureActivity;

import java.util.ArrayList;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * 发布任务时的选择图片
 */
public class PublishPictureAdapter extends BaseAdapter {
    public ArrayList<String> pictureList;
    private Context context;
    public int actualCount = 0;

    public PublishPictureAdapter(Context context, ArrayList<String> pictureList) {
        this.context = context;
        this.pictureList = pictureList;
        this.pictureList.clear();
        actualCount = this.pictureList.size();

        if (this.pictureList.size() < 9) {
            this.pictureList.add("drawable://" + R.drawable.add_picture);
        }
    }

    /**
     * 插入新图片
     *
     * @param pictureList 新图片List
     */
    public void insertPicture(ArrayList<String> pictureList) {
        this.pictureList = pictureList;

        actualCount = this.pictureList.size();

        // 保留添加入口
        if (this.pictureList.size() < 9) {
            this.pictureList.add("drawable://" + R.drawable.add_picture);
        }
    }

    @Override
    public int getCount() {
        return pictureList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_picture_small, null);

            holder = new ViewHolder(convertView);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // 最后一项点击可以添加照片
        if (actualCount < 9 && position == getCount() - 1) {
            holder.iv_picture.setImageResource(R.drawable.add_picture);
            holder.iv_picture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MultiImageSelectorActivity.class);

                    // 是否显示调用相机拍照
                    intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);

                    // 最大图片选择数量
                    intent.putExtra(
                            MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 9 - getCount() + 1);

                    // 设置模式 (支持 单选/MultiImageSelectorActivity.MODE_SINGLE 或者
                    // 多选/MultiImageSelectorActivity.MODE_MULTI)
                    intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE,
                            MultiImageSelectorActivity.MODE_MULTI);

                    if(pictureList!=null && !pictureList.isEmpty()){
                        ArrayList<String> list = new ArrayList<>(pictureList);
                        list.remove(list.size() - 1);

                        intent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, list);
                    }

                    ((Activity) context).startActivityForResult(
                            intent, Constants.CODE_MULTIPLE_PICTURE);
                    ((Activity) context).overridePendingTransition(
                            R.anim.in_right_left, R.anim.scale_stay);
                }
            });
        } else {
            holder.iv_picture.setImageBitmap(UtilBox.getLocalBitmap(pictureList.get(position),
                    UtilBox.dip2px(context, 60), UtilBox.dip2px(context, 60)));
            holder.iv_picture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, CheckPictureActivity.class);

                    ArrayList<String> list = new ArrayList<>(pictureList);
                    list.remove(list.size() - 1);

                    intent.putStringArrayListExtra("pictureList", list);
                    intent.putExtra("index", position);
                    intent.putExtra("fromWhere", "local");

                    ((Activity) context).startActivityForResult(
                            intent, Constants.CODE_CHECK_PICTURE);
                    ((Activity) context).overridePendingTransition(
                            R.anim.zoom_in_quick, R.anim.scale_stay);
                }
            });
        }

        return convertView;
    }

    private class ViewHolder {
        ImageView iv_picture;

        public ViewHolder(View view) {
            iv_picture = (ImageView) view.findViewById(R.id.iv_picture);
        }
    }
}