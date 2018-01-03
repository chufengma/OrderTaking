package ordertaking.itaobuxiu.com.ordertaking.ui


import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_buyer_history.*

import ordertaking.itaobuxiu.com.ordertaking.R
import org.jetbrains.anko.find


/**
 * A simple [Fragment] subclass.
 */
class BuyerTodayFragment : Fragment() {

    var doingFragment: RecyclerViewFragment = RecyclerViewFragment()
    var doneFragment: RecyclerViewFragment = RecyclerViewFragment()
    var outFragment: RecyclerViewFragment = RecyclerViewFragment()

    var todayViewPager :View? = null
    var todayTabLayout :View?  = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view = inflater!!.inflate(R.layout.fragment_buyer_today, container, false)

        todayViewPager = view?.find<ViewPager>(R.id.contentViewPager)
        todayTabLayout = view?.find<TabLayout>(R.id.statusTabLayout)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        var bundle = Bundle()
        bundle.putInt("buyStatus", 1)
        bundle.putInt("today", 1)
        doingFragment.arguments = bundle

        var bundle2 = Bundle()
        bundle2.putInt("buyStatus", 2)
        bundle2.putInt("today", 1)
        doneFragment.arguments = bundle2

        var bundle3 = Bundle()
        bundle3.putInt("buyStatus", 3)
        bundle3.putInt("today", 1)
        outFragment.arguments = bundle3

        var todayViewPager = view?.find<ViewPager>(R.id.contentViewPager)
        var todayTabLayout = view?.find<TabLayout>(R.id.statusTabLayout)

        todayViewPager?.offscreenPageLimit = 3
        todayViewPager?.adapter = object : FragmentPagerAdapter(fragmentManager) {

            override fun getItem(index: Int): Fragment {
                return when(index) {
                    0 -> doingFragment
                    1 -> doneFragment
                    else -> outFragment
                }
            }

            override fun getCount(): Int {
                return 3
            }

        }
        todayTabLayout?.setupWithViewPager(todayViewPager)
        todayTabLayout?.getTabAt(0)?.setText("进行中")
        todayTabLayout?.getTabAt(1)?.setText("已完成")
        todayTabLayout?.getTabAt(2)?.setText("已失效")

        BuyerFragment.addListener { ing, get, end, status ->
            if (status == 1) {
                if (!ing.isNullOrBlank()) {
                    todayTabLayout?.getTabAt(0)?.setText("进行中($ing)")
                } else {
                    todayTabLayout?.getTabAt(0)?.setText("进行中")
                }
                if (!get.isNullOrBlank()) {
                    todayTabLayout?.getTabAt(1)?.setText("已完成($get)")
                } else {
                    todayTabLayout?.getTabAt(1)?.setText("已完成")
                }
                if (!end.isNullOrBlank()) {
                    todayTabLayout?.getTabAt(2)?.setText("已失效($end)")
                } else {
                    todayTabLayout?.getTabAt(2)?.setText("已失效")
                }
            }
        }
    }


}
