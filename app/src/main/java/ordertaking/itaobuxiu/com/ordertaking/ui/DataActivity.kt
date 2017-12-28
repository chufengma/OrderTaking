package ordertaking.itaobuxiu.com.ordertaking.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_data.*
import kotlinx.android.synthetic.main.fragment_data_buyer.*
import kotlinx.android.synthetic.main.fragment_data_seller.*
import ordertaking.itaobuxiu.com.ordertaking.BaseActivity
import ordertaking.itaobuxiu.com.ordertaking.R
import ordertaking.itaobuxiu.com.ordertaking.apis.BuyerCompany
import ordertaking.itaobuxiu.com.ordertaking.apis.UserApiService
import ordertaking.itaobuxiu.com.ordertaking.apis.configCircle
import ordertaking.itaobuxiu.com.ordertaking.apis.networkWrap
import ordertaking.itaobuxiu.com.ordertaking.engine.Network
import org.jetbrains.anko.find

class DataActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data)
        useNormalBack()

        viewPager.adapter = object : FragmentPagerAdapter(supportFragmentManager) {

            override fun getItem(index: Int): Fragment {
                return when (index) {
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

    class DataBuyerFragment : Fragment() {

        override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            var view: View? = inflater?.inflate(R.layout.fragment_data_buyer, null)
            return view
        }

        override fun onActivityCreated(savedInstanceState: Bundle?) {
            super.onActivityCreated(savedInstanceState)

            configCircle(todayRate)
            configCircle(todayDoneRate)

            networkWrap(Network.create(UserApiService::class.java)?.getBuyerData(""))?.subscribe { result ->
                result.data.cooperation.forEachIndexed { index, buyerCompany ->
                    sellerLayout.addView(createSellerItem(buyerCompany, index))
                }

                todayBuyTotal.setText("总报价量：${result.data.todayBuyTotal}")
                todayMiss.setText("${result.data.todayBuyMiss}")
                todayValid.setText("${result.data.todayBuyValid}")

                todayRate.showValue(result.data.todayBuyRate.toFloat(), 100f, true)

                today.setOnClickListener {
                    todayDoneRate.showValue(result.data.todaySellRate.toFloat(), 100f, true)
                    todayDone.setText("${result.data.todaySellGet}")
                    todayDoneFailed.setText("${result.data.todaySellMiss}")
                    todayDoneTotal.setText("总求购数：${result.data.todaySellTotal}")
                    today.isSelected = true
                    month.isSelected = false
                    total.isSelected = false
                }

                month.setOnClickListener {
                    todayDoneRate.showValue(result.data.monthSellRate.toFloat(), 100f, true)
                    todayDone.setText("${result.data.monthSellGet}")
                    todayDoneFailed.setText("${result.data.monthSellMiss}")
                    todayDoneTotal.setText("总求购数：${result.data.monthSellTotal}")
                    today.isSelected = false
                    month.isSelected = true
                    total.isSelected = false
                }

                total.setOnClickListener {
                    todayDoneRate.showValue(result.data.allSellRate.toFloat(), 100f, true)
                    todayDone.setText("${result.data.allSellGet}")
                    todayDoneFailed.setText("${result.data.allSellMiss}")
                    todayDoneTotal.setText("总求购数：${result.data.allSellTotal}")
                    today.isSelected = false
                    month.isSelected = false
                    total.isSelected = true
                }

                today.performClick()
            }
        }

        fun createSellerItem(buyerCompany: BuyerCompany, i: Int): View {
            var view = LayoutInflater.from(context).inflate(R.layout.data_seller_tab_item, null)
            var index: TextView = view.find(R.id.index)
            var company: TextView = view.find(R.id.companyName)
            var num: TextView = view.find(R.id.num)
            company.setText(buyerCompany.companyName)
            num.setText(buyerCompany.coopLveve)
            index.setText("${i + 1}")
            when (i) {
                0 -> {
                    index.setTextColor(resources.getColor(R.color.main_red))
                    num.setBackgroundResource(R.drawable.data_seller_1)
                }
                1 -> {
                    index.setTextColor(resources.getColor(R.color.main_yellow))
                    num.setBackgroundResource(R.drawable.data_seller_2)
                }
                2 -> {
                    index.setTextColor(resources.getColor(R.color.main_green))
                    num.setBackgroundResource(R.drawable.data_seller_3)
                }
            }
            return view
        }


    }

    class DataSellerFragment : Fragment() {

        override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            var view: View? = inflater?.inflate(R.layout.fragment_data_seller, null)
            return view
        }

        override fun onActivityCreated(savedInstanceState: Bundle?) {
            super.onActivityCreated(savedInstanceState)

            configCircle(quotaRate)
            configCircle(offerRate)

            networkWrap(Network.create(UserApiService::class.java)?.getSellerData(""))?.subscribe { result ->

                todayQuote.setOnClickListener {
                    quotaRate.showValue(result.data.todaySellRate.toFloat(), 100f, true)
                    quoteDone.setText("${result.data.todaySellValid}")
                    quoteMiss.setText("${result.data.todaySellMiss}")
                    quoteTotal.setText("总响应次数：${result.data.todaySellQuote}")
                    todayQuote.isSelected = true
                    monthQuote.isSelected = false
                    totalQuote.isSelected = false
                }

                monthQuote.setOnClickListener {
                    quotaRate.showValue(result.data.monthSellRate.toFloat(), 100f, true)
                    quoteDone.setText("${result.data.monthSellValid}")
                    quoteMiss.setText("${result.data.mothSellMiss}")
                    quoteTotal.setText("总响应次数：${result.data.monthSellQuote}")
                    todayQuote.isSelected = false
                    monthQuote.isSelected = true
                    totalQuote.isSelected = false
                }

                totalQuote.setOnClickListener {
                    quotaRate.showValue(result.data.sellRate.toFloat(), 100f, true)
                    quoteDone.setText("${result.data.sellValid}")
                    quoteMiss.setText("${result.data.sellMiss}")
                    quoteTotal.setText("总响应次数：${result.data.sellQuote}")
                    todayQuote.isSelected = false
                    monthQuote.isSelected = false
                    totalQuote.isSelected = true
                }

                todayOffer.setOnClickListener {
                    offerRate.showValue(result.data.todayOfferRate.toFloat(), 100f, true)
                    offerDone.setText("${result.data.todayOfferGet}")
                    offerMiss.setText("${result.data.todayOfferNever}")
                    offerTotal.setText("有效报价数：${result.data.todayOfferQuote}")
                    todayOffer.isSelected = true
                    monthOffer.isSelected = false
                    totalOffer.isSelected = false
                }


                monthOffer.setOnClickListener {
                    offerRate.showValue(result.data.monthOfferRate.toFloat(), 100f, true)
                    offerDone.setText("${result.data.monthOfferGet}")
                    offerMiss.setText("${result.data.monthOfferNot}")
                    offerTotal.setText("有效报价数：${result.data.monthOfferAll}")
                    todayOffer.isSelected = false
                    monthOffer.isSelected = true
                    totalOffer.isSelected = false
                }

                totalOffer.setOnClickListener {
                    offerRate.showValue(result.data.offerRate.toFloat(), 100f, true)
                    offerDone.setText("${result.data.offerGet}")
                    offerMiss.setText("${result.data.offerNot}")
                    offerTotal.setText("有效报价数：${result.data.offerAll}")
                    todayOffer.isSelected = false
                    monthOffer.isSelected = false
                    totalOffer.isSelected = true
                }

                todayQuote.performClick()
                todayOffer.performClick()
            }
        }

    }

}
