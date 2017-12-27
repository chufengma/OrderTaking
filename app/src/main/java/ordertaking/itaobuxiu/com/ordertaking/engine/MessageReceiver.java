package ordertaking.itaobuxiu.com.ordertaking.engine;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;
import ordertaking.itaobuxiu.com.ordertaking.MainActivity;

/**
 * Created by dev on 2017/12/27.
 */

public class MessageReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equalsIgnoreCase(JPushInterface.ACTION_NOTIFICATION_OPENED)) {
            Bundle bundle = intent.getExtras();
            String title = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
            Log.e("MessageReceiver", "asdfasdfasdfasdf:" + title + "," + extras);
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
        }
    }
}
