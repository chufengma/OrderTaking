package ordertaking.itaobuxiu.com.ordertaking.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ordertaking.itaobuxiu.com.ordertaking.R
import ordertaking.itaobuxiu.com.ordertaking.apis.ChargeData
import ordertaking.itaobuxiu.com.ordertaking.apis.CityDescData
import ordertaking.itaobuxiu.com.ordertaking.apis.CityModel
import ordertaking.itaobuxiu.com.ordertaking.apis.StoreData
import org.jetbrains.anko.find

/**
 * Created by dev on 2017/12/20.
 */
interface OnStoreItemClickListener {
    fun onStore(store: StoreData?)
}

class StoreAdapter : RecyclerView.Adapter<VHStore>() {

    var data: List<StoreData>? = null
    var listener: OnStoreItemClickListener? = null

    fun updateData(data: List<StoreData>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: VHStore?, position: Int) {
        holder?.update(data!![position])
        holder?.itemView?.setOnClickListener {
            listener?.onStore(data?.get(position))
        }
    }

    override fun getItemCount(): Int {
        return if(data == null) 0 else data!!.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): VHStore {
        return VHStore((LayoutInflater.from(
                parent?.context).inflate(R.layout.simple_text_item_layout, parent,
                false)))
    }

    fun setOnStoreListener(listener: OnStoreItemClickListener) {
        this.listener = listener
    }

}

class VHStore(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var text: TextView? = null

    init {
        text = itemView.find(R.id.text)
        itemView.setBackgroundResource(R.drawable.simple_item_bg_color)
    }

    fun update(data: StoreData) {
        text?.text = data.notice
    }

}

interface OnCityDescItemClickListener {
    fun citySelected(cityModel: CityDescData?)
}

class CityDescAdapter : RecyclerView.Adapter<VHCityDesc>() {

    var data: List<CityDescData>? = null
    var listener: OnCityDescItemClickListener? = null

    fun updateData(data: List<CityDescData>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: VHCityDesc?, position: Int) {
        holder?.update(data!![position])
        holder?.itemView?.setOnClickListener {
            listener?.citySelected(data?.get(position))
        }
    }

    override fun getItemCount(): Int {
        return if(data == null) 0 else data!!.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): VHCityDesc {
        return VHCityDesc((LayoutInflater.from(
                parent?.context).inflate(R.layout.simple_text_item_layout, parent,
                false)))
    }

    fun setOnCitySelectedListener(listener: OnCityDescItemClickListener) {
        this.listener = listener
    }

}

class VHCityDesc(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var text: TextView? = null

    init {
        text = itemView.find(R.id.text)
        itemView.setBackgroundResource(R.drawable.simple_item_bg_color)
    }

    fun update(data: CityDescData) {
        text?.text = data.label
    }

}

interface OnChargeItemClickListener {
    fun onChargeSelected(chargeData: ChargeData?)
}

class ChargeAdapter : RecyclerView.Adapter<VHCharge>() {

    var data: List<ChargeData>? = null
    var listener: OnChargeItemClickListener? = null

    fun updateData(data: List<ChargeData>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: VHCharge?, position: Int) {
        holder?.update(data!![position])
        holder?.itemView?.setOnClickListener {
            listener?.onChargeSelected(data?.get(position))
        }
    }

    override fun getItemCount(): Int {
        return if(data == null) 0 else data!!.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): VHCharge {
        return VHCharge((LayoutInflater.from(
                parent?.context).inflate(R.layout.charge_text, parent,
                false)))
    }

    fun setOnChargeItemClickListener(listener: OnChargeItemClickListener) {
        this.listener = listener
    }

}

class VHCharge(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var text: TextView? = null

    init {
        text = itemView.find(R.id.text)
    }

    fun update(data: ChargeData) {
        text?.text = data.info
    }

}
