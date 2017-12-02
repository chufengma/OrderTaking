package ordertaking.itaobuxiu.com.ordertaking.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_buyer_history.*
import ordertaking.itaobuxiu.com.ordertaking.R

/**
 * A simple [Fragment] subclass.
 */
class BuyerHistoryFragment : Fragment() {

    var doingFragment: RecyclerViewFragment = RecyclerViewFragment(1, 0)
    var doneFragment: RecyclerViewFragment = RecyclerViewFragment(2, 0)
    var outFragment: RecyclerViewFragment = RecyclerViewFragment(3, 0)

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_buyer_history, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        contentViewPagerHistory.offscreenPageLimit = 3
        contentViewPagerHistory.adapter = object : FragmentPagerAdapter(fragmentManager) {

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

        statusTabLayoutHistory?.setupWithViewPager(contentViewPagerHistory)
        statusTabLayoutHistory?.getTabAt(0)?.setText("进行中")
        statusTabLayoutHistory?.getTabAt(1)?.setText("已完成")
        statusTabLayoutHistory?.getTabAt(2)?.setText("已失效")

        BuyerFragment.addListener { ing, get, end, status ->
            if (status == 0) {
                statusTabLayoutHistory?.getTabAt(0)?.setText("进行中($ing)")
                statusTabLayoutHistory?.getTabAt(1)?.setText("已完成($get)")
                statusTabLayoutHistory?.getTabAt(2)?.setText("已失效($end)")
            }
        }
    }

}