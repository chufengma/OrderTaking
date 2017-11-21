package ordertaking.itaobuxiu.com.ordertaking.ui

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.youth.banner.BannerConfig
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_home.*
import ordertaking.itaobuxiu.com.ordertaking.R
import ordertaking.itaobuxiu.com.ordertaking.apis.HomeApiService
import ordertaking.itaobuxiu.com.ordertaking.engine.GlideImageLoader
import ordertaking.itaobuxiu.com.ordertaking.engine.Network
import ordertaking.itaobuxiu.com.ordertaking.apis.HomePriceData
import ordertaking.itaobuxiu.com.ordertaking.apis.HomePriceMonthData
import org.jetbrains.anko.dip
import java.text.SimpleDateFormat
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter


/**
 * Created by chufengma on 2017/11/19.
 */
class HomeFragment: Fragment() {

    var todayDataList: List<HomePriceData>? = null
    var monthDataList: List<HomePriceMonthData>? = null

    var refreshTodayData = {
        priceLoading.visibility = View.VISIBLE
        Network.create(HomeApiService::class.java)
                ?.getPriceToday()
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(
                        { result ->
                            var todayData: MutableList<Entry> = mutableListOf();
                            todayDataList = result.data.subList(result.data.size - 20, result.data.size)
                            todayDataList!!.mapIndexedTo(todayData) { index, value -> Entry(index.toFloat(), value.currentPrice.toFloat(), value) }

                            configChart(chart, todayData)
                            chart.xAxis.valueFormatter = object: DefaultAxisValueFormatter(0) {
                                override fun getFormattedValue(value: Float, axis: AxisBase?): String {
                                    var data: HomePriceData? = todayDataList?.get(value.toInt())
                                    return SimpleDateFormat("HH:mm").format(data?.createTime)
                                }
                            }
                            priceLoading.visibility = View.GONE
                            chart.invalidate()
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
                            Log.e("TEST4", "success")
                            var todayData: MutableList<Entry> = mutableListOf();
                            monthDataList = result.data.subList(result.data.size - 20, result.data.size)
                            monthDataList!!.mapIndexedTo(todayData) { index, value -> Entry(index.toFloat(), value.endPrice.toFloat(), value) }

                            configChart(chartMonth, todayData)
                            chartMonth.xAxis.valueFormatter = object: DefaultAxisValueFormatter(0) {
                                override fun getFormattedValue(value: Float, axis: AxisBase?): String {
                                    var data: HomePriceMonthData? = monthDataList?.get(value.toInt())
                                    return SimpleDateFormat("yyyy-MM-dd").format(data?.logTime)
                                }
                            }
                            chartMonth.xAxis.setLabelCount(4, false)

                            priceLoading.visibility = View.GONE
                            chartMonth.invalidate()
                        },
                        {
                            Log.e("TEST4", "error")
                            priceLoading.visibility = View.GONE
                        }
                )
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
        todayPrice.setOnClickListener {
            todayPrice.isSelected = true
            monthPrice.isSelected = false
            chart.visibility = View.VISIBLE
            chartMonth.visibility = View.GONE
            refreshTodayData()
        }

        monthPrice.setOnClickListener {
            monthPrice.isSelected = true
            todayPrice.isSelected = false
            chart.visibility = View.GONE
            chartMonth.visibility = View.VISIBLE
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


    private fun configChart(chart: LineChart, data: List<Entry>): LineChart {
        var dataSet = LineDataSet(data, "")
        val lineData = LineData(dataSet)
        chart.data = lineData
        chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        chart.axisRight.isEnabled = false

        lineData.setDrawValues(false)
        chart.xAxis.gridColor = Color.parseColor("#f8f8f8")
        chart.xAxis.textColor = Color.parseColor("#f8f8f8")
        chart.axisLeft.gridColor = Color.parseColor("#f8f8f8")
        chart.axisLeft.textColor = Color.parseColor("#f8f8f8")
        chart.xAxis.axisLineColor = Color.parseColor("#f8f8f8")
        chart.setScaleEnabled(false)

        dataSet.axisDependency = YAxis.AxisDependency.LEFT
        dataSet.setDrawCircles(false)
        dataSet.lineWidth = 1f
        dataSet.color = Color.WHITE

        chart.description.isEnabled = false

        dataSet.setDrawFilled(true)
        dataSet.fillDrawable = ContextCompat.getDrawable(getContext(), R.drawable.char_under_line_bg)

        chart.legend.isEnabled = false
        chart.setDrawMarkers(true)
        chart.setTouchEnabled(true)

        val mv = MyMarkerView(getContext(), R.layout.marker_view)
        mv.chartView = chart
        chart.marker = mv

        mv.elevation = context.dip(10).toFloat()

        return chart
    }

}