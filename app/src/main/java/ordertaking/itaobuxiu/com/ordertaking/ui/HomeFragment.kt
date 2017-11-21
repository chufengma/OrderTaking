package ordertaking.itaobuxiu.com.ordertaking.ui

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jjoe64.graphview.DefaultLabelFormatter
import com.jjoe64.graphview.LabelFormatter
import com.youth.banner.BannerConfig
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_home.*
import ordertaking.itaobuxiu.com.ordertaking.R
import ordertaking.itaobuxiu.com.ordertaking.apis.HomeApiService
import ordertaking.itaobuxiu.com.ordertaking.engine.GlideImageLoader
import ordertaking.itaobuxiu.com.ordertaking.engine.Network
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.jjoe64.graphview.series.Series
import ordertaking.itaobuxiu.com.ordertaking.apis.HomePriceData
import ordertaking.itaobuxiu.com.ordertaking.apis.HomePriceMonthData
import org.jetbrains.anko.dip
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by chufengma on 2017/11/19.
 */
class HomeFragment: Fragment() {

    var todayDataList: List<HomePriceData>? = null
    var monthDataList: List<HomePriceMonthData>? = null

    private var labelFormatter: LabelFormatter = object : DefaultLabelFormatter() {
        override fun formatLabel(time: Double, isValueX: Boolean): String {
            return if (isValueX) {
                var data: HomePriceData? = todayDataList?.get(time.toInt())
                if (data != null) {
                    SimpleDateFormat("HH:mm").format(Date(data?.createTime!!))
                } else {
                    super.formatLabel(time, isValueX)
                }
            } else {
                super.formatLabel(time, isValueX)
            }
        }
    }

    private var monthlabelFormatter: LabelFormatter = object : DefaultLabelFormatter() {
        override fun formatLabel(time: Double, isValueX: Boolean): String {
            return if (isValueX) {
                var data: HomePriceMonthData? = monthDataList?.get(time.toInt())
                if (data != null) {
                    SimpleDateFormat("yyyy-MM-dd HH:mm").format(Date(data?.logTime!!))
                } else {
                    super.formatLabel(time, isValueX)
                }
            } else {
                super.formatLabel(time, isValueX)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_home, null)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupBanner()
        setupGripView()
    }

    private fun setupGripView() {
        graph.gridLabelRenderer.isHighlightZeroLines = false
        graph.gridLabelRenderer.textSize = context.dip(8).toFloat()
        graph.gridLabelRenderer.gridColor = Color.parseColor("#f8f8f8")
        graph.gridLabelRenderer.horizontalLabelsColor = Color.parseColor("#f8f8f8")
        graph.gridLabelRenderer.verticalLabelsColor = Color.parseColor("#f8f8f8")
        graph.gridLabelRenderer.gridColor = Color.parseColor("#81f8f8f8")
        graph.gridLabelRenderer.numHorizontalLabels = 10
        graph.gridLabelRenderer.labelFormatter = labelFormatter


        graphMonth.gridLabelRenderer.isHighlightZeroLines = false
        graphMonth.gridLabelRenderer.textSize = context.dip(8).toFloat()
        graphMonth.gridLabelRenderer.gridColor = Color.parseColor("#f8f8f8")
        graphMonth.gridLabelRenderer.horizontalLabelsColor = Color.parseColor("#f8f8f8")
        graphMonth.gridLabelRenderer.verticalLabelsColor = Color.parseColor("#f8f8f8")
        graphMonth.gridLabelRenderer.gridColor = Color.parseColor("#81f8f8f8")
        graphMonth.gridLabelRenderer.numHorizontalLabels = 3
        graphMonth.gridLabelRenderer.labelFormatter = monthlabelFormatter

        var refreshTodayData = {
            priceLoading.visibility = View.VISIBLE
            Network.create(HomeApiService::class.java)
                    ?.getPriceToday()
                    ?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(
                            { result ->
                                var todayData: MutableList<DataPoint> = mutableListOf();
                                todayDataList = result.data.subList(result.data.size - 20, result.data.size)
                                todayDataList!!.mapIndexedTo(todayData) { index, value -> DataPoint(index.toDouble(), value.currentPrice.toDouble()) }

                                var series = graph.tag
                                if (series == null) {
                                    val series = LineGraphSeries(todayData.toTypedArray())
                                    series.color = Color.parseColor("#ffffff")
                                    series.backgroundColor = Color.parseColor("#3ff8f8f8")
                                    series.isDrawBackground = true
                                    series.thickness = context.dip(1)

                                    graph.addSeries(series)

                                    graph.tag = series
                                } else {
                                    (series as LineGraphSeries<DataPoint>).resetData(todayData.toTypedArray())
                                }
                                priceLoading.visibility = View.GONE
                            },
                            {
                                priceLoading.visibility = View.GONE
                            }
                    )
        }

        var refreshMonthData = {
            priceLoading.visibility = View.VISIBLE
            Network.create(HomeApiService::class.java)
                    ?.getPriceMonth()
                    ?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(
                            { result ->
                                var todayData: MutableList<DataPoint> = mutableListOf()
                                monthDataList = result.data.subList(result.data.size - 21, result.data.size)
                                monthDataList!!.mapIndexedTo(todayData) { index, value ->
                                    DataPoint(index.toDouble(), value.endPrice.toDouble())
                                }

                                var series = graphMonth.tag
                                if (series == null) {
                                    val seriesMonth = LineGraphSeries(todayData.toTypedArray())
                                    seriesMonth.color = Color.parseColor("#ffffff")
                                    seriesMonth.backgroundColor = Color.parseColor("#3ff8f8f8")
                                    seriesMonth.isDrawBackground = true
                                    seriesMonth.thickness = context.dip(1)
                                    graphMonth.addSeries(seriesMonth)

                                    graphMonth.tag = seriesMonth
                                } else {
                                    (series as LineGraphSeries<DataPoint>).resetData(todayData.toTypedArray())
                                }
                                    priceLoading.visibility = View.GONE
                            },
                            {
                                priceLoading.visibility = View.GONE
                            }
                    )
        }

        todayPrice.setOnClickListener {
            todayPrice.isSelected = true
            monthPrice.isSelected = false
            graph.visibility = View.VISIBLE
            graphMonth.visibility = View.GONE
            refreshTodayData()
        }

        monthPrice.setOnClickListener {
            monthPrice.isSelected = true
            todayPrice.isSelected = false
            graph.visibility = View.GONE
            graphMonth.visibility = View.VISIBLE
            refreshMonthData()
        }


        todayPrice.performClick()
    }

    private fun setupBanner() {
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
        banner.setImageLoader(GlideImageLoader)
        banner.setImages(mutableListOf("http://tbxoss.oss-cn-hangzhou.aliyuncs.com/2017/11/20/20171120111338025417.jpg"))
        banner.start()

        Network.create(HomeApiService::class.java)
                ?.getHomeAdd()
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe (
                        { result ->
                            var imageList = result.data.data.adList.map {
                                it.url
                            }
                            banner.update(imageList)
                        },
                        { error -> Log.e("Network", "failed:" + error.message) }
                )
    }

}