package ordertaking.itaobuxiu.com.ordertaking.engine;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import cn.jpush.android.api.JPushInterface;
import ordertaking.itaobuxiu.com.ordertaking.MainActivity;
import ordertaking.itaobuxiu.com.ordertaking.R;

/**
 * Created by dev on 2017/12/27.
 */

public class MessageReceiver extends BroadcastReceiver {

    public static int count = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("MessageReceiver", "onReceive:");
        if (intent.getAction().equalsIgnoreCase(JPushInterface.ACTION_NOTIFICATION_OPENED)) {
            Bundle bundle = intent.getExtras();
            String title = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
            try {
                JSONObject data = new JSONObject(extras);
                String code = data.getString("code");

                Intent intent1 = new Intent(context, MainActivity.class);
                intent1.putExtra("pushCode", code);
                context.startActivity(intent1);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("MessageReceiver", "error:" + e.getMessage());
            }
        } else if (intent.getAction().equalsIgnoreCase(JPushInterface.ACTION_NOTIFICATION_RECEIVED)) {
        }
    }

}
