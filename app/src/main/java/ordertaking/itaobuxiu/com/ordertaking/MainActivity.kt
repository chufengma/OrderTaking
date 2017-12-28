package ordertaking.itaobuxiu.com.ordertaking

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import com.orhanobut.hawk.Hawk
import com.sdsmdg.tastytoast.TastyToast
import kotlinx.android.synthetic.main.activity_main.*;
import ordertaking.itaobuxiu.com.ordertaking.apis.LOGIN_USER
import ordertaking.itaobuxiu.com.ordertaking.apis.UserBean
import ordertaking.itaobuxiu.com.ordertaking.apis.isLogin
import ordertaking.itaobuxiu.com.ordertaking.ui.*


class MainActivity : BaseActivity() {

    var homeTab: TabExt? = null
    var buyerTab: TabExt? = null
    var sellerTab: TabExt? = null
    var meTab: TabExt? = null
    var currentTabIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS or WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        }

        setContentView(R.layout.activity_main)
        initMainLayout()

        viewPager.postDelayed({
            dealWithPushIntent(intent)
        }, 300)
    }


    fun initMainLayout() {
        viewPager.adapter = object : FragmentPagerAdapter(supportFragmentManager) {

            override fun getItem(index: Int): Fragment {
                return when(index) {
                    0 -> HomeFragment()
                    1 -> BuyerFragment()
                    2 -> SellerFragment()
                    else -> MeFragment()
                }
            }

            override fun getCount(): Int {
                return 4
            }

            override fun isViewFromObject(view: View, obj: Any): Boolean {
                return view === (obj as Fragment).view
            }

            override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
                val fragment = `object` as Fragment
            }

        }

        tabLayout.setupWithViewPager(viewPager)

        homeTab = TabExt()
        buyerTab = TabExt()
        sellerTab = TabExt()
        meTab = TabExt()

        tabLayout.getTabAt(0)?.customView = homeTab?.view
        tabLayout.getTabAt(1)?.customView = buyerTab?.view
        tabLayout.getTabAt(2)?.customView = sellerTab?.view
        tabLayout.getTabAt(3)?.customView = meTab?.view

        homeTab?.text?.text = "首页"
        buyerTab?.text?.text = "买家中心"
        sellerTab?.text?.text = "卖家中心"
        meTab?.text?.text = "我的"

        homeTab?.icon?.setImageResource(R.drawable.tab_layout_button_home)
        buyerTab?.icon?.setImageResource(R.drawable.tab_layout_button_buyer)
        sellerTab?.icon?.setImageResource(R.drawable.tab_layout_button_seller)
        meTab?.icon?.setImageResource(R.drawable.tab_layout_button_me)

        viewPager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                if (position != 0) {
                    if (!isLogin() && position != 3) {
//                        TastyToast.makeText(this@MainActivity, "暂未登陆", TastyToast.LENGTH_SHORT, TastyToast.ERROR)
                        viewPager.currentItem = if (currentTabIndex == 3) 3 else 0
                        showLoginDialog()
                    } else {
//                        TastyToast.makeText(this@MainActivity, Hawk.get<UserBean>(LOGIN_USER).realName, TastyToast.LENGTH_SHORT, TastyToast.ERROR)
                    }
                }
                currentTabIndex = viewPager.currentItem
            }

        })
    }

    fun gotoPage(index: Int) {
        viewPager.setCurrentItem(index, true)
    }

    inner class TabExt {
        val view: View = LayoutInflater.from(this@MainActivity).inflate(R.layout.tab_item_layout, null)
        val text: TextView = view?.findViewById(R.id.text) as TextView
        val icon: ImageView = view?.findViewById(R.id.icon) as ImageView
        val redDot: TextView = view?.findViewById(R.id.red_dot) as TextView
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.e("activity", "activity onTouchEvent:" + event?.action);
        return super.onTouchEvent(event)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        viewPager.currentItem = 0
        if (intent?.getBooleanExtra("loginOut", false)!!) {
            finish()
            var newIntent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(newIntent)
        }
        dealWithPushIntent(intent)
        dealWithInner(intent)
    }

    fun dealWithPushIntent(intent: Intent?) {
        var code: String? = intent?.getStringExtra("pushCode")
        if (!code.isNullOrBlank()) {
            if (code == "1" || code == "3") {
                viewPager.currentItem = 2
            } else if (code == "2" || code == "5") {
                viewPager.currentItem = 1
            }
        }
    }

    fun dealWithInner(intent: Intent?) {
        var code: String? = intent?.getStringExtra("innerCode")
        if (!code.isNullOrBlank()) {
            viewPager.currentItem = code?.toInt()!!
        }
    }
}
