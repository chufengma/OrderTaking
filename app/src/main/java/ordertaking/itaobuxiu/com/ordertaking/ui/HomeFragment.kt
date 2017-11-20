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
import com.jjoe64.graphview.Viewport
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter
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
import ordertaking.itaobuxiu.com.ordertaking.apis.HomePriceData
import org.jetbrains.anko.custom.asyncResult
import org.jetbrains.anko.dip
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by chufengma on 2017/11/19.
 */
class HomeFragment: Fragment() {

    var todayDataList: List<HomePriceData>? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_home, null)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupBanner()
        setupGripView()
    }

    private fun setupGripView() {
        val series = LineGraphSeries(arrayOf(
                DataPoint(0.0, 1000.0),
                DataPoint(2.0, 5000.0),
                DataPoint(4.0, 3000.0),
                DataPoint(8.0, 2000.0),
                DataPoint(12.0, 6000.0),
                DataPoint(16.0, 5000.0),
                DataPoint(18.0, 3000.0),
                DataPoint(24.0, 2000.0),
                        DataPoint(18.0, 3000.0),
                DataPoint(24.0, 2000.0)))
        series.color = Color.parseColor("#ffffff")
        series.backgroundColor = Color.parseColor("#3ff8f8f8")
        series.isDrawBackground = true
        series.thickness = context.dip(1)

        graph.addSeries(series)
        graph.gridLabelRenderer.isHighlightZeroLines = false
        graph.gridLabelRenderer.textSize = context.dip(8).toFloat()
        graph.gridLabelRenderer.gridColor = Color.parseColor("#f8f8f8")
        graph.gridLabelRenderer.horizontalLabelsColor = Color.parseColor("#f8f8f8")
        graph.gridLabelRenderer.verticalLabelsColor = Color.parseColor("#f8f8f8")
        graph.gridLabelRenderer.gridColor = Color.parseColor("#81f8f8f8")
        graph.gridLabelRenderer.numHorizontalLabels = 10

        todayPrice.isSelected = true
//
        graph.gridLabelRenderer.labelFormatter = object : DefaultLabelFormatter() {

            override fun formatLabel(time: Double, isValueX: Boolean): String {
                return if (isValueX) {
                    var data: HomePriceData? = todayDataList?.get(time.toInt() * 2)
                    SimpleDateFormat("HH:mm").format(Date(data?.createTime))
                } else {
                    super.formatLabel(time, isValueX)
                }
            }

        }

//        graph.gridLabelRenderer.labelFormatter = DateAsXAxisLabelFormatter(getActivity())


        todayPrice.setOnClickListener {
            todayPrice.isSelected = true
            monthPrice.isSelected = false
        }

        monthPrice.setOnClickListener {
            monthPrice.isSelected = true
            todayPrice.isSelected = false
        }
        Network.create(HomeApiService::class.java)
                ?.getPriceToday()
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe { result ->
                    var index = -1
                    todayDataList = result.data.subList(result.data.size - 20, result.data.size)
                    var todayData = todayDataList?.map {
                        index++
                        DataPoint(index, it.currentPrice.toDouble())
                    }.toTypedArray()
                    series.resetData(todayData)
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

}