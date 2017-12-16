package ordertaking.itaobuxiu.com.ordertaking

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.orhanobut.hawk.Hawk
import com.sdsmdg.tastytoast.TastyToast
import kotlinx.android.synthetic.main.activity_main.*;
import ordertaking.itaobuxiu.com.ordertaking.apis.LOGIN_USER
import ordertaking.itaobuxiu.com.ordertaking.apis.UserBean
import ordertaking.itaobuxiu.com.ordertaking.apis.isLogin
import ordertaking.itaobuxiu.com.ordertaking.ui.*
import android.view.ViewGroup



class MainActivity : BaseActivity() {

    var homeTab: TabExt? = null
    var buyerTab: TabExt? = null
    var sellerTab: TabExt? = null
    var meTab: TabExt? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initMainLayout()
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
                    if (!isLogin()) {
//                        TastyToast.makeText(this@MainActivity, "暂未登陆", TastyToast.LENGTH_SHORT, TastyToast.ERROR)
                        viewPager.currentItem = 0
                        showLoginDialog()
                    } else {
//                        TastyToast.makeText(this@MainActivity, Hawk.get<UserBean>(LOGIN_USER).realName, TastyToast.LENGTH_SHORT, TastyToast.ERROR)
                    }
                }
            }

        })
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
    }
}
