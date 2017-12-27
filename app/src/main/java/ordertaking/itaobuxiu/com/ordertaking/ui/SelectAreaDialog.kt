package ordertaking.itaobuxiu.com.ordertaking.ui

import android.app.Dialog
import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import kotlinx.android.synthetic.main.select_city_dialog_2.*
import ordertaking.itaobuxiu.com.ordertaking.R
import ordertaking.itaobuxiu.com.ordertaking.apis.*
import ordertaking.itaobuxiu.com.ordertaking.engine.Network

/**
 * Created by dev on 2017/11/25.
 */
class SelectAreaDialog(context: Context?) : Dialog(context, R.style.Dialog) {

    var provicesAdapter: CityDescAdapter? = null
    var cityAdapter: CityDescAdapter? = null
    var areaAdapter: CityDescAdapter? = null

    var currentProvice: CityDescData? = null
    var currentCity: CityDescData? = null
    var currentDis: CityDescData? = null

    var listener: OnCityDescALlClickListener? = null

    interface OnCityDescALlClickListener {
        fun onSelected(currentProvice: CityDescData?, currentCity: CityDescData?, currentDis: CityDescData?)
    }

    init {
        setContentView(R.layout.select_city_dialog_2)

        close.setOnClickListener {
            dismiss()
        }

        provicesAdapter = CityDescAdapter()
        provices.adapter = provicesAdapter
        provices.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        cityAdapter = CityDescAdapter()
        cities.adapter = cityAdapter
        cities.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        areaAdapter = CityDescAdapter()
        dis.adapter = areaAdapter
        dis.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        networkWrap(Network.create(UserApiService::class.java)?.getCityDatas(""))?.subscribe { result ->
            provicesAdapter?.updateData(result.data)
            p.isSelected = true
            c.isSelected = false
            q.isSelected = false
        }

        provicesAdapter?.setOnCitySelectedListener(object : OnCityDescItemClickListener {
            override fun citySelected(cityModel: CityDescData?) {
                currentProvice = cityModel
                if (cityModel?.children != null && !cityModel?.children?.isEmpty()!!) {
                    cityAdapter?.updateData(cityModel?.children!!)
                } else {
                    dismiss()
                    listener?.onSelected(currentProvice, currentCity, currentDis)
                }
                p.isSelected = false
                c.isSelected = true
                q.isSelected = false
            }
        })

        cityAdapter?.setOnCitySelectedListener(object : OnCityDescItemClickListener {
            override fun citySelected(cityModel: CityDescData?) {
                currentCity = cityModel
                if (cityModel?.children != null && !cityModel?.children?.isEmpty()!!) {
                    areaAdapter?.updateData(cityModel?.children!!)
                } else {
                    dismiss()
                    listener?.onSelected(currentProvice, currentCity, currentDis)
                }

                p.isSelected = false
                c.isSelected = false
                q.isSelected = true
            }
        })

        areaAdapter?.setOnCitySelectedListener(object : OnCityDescItemClickListener {
            override fun citySelected(cityModel: CityDescData?) {
                currentDis = cityModel
                dismiss()
                listener?.onSelected(currentProvice, currentCity, currentDis)
            }
        })
    }

    fun setOnCitySelectedListener(listener: OnCityDescALlClickListener) {
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