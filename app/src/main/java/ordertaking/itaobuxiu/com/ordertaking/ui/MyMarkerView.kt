package ordertaking.itaobuxiu.com.ordertaking.ui


import android.content.Context
import android.view.View
import android.widget.TextView

import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF

import java.text.SimpleDateFormat

import ordertaking.itaobuxiu.com.ordertaking.R
import ordertaking.itaobuxiu.com.ordertaking.apis.HomePriceData
import ordertaking.itaobuxiu.com.ordertaking.apis.HomePriceMonthData
import org.jetbrains.anko.dip

/**
 * Custom implementation of the MarkerView.
 *
 * @author Philipp Jahoda
 */
class MyMarkerView(context: Context, layoutResource: Int) : MarkerView(context, layoutResource) {

    private val title: TextView = findViewById(R.id.title) as TextView
    private val content: TextView = findViewById(R.id.data) as TextView
    private val kaipan: TextView = findViewById(R.id.kaipan) as TextView
    private val zuigao: TextView = findViewById(R.id.zuigao) as TextView
    private val zuidi: TextView = findViewById(R.id.zuidi) as TextView
    private val shoupan: TextView = findViewById(R.id.shoupan) as TextView

    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        if (e!!.data is HomePriceData) {
            title.text = SimpleDateFormat("HH:mm").format((e.data as HomePriceData).createTime)
            content.text = "当前：￥" + (e.data as HomePriceData).currentPrice
            kaipan.visibility = View.GONE
            zuigao.visibility = View.GONE
            zuidi.visibility = View.GONE
            shoupan.visibility = View.GONE
        } else {
            title.text = SimpleDateFormat("yyyy-MM-dd HH:mm").format((e.data as HomePriceMonthData).logTime)
            content.text = "当日：￥" + (e.data as HomePriceMonthData).endPrice
            kaipan.text = "开盘：￥" + (e.data as HomePriceMonthData).startPrice
            zuigao.text = "最高：￥" + (e.data as HomePriceMonthData).maxPrice
            zuidi.text = "最低：￥" + (e.data as HomePriceMonthData).minPrice
            shoupan.text = "收盘：￥" + (e.data as HomePriceMonthData).endPrice
        }
        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF {
        return MPPointF((-(width / 2)).toFloat(), (-height).toFloat() - context.dip(10))
    }
}