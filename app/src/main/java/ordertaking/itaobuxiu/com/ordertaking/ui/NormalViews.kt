package ordertaking.itaobuxiu.com.ordertaking.ui

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.TextView
import com.daimajia.swipe.SwipeLayout
import com.sdsmdg.tastytoast.TastyToast
import ordertaking.itaobuxiu.com.ordertaking.R
import ordertaking.itaobuxiu.com.ordertaking.apis.*
import org.jetbrains.anko.find
import java.text.SimpleDateFormat

/**
 * Created by dev on 2017/11/25.
 */
class IronBuyHistoryAdapter(var showDone: Boolean) : RecyclerView.Adapter<VHIronBuyHistory>() {

    var data: List<IronBuySellerOfferInfo>? = null

    fun updateData(data: List<IronBuySellerOfferInfo>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: VHIronBuyHistory?, position: Int) {
        holder?.update(data!![position])
        holder?.doneFlag?.visibility = if (position == 0 && showDone) View.VISIBLE else View.GONE
        holder?.downLine?.visibility = if (position == itemCount - 1) View.GONE else View.VISIBLE
    }

    override fun getItemCount(): Int {
        return if(data == null) 0 else data!!.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): VHIronBuyHistory {
        return VHIronBuyHistory((LayoutInflater.from(
                parent?.context).inflate(R.layout.iron_buy_history_item_layout, parent,
                false)))
    }
}

class VHIronBuyHistory(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var price: TextView? = null
    var unit: TextView? = null
    var to: TextView? = null
    var proPlace: TextView? = null
    var remark: TextView? = null
    var doneFlag: ImageView? = null
    var downLine: View?=null
    var time: TextView?=null

    init {
        price = itemView.find(R.id.price)
        unit = itemView.find(R.id.unitText)
        to = itemView.find(R.id.to)
        proPlace = itemView.find(R.id.proPlace)
        remark = itemView.find(R.id.remark)
        doneFlag = itemView.find(R.id.doneIcon)
        downLine = itemView.find(R.id.missLine)
        time = itemView.find(R.id.time)
    }

    fun update(data: IronBuySellerOfferInfo?) {
        price?.text = "￥${data?.offerPerPrice}"
        unit?.text = "元/${data?.baseUnit}"
        to?.text = if (data?.tolerance.isNullOrBlank()) "--" else data?.tolerance
        proPlace?.text = data?.offerPlaces
        remark?.text = data?.offerRemark

        time?.text = SimpleDateFormat("yyyy-MM-dd HH:mm").format(data?.createTime)
    }

}


interface OnOfferSellActionListener {
    fun onHistory(seller: IronBuySellerInfo)
    fun onChoose(seller: IronBuySellerInfo)
    fun onContact(seller: IronBuySellerInfo)
}

class IronBuyOfferAdapter(val buyStatus: Int) : RecyclerView.Adapter<VHIronBuyOffer>() {

    var data: List<IronBuySellerInfo>? = null
    var listener: OnOfferSellActionListener? = null

    fun updateData(data: List<IronBuySellerInfo>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: VHIronBuyOffer?, position: Int) {
        holder?.update(buyStatus, data!![position])
        holder?.choose?.setOnClickListener {
            listener?.onChoose(data!![position])
        }
        holder?.offerHistory?.setOnClickListener {
            listener?.onHistory(data!![position])
        }
        holder?.contact?.setOnClickListener {
            listener?.onContact(data!![position])
        }
    }

    override fun getItemCount(): Int {
        return if(data == null) 0 else data!!.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): VHIronBuyOffer {
        return VHIronBuyOffer((LayoutInflater.from(
                parent?.context).inflate(R.layout.offer_list_item_layout, parent,
                false)))
    }

    fun setActionListener(listener: OnOfferSellActionListener) {
        this.listener = listener
    }

}

class VHIronBuyOffer(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var baoView: View? = null
    var chengView: View? = null
    var companyName: TextView? = null
    var offerTime: TextView? = null
    var priceTitle: TextView? = null
    var price: TextView? = null
    var unit: TextView? = null
    var offerNew: View? = null
    var offerHolder: View? = null
    var offerHistory: View? = null
    var offerDoneFlag: View? = null
    var toTitle: TextView? = null
    var to: TextView? = null
    var proPlaceTitle: TextView? = null
    var proPlace: TextView? = null
    var remark: TextView? = null
    var remarkTitle: TextView? = null

    var actionLine: View? = null
    var actionLayout: View? = null
    var contact: View? = null
    var choose: View? = null

    var validLayout: View? = null
    var missText: View?=null

    init {
        baoView = itemView.find(R.id.baoBig)
        chengView = itemView.find(R.id.chengBig)
        companyName = itemView.find(R.id.companyName)
        offerTime = itemView.find(R.id.offerTime)
        priceTitle = itemView.find(R.id.priceTitle)
        price = itemView.find(R.id.price)
        unit = itemView.find(R.id.unit)
        offerNew = itemView.find(R.id.offerNew)
        offerHolder = itemView.find(R.id.priceHolder)
        offerHistory = itemView.find(R.id.historyOffers)
        offerDoneFlag = itemView.find(R.id.successFlag)
        toTitle = itemView.find(R.id.toTitle)
        to = itemView.find(R.id.to)
        proPlace = itemView.find(R.id.proPlace)
        proPlaceTitle = itemView.find(R.id.proPlaceTitle)
        remark = itemView.find(R.id.remark)
        remarkTitle = itemView.find(R.id.remarkTitle)

        actionLine = itemView.find(R.id.actionLine)
        actionLayout = itemView.find(R.id.actionLayout)
        contact = itemView.find(R.id.contactSell)
        choose = itemView.find(R.id.chooseSell)

        validLayout = itemView.find(R.id.validLayout)
        missText = itemView.find(R.id.missText)
    }

    fun update(buyInfoStatus:Int, data: IronBuySellerInfo?) {

        var initValidText = {
            companyName?.text = data?.companyName
            baoView?.visibility = if (data?.isGuaranteeUser == "1") View.VISIBLE else View.GONE
            chengView?.visibility = if (data?.isFaithUser == "1") View.VISIBLE else View.GONE

            offerTime?.text = SimpleDateFormat("yyyy-MM-dd HH:mm").format(data?.createTime)

            price?.text = data?.offerPerPrice
            unit?.text = "元/${data?.baseUnit}"

            to?.text = if (data?.tolerance.isNullOrBlank()) "--" else data?.tolerance
            proPlace?.text = data?.proInfo
            remark?.text = data?.offerRemark
            offerNew?.visibility = if (data?.hasNewOffer == "1") View.VISIBLE else View.GONE
        }

        offerHistory?.visibility = if (data?.ironSell == null || data?.ironSell!!.isEmpty()) View.GONE else View.VISIBLE

        if (data?.offerStatus != 4) {
            missText?.visibility = View.GONE
            validLayout?.visibility = View.VISIBLE
            initValidText()
        } else {
            missText?.visibility = View.VISIBLE
            validLayout?.visibility = View.GONE
        }

        var changeEnable = { enable: Boolean ->
            proPlaceTitle?.isEnabled = enable
            proPlace?.isEnabled = enable
            priceTitle?.isEnabled = enable
            price?.isEnabled = enable
            to?.isEnabled = enable
            toTitle?.isEnabled = enable
            unit?.isEnabled = enable
            remark?.isEnabled = enable
            remarkTitle?.isEnabled = enable
        }

        when(buyInfoStatus) {
            1 -> {
                changeEnable(true)

                actionLine?.visibility = View.VISIBLE
                actionLayout?.visibility = View.VISIBLE
                offerHolder?.visibility = View.VISIBLE
                offerDoneFlag?.visibility = View.GONE
            }
            2 -> {
                changeEnable(true)
                actionLine?.visibility = View.GONE
                actionLayout?.visibility = View.GONE

                if (data?.offerStatus == 2) {
                    offerHolder?.visibility = View.GONE
                    offerDoneFlag?.visibility = View.VISIBLE
                } else {
                    offerHolder?.visibility = View.VISIBLE
                    offerDoneFlag?.visibility = View.GONE
                }
            }
            else -> {
                changeEnable(false)

                actionLine?.visibility = View.GONE
                actionLayout?.visibility = View.GONE
                offerHolder?.visibility = View.VISIBLE
                offerDoneFlag?.visibility = View.GONE
            }
        }


    }
}


interface OnIronBuyInfoActionListener {
    fun onCopy(request: IronBuyInfo)
    fun onDelete(request: IronBuyInfo)
    fun onEdit(request: IronBuyInfo)
    fun onContact(request: IronBuyInfo)
    fun onItemClick(request: IronBuyInfo)
}


class IronBuyInfoAdapter : RecyclerView.Adapter<VHIronBuyInfo>() {

    var data: List<IronBuyInfo>? = null
    var listener: OnIronBuyInfoActionListener? = null

    fun updateData(data: List<IronBuyInfo>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: VHIronBuyInfo?, position: Int) {
        holder?.update(data!![position])
        holder?.copy?.setOnClickListener {
            this.listener?.onCopy(data!![position])
        }
        holder?.delete?.setOnClickListener {
            this.listener?.onDelete(data!![position])
        }
        holder?.edit?.setOnClickListener {
            this.listener?.onEdit(data!![position])
        }
        holder?.contact?.setOnClickListener {
            this.listener?.onContact(data!![position])
        }
        holder?.itemView?.setOnClickListener {
            this@IronBuyInfoAdapter.listener?.onItemClick(data!![position])
        }
    }

    override fun getItemCount(): Int {
        return if(data == null) 0 else data!!.size
    }

    fun getAllData(): List<IronBuyInfo>? {
        return data
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): VHIronBuyInfo {
        return VHIronBuyInfo((LayoutInflater.from(
                parent?.context).inflate(R.layout.iron_buy_item_layout, parent,
                false)))
    }

    fun setActionListener(listener: OnIronBuyInfoActionListener) {
        this.listener = listener
    }

}

class VHIronBuyInfo(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var ironType: TextView? = null
    var baseInfo: TextView? = null
    var proPlace: TextView? = null
    var surface: TextView? = null
    var to: TextView? = null
    var toLayout: View? = null
    var spec: TextView? = null
    var unit: TextView? = null
    var remark: TextView? = null
    var copy: View?= null
    var delete: View?= null
    var contact: View?= null
    var edit: View?= null
    var new: View?= null
    var updateTime: TextView?= null
    var doneText: TextView?= null
    var endText: TextView?= null
    var doingLayout: View?= null
    var offerNum: TextView?= null
    var blueDot: View?= null
    var iconTime: View?=null


    init {
        ironType = itemView.find(R.id.ironType)
        baseInfo = itemView.find(R.id.baseInfo)
        proPlace = itemView.find(R.id.proPlace)
        surface = itemView.find(R.id.surface)
        to = itemView.find(R.id.to)
        toLayout = itemView.find(R.id.toLayout)
        spec = itemView.find(R.id.spec)
        unit = itemView.find(R.id.unit)
        remark = itemView.find(R.id.remark)
        new = itemView.find(R.id.newIcon)
        updateTime = itemView.find(R.id.updateTime)
        doingLayout = itemView.find(R.id.doingLayout)
        offerNum = itemView.find(R.id.offerNum)
        doneText = itemView.find(R.id.doneText)
        endText = itemView.find(R.id.endText)
        blueDot = itemView.find(R.id.blueDot)
        iconTime = itemView.find(R.id.iconTime)


        copy = itemView.find(R.id.copy)
        delete = itemView.find(R.id.delete)
        edit = itemView.find(R.id.edit)
        contact = itemView.find(R.id.contactSell)
    }

    fun update(data: IronBuyInfo?) {
        ironType?.text = data?.ironTypeName
        baseInfo?.text = "${data?.materialName}"
        proPlace?.text = data?.proPlacesName
        surface?.text = data?.surfaceName
        to?.text = if (data?.tolerance.isNullOrBlank()) "--" else data?.tolerance
        spec?.text = if (data?.specifications.isNullOrBlank()) "${data?.height}*${data?.width}*${data?.length}" else data?.specifications
        remark?.text = data?.remark

        unit?.text = when {
            data?.numbers.isNullOrBlank() && !data?.weights.isNullOrBlank() -> "${data?.weights}${data?.weightUnit}"
            !data?.numbers.isNullOrBlank() && data?.weights.isNullOrBlank() -> "${data?.numbers}${data?.numberUnit}"
            else -> "${data?.numbers}${data?.numberUnit}/${data?.weights}${data?.weightUnit}"
        }

        new?.visibility = if (data?.hasNewoffer == 0) View.VISIBLE else View.GONE
        updateTime?.text = SimpleDateFormat("yyyy-MM-dd HH:mm").format(data?.updateTime)

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
        }

        when(data?.buyStatus) {
            1 -> {
                doneText?.visibility = View.GONE
                endText?.visibility = View.GONE
                doingLayout?.visibility = View.VISIBLE
                offerNum?.text = "${data?.ironSell?.validSell?.size}"
                changeEnable(true)
                copy?.visibility = View.VISIBLE
                delete?.visibility = View.VISIBLE
                contact?.visibility = View.GONE

                edit?.visibility = if (data?.editStatus == 0) View.VISIBLE else View.GONE
            }
            2 -> {
                doneText?.visibility = View.VISIBLE
                endText?.visibility = View.GONE
                doingLayout?.visibility = View.GONE
                doneText?.text = "￥${data?.ironSell?.validSell?.get(0)?.offerPrice}"
                changeEnable(true)

                copy?.visibility = View.VISIBLE
                delete?.visibility = View.GONE
                contact?.visibility = View.VISIBLE
                edit?.visibility = View.GONE
            }
            else -> {
                doneText?.visibility = View.GONE
                endText?.visibility = View.VISIBLE
                doingLayout?.visibility = View.GONE
                changeEnable(false)

                copy?.visibility = View.VISIBLE
                delete?.visibility = View.GONE
                contact?.visibility = View.GONE
                edit?.visibility = View.GONE
            }
         }


    }
}


interface OnHistoryPostRequestActionListener {
    fun onItemClick(request: PostRequestBean)
}

class HistoryPostRequestAdapter : RecyclerView.Adapter<VHHistoryPostRequest>() {

    var data: List<PostRequestBean>? = null
    var listener: OnHistoryPostRequestActionListener? = null

    fun updateData(data: List<PostRequestBean>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: VHHistoryPostRequest?, position: Int) {
        holder?.update(data!![position])
        holder?.downLine?.visibility = if (position == itemCount - 1) View.GONE else View.VISIBLE
        holder?.itemView?.setOnClickListener {
            listener?.onItemClick(data!![position])
        }
    }

    override fun getItemCount(): Int {
        return if(data == null) 0 else data!!.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): VHHistoryPostRequest {
        return VHHistoryPostRequest((LayoutInflater.from(
                parent?.context).inflate(R.layout.request_history_item_layout, parent,
                false)))
    }

    fun setActionListener(listener: OnHistoryPostRequestActionListener) {
        this.listener = listener
    }
}

class VHHistoryPostRequest(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var time: TextView? = null
    var ironType: TextView? = null
    var baseInfo: TextView? = null
    var proPlace: TextView? = null
    var surface: TextView? = null
    var to: TextView? = null
    var toLine: View? = null
    var spec: TextView? = null
    var unit: TextView? = null
    var location: TextView? = null
    var downLine: View? = null

    init {
        time = itemView.find(R.id.time)
        ironType = itemView.find(R.id.ironType)
        baseInfo = itemView.find(R.id.baseInfo)
        proPlace = itemView.find(R.id.proPlace)
        surface = itemView.find(R.id.surface)
        to = itemView.find(R.id.to)
        toLine = itemView.find(R.id.toLine)
        spec = itemView.find(R.id.spec)
        unit = itemView.find(R.id.unit)
        location = itemView.find(R.id.location)
        downLine = itemView.find(R.id.missLine)
    }

    fun update(data: PostRequestBean?) {
        location?.text = data?.location?.shortName
        ironType?.text = data?.ironType?.name
        baseInfo?.text = "${data?.materialModel?.name}"
        proPlace?.text = data?.proPlaceModel?.name
        surface?.text = data?.surfaceModel?.name

        toLine?.visibility = if (data?.tolerance.isNullOrBlank()) View.GONE else View.VISIBLE
        to?.visibility = if (data?.tolerance.isNullOrBlank()) View.GONE else View.VISIBLE
        to?.text = data?.tolerance
        spec?.text = if (data?.specifications.isNullOrBlank()) "${data?.height}*${data?.width}*${data?.length}" else data?.specifications

        unit?.text = when {
            data?.numbers.isNullOrBlank() && !data?.weights.isNullOrBlank() -> "${data?.weights}${data?.unitModel?.weightUnitCName}"
            !data?.numbers.isNullOrBlank() && data?.weights.isNullOrBlank() -> "${data?.numbers}${data?.unitModel?.numUnitCName}"
            else -> "${data?.numbers}${data?.unitModel?.numUnitCName}  ${data?.weights}${data?.unitModel?.weightUnitCName}"
        }
        time?.text = SimpleDateFormat("yyyy-MM-dd HH:mm").format(data?.time)
    }

}


interface OnPostRequestActionListener {
    fun onCopy(request: PostRequestBean)
    fun onDelete(request: PostRequestBean)
    fun onCheckChange(request: PostRequestBean, check: Boolean)
    fun onItemClick(request: PostRequestBean)
}

class PostRequestAdapter : RecyclerView.Adapter<VHPostRequest>() {

    var data: List<PostRequestBean>? = null
    var listener: OnPostRequestActionListener? = null

    fun updateData(data: List<PostRequestBean>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: VHPostRequest?, position: Int) {
        holder?.update(data!![position])
        holder?.line?.visibility = if (position == (itemCount - 1)) View.GONE else View.VISIBLE
        holder?.copy?.setOnClickListener {
            this.listener?.onCopy(data!![position])
        }
        holder?.delete?.setOnClickListener {
            this.listener?.onDelete(data!![position])
        }
        holder?.selectFlag?.setOnCheckedChangeListener(object: CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                data!![position].localCheck = isChecked
                this@PostRequestAdapter.listener?.onCheckChange(data!![position], isChecked)
            }
        })
        holder?.itemView?.setOnClickListener {
            if (holder.swipeLayout?.openStatus != SwipeLayout.Status.Close) {
                return@setOnClickListener
            }
            this@PostRequestAdapter.listener?.onItemClick(data!![position])
        }
    }

    override fun getItemCount(): Int {
        return if(data == null) 0 else data!!.size
    }

    fun getAllData(): List<PostRequestBean>? {
        return data
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): VHPostRequest {
        return VHPostRequest((LayoutInflater.from(
                parent?.context).inflate(R.layout.iron_request_item_layout, parent,
                false)))
    }

    fun setActionListener(listener: OnPostRequestActionListener) {
        this.listener = listener
    }

}

class VHPostRequest(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var location: TextView? = null
    var ironType: TextView? = null
    var baseInfo: TextView? = null
    var proPlace: TextView? = null
    var surface: TextView? = null
    var to: TextView? = null
    var toLayout: View? = null
    var spec: TextView? = null
    var unit: TextView? = null
    var remark: TextView? = null
    var line: View?= null
    var copy: View?= null
    var delete: View?= null
    var selectFlag: CheckBox?= null
    var swipeLayout: SwipeLayout?= null

    init {
        location = itemView.find(R.id.location)
        ironType = itemView.find(R.id.ironType)
        baseInfo = itemView.find(R.id.baseInfo)
        proPlace = itemView.find(R.id.proPlace)
        surface = itemView.find(R.id.surface)
        to = itemView.find(R.id.to)
        toLayout = itemView.find(R.id.toLayout)
        spec = itemView.find(R.id.spec)
        unit = itemView.find(R.id.unit)
        remark = itemView.find(R.id.remark)
        line = itemView.find(R.id.line)
        copy = itemView.find(R.id.copy)
        delete = itemView.find(R.id.delete)
        selectFlag = itemView.find(R.id.selectFlag)

        swipeLayout = itemView.find<SwipeLayout>(R.id.swipeLayout)
        swipeLayout?.showMode = SwipeLayout.ShowMode.PullOut
        swipeLayout?.addDrag(SwipeLayout.DragEdge.Left, itemView.findViewById(R.id.bottom_wrapper))
    }

    fun update(data: PostRequestBean?) {
        location?.text = data?.location?.shortName
        ironType?.text = data?.ironType?.name
        baseInfo?.text = "${data?.materialModel?.name}"
        proPlace?.text = data?.proPlaceModel?.name
        surface?.text = data?.surfaceModel?.name
        toLayout?.visibility = if (data?.tolerance.isNullOrBlank()) View.INVISIBLE else View.VISIBLE
        to?.text = data?.tolerance
        spec?.text = if (data?.specifications.isNullOrBlank()) "${data?.height}*${data?.width}*${data?.length}" else data?.specifications
        remark?.text = data?.remark

        unit?.text = when {
            data?.numbers.isNullOrBlank() && !data?.weights.isNullOrBlank() -> "${data?.weights}${data?.unitModel?.weightUnitCName}"
            !data?.numbers.isNullOrBlank() && data?.weights.isNullOrBlank() -> "${data?.numbers}${data?.unitModel?.numUnitCName}"
            else -> "${data?.numbers}${data?.unitModel?.numUnitCName}/${data?.weights}${data?.unitModel?.weightUnitCName}"
        }

        selectFlag?.isChecked = data?.localCheck!!
    }

}

class SpecAdapter : RecyclerView.Adapter<VHSpec>() {

    var data: List<SuggestSpecModel>? = null
    var listener: OnSpecItemClickListener? = null

    fun updateData(data: List<SuggestSpecModel>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: VHSpec?, position: Int) {
        holder?.update(data!![position])
        holder?.itemView?.setOnClickListener {
            listener?.specSelected(data?.get(position))
        }
    }

    override fun getItemCount(): Int {
        return if(data == null) 0 else data!!.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): VHSpec {
        return VHSpec((LayoutInflater.from(
                parent?.context).inflate(R.layout.suggest_text_item_layout, parent,
                false)))
    }

    fun setOnSpecSelectedListener(listener: OnSpecItemClickListener) {
        this.listener = listener
    }

}

interface OnSpecItemClickListener {
    fun specSelected(spec: SuggestSpecModel?)
}

interface OnCityItemClickListener {
    fun citySelected(cityModel: CityModel?)
}

class VHSpec(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var text: TextView? = null

    init {
        text = itemView.find(R.id.suggest)
    }

    fun update(data: SuggestSpecModel) {
        text?.text = "${data.height}*${data.weight}*${data.length}"
    }

}

class CityAdapter : RecyclerView.Adapter<VHCity>() {

    var data: List<CityModel>? = null
    var listener: OnCityItemClickListener? = null

    fun updateData(data: List<CityModel>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: VHCity?, position: Int) {
        holder?.update(data!![position])
        holder?.itemView?.setOnClickListener {
            listener?.citySelected(data?.get(position))
        }
    }

    override fun getItemCount(): Int {
        return if(data == null) 0 else data!!.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): VHCity {
        return VHCity((LayoutInflater.from(
                parent?.context).inflate(R.layout.simple_text_item_layout, parent,
                false)))
    }

    fun setOnCitySelectedListener(listener: OnCityItemClickListener) {
        this.listener = listener
    }

}

class VHCity(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var text: TextView? = null

    init {
        text = itemView.find(R.id.text)
        itemView.setBackgroundResource(R.drawable.simple_item_bg_color)
    }

    fun update(data: CityModel) {
        text?.text = data.shortName
    }

}


fun toast(context: Context, text: String, type: Int) {
    TastyToast.makeText(context, text, TastyToast.LENGTH_SHORT, type)
}
