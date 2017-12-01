package ordertaking.itaobuxiu.com.ordertaking.engine

import android.app.Application
import com.orhanobut.hawk.Hawk
import ordertaking.itaobuxiu.com.ordertaking.BaseActivity

/**
 * Created by dev on 2017/11/25.
 */
class MainApplication: Application() {

    var activity: BaseActivity? = null;

    override fun onCreate() {
        super.onCreate()
        Hawk.init(this).build()
        instance = this
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