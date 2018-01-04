package ordertaking.itaobuxiu.com.ordertaking.ui

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.activity_splash.*
import ordertaking.itaobuxiu.com.ordertaking.BaseActivity
import ordertaking.itaobuxiu.com.ordertaking.R
import org.jetbrains.anko.dip

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

//        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
//        banner.setImageLoader(GlideImageLoader)

//        //资源文件
        val images = arrayOf<Int>(R.drawable.walkthough_1, R.drawable.walkthough_2, R.drawable.walkthough_3, R.drawable.walkthough_4)
//        banner.setImages(images.asList())
//        banner.isAutoPlay(false)
//        banner.start()

        viewPager.adapter = object: PagerAdapter() {

            override fun instantiateItem(container: ViewGroup?, position: Int): Any {
                var view = LayoutInflater.from(this@SplashActivity).inflate(R.layout.banner_item, null)
                var imageView:ImageView = view.findViewById(R.id.image) as ImageView
                imageView.setImageResource(images[position])
                container?.addView(view)

                if (position == 3 ) {
                    view.setOnClickListener {
                        saveHasShowSplash()
                    }
                }
                return view
            }

            override fun destroyItem(container: View?, position: Int, `object`: Any?) {
                (container as ViewGroup).removeView( `object` as View)
            }

            override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
                container?.removeView( `object` as View)
            }

            override fun isViewFromObject(view: View?, `object`: Any?): Boolean {
                return view == `object`
            }

            override fun getCount(): Int {
                return 4
            }
        }

        for(i in 0..3) {
            var view: View = View(this)
            view.setBackgroundResource(R.drawable.iv_bg)
            var lp = LinearLayout.LayoutParams(dip(5), dip(5))
            lp.leftMargin = dip(5)
            indicator.addView(view, lp)
        }
        indicator.getChildAt(0).isEnabled = false
        viewPager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener {

            var lastIndex = 0

            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                indicator.getChildAt(position).isEnabled = false
                indicator.getChildAt(lastIndex).isEnabled = true
                lastIndex = position
            }

        })

        skip.setOnClickListener {
            saveHasShowSplash()
        }


//        Network.create(HomeApiService::class.java)
//                ?.getHomeAdd()
//                ?.subscribeOn(Schedulers.io())
//                ?.observeOn(AndroidSchedulers.mainThread())
//                ?.subscribe (
//                        { result ->
//                            var imageList = result.data.data.adList.map {
//                                it.url
//                            }
//                            banner.update(imageList)
//                        },
//                        { error -> Log.e("Network", "failed:" + error.message) }
//                )
    }

    fun saveHasShowSplash() {
        finish()
        gotoMainActivity()
        Hawk.put("hasShowSplash_" + this.packageManager.getPackageInfo(
                this.getPackageName(), 0).versionName, "has")
    }

}
