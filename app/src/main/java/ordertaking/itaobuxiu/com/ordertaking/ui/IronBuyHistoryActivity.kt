package ordertaking.itaobuxiu.com.ordertaking.ui

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_iron_buy_history.*
import ordertaking.itaobuxiu.com.ordertaking.BaseActivity
import ordertaking.itaobuxiu.com.ordertaking.R
import ordertaking.itaobuxiu.com.ordertaking.apis.IronBuySellerInfo

class IronBuyHistoryActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_iron_buy_history)
        useNormalBack()

        var info: IronBuySellerInfo = intent.getSerializableExtra("ironSellerInfo") as IronBuySellerInfo
        var adapter = IronBuyHistoryAdapter(info.offerStatus == 2)
        historyRecycler.layoutManager = LinearLayoutManager(this)
        historyRecycler.adapter = adapter
        adapter.updateData(info.ironSell!!)
        companyName?.text = info.companyName
    }

}
