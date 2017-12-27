package ordertaking.itaobuxiu.com.ordertaking.ui

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import kotlinx.android.synthetic.main.activity_post_charge.*
import ordertaking.itaobuxiu.com.ordertaking.BaseActivity
import ordertaking.itaobuxiu.com.ordertaking.R
import ordertaking.itaobuxiu.com.ordertaking.engine.Network
import android.text.InputFilter.LengthFilter
import android.text.InputFilter
import com.orhanobut.hawk.Hawk
import ordertaking.itaobuxiu.com.ordertaking.apis.*


class PostChargeActivity : BaseActivity() {

    var adapter: ChargeAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_charge)

        chargeRecycler.layoutManager = LinearLayoutManager(this)
        adapter = ChargeAdapter()
        chargeRecycler.adapter = adapter

        adapter?.setOnChargeItemClickListener(object: OnChargeItemClickListener {
            override fun onChargeSelected(chargeData: ChargeData?) {
                chargeText?.setText(chargeData?.info)
            }
        })

        val filters = arrayOf<InputFilter>(LengthFilter(35))
        chargeText.filters = filters

        chargeText?.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                changeNum.setText("${chargeText.editableText.length}/35")
            }
        })

        fetchChargeInfos()


        var user: UserInfo? = Hawk.get(LOGIN_USER)
        if (!user?.buserInfo?.proInfo.isNullOrBlank()) {
            chargeText.setText(user?.buserInfo?.proInfo)
        }


        right.setOnClickListener {
            var text = chargeText.text.toString()
            if (text.isNullOrBlank()) {
                toastInfo("请输入优惠信息")
            }
            showLoading()
            networkWrap(Network.create(UserApiService::class.java)?.postCharge(text))?.subscribe({
                result ->
                toastInfo("发布优惠成功")
                hideLoading()
                finish()
            }, { error ->
                toastInfo("发布优惠失败:" + error.message)
                hideLoading()
            })
        }

    }

    fun fetchChargeInfos() {
        showLoading()
        networkWrap(Network.create(UserApiService::class.java)?.getChargeInfos(""))?.subscribe({
            result ->
            adapter?.updateData(result.data)
            hideLoading()
        }, {
            hideLoading()
        })

//        networkWrap(Network.create(UserApiService::class.java)?.findAllPro(""))?.subscribe({
//            result ->
//            if (result.data != null && result.data.size >= 1) {
//                chargeText.setText(result.data.get(0).info)
//            }
//        }, {
//        })
    }

}
