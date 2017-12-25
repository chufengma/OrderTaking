package ordertaking.itaobuxiu.com.ordertaking.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import ordertaking.itaobuxiu.com.ordertaking.R
import ordertaking.itaobuxiu.com.ordertaking.apis.HomeMarketPriceData
import ordertaking.itaobuxiu.com.ordertaking.apis.HomeSellerDataItem
import ordertaking.itaobuxiu.com.ordertaking.apis.IronInfoData
import org.jetbrains.anko.dip
import org.jetbrains.anko.find
import java.text.SimpleDateFormat

/**
 * Created by dev on 2017/11/22.
 */
class MarketPriceAllAdapter : RecyclerView.Adapter<VHMarketPriceAll>() {

    var data: List<HomeMarketPriceData>? = null

    fun updateData(data: List<HomeMarketPriceData>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: VHMarketPriceAll?, position: Int) {
        holder?.update(data!![position])
    }

    override fun getItemCount(): Int {
        return if(data == null) 0 else data!!.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): VHMarketPriceAll {
        return VHMarketPriceAll((LayoutInflater.from(
                parent?.context).inflate(R.layout.market_all_price_tab_layout, parent,
                false)))
    }

}

class VHMarketPriceAll(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var layout: LinearLayout? = null
    var price: TextView? = null
    var upDown: ImageView? = null
    var proPlace: TextView? = null
    var area: TextView? = null
    var materialSurface: TextView? = null

    init {
        layout = itemView.find(R.id.layout)
        price = itemView.find(R.id.price)
        upDown = itemView.find(R.id.upDown)
        proPlace = itemView.find(R.id.proPlace)
        area = itemView.find(R.id.area)
        materialSurface = itemView.find(R.id.materialSurface)
    }

    fun update(data: HomeMarketPriceData?) {
        when {
            data?.gains == 2 -> {
                price?.setTextColor(itemView.context.resources.getColor(R.color.main_red))
                upDown?.setImageResource(R.drawable.ic_price_up_2)
            }
            data?.gains == 1 -> {
                price?.setTextColor(itemView.context.resources.getColor(R.color.main_green))
                upDown?.setImageResource(R.drawable.ic_price_down_2)
            }
            else -> {
                price?.setTextColor(itemView.context.resources.getColor(R.color.main_yellow))
                upDown?.setImageResource(R.drawable.ic_price_equal_2)
            }
        }

        area?.text = data?.area
        price?.text = "￥${data?.price}"
        proPlace?.text = data?.proPlace
        materialSurface?.text = "${data?.material}/${data?.surface} ${data?.width} ${data?.tranStatus} ${data?.height} ${data?.ironType}"
    }

}

class IronInfoAdapter : RecyclerView.Adapter<VHIronInfo>() {

    var data: List<IronInfoData>? = null

    fun updateData(data: List<IronInfoData>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: VHIronInfo?, position: Int) {
        holder?.update(data!![position])
    }

    override fun getItemCount(): Int {
        return if(data == null) 0 else data!!.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): VHIronInfo {
        return VHIronInfo((LayoutInflater.from(
                parent?.context).inflate(R.layout.iron_info_tab_layout, parent,
                false)))
    }

}

class VHIronInfo(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var proPlace: TextView? = null
    var buyerStatus: TextView? = null
    var baseInfo: TextView? = null
    var spec: TextView? = null
    var weight: TextView? = null
    var updateTime: TextView? = null
    var num: TextView? = null

    init {
        proPlace = itemView.find(R.id.proPlace)
        buyerStatus = itemView.find(R.id.buyerStatus)
        baseInfo = itemView.find(R.id.baseInfo)
        spec = itemView.find(R.id.spec)
        weight = itemView.find(R.id.weight)
        updateTime = itemView.find(R.id.updateTime)
        num = itemView.find(R.id.num)
    }

    fun update(data: IronInfoData?) {
        if (data?.buyStatus == 3) {
            buyerStatus?.text = "已失效"
            buyerStatus?.setTextColor(itemView.context.resources.getColor(R.color.main_light_dark))
        } else if (data?.buyStatus == 2) {
            buyerStatus?.text = "已成交"
            buyerStatus?.setTextColor(itemView.context.resources.getColor(R.color.main_red))
        } else {
            buyerStatus?.text = "求购中"
            buyerStatus?.setTextColor(itemView.context.resources.getColor(R.color.main_green))
        }

        proPlace?.text = data?.proPlacesName
        num?.text = data?.sellNum

        var empty = ""
        var specText = data?.height + "*" + data?.width + "*" + data?.length
        if (data?.specifications == "") {
            spec?.text = specText
        } else {
            spec?.text = data?.specifications
        }
        updateTime?.text = SimpleDateFormat("MM-dd HH:mm").format(data?.updateTime)
        baseInfo?.text = "${data?.ironTypeName}  ${data?.materialName}   ${data?.surfaceName}"
        weight?.text = "${if (data?.numbers == "") empty  else data?.numbers + " " + data?.numberUnit} ${if (data?.weights == "") empty else data?.weights + " " +  data?.weightUnit}"
    }

}

class MarketPriceAdapter : RecyclerView.Adapter<VHMarketPrice>() {

    var data: List<HomeMarketPriceData>? = null

    fun updateData(data: List<HomeMarketPriceData>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: VHMarketPrice?, position: Int) {
        holder?.update(data!![position])
    }

    override fun getItemCount(): Int {
        return if(data == null) 0 else data!!.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): VHMarketPrice {
        return VHMarketPrice((LayoutInflater.from(
                parent?.context).inflate(R.layout.market_price_tab_layout, parent,
                false)))
    }

}

class VHMarketPrice(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var layout: RelativeLayout? = null
    var price: TextView? = null
    var upDown: ImageView? = null
    var proPlace: TextView? = null
    var area: TextView? = null
    var materialSurface: TextView? = null
    var otherDesc: TextView? = null

    init {
        layout = itemView.find(R.id.layout)
        price = itemView.find(R.id.price)
        upDown = itemView.find(R.id.upDown)
        proPlace = itemView.find(R.id.proPlace)
        area = itemView.find(R.id.area)
        materialSurface = itemView.find(R.id.materialSurface)
        otherDesc = itemView.find(R.id.otherDesc)
    }

    fun update(data: HomeMarketPriceData?) {
        when {
            data?.gains == 2 -> {
                layout?.setBackgroundResource(R.drawable.market_price_up_bg)
                upDown?.setImageResource(R.drawable.ic_price_up)
            }
            data?.gains == 0 -> {
                layout?.setBackgroundResource(R.drawable.market_price_down_bg)
                upDown?.setImageResource(R.drawable.ic_price_down)
            }
            else -> {
                layout?.setBackgroundResource(R.drawable.market_price_equal_bg)
                upDown?.setImageResource(R.drawable.ic_price_equal)
            }
        }

        area?.text = data?.area
        price?.text = "￥${data?.price}"
        proPlace?.text = data?.proPlace
        materialSurface?.text = "${data?.material}/${data?.surface}"
        otherDesc?.text = "${data?.width} ${data?.tranStatus} ${data?.height} ${data?.ironType}"
    }

}


class SellerTabAdapter : RecyclerView.Adapter<VH>() {

    var sellerTabData: List<List<HomeSellerDataItem>>? = null

    fun updateData(sellerTabData: List<List<HomeSellerDataItem>>) {
        this.sellerTabData = sellerTabData
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: VH?, position: Int) {
        holder?.setupData(sellerTabData!![position], position)
    }

    override fun getItemCount(): Int {
        return if(sellerTabData == null) 0 else sellerTabData!!.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): VH {
        return VH((LayoutInflater.from(
                parent?.context).inflate(R.layout.seller_tab_layout, parent,
                false)))
    }

}
class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun setupData(sellerTabData: List<HomeSellerDataItem>, startIndex: Int) {
        var layout = itemView.find<LinearLayout>(R.id.layout)
        layout.removeAllViews()
        for((index, value) in sellerTabData.withIndex()) {
            layout.addView(itemLayoutView(value, index + startIndex * 4 + 1))
        }
    }

    fun itemLayoutView(item: HomeSellerDataItem, index: Int): View {
        var itemLayoutItem = LayoutInflater.from(itemView.context).inflate(R.layout.seller_tab_item, null)
        var sellerTop = itemLayoutItem.find<ImageView>(R.id.seller_top)
        var sellerTopText = itemLayoutItem.find<TextView>(R.id.seller_top_text)
        var sellerName = itemLayoutItem.find<TextView>(R.id.company_name)
        var levelIconsLayout = itemLayoutItem.find<LinearLayout>(R.id.level_icons)
        var nums = itemLayoutItem.find<TextView>(R.id.level_num)
        sellerName.text = item.companyName
        nums.text = "${item.num}"
        setupTop(index, sellerTop, sellerTopText)
        setupLevels(levelIconsLayout, item.day)
        return itemLayoutItem
    }

    fun setupLevels(layout: LinearLayout, day: String) {
        var levelArray = day.split("-")
        var level = levelArray[0].toInt()
        var levelNum = levelArray[1].toInt()

        for(i in 0..(levelNum-1)) {
            var image: ImageView = ImageView(itemView.context)
            var params = LinearLayout.LayoutParams(itemView.context.dip(12), itemView.context.dip(12))
            image.layoutParams = params

            when(level) {
                1 -> image.setImageResource(R.drawable.ic_level_one)
                2 -> image.setImageResource(R.drawable.ic_level_two)
                3 -> image.setImageResource(R.drawable.ic_level_three)
            }

            layout.addView(image)
        }
    }

    fun setupTop(index: Int, imageView: ImageView, textView: TextView) {
        when (index) {
            1 -> {
                textView.visibility = View.GONE
                imageView.visibility = View.VISIBLE
                imageView.setImageResource(R.drawable.ic_seller_one)
            }
            2 -> {
                textView.visibility = View.GONE
                imageView.visibility = View.VISIBLE
                imageView.setImageResource(R.drawable.ic_seller_two)
            }
            3 -> {
                textView.visibility = View.GONE
                imageView.visibility = View.VISIBLE
                imageView.setImageResource(R.drawable.ic_seller_three)
            }
            else -> {
                textView.visibility = View.VISIBLE
                imageView.visibility = View.GONE
                textView.text = "$index"
            }
        }
    }

}

