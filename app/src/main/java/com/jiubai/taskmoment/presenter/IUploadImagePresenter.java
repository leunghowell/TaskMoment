package com.jiubai.taskmoment.presenter;

import android.graphics.Bitmap;

/**
 * Created by howell on 2015/11/29.
 * UploadPresenter接口
 */
public interface IUploadImagePresenter {
    void doUploadImage(Bitmap bitmap, String dir, String objectName, String type);
}
