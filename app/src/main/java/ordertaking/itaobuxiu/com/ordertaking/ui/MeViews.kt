package ordertaking.itaobuxiu.com.ordertaking.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ordertaking.itaobuxiu.com.ordertaking.R
import ordertaking.itaobuxiu.com.ordertaking.apis.ChargeData
import org.jetbrains.anko.find

/**
 * Created by dev on 2017/12/20.
 */
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
