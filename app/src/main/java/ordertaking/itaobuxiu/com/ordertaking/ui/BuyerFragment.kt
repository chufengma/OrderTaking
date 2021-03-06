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
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.fragment_buyer.*
import ordertaking.itaobuxiu.com.ordertaking.R
import ordertaking.itaobuxiu.com.ordertaking.apis.IronBuyInfo
import ordertaking.itaobuxiu.com.ordertaking.apis.getStatusBarHeight
import ordertaking.itaobuxiu.com.ordertaking.apis.gotoPostRequest
import org.jetbrains.anko.dip

/**
 * Created by chufengma on 2017/11/19.
 */
class BuyerFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_buyer, null)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        header.layoutParams.height = getStatusBarHeight(context)

        mainViewPager.adapter = object : FragmentPagerAdapter(fragmentManager) {

            override fun getItem(index: Int): Fragment {
                return when(index) {
                    0 -> BuyerTodayFragment()
                    else -> BuyerHistoryFragment()
                }
            }

            override fun getCount(): Int {
                return 2
            }
        }

        mainViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                Log.e("mainViewPager", "$positionOffset  $position")
                if (position != 0) {
                    return
                }
                (tabBgView.layoutParams as (RelativeLayout.LayoutParams)).leftMargin = context.dip(90 * positionOffset)
                tabBgView.requestLayout()
            }

            override fun onPageSelected(position: Int) {
                if (position == 0) {
                    tabOne.setTextColor(Color.parseColor("#ffffff"))
                    tabTwo.setTextColor(Color.parseColor("#999999"))
                } else {
                    tabTwo.setTextColor(Color.parseColor("#ffffff"))
                    tabOne.setTextColor(Color.parseColor("#999999"))
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })

        tabOne.setOnClickListener {
            mainViewPager.setCurrentItem(0, true)
        }

        tabTwo.setOnClickListener {
            mainViewPager.setCurrentItem(1, true)
        }

        right.setOnClickListener {
            gotoPostRequest(context)
        }
    }

    companion object {
        var listeners: MutableList<((ing:String?, get:String?, end:String?, status: Int) -> Unit)?> = mutableListOf()
        var refreshListeners: MutableList<((ironInfo: IronBuyInfo?, position: Int) -> Unit)?> = mutableListOf()

        fun addListener(listener: (ing:String?, get:String?, end:String?, status: Int) -> Unit) {
            this.listeners.add(listener)
        }

        fun notify(ing:String?, get:String?, end:String?, status: Int) {
            this.listeners.forEach {
                it?.invoke(ing, get, end, status)
            }
        }

        fun addRefreshListener(listener: ((ironInfo: IronBuyInfo?, position: Int) -> Unit)?) {
            this.refreshListeners.add(listener)
        }

        fun notifyRefrsh(ironInfo: IronBuyInfo?, position: Int = -1) {
            this.refreshListeners.forEach {
                it?.invoke(ironInfo, position)
            }
        }
    }
}