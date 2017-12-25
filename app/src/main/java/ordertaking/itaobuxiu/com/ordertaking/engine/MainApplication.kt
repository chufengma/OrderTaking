package ordertaking.itaobuxiu.com.ordertaking.engine

import android.app.Application
import cn.jpush.android.api.JPushInterface
import com.orhanobut.hawk.Hawk
import ordertaking.itaobuxiu.com.ordertaking.BaseActivity
import ordertaking.itaobuxiu.com.ordertaking.apis.LOGIN_USER
import ordertaking.itaobuxiu.com.ordertaking.apis.UserInfo
import ordertaking.itaobuxiu.com.ordertaking.apis.UserProfile

/**
 * Created by dev on 2017/11/25.
 */
class MainApplication: Application() {

    var activity: BaseActivity? = null;

    override fun onCreate() {
        super.onCreate()
        Hawk.init(this).build()
        instance = this

        JPushInterface.setDebugMode(false)
        JPushInterface.init(this)

        var user: UserInfo? = Hawk.get(LOGIN_USER)
        if (user != null) {
            JPushInterface.setAlias(this, 909090, user?.id)
        } else {
            JPushInterface.stopPush(this)
        }
    }


    fun getCurrentActivity(): BaseActivity? {
        return activity
    }

    fun enterActivity(activity: BaseActivity) {
        this.activity = activity
    }

    companion object {

        var instance: MainApplication? = null

        fun instance(): MainApplication? {
            return instance
        }
    }

}