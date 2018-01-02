package ordertaking.itaobuxiu.com.ordertaking.ui

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import com.youth.banner.BannerConfig
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_home.*
import ordertaking.itaobuxiu.com.ordertaking.BaseActivity
import ordertaking.itaobuxiu.com.ordertaking.R
import ordertaking.itaobuxiu.com.ordertaking.apis.HomeApiService
import ordertaking.itaobuxiu.com.ordertaking.engine.GlideImageLoader
import ordertaking.itaobuxiu.com.ordertaking.engine.Network

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS or WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
            window.navigationBarColor = Color.TRANSPARENT
        }


        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
        banner.setImageLoader(GlideImageLoader)
        banner.setImages(mutableListOf("http://tbxoss.oss-cn-hangzhou.aliyuncs.com/2017/11/20/20171120111338025417.jpg"))
        banner.start()

        Network.create(HomeApiService::class.java)
                ?.getHomeAdd()
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe (
                        { result ->
                            var imageList = result.data.data.adList.map {
                                it.url
                            }
                            banner.update(imageList)
                        },
                        { error -> Log.e("Network", "failed:" + error.message) }
                )
    }
}
