package ordertaking.itaobuxiu.com.ordertaking.apis

import retrofit2.http.Field
import java.io.Serializable

/**
 * Created by dev on 2017/11/20.
 */
data class Response<T>(val code:String, val message:String, val data:T)

data class UserLoginData(val authorization:String, val loginId: String, val user: UserBean)
data class UserBean(val id:String, val createTime: Long, val updateTIme:Long, val name: String, val realName: String, val mobile: String, val status: Int)

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

data class CityModel(val id: String = "",
                     val shortName: String = "",
                     val name: String = "",
                     val levelType: Int = 0,
                     val mergeName: String = "",
                     val mergeShortName: String = "",
                     val parentId: String = "") : Serializable
data class BaseIronInfo(val name: String, val id: String) : Serializable

data class UnitModel(val weightUnitId: String, val weightUnitCName: String, val weightUnitEName: String, val numUnitId: String, val numUnitCName: String, val numUnitEName: String) : Serializable

data class SuggestSpecModel(val height: String, val weight: String, val length: String)

data class PostRequestHistoryBean(
        val ironTypeId: String,                //类型：String  必有字段  备注：品类编号
        val ironTypeName: String,                //类型：String  必有字段  备注：品类
        val proPlacesId: String,                //类型：String  必有字段  备注：产地编号
        val proPlacesName: String,                //类型：String  必有字段  备注：产地
        val surfaceId: String,                //类型：String  必有字段  备注：表面编号
        val surfaceName: String,                //类型：String  必有字段  备注：表面名称
        val editStatus: Int,                //类型：String  必有字段  备注：编辑状态（0可编辑1不可编辑）
        val materialId: String,                //类型：String  必有字段  备注：材质编号
        val remark: String,                //类型：String  必有字段  备注：备注
        val numbers: String,                //类型：String  必有字段  备注：货物数量
        val numberUnitId: String,                //类型：String  必有字段  备注：数量单位编号
        val numberUnit: String,                //类型：String  必有字段  备注：数量单位
        val weights: String,                //类型：Number  必有字段  备注：重量
        val weightUnit: String,                //类型：String  必有字段  备注：重量单位
        val weightUnitId: String,                //类型：String  必有字段  备注：重量单位编号
        val specifications: String,                //类型：String  必有字段  备注：规格
        val materialName: String,                //类型：String  必有字段  备注：材质
        val locationId: String,                //类型：String  必有字段  备注：区域编号
        val locationName: String,                //类型：String  必有字段  备注：区域名
        val id : String,                //类型：String  必有字段  备注：编号
        val tolerance: String,                //类型：String  必有字段  备注：公差
        val height: String,                //类型：String  必有字段  备注：厚
        val updateUserId: String,                //类型：String  必有字段  备注：更新人编号
        val length: String,                //类型：String  必有字段  备注：长
        val updateTime: String,                //类型：Number  必有字段  备注：更新时间
        val timeLimit: String,                //类型：Number  必有字段  备注：时间
        val createTime: String,                //类型：Number  必有字段  备注：无
        val buyStatus: String,                //类型：String  必有字段  备注：求购状态（123，进行中/已成交/已失效）
        val width: String,                //类型：String  必有字段  备注：宽
        val status: String                //类型：String  必有字段  备注：状态（1启用）
) {

    fun toPostReuqestBean(): PostRequestBean {
        var bean: PostRequestBean = PostRequestBean()
        bean.ironType = BaseIronInfo(ironTypeName, ironTypeId)
        bean.materialModel = BaseIronInfo(materialName, materialId)
        bean.surfaceModel = BaseIronInfo(surfaceName, surfaceId)
        bean.proPlaceModel = BaseIronInfo(proPlacesName, proPlacesId)
        bean.location = CityModel(locationId, locationName)
        bean.remark = remark
        bean.length = length
        bean.width = width
        bean.height = height
        bean.specifications = specifications
        bean.tolerance = tolerance
        bean.timeLimit = timeLimit
        bean.numbers = numbers
        bean.weights = weights
        bean.unitModel = UnitModel(weightUnitId, weightUnit, weightUnit, numberUnitId, numberUnit, numberUnit)
        bean.time = updateTime.toLong()
        return bean
    }

}

data class PostRequestServerBean(
    var ironTypeId: String ? = "",
    var ironTypeName: String ? = "",
    var materialId: String? = "",
    var materialName: String? = "",
    var surfaceId: String? = "",
    var surfaceName: String? = "",
    var proPlacesId: String? = "",
    var proPlacesName: String? = "",
    var locationId: String? = "",
    var locationName: String? = "",
    var remark: String? = "",
    var length: String? = "",
    var width: String? = "",
    var height: String? = "",
    var specifications: String? = "",
    var tolerance: String? = "",
    var timeLimit: String? = "",
    var numbers: String? = "",
    var numberUnitId: String? = "",
    var numberUnit: String? = "",
    var weights: String? = "",
    var weightUnitId: String? = "",
    var weightUnit: String? = "",
    val appFlag: String = "4")

data class PostRequestBean(var localId: String = "",
                           var localCheck: Boolean = false,
                           var ironType: BaseIronInfo? = null,
                           var materialModel: BaseIronInfo? = null,
                           var surfaceModel: BaseIronInfo? = null,
                           var proPlaceModel: BaseIronInfo? = null,
                           var location: CityModel? = null,
                           var remark: String? = null,
                           var length: String? = null,
                           var width: String? = null,
                           var height: String? = null,
                           var specifications: String? = null,
                           var tolerance: String? = null,
                           var timeLimit: String? = null,
                           var numbers: String? = null,
                           var weights: String? = null,
                           var unitModel: UnitModel? = null,
                           var time: Long = 0
) : Serializable {
    override fun equals(other: Any?): Boolean {
        return(other as PostRequestBean).localId == localId
    }

    fun trans(): PostRequestServerBean {
        var request = this
        return PostRequestServerBean(
                request?.ironType?.id,
                request?.ironType?.name,
                request?.materialModel?.id,
                request?.materialModel?.name,
                request?.surfaceModel?.id,
                request?.surfaceModel?.name,
                request?.proPlaceModel?.id,
                request?.proPlaceModel?.name,
                request?.location?.id,
                request?.location?.shortName,
                request?.remark,
                request?.length,
                request?.width,
                request?.height,
                request?.specifications,
                request?.tolerance,
                (24*60*60*1000).toString(),
                request?.numbers,
                request?.unitModel?.numUnitId,
                request?.unitModel?.numUnitCName,
                request?.weights,
                request?.unitModel?.weightUnitId,
                request?.unitModel?.weightUnitCName
        )
    }
}

data class LocalRequests(var requests: MutableList<PostRequestBean>)

const val USER_LOGIN_INFO  = "user_login_info"
const val LOGIN_USER  = "login_user"
const val LOCAL_REQUESTS  = "local_requests"