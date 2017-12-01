package ordertaking.itaobuxiu.com.ordertaking.ui

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import com.daimajia.swipe.SwipeLayout
import com.sdsmdg.tastytoast.TastyToast
import ordertaking.itaobuxiu.com.ordertaking.R
import ordertaking.itaobuxiu.com.ordertaking.apis.CityModel
import ordertaking.itaobuxiu.com.ordertaking.apis.IronBuyInfo
import ordertaking.itaobuxiu.com.ordertaking.apis.PostRequestBean
import ordertaking.itaobuxiu.com.ordertaking.apis.SuggestSpecModel
import org.jetbrains.anko.find
import java.text.SimpleDateFormat

/**
 * Created by dev on 2017/11/25.
 */


interface OnIronBuyInfoActionListener {
    fun onCopy(request: IronBuyInfo)
    fun onDelete(request: IronBuyInfo)
    fun onEdit(request: IronBuyInfo)
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
    var edit: View?= null
    var new: View?= null
    var updateTime: TextView?= null

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
        copy = itemView.find(R.id.copy)
        delete = itemView.find(R.id.delete)
        edit = itemView.find(R.id.edit)
        new = itemView.find(R.id.newIcon)
        updateTime = itemView.find(R.id.updateTime)
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
        updateTime?.text = SimpleDateFormat("yyyy-MM-dd HH:mm").format(data?.createTime)
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
