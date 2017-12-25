package ordertaking.itaobuxiu.com.ordertaking.ui

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.fragment_seller.*
import ordertaking.itaobuxiu.com.ordertaking.R
import ordertaking.itaobuxiu.com.ordertaking.apis.IronBuyInfo
import ordertaking.itaobuxiu.com.ordertaking.apis.getStatusBarHeight
import ordertaking.itaobuxiu.com.ordertaking.apis.gotoPostRequest
import org.jetbrains.anko.dip

/**
 * Created by chufengma on 2017/11/19.
 */
class SellerFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_seller, null)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

//        titleLayout.setPadding(0, getStatusBarHeight(context), 0, 0)

        header.layoutParams.height = getStatusBarHeight(context)

        mainViewPagerSeller.adapter = object : FragmentPagerAdapter(fragmentManager) {

            override fun getItem(index: Int): Fragment {
                return when(index) {
                    0 -> SellerTodayFragment()
                    else -> SellerAllFragment()
                }
            }

            override fun getCount(): Int {
                return 2
            }
        }

        mainViewPagerSeller.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                Log.e("mainViewPager", "$positionOffset  $position")
                if (position != 0) {
                    return
                }
                (tabBgViewSeller.layoutParams as (RelativeLayout.LayoutParams)).leftMargin = context.dip(90 * positionOffset)
                tabBgViewSeller.requestLayout()
            }

            override fun onPageSelected(position: Int) {
                if (position == 0) {
                    tabOneSeller.setTextColor(Color.parseColor("#ffffff"))
                    tabTwoSeller.setTextColor(Color.parseColor("#999999"))
                } else {
                    tabTwoSeller.setTextColor(Color.parseColor("#ffffff"))
                    tabOneSeller.setTextColor(Color.parseColor("#999999"))
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })

        tabOneSeller.setOnClickListener {
            mainViewPagerSeller.setCurrentItem(0, true)
        }

        tabTwoSeller.setOnClickListener {
            mainViewPagerSeller.setCurrentItem(1, true)
        }

        rightSeller.setOnClickListener {
            gotoPostRequest(context)
        }
    }

    companion object {
        var listeners: MutableList<((ing:String?, get:String?, end:String?, miss:String?, status: Int) -> Unit)?> = mutableListOf()
        var refreshListeners: MutableList<((ironInfo: IronBuyInfo?) -> Unit)?> = mutableListOf()

        fun addListener(listener: (ing:String?, get:String?, end:String?, miss:String?, status: Int) -> Unit) {
            this.listeners.add(listener)
        }

        fun notify(ing:String?, get:String?, end:String?, miss:String?, status: Int) {
            this.listeners.forEach {
                it?.invoke(ing, get, end,miss, status)
            }
        }

        fun addRefreshListener(listener: ((ironInfo: IronBuyInfo?) -> Unit)?) {
            this.refreshListeners.add(listener)
        }

        fun notifyRefrsh(ironInfo: IronBuyInfo?) {
            this.refreshListeners.forEach {
                it?.invoke(ironInfo)
            }
        }
    }
}