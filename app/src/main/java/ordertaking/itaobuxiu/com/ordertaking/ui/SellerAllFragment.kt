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
class SellerAllFragment : Fragment() {

    var neverFragment: RecyclerViewSellerFragment = RecyclerViewSellerFragment()
    var offerFragment: RecyclerViewSellerFragment = RecyclerViewSellerFragment()
    var doneFragment: RecyclerViewSellerFragment = RecyclerViewSellerFragment()
    var missFragment: RecyclerViewSellerFragment = RecyclerViewSellerFragment()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view = inflater!!.inflate(R.layout.fragment_seller_all, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        var bundle = Bundle()
        bundle.putInt("buyStatus", 0)
        bundle.putInt("today", 0)
        neverFragment.arguments = bundle

        var bundle2 = Bundle()
        bundle2.putInt("buyStatus", 1)
        bundle2.putInt("today", 0)
        offerFragment.arguments = bundle2

        var bundle3 = Bundle()
        bundle3.putInt("buyStatus", 2)
        bundle3.putInt("today", 0)
        doneFragment.arguments = bundle3

        var bundle4 = Bundle()
        bundle4.putInt("buyStatus", 3)
        bundle4.putInt("today", 0)
        missFragment.arguments = bundle4

        var contentViewPagerSellerToday = view?.find<ViewPager>(R.id.contentViewPagerSellerAll)
        var statusTabLayoutSellerToday = view?.find<TabLayout>(R.id.statusTabLayoutSellerAll)

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
            if (status == 0) {
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