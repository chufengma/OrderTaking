package ordertaking.itaobuxiu.com.ordertaking.apis

/**
 * Created by dev on 2017/11/20.
 */

data class Response<T>(val code:String, val message:String, val data:T)

data class HomeAdsModelData(val code: String, val message: String, val data: HomeAdsModelDataReal)
data class HomeAdsModelDataReal(val defaultImg:String, val width:Float, val adList:List<HomeAdsModelDataItem>)
data class HomeAdsModelDataItem(val url:String)

data class HomePriceData(val currentPrice: String, val createTime: Long)

data class HomePriceMonthModel(val code: String, val message: String, val data: List<HomePriceMonthData>)
data class HomePriceMonthData(val endPrice: String, val logTime: Long)


data class HomeSellerModel(val code: String, val message: String, val data: HomeSellerDataAll)
data class HomeSellerDataAll(val all: List<HomeSellerDataItem>, val day: List<HomeSellerDataItem>)
data class HomeSellerDataItem(val companyName: String, val day: String, val num: Int)


data class HomeMarketPriceModel(val code: String, val message: String, val data: List<HomeMarketPriceData>)
data class HomeMarketPriceData(val area: String, val price: String, val proPlace: String, val updateTime: Long, val createTime: Long, val ironType: String, val material: String, val surface: String, val gains: Int, val tranStatus: String, val height:String, val width: String)


data class IronInfoModel(val code: String, val message: String, val data: List<IronInfoData>)
data class IronInfoData(val proPlacesName: String,
                        val buyStatus: Int,
                        val ironTypeName: String,
                        val surfaceName: String,
                        val materialName: String,
                        val specifications: String,
                        val sellNum: String,
                        val weightUnit: String,
                        val weights: String = "0",
                        val numberUnit: String,
                        val numbers: String = "0",
                        val height: String,
                        val width: String,
                        val length: String,
                        val updateTime: Long)
