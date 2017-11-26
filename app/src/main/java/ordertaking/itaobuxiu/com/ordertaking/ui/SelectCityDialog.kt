package ordertaking.itaobuxiu.com.ordertaking.ui

import android.app.Dialog
import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import ordertaking.itaobuxiu.com.ordertaking.R
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_new_request.*
import kotlinx.android.synthetic.main.select_city_dialog.*
import ordertaking.itaobuxiu.com.ordertaking.apis.CityModel
import ordertaking.itaobuxiu.com.ordertaking.apis.IronRequestService
import ordertaking.itaobuxiu.com.ordertaking.apis.networkWrap
import ordertaking.itaobuxiu.com.ordertaking.engine.Network


/**
 * Created by dev on 2017/11/25.
 */
class SelectCityDialog(context: Context?) : Dialog(context, R.style.Dialog) {

    var provicesAdapter: CityAdapter? = null
    var cityAdapter: CityAdapter? = null

    var currentProvice: CityModel? = null
    var currentCity: CityModel? = null

    var listener: OnCityItemClickListener? = null

    init {
        setContentView(R.layout.select_city_dialog)

        close.setOnClickListener {
            dismiss()
        }

        provicesAdapter = CityAdapter()
        provices.adapter = provicesAdapter
        provices.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        cityAdapter = CityAdapter()
        cities.adapter = cityAdapter
        cities.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        networkWrap(Network.create(IronRequestService::class.java)?.getProvices())?.subscribe { result ->
            provicesAdapter?.updateData(result.data)
            p.isSelected = true
            c.isSelected = false
        }

        provicesAdapter?.setOnCitySelectedListener(object : OnCityItemClickListener {
            override fun citySelected(cityModel: CityModel?) {
                currentProvice = cityModel
                findCity()
                provices.visibility = View.GONE
                cities.visibility = View.VISIBLE
            }
        })

        cityAdapter?.setOnCitySelectedListener(object : OnCityItemClickListener {
            override fun citySelected(cityModel: CityModel?) {
                currentCity = cityModel
                dismiss()
                listener?.citySelected(currentCity)
            }
        })
    }

    fun setOnCitySelectedListener(listener: OnCityItemClickListener) {
        this.listener = listener
    }

    fun findCity() {
        if (currentProvice == null) {
            return
        }
        networkWrap(Network.create(IronRequestService::class.java)?.getCity(currentProvice?.id!!))?.subscribe { result ->
            cityAdapter?.updateData(result.data)
            p.isSelected = false
            c.isSelected = true
        }
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