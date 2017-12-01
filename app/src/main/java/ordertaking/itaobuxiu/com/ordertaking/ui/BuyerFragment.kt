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
    }

    companion object {
        var listener: ((ing:String?, get:String?, end:String?, status: Int) -> Unit)? = null

        fun addListener(listener: (ing:String?, get:String?, end:String?, status: Int) -> Unit) {
            this.listener = listener
        }

        fun notify(ing:String?, get:String?, end:String?, status: Int) {
            this.listener?.invoke(ing, get, end, status)
        }
    }
}