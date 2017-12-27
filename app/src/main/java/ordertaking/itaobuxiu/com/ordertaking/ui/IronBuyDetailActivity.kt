package ordertaking.itaobuxiu.com.ordertaking.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import ordertaking.itaobuxiu.com.ordertaking.BaseActivity
import ordertaking.itaobuxiu.com.ordertaking.R
import android.view.WindowManager
import android.os.Build.VERSION_CODES
import android.os.Build.VERSION
import android.os.CountDownTimer
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.activity_iron_buy_detail.*
import ordertaking.itaobuxiu.com.ordertaking.apis.*
import ordertaking.itaobuxiu.com.ordertaking.engine.Network
import java.text.SimpleDateFormat
import java.util.*


class IronBuyDetailActivity : BaseActivity() {

    var ironBuyInfo: IronBuyInfo? = null
    var adapter: IronBuyOfferAdapter? = null
    var countDonw: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_iron_buy_detail)

        useNormalBack()

        if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
            val window = window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS or WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
            window.navigationBarColor = Color.TRANSPARENT
        }

        ironBuyInfo = intent.getSerializableExtra("ironBuyInfo") as IronBuyInfo?

        statusLayout.viewTreeObserver.addOnGlobalLayoutListener {
            var height = statusLayout.measuredHeight
            slideOne.layoutParams.height = height / 2
            slideOne.requestLayout()
        }

        titleLayout.setPadding(0, getStatusBarHeight(this), 0, 0)

        offerRecycler.layoutManager = LinearLayoutManager(this)
        adapter = IronBuyOfferAdapter(ironBuyInfo?.buyStatus!!)
        offerRecycler.adapter = adapter

        adapter?.setActionListener(object : OnOfferSellActionListener {

            override fun onHistory(seller: IronBuySellerInfo) {
                if (seller.ironSell != null) {
                    var intent = Intent(this@IronBuyDetailActivity, IronBuyHistoryActivity::class.java)
                    intent.putExtra("ironSellerInfo", seller)
                    startActivity(intent)
                }
            }

            override fun onChoose(seller: IronBuySellerInfo) {
                showLoading()
                networkWrap(Network.create(IronRequestService::class.java)?.chooseSeller(ironBuyInfo?.id!!, seller.ironSellId!!))
                        ?.subscribe({
                            hideLoading()
                            toastInfo("选标成功")
                            BuyerFragment.notifyRefrsh(null)
                        }, { error ->
                            hideLoading()
                            toastInfo("选标失败：" + error.message)
                        })
            }

            override fun onContact(seller: IronBuySellerInfo) {
                showCall(seller.contactNum)
            }
        })

        updateData()

        BuyerFragment.addRefreshListener {
            finish()
        }

        right.setOnClickListener {
            var user: UserInfo? = Hawk.get(LOGIN_USER)
            showCall(user?.sellManTel, user?.sellManName)
        }

        right.postDelayed({
            mainContent.scrollTo(0, 0)
        }, 100)
    }

    fun updateData() {
        update(ironBuyInfo)
        if (ironBuyInfo?.ironSell?.validSell != null || ironBuyInfo?.ironSell?.missSell != null) {
            var list = mutableListOf<IronBuySellerInfo>()
            list.addAll(ironBuyInfo?.ironSell?.validSell!!)
            list.addAll(ironBuyInfo?.ironSell?.missSell!!)
            adapter?.updateData(list)
        } else {
            adapter?.updateData(mutableListOf())
        }

        briefInfo.text = "报价${if (ironBuyInfo?.ironSell?.validSell == null) 0 else ironBuyInfo?.ironSell?.validSell?.size} " +
                "错过${if (ironBuyInfo?.ironSell?.missSell == null) 0 else ironBuyInfo?.ironSell?.missSell?.size}"
    }

    fun updateTime() {
        var offset = ironBuyInfo?.createTime?.plus(ironBuyInfo?.timeLimit?.toLong()!!)!! - System.currentTimeMillis()
        var hourTime = offset / (1000 * 60 * 60)
        var minuteTime = (offset - hourTime * (1000 * 60 * 60)) / (1000 * 60)
        var secondTime = (offset - hourTime * (1000 * 60 * 60) - minuteTime * (1000 * 60)) / 1000

        var hourTimeStr = if (hourTime < 10) "0$hourTime" else hourTime
        var minuteTimeStr = if (minuteTime < 10) "0$minuteTime" else minuteTime
        var secondTimeStr = if (secondTime < 10) "0$secondTime" else secondTime

        hour?.text = hourTimeStr.toString()
        minute?.text = minuteTimeStr.toString()
        second?.text = secondTimeStr.toString()
    }

    override fun finish() {
        super.finish()
        countDonw?.cancel()
    }

    fun update(data: IronBuyInfo?) {
        // 订单详情
        ironType?.text = data?.ironTypeName
        baseInfo?.text = "${data?.materialName}"
        proPlace?.text = data?.proPlacesName
        surface?.text = data?.surfaceName
        to?.text = if (data?.tolerance.isNullOrBlank()) "--" else data?.tolerance
        spec?.text = if (data?.specifications.isNullOrBlank()) "${data?.height}*${data?.width}*${data?.length}" else data?.specifications
        remark?.text = data?.remark
        location?.text = data?.locationName


        unit?.text = when {
            data?.numbers.isNullOrBlank() && !data?.weights.isNullOrBlank() -> "${data?.weights}${data?.weightUnit}"
            !data?.numbers.isNullOrBlank() && data?.weights.isNullOrBlank() -> "${data?.numbers}${data?.numberUnit}"
            else -> "${data?.numbers}${data?.numberUnit}/${data?.weights}${data?.weightUnit}"
        }

        updateTime?.text = SimpleDateFormat("yyyy-MM-dd HH:mm").format(data?.createTime)
        doneTimeBig?.text = SimpleDateFormat("yyyy-MM-dd HH:mm").format(data?.updateTime)

        var changeEnable = { enable: Boolean ->
            ironType?.isEnabled = enable
            baseInfo?.isEnabled = enable
            proPlace?.isEnabled = enable
            surface?.isEnabled = enable
            to?.isEnabled = enable
            spec?.isEnabled = enable
            unit?.isEnabled = enable
            remark?.isEnabled = enable
            blueDot?.isEnabled = enable
            iconTime?.isEnabled = enable
            location?.isEnabled = enable
            locationIcon?.isEnabled = enable
        }

        when (data?.buyStatus) {
            1 -> {
                changeEnable(true)
                copy?.visibility = View.VISIBLE
                delete?.visibility = View.VISIBLE
                edit?.visibility = if (data?.editStatus == 0) View.VISIBLE else View.GONE

                doingLayout.visibility = View.VISIBLE
                doneLayout.visibility = View.GONE
                endLayout.visibility = View.GONE

                slideOne.setBackgroundResource(R.drawable.iron_buy_detail_doing_bg)
                titleLayout.setBackgroundResource(R.drawable.iron_buy_detail_doing_bg)
                doneCompanyLayout.visibility = View.GONE
                updateTime()

                var countDonw = object: CountDownTimer(ironBuyInfo?.createTime?.plus(ironBuyInfo?.timeLimit?.toLong()!!)!! - ironBuyInfo?.serveTime!!, 1000) {
                    override fun onFinish() {
                        toastInfo("该求购已失效")
                        finish()
                    }

                    override fun onTick(millisUntilFinished: Long) {
                        updateTime()
                    }
                }
                countDonw.start()
            }
            2 -> {
                changeEnable(true)

                copy?.visibility = View.VISIBLE
                delete?.visibility = View.GONE
                edit?.visibility = View.GONE


                doingLayout.visibility = View.GONE
                doneLayout.visibility = View.VISIBLE
                endLayout.visibility = View.GONE
                doneCompanyLayout.visibility = View.GONE


                doneCompanyLayout.visibility = View.VISIBLE


                var ironSellInfoSuccess = data?.ironSell?.validSell?.get(0)
                donePrice.text = ironSellInfoSuccess?.offerPerPrice
                unitBig.text = "元/${ironSellInfoSuccess?.baseUnit}"
                proPlaceBig.text = ironSellInfoSuccess?.offerPlaces
                remarkBig.text = ironSellInfoSuccess?.offerRemark
                toBig.text = if (ironSellInfoSuccess?.tolerance.isNullOrBlank()) "" else ironSellInfoSuccess?.tolerance

                baoBig.visibility = if (ironSellInfoSuccess?.isGuaranteeUser == "1") View.VISIBLE else View.GONE
                chengBig.visibility = if (ironSellInfoSuccess?.isFaithUser == "1") View.VISIBLE else View.GONE
                companyNameBig.text = ironSellInfoSuccess?.companyName

                companyNameBig.setOnClickListener {
                    CompanyDialog(ironSellInfoSuccess?.companyName, ironSellInfoSuccess?.level, this).show()
                }

                contactSellBig.setOnClickListener {
                    showCall(ironSellInfoSuccess?.contactNum)
                }


                slideOne.setBackgroundResource(R.drawable.iron_buy_detail_done_bg)
                titleLayout.setBackgroundResource(R.drawable.iron_buy_detail_done_bg)
                doneCompanyLayout.visibility = View.VISIBLE
            }
            else -> {
                changeEnable(false)

                copy?.visibility = View.VISIBLE
                delete?.visibility = View.GONE
                edit?.visibility = View.GONE

                doingLayout.visibility = View.GONE
                doneLayout.visibility = View.GONE
                endLayout.visibility = View.VISIBLE
                doneCompanyLayout.visibility = View.GONE

                slideOne.setBackgroundResource(R.drawable.iron_buy_detail_end_bg)
                titleLayout.setBackgroundResource(R.drawable.iron_buy_detail_end_bg)

                endTimeBig?.text = SimpleDateFormat("yyyy-MM-dd HH:mm").format(data?.createTime!! + data?.timeLimit?.toLong()!!)
            }
        }

        copy.setOnClickListener {
            gotoNewRequest(this, data?.toPostReuqestBean(), false)
        }

        edit.setOnClickListener {
            gotoNewRequest(this, data?.toPostReuqestBean(), true)
        }

        delete.setOnClickListener {
            AlertDialog.Builder(this).setMessage("确认删除")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确定", { dialog, which ->
                        showLoading()
                        if (data != null) {
                            doEditIronBuy(data, false)?.subscribe({
                                hideLoading()
                                toastInfo("删除成功")
                                BuyerFragment.notifyRefrsh(null)
                                finish()
                            }, { error ->
                                hideLoading()
                                toastInfo("删除失败：" + error.message)
                            })
                        }
                        dialog.dismiss()
                    }).show()
        }
    }


}
