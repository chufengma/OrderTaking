package ordertaking.itaobuxiu.com.ordertaking

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*;
import ordertaking.itaobuxiu.com.ordertaking.ui.BuyerFragment
import ordertaking.itaobuxiu.com.ordertaking.ui.HomeFragment
import ordertaking.itaobuxiu.com.ordertaking.ui.MeFragment
import ordertaking.itaobuxiu.com.ordertaking.ui.SellerFragment

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

        buyerTab?.redDot?.text = "11"
        buyerTab?.redDot?.visibility = View.VISIBLE
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

}
