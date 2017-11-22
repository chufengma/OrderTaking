package ordertaking.itaobuxiu.com.ordertaking.ui

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_day_price_all.*
import ordertaking.itaobuxiu.com.ordertaking.BaseActivity
import ordertaking.itaobuxiu.com.ordertaking.R
import ordertaking.itaobuxiu.com.ordertaking.apis.HomeApiService
import ordertaking.itaobuxiu.com.ordertaking.apis.configSwipe
import ordertaking.itaobuxiu.com.ordertaking.engine.Network
import java.text.SimpleDateFormat

class DayPriceAllActivity : BaseActivity() {

    var marketPriceAdapter: MarketPriceAllAdapter?= null

    var refreshMarketPriceData = {
        Network.create(HomeApiService::class.java)
                ?.getMarketPrice()
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(
                        { result ->
                            marketPriceAdapter?.updateData(result.data)
                            if (result.data != null && result.data.size > 0) {
                                marketPriceUpdateTime.text = "最后更新于" + SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(result.data[0].createTime)
                            }
                            swipe.isRefreshing = false
                        },
                        {

                        }
                )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_day_price_all)
        setupMarketPriceLayout()
        useNormalBack()
        swipe.setOnRefreshListener {
            refreshMarketPriceData()
        }
        configSwipe(swipe)

        refreshMarketPriceData()
    }

    private fun setupMarketPriceLayout() {
        marketPriceAdapter = MarketPriceAllAdapter()
        priceRecyclerView.adapter = marketPriceAdapter
        var layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        priceRecyclerView.layoutManager = layoutManager
    }

}
