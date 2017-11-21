package ordertaking.itaobuxiu.com.ordertaking.apis

/**
 * Created by dev on 2017/11/20.
 */
data class HomeAdsModel(val code: String, val message: String, val data: HomeAdsModelData)
data class HomeAdsModelData(val code: String, val message: String, val data: HomeAdsModelDataReal)
data class HomeAdsModelDataReal(val defaultImg:String, val width:Float, val adList:List<HomeAdsModelDataItem>)
data class HomeAdsModelDataItem(val url:String)


data class HomePriceModel(val code: String, val message: String, val data: List<HomePriceData>)
data class HomePriceData(val currentPrice: String, val createTime: Long)


data class HomePriceMonthModel(val code: String, val message: String, val data: List<HomePriceMonthData>)
data class HomePriceMonthData(val endPrice: String, val logTime: Long)