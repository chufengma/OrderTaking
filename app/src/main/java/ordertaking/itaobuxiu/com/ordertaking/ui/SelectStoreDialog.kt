package ordertaking.itaobuxiu.com.ordertaking.ui

import android.app.Dialog
import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import kotlinx.android.synthetic.main.select_city_dialog_3.*
import ordertaking.itaobuxiu.com.ordertaking.R
import ordertaking.itaobuxiu.com.ordertaking.apis.CityDescData
import ordertaking.itaobuxiu.com.ordertaking.apis.StoreData
import ordertaking.itaobuxiu.com.ordertaking.apis.UserApiService
import ordertaking.itaobuxiu.com.ordertaking.apis.networkWrap
import ordertaking.itaobuxiu.com.ordertaking.engine.Network

/**
 * Created by dev on 2017/11/25.
 */
class SelectStoreDialog(context: Context?) : Dialog(context, R.style.Dialog) {

    var storeAdapter: StoreAdapter? = null

    var listener: OnStoreBackListener? = null

    interface OnStoreBackListener {
        fun onSelected(store: StoreData?)
    }

    init {
        setContentView(R.layout.select_city_dialog_3)

        close.setOnClickListener {
            dismiss()
        }

        storeAdapter = StoreAdapter()
        stores.adapter = storeAdapter
        stores.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        networkWrap(Network.create(UserApiService::class.java)?.getStoreHouse(""))?.subscribe({ result ->
            storeAdapter?.updateData(result.data)
        }, {})

        storeAdapter?.setOnStoreListener(object : OnStoreItemClickListener {
            override fun onStore(store: StoreData?) {
                listener?.onSelected(store)
                dismiss()
            }
        })
    }

    fun setOnStoreListener(listener: OnStoreBackListener) {
        this.listener = listener
    }

    override fun show() {
        super.show()

        val layoutParams = window!!.attributes
        layoutParams.gravity = Gravity.BOTTOM
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        window!!.decorView.setPadding(0, 0, 0, 0)
        window!!.attributes = layoutParams
    }



}