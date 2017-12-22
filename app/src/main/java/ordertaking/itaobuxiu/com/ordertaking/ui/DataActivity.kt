package ordertaking.itaobuxiu.com.ordertaking.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_data.*
import ordertaking.itaobuxiu.com.ordertaking.BaseActivity
import ordertaking.itaobuxiu.com.ordertaking.R

class DataActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data)
        useNormalBack()

        viewPager.adapter = object : FragmentPagerAdapter(supportFragmentManager) {

            override fun getItem(index: Int): Fragment {
                return when(index) {
                    0 -> DataBuyerFragment()
                    else -> DataSellerFragment()
                }
            }

            override fun getCount(): Int {
                return 2
            }

            override fun isViewFromObject(view: View, obj: Any): Boolean {
                return view === (obj as Fragment).view
            }

            override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
                val fragment = `object` as Fragment
            }

            override fun getPageTitle(position: Int): CharSequence {
                if (position == 0) {
                    return "买家"
                } else {
                    return "卖家"
                }
            }

        }


        tab.setupWithViewPager(viewPager)
    }

    class DataBuyerFragment: Fragment() {

        override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            var view:View? = inflater?.inflate(R.layout.fragment_data_buyer, null)
            return view
        }

    }

    class DataSellerFragment: Fragment() {

        override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            var view:View? = inflater?.inflate(R.layout.fragment_data_buyer, null)
            return view
        }

    }

}
