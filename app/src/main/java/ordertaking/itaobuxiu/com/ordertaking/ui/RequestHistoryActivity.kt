package ordertaking.itaobuxiu.com.ordertaking.ui

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_request_history.*
import ordertaking.itaobuxiu.com.ordertaking.BaseActivity
import ordertaking.itaobuxiu.com.ordertaking.R
import ordertaking.itaobuxiu.com.ordertaking.apis.IronRequestService
import ordertaking.itaobuxiu.com.ordertaking.apis.PostRequestBean
import ordertaking.itaobuxiu.com.ordertaking.apis.networkWrap
import ordertaking.itaobuxiu.com.ordertaking.apis.toastInfo
import ordertaking.itaobuxiu.com.ordertaking.engine.Network
import ordertaking.itaobuxiu.com.ordertaking.apis.gotoNewRequest

class RequestHistoryActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_history)


        var adapter = HistoryPostRequestAdapter()
        historyRecycler.layoutManager = LinearLayoutManager(this)
        historyRecycler.adapter = adapter

        showLoading()
        networkWrap(Network.create(IronRequestService::class.java)?.getRequestHistory())
                ?.subscribe({
                    hideLoading()
                    adapter.updateData(it.data.map { it.toPostReuqestBean() })
                },
                { error ->
                    hideLoading()
                    toastInfo("获取历史求购信息" + error.message)
                })

        adapter.setActionListener(object: OnHistoryPostRequestActionListener {
            override fun onItemClick(request: PostRequestBean) {
                gotoNewRequest(this@RequestHistoryActivity, request, false)
            }
        })

    }
}
