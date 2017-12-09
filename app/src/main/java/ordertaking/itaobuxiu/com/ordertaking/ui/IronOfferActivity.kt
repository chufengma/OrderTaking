package ordertaking.itaobuxiu.com.ordertaking.ui

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_iron_offer.*
import ordertaking.itaobuxiu.com.ordertaking.BaseActivity
import ordertaking.itaobuxiu.com.ordertaking.R
import ordertaking.itaobuxiu.com.ordertaking.apis.*
import org.jetbrains.anko.dip
import java.text.SimpleDateFormat

class IronOfferActivity : BaseActivity() {

    var ironOffer: SellerOfferInfoListItem? = null
    var countDonw: CountDownTimer? = null
    var proPlaceModel: BaseIronInfo? = null

    var currentUpdateing = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_iron_offer)
        useNormalBack()

        ironOffer = intent.getSerializableExtra("sellerOffer") as SellerOfferInfoListItem?

        update(ironOffer)

        historyOffers.setOnClickListener {
            var intent = Intent(this@IronOfferActivity, IronOfferHistoryActivity::class.java)
            intent.putExtra("sellerOffer", ironOffer)
            startActivity(intent)
        }

        proPlaceLayout.setOnClickListener {
            var dialog = SelectTagDialog(2,this)
            dialog.show()
            dialog.setOnTagSelectedListener(object : SelectTagDialog.OnTagItemClickListener {
                override fun tagSelected(ironType: BaseIronInfo?) {
                    this@IronOfferActivity?.proPlaceModel = ironType
                    offerProPlace.text = ironType?.name
                }
            })
        }
    }

    fun setupLevels(layout: LinearLayout, day: String) {
        var levelArray = day.split("-")
        var level = levelArray[0].toInt()
        var levelNum = levelArray[1].toInt()

        for(i in 0..levelNum) {
            var image: ImageView = ImageView(this)
            var params = LinearLayout.LayoutParams(dip(12), dip(12))
            image.layoutParams = params

            when(level) {
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

        unit?.text = when {
            data?.numbers.isNullOrBlank() && !data?.weights.isNullOrBlank() -> "${data?.weights}${data?.weightUnit}"
            !data?.numbers.isNullOrBlank() && data?.weights.isNullOrBlank() -> "${data?.numbers}${data?.numberUnit}"
            else -> "${data?.numbers}${data?.numberUnit}/${data?.weights}${data?.weightUnit}"
        }

        updateTime?.text = SimpleDateFormat("yyyy-MM-dd HH:mm").format(data?.updateTime)
        doneTimeBig?.text = SimpleDateFormat("yyyy-MM-dd HH:mm").format(data?.updateTime)

        buyerCompanyName?.text = data?.companyName
        offerNums?.text = "快去报价吧，已有${ironOffer?.sellAllNum}家商户在和您竞争"

        if (!data?.day.isNullOrBlank()) {
            setupLevels(levelLayout, data?.day!!)
        }

        if (data?.ironTypeName == "不锈钢板" || data?.ironTypeName == "不锈钢卷") {
            offerToLayout?.visibility = View.VISIBLE
            offerToline?.visibility = View.VISIBLE
        } else {
            offerToLayout?.visibility = View.GONE
            offerToline?.visibility = View.GONE
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

        // FIXME
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

                var countDonw = object: CountDownTimer(ironOffer?.createTime?.plus(ironOffer?.timeLimit?.toLong()!!)!! - System.currentTimeMillis()!!, 1000) {
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
            1 -> {
                changeEnable(true)
                missIt?.visibility = View.GONE
                offerIt?.visibility = View.GONE
                postEdit?.visibility = View.VISIBLE
                update?.visibility = View.GONE
                ignore?.visibility = View.GONE

                updateTime()
                var countDonw = object: CountDownTimer(ironOffer?.createTime?.plus(ironOffer?.timeLimit?.toLong()!!)!! - System.currentTimeMillis()!!, 1000) {
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
            else -> {
                changeEnable(true)
                missIt?.visibility = View.GONE
                offerIt?.visibility = View.GONE
                postEdit?.visibility = View.VISIBLE
                update?.visibility = View.GONE
                ignore?.visibility = View.GONE

                updateTime()
                var countDonw = object: CountDownTimer(ironOffer?.createTime?.plus(ironOffer?.timeLimit?.toLong()!!)!! - System.currentTimeMillis()!!, 1000) {
                    override fun onFinish() {
                        // toastInfo("该求购已失效")
                        // finish()
                    }

                    override fun onTick(millisUntilFinished: Long) {
                        updateTime()
                    }
                }
                countDonw.start()
            }
        }
    }

    fun updateActions() {
        postEdit?.visibility = if (currentUpdateing) View.GONE else View.VISIBLE
        ignore?.visibility = if (currentUpdateing) View.VISIBLE else View.GONE
        update?.visibility = if (currentUpdateing) View.VISIBLE else View.GONE

        proPlaceLayout?.isEnabled = currentUpdateing
        offerPrice?.isEnabled = currentUpdateing
        unitGroup?.isEnabled = currentUpdateing
        offerTo?.isEnabled = currentUpdateing
        offerRemark?.isEnabled = currentUpdateing

        updateUnits()
    }

    fun updateUnits() {
        if (ironOffer?.weights.isNullOrBlank()) {
            unitWeight?.visibility = View.GONE
            unitNumbers.isChecked = true
            unitNumbers.isEnabled = false
            unitWeight?.isChecked = false
        }

        if (ironOffer?.numbers.isNullOrBlank()) {
            unitNumbers?.visibility = View.GONE
            unitWeight.isChecked = true
            unitWeight.isEnabled = false
            unitNumbers?.isChecked = false
        }

        unitWeight?.text = "元/${ironOffer?.weightUnit}"
        unitNumbers?.text = "元/${ironOffer?.numberUnit}"

        if (!ironOffer?.numbers.isNullOrBlank() && !ironOffer?.weights.isNullOrBlank()) {
            unitWeight?.isChecked = true

            unitWeight.isEnabled = currentUpdateing
            unitNumbers.isEnabled = currentUpdateing


            if (!currentUpdateing && (ironOffer?.offerStatus == 1 || ironOffer?.offerStatus == 2) ) {
                unitNumbers?.visibility = View.GONE
                unitWeight?.visibility = View.VISIBLE
                unitWeight?.text = "元/${ironOffer?.ironSell?.get(0)?.baseUnit}"
            }
        }

    }

    fun resetIronSellers() {
        offerRemark?.setText(ironOffer?.ironSell?.get(0)?.offerRemark)
        offerPrice?.setText(ironOffer?.ironSell?.get(0)?.offerPerPrice)

        offerTo?.setText(ironOffer?.ironSell?.get(0)?.tolerance)
        offerProPlace?.setText(ironOffer?.ironSell?.get(0)?.offerPlaces)

        offerToLayout?.visibility = if (ironOffer?.ironSell?.get(0)?.tolerance == null || ironOffer?.ironSell?.get(0)?.tolerance?.isNullOrBlank()!!) View.GONE else View.VISIBLE
        offerToline?.visibility = if (ironOffer?.ironSell?.get(0)?.tolerance == null || ironOffer?.ironSell?.get(0)?.tolerance?.isNullOrBlank()!!) View.GONE else View.VISIBLE
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
