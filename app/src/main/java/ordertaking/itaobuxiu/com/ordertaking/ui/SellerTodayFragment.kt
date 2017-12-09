package ordertaking.itaobuxiu.com.ordertaking.ui

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ordertaking.itaobuxiu.com.ordertaking.R
import org.jetbrains.anko.find

/**
 * A simple [Fragment] subclass.
 */
class SellerTodayFragment : Fragment() {

    var neverFragment: RecyclerViewSellerFragment = RecyclerViewSellerFragment(0, 1)
    var offerFragment: RecyclerViewSellerFragment = RecyclerViewSellerFragment(1, 1)
    var doneFragment: RecyclerViewSellerFragment = RecyclerViewSellerFragment(2, 1)
    var missFragment: RecyclerViewSellerFragment = RecyclerViewSellerFragment(3, 1)

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view = inflater!!.inflate(R.layout.fragment_seller_today, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        var contentViewPagerSellerToday = view?.find<ViewPager>(R.id.contentViewPagerSellerToday)
        var statusTabLayoutSellerToday = view?.find<TabLayout>(R.id.statusTabLayoutSellerToday)

        contentViewPagerSellerToday?.offscreenPageLimit = 4
        contentViewPagerSellerToday?.adapter = object : FragmentPagerAdapter(fragmentManager) {

            override fun getItem(index: Int): Fragment {
                return when(index) {
                    0 -> neverFragment
                    1 -> offerFragment
                    2 -> doneFragment
                    else -> missFragment
                }
            }

            override fun getCount(): Int {
                return 4
            }

        }
        statusTabLayoutSellerToday?.setupWithViewPager(contentViewPagerSellerToday)
        statusTabLayoutSellerToday?.getTabAt(0)?.setText("待报价")
        statusTabLayoutSellerToday?.getTabAt(1)?.setText("已报价")
        statusTabLayoutSellerToday?.getTabAt(2)?.setText("已成交")
        statusTabLayoutSellerToday?.getTabAt(3)?.setText("已失效")

        SellerFragment.addListener { never, offer, deal, miss, status ->
            if (status == 1) {
                if (!never.isNullOrBlank()) {
                    statusTabLayoutSellerToday?.getTabAt(0)?.setText("待报价($never)")
                } else {
                    statusTabLayoutSellerToday?.getTabAt(0)?.setText("待报价")
                }
                if (!offer.isNullOrBlank()) {
                    statusTabLayoutSellerToday?.getTabAt(1)?.setText("已报价($offer)")
                } else {
                    statusTabLayoutSellerToday?.getTabAt(1)?.setText("已报价")
                }
                if (!deal.isNullOrBlank()) {
                    statusTabLayoutSellerToday?.getTabAt(2)?.setText("已成交($deal)")
                } else {
                    statusTabLayoutSellerToday?.getTabAt(2)?.setText("已成交")
                }
                if (!miss.isNullOrBlank()) {
                    statusTabLayoutSellerToday?.getTabAt(3)?.setText("已失效($miss)")
                } else {
                    statusTabLayoutSellerToday?.getTabAt(3)?.setText("已失效")
                }
            }
        }
    }


}