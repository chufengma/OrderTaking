package ordertaking.itaobuxiu.com.ordertaking.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
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
import ordertaking.itaobuxiu.com.ordertaking.engine.GlideImageLoader
import ordertaking.itaobuxiu.com.ordertaking.engine.Network
import org.jetbrains.anko.dip
import java.text.SimpleDateFormat
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter
import com.sdsmdg.tastytoast.TastyToast
import ordertaking.itaobuxiu.com.ordertaking.apis.*


/**
 * Created by chufengma on 2017/11/19.
 */
class HomeFragment: Fragment() {

    var todayDataList: List<HomePriceData>? = null
    var monthDataList: List<HomePriceMonthData>? = null

    var sellerAdapter: SellerTabAdapter?= null
    var marketPriceAdapter: MarketPriceAdapter?= null
    var ironInfoAdapter: IronInfoAdapter?= null

    var refreshTodayData = {
        priceLoading.visibility = View.VISIBLE
        networkWrap(Network.create(HomeApiService::class.java)?.getPriceToday())
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
                        {  error ->
                            priceLoading.visibility = View.GONE
                            TastyToast.makeText(context, error.message, TastyToast.LENGTH_SHORT, TastyToast.ERROR)
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

    var refreshSellerData = {
        Network.create(HomeApiService::class.java)
                ?.getSellers()
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(
                        { result ->
                            Log.e("refreshSellerData",  "${daySellers.isSelected},${allSellers.isSelected}")
                            if (!allSellers.isSelected) {
                                if (result.data.day != null && result.data.day.size >= 8) {
                                    var allData = mutableListOf<List<HomeSellerDataItem>>()
                                    allData.add(result.data.day.subList(0, 4))
                                    allData.add(result.data.day.subList(4, 8))
                                    sellerAdapter?.updateData(allData)
                                }
                            } else {
                                if (result.data.all != null && result.data.all.size >= 8) {
                                    var allData = mutableListOf<List<HomeSellerDataItem>>()
                                    allData.add(result.data.all.subList(0, 4))
                                    allData.add(result.data.all.subList(4, 8))
                                    sellerAdapter?.updateData(allData)
                                }
                            }
                        },
                        {

                        }
                )
    }

    var refreshMarketPriceData = {
        Network.create(HomeApiService::class.java)
                ?.getMarketPrice()
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(
                        { result ->
                            marketPriceAdapter?.updateData(result.data)
                            if (result.data != null && result.data.size > 0) {
                                marketPriceUpdateTime.text = "最后更新于" + SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(result.data[0].createTime)
                            }
                        },
                        {

                        }
                )
    }


    var refreshIronInfoData = {
        Network.create(HomeApiService::class.java)
                ?.getIronInfos()
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(
                        { result ->
                            Log.e("refreshIronInfoData", result.data.size.toString())
                            ironInfoAdapter?.updateData(result.data)
                        },
                        {  error ->
                            error.printStackTrace()
                            Log.e("refreshIronInfoData", error.message)
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
        setupSellerLayout()
        setupMarketPriceLayout()
        setupIronInfoLayout()

        swipeRefresh.setColorSchemeResources(R.color.main_blue);
        swipeRefresh.setProgressBackgroundColorSchemeResource(android.R.color.white)
        swipeRefresh.setOnRefreshListener {
            TastyToast.makeText(context, "首页数据更新中，请稍后...", TastyToast.LENGTH_LONG, TastyToast.SUCCESS)
            refreshAllData()
            swipeRefresh.isRefreshing = false
        }

        refreshAllData()
    }

    private fun refreshAllData() {
        daySellers.performClick()
        todayPrice.performClick()
        refreshIronInfoData()
        refreshMarketPriceData()
    }

    private fun setupIronInfoLayout() {
        ironInfoAdapter = IronInfoAdapter()
        ironInfoRecycler.adapter = ironInfoAdapter
        ironInfoRecycler.layoutManager = GridLayoutManager(context, 2)

        ironInfoRecycler.isNestedScrollingEnabled = false
    }


    private fun setupMarketPriceLayout() {
        marketPriceAdapter = MarketPriceAdapter()
        marketPriceRecycler.adapter = marketPriceAdapter
        var layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        marketPriceRecycler.layoutManager = layoutManager

        allMarketPrice.setOnClickListener {
            startActivity(Intent(context, DayPriceAllActivity::class.java))
        }
    }

    private fun setupSellerLayout() {
        sellerAdapter = SellerTabAdapter()
        sellerRecycler.adapter = sellerAdapter
        var layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        sellerRecycler.layoutManager = layoutManager

        daySellers.setOnClickListener {
            daySellers.isSelected = true
            allSellers.isSelected = false
            refreshSellerData()
        }

        allSellers.setOnClickListener {
            allSellers.isSelected = true
            daySellers.isSelected = false
            refreshSellerData()
        }

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
        chart.setNoDataText("暂无数据")
        chart.setNoDataTextColor(Color.WHITE)

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