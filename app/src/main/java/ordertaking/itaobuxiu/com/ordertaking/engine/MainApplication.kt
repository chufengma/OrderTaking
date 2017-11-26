package ordertaking.itaobuxiu.com.ordertaking.engine

import android.app.Application
import com.orhanobut.hawk.Hawk

/**
 * Created by dev on 2017/11/25.
 */
class MainApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        Hawk.init(this).build()
        instance = this
    }

    companion object {

        var instance: MainApplication? = null

        fun instance(): MainApplication? {
            return instance
        }
    }

}