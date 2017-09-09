package com.luoruiyong.weblog.base;

import android.os.Handler;
import android.os.Message;

/**消息处理器
 * Created by Administrator on 2017/9/8.
 */

public class BaseHandler extends Handler{
    private BaseUi ui;

    public BaseHandler(BaseUi ui) {
        this.ui = ui;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what){

        }
    }
}
