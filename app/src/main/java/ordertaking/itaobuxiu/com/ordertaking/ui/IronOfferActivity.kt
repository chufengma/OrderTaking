package ordertaking.itaobuxiu.com.ordertaking.ui

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v7.app.AlertDialog
import android.text.InputFilter
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioGroup
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.activity_iron_offer.*
import ordertaking.itaobuxiu.com.ordertaking.BaseActivity
import ordertaking.itaobuxiu.com.ordertaking.R
import ordertaking.itaobuxiu.com.ordertaking.apis.*
import ordertaking.itaobuxiu.com.ordertaking.engine.Network
import org.jetbrains.anko.dip
import java.text.SimpleDateFormat


class IronOfferActivity : BaseActivity() {

    var ironOffer: SellerOfferInfoListItem? = null
    var countDonw: CountDownTimer? = null

    var currentUpdateing = false
    var noOffer = false

    var currentUnit : BaseIronInfo? = null
    var currentProPlace : BaseIronInfo? = null
    var myOffer: IronBuySellerOfferInfo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_iron_offer)
        useNormalBack()

        ironOffer = intent.getSerializableExtra("sellerOffer") as SellerOfferInfoListItem?

        noOffer = ironOffer?.ironSell == null || ironOffer?.ironSell?.isEmpty()!!
        if (ironOffer?.ironSell != null && !ironOffer?.ironSell?.isEmpty()!!) {
            myOffer = ironOffer?.ironSell?.get(0)
            noOffer = myOffer == null
        }

        if (myOffer != null) {
            currentUnit = BaseIronInfo(myOffer?.baseUnit, myOffer?.baseUnitId)
            currentProPlace = BaseIronInfo(myOffer?.offerPlaces, myOffer?.offerPlacesId)
        }

        update(ironOffer)

        offerRemark.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(35))

        historyOffers.setOnClickListener {
            var intent = Intent(this@IronOfferActivity, IronOfferHistoryActivity::class.java)
            intent.putExtra("sellerOffer", ironOffer)
            startActivity(intent)
        }

        proPlaceLayout.setOnClickListener {
            var dialog = SelectTagDialog(2, this)
            dialog.show()
            dialog.setOnTagSelectedListener(object : SelectTagDialog.OnTagItemClickListener {
                override fun tagSelected(ironType: BaseIronInfo?) {
                    this@IronOfferActivity?.currentProPlace = ironType
                    offerProPlace.text = ironType?.name
                }
            })
        }

        right.setOnClickListener {
            var user: UserInfo? = Hawk.get(LOGIN_USER)
            showCall(user?.sellManTel, user?.sellManName)
        }

    }

    fun setupLevels(layout: LinearLayout, day: String) {
        var levelArray = day.split("-")
        var level = levelArray[0].toInt()
        var levelNum = levelArray[1].toInt()

        for (i in 0..levelNum-1) {
            var image: ImageView = ImageView(this)
            var params = LinearLayout.LayoutParams(dip(12), dip(12))
            image.layoutParams = params

            when (level) {
                1 -> image.setImageResource(R.drawable.ic_level_one)
                2 -> image.setImageResource(R.drawable.ic_level_two)
                3 -> image.setImageResource(R.drawable.ic_level_three)
            }

            layout.addView(image)
        }
    }

    fun update(data: SellerOfferInfoListItem?) {
        // 订单详情
        ironType?.text = data?.ironTypeName
        baseInfo?.text = "${data?.materialName}"
        proPlace?.text = data?.proPlacesName
        surface?.text = data?.surfaceName
        to?.text = if (data?.tolerance.isNullOrBlank()) "--" else data?.tolerance
        spec?.text = if (data?.specifications.isNullOrBlank()) "${data?.height}*${data?.width}*${data?.length}" else data?.specifications
        remark?.text = data?.remark
        location?.text = data?.locationName

        if (myOffer != null) {
            offerProPlace?.text = myOffer?.offerPlaces
        }

        unit?.text = when {
            data?.numbers.isNullOrBlank() && !data?.weights.isNullOrBlank() -> "${data?.weights}${data?.weightUnit}"
            !data?.numbers.isNullOrBlank() && data?.weights.isNullOrBlank() -> "${data?.numbers}${data?.numberUnit}"
            else -> "${data?.numbers}${data?.numberUnit}/${data?.weights}${data?.weightUnit}"
        }

        updateTime?.text = SimpleDateFormat("yyyy-MM-dd HH:mm").format(data?.updateTime)
        doneTimeBig?.text = SimpleDateFormat("yyyy-MM-dd HH:mm").format(data?.updateTime)
        if (!data?.day.isNullOrBlank()) {
            setupLevels(levelLayout, data?.day!!)
        }
        buyerCompanyName?.text = data?.companyName
        historyOffers?.visibility = if (data?.ironSell == null || data?.ironSell?.isEmpty()!!) View.GONE else View.VISIBLE


        var normalFill = {
            offerNums?.text = "快去报价吧，已有${data?.sellNum}家商户在和您竞争"
            if (data?.ironTypeName == "不锈钢板" || data?.ironTypeName == "不锈钢卷") {
                offerToLayout?.visibility = View.VISIBLE
                offerToline?.visibility = View.VISIBLE
            } else {
                offerToLayout?.visibility = View.GONE
                offerToline?.visibility = View.GONE
            }
        }

        var doneFill = {
            if (data?.ironSell == null || data?.ironSell?.isEmpty()!!) {
                donePriceLayout.visibility = View.GONE
            } else {
                donePriceLayout.visibility = View.VISIBLE
                doneMyPrice?.text = "￥${data?.ironSell?.get(0)?.offerPerPrice}/${data?.ironSell?.get(0)?.baseUnit}"
                offerToLayoutDone?.visibility = if (data?.ironSell?.get(0)?.tolerance.isNullOrBlank()) View.GONE else View.VISIBLE
                offerToDone?.text = data?.ironSell?.get(0)?.tolerance
                offerProPlaceDone?.text = data?.ironSell?.get(0)?.offerPlaces
                remarkDone?.setText(data?.ironSell?.get(0)?.offerRemark)
            }
        }

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

            offerToDone?.isEnabled = enable
            toDoneTitle?.isEnabled = enable

            offerProPlaceDone?.isEnabled = enable
            proPlaceDoneTitle?.isEnabled = enable

            remarkDone?.isEnabled = enable
            remarkDoneTitle?.isEnabled = enable

            doneMyPrice?.isEnabled = enable
            doneMyPriceTitle?.isEnabled = enable
        }

        postEdit?.setOnClickListener {
            currentUpdateing = true
            updateActions()
        }

        ignore?.setOnClickListener {
            currentUpdateing = false
            resetIronSellers()
            updateActions()
        }

        var saveOffer = lit@{
            var price = offerPrice?.text?.toString()
            if (!price.isNullOrBlank() && price?.toDouble()!! <= 0) {
                toastInfo("价格必须大于0")
                return@lit
            }
            var needTo = false
            if (data?.ironTypeName == "不锈钢板" || data?.ironTypeName == "不锈钢卷") {
                needTo = true
            }
            if (currentUnit == null || currentProPlace == null || price.isNullOrBlank() || (needTo && offerTo.text.toString().isNullOrBlank())) {
                toastInfo("请补充完整报价信息")
                return@lit
            }
            showLoading()
            networkWrap(Network.create(IronBuyOfferService::class.java)?.postOffer(
                    ironOffer?.id!!,
                    price,
                    "",
                    offerTo.text.toString(),
                    currentProPlace?.id,
                    currentProPlace?.name,
                    offerRemark.text?.toString(),
                    currentUnit?.id,
                    currentUnit?.name))
                    ?.subscribe({
                        hideLoading()
                        toastInfo("操作成功")
                        SellerFragment.notifyRefrsh(null)
                        finish()
                    }, { error ->
                        hideLoading()
                        toastInfo("操作失败：" + error.message)
                    })
        }

        offerIt?.setOnClickListener {
            saveOffer()
        }

        update?.setOnClickListener {
            saveOffer()
        }

        missIt?.setOnClickListener {
            showLoading()
            networkWrap(Network.create(IronBuyOfferService::class.java)?.missOffer(ironOffer?.id!!))
                    ?.subscribe({
                        hideLoading()
                        toastInfo("操作成功")
                        SellerFragment.notifyRefrsh(null)
                        finish()
                    }, { error ->
                        hideLoading()
                        toastInfo("操作失败：" + error.message)
                    })
        }

        resetIronSellers()
        updateActions()

        when (data?.offerStatus) {
            0 -> {
                changeEnable(true)
                missIt?.visibility = View.VISIBLE
                offerIt?.visibility = View.VISIBLE
                postEdit?.visibility = View.GONE
                update?.visibility = View.GONE
                ignore?.visibility = View.GONE

                updateTime()
                buyerCompanyName?.text = "买家公司"
                var countDonw = object : CountDownTimer(ironOffer?.createTime?.plus(ironOffer?.timeLimit?.toLong()!!)!! - System.currentTimeMillis()!!, 1000) {
                    override fun onFinish() {
                        toastInfo("该求购已失效")
                        finish()
                    }

                    override fun onTick(millisUntilFinished: Long) {
                        updateTime()
                    }
                }
                countDonw.start()

                doingLayout.visibility = View.VISIBLE
                doneLayout.visibility = View.GONE
                endLayout.visibility = View.GONE
                actionLayout.visibility = View.VISIBLE
                normalPriceLayout.visibility = View.VISIBLE
                donePriceLayout.visibility = View.GONE
                normalFill()
            }
            1 -> {
                changeEnable(true)
                missIt?.visibility = View.GONE
                offerIt?.visibility = View.GONE
                postEdit?.visibility = View.VISIBLE
                update?.visibility = View.GONE
                ignore?.visibility = View.GONE

                updateTime()
                var countDonw = object : CountDownTimer(ironOffer?.createTime?.plus(ironOffer?.timeLimit?.toLong()!!)!! - System.currentTimeMillis()!!, 1000) {
                    override fun onFinish() {
                        toastInfo("该求购已失效")
                        finish()
                    }

                    override fun onTick(millisUntilFinished: Long) {
                        updateTime()
                    }
                }
                countDonw.start()

                doingLayout.visibility = View.VISIBLE
                doneLayout.visibility = View.GONE
                endLayout.visibility = View.GONE
                actionLayout.visibility = View.VISIBLE
                normalPriceLayout.visibility = View.VISIBLE
                donePriceLayout.visibility = View.GONE
                normalFill()

                offerDetailCompanyLayout.setOnClickListener {
                    CompanyDialog(ironOffer?.companyName, ironOffer?.level, this@IronOfferActivity).show()
                }
            }
            2 -> {
                changeEnable(true)

                doingLayout.visibility = View.GONE
                doneLayout.visibility = View.VISIBLE
                endLayout.visibility = View.GONE
                actionLayout.visibility = View.GONE
                normalPriceLayout.visibility = View.GONE
                donePriceLayout.visibility = View.VISIBLE
                doneMyPriceTitle.text = "成交单价"
                doneTimeBig.text = "成交时间 ${SimpleDateFormat("yyyy-MM-dd HH:mm").format(data?.updateTime)}"

                doneContact.setOnClickListener {
                    showCall(data?.contactNum)
                }

                doneFill()
                offerDetailCompanyLayout.setOnClickListener {
                    CompanyDialog(ironOffer?.companyName, ironOffer?.level, this@IronOfferActivity).show()
                }
            }
            else -> {
                changeEnable(false)

                doingLayout.visibility = View.GONE
                doneLayout.visibility = View.GONE
                endLayout.visibility = View.VISIBLE
                actionLayout.visibility = View.GONE
                normalPriceLayout.visibility = View.GONE
                donePriceLayout.visibility = View.VISIBLE
                doneMyPriceTitle.text = "我的报价"
                endTimeBig.text = "错过时间 ${SimpleDateFormat("yyyy-MM-dd HH:mm").format(data?.updateTime)}"
                doneFill()
            }
        }
    }

    fun updateActions() {
        postEdit?.visibility = if (currentUpdateing) View.GONE else View.VISIBLE
        ignore?.visibility = if (currentUpdateing) View.VISIBLE else View.GONE
        update?.visibility = if (currentUpdateing) View.VISIBLE else View.GONE

        proPlaceLayout?.isEnabled = noOffer || currentUpdateing
        offerPrice?.isEnabled = noOffer || currentUpdateing
        unitGroup?.isEnabled = noOffer || currentUpdateing
        offerTo?.isEnabled = noOffer || currentUpdateing
        offerRemark?.isEnabled = noOffer || currentUpdateing

        updateUnits()
    }

    fun updateUnits() {
        var normalCheck = {
            if (ironOffer?.weights.isNullOrBlank()) {
                unitWeight?.visibility = View.GONE
                unitNumbers.isChecked = true
                unitNumbers.isEnabled = false
                unitWeight?.isChecked = false

                currentUnit = BaseIronInfo(ironOffer?.numberUnit, ironOffer?.numberUnitId)
            }

            if (ironOffer?.numbers.isNullOrBlank()) {
                unitNumbers?.visibility = View.GONE
                unitWeight.isChecked = true
                unitWeight.isEnabled = false
                unitNumbers?.isChecked = false

                currentUnit = BaseIronInfo(ironOffer?.weightUnit, ironOffer?.weightUnitId)
            }

            unitWeight?.text = "元/${ironOffer?.weightUnit}"
            unitNumbers?.text = "元/${ironOffer?.numberUnit}"

            if (!ironOffer?.numbers.isNullOrBlank() && !ironOffer?.weights.isNullOrBlank()) {
                unitWeight?.visibility = View.VISIBLE
                unitNumbers?.visibility = View.VISIBLE
            }
        }

        normalCheck()

        if (!ironOffer?.numbers.isNullOrBlank() && !ironOffer?.weights.isNullOrBlank()) {
            unitWeight?.isChecked = true

            unitWeight.isEnabled = noOffer || currentUpdateing
            unitNumbers.isEnabled = noOffer || currentUpdateing

            currentUnit = BaseIronInfo(ironOffer?.weightUnit, ironOffer?.weightUnitId)

            if (!currentUpdateing && myOffer != null) {
                unitNumbers?.visibility = View.GONE
                unitWeight?.visibility = View.VISIBLE
                unitWeight?.text = "元/${myOffer?.baseUnit}"
                currentUnit = BaseIronInfo(myOffer?.baseUnit, myOffer?.baseUnitId)
            }

            if (currentUpdateing) {
                normalCheck()
            }
        }

        unitGroup.setOnCheckedChangeListener(object: RadioGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
                if (unitWeight.isChecked) {
                    currentUnit = BaseIronInfo(ironOffer?.weightUnit, ironOffer?.weightUnitId)
                } else {
                    currentUnit = BaseIronInfo(ironOffer?.numberUnit, ironOffer?.numberUnitId)
                }
            }
        })

    }

    fun resetIronSellers() {
        if (ironOffer?.ironSell != null && !ironOffer?.ironSell?.isEmpty()!!) {
            offerRemark?.setText(ironOffer?.ironSell?.get(0)?.offerRemark)
            offerPrice?.setText(ironOffer?.ironSell?.get(0)?.offerPerPrice)

            offerTo?.setText(ironOffer?.ironSell?.get(0)?.tolerance)
            offerProPlace?.setText(ironOffer?.ironSell?.get(0)?.offerPlaces)

            // offerToLayout?.visibility = if (ironOffer?.ironSell?.get(0)?.tolerance == null || ironOffer?.ironSell?.get(0)?.tolerance?.isNullOrBlank()!!) View.GONE else View.VISIBLE
            // offerToline?.visibility = if (ironOffer?.ironSell?.get(0)?.tolerance == null || ironOffer?.ironSell?.get(0)?.tolerance?.isNullOrBlank()!!) View.GONE else View.VISIBLE
        }
    }

    fun updateTime() {
        var offset = ironOffer?.createTime?.plus(ironOffer?.timeLimit?.toLong()!!)!! - System.currentTimeMillis()
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

}
