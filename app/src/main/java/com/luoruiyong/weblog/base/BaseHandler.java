package com.luoruiyong.weblog.base;

import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

/**消息处理器
 * Created by Administrator on 2017/9/8.
 */

public class BaseHandler extends Handler{

    private final static String CLASS_NAME = BaseHandler.class.getSimpleName() + "-->";
    private BaseUi ui;

    public BaseHandler(BaseUi ui) {
        this.ui = ui;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what){
            case C.handler.taskStart:

                break;
            case C.handler.taskPause:

                break;
            case C.handler.taskStop:

                break;
            case C.handler.taskComplete:
                Toast.makeText(ui,msg.getData().toString(),Toast.LENGTH_SHORT).show();
                break;
            case C.handler.taskError:

                break;

        }
    }
}
