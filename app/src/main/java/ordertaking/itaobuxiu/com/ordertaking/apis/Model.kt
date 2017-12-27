package ordertaking.itaobuxiu.com.ordertaking.apis

import android.view.inputmethod.BaseInputConnection
import java.io.Serializable

/**
 * Created by dev on 2017/11/20.
 */
data class Response<T>(val code: String, val message: String, val data: T)

data class UserLoginData(val authorization: String, val loginId: String, val user: UserBean)
data class UserBean(val id: String, val createTime: Long, val updateTIme: Long, val name: String, val realName: String, val mobile: String, val status: Int, val sellManTel: String)

data class UserInfo(
        var id: String?,                //类型：String?  必有字段  备注：无
        var createTime: String?,                //类型：String?  必有字段  备注：无
        var updateTime: String?,                //类型：String?  必有字段  备注：无
        var createUser: String?,                //类型：String?  必有字段  备注：无
        var createUserId: String?,                //类型：String?  必有字段  备注：无
        var updateUser: String?,                //类型：String?  必有字段  备注：无
        var updateUserId: String?,                //类型：String?  必有字段  备注：无
        var name: String?,                //类型：String?  必有字段  备注：无
        var realName: String?,                //类型：String?  必有字段  备注：无
        var password: String?,                //类型：String?  必有字段  备注：无
        var mobile: String?,                //类型：String?  必有字段  备注：无
        var status: String?,                //类型：String?  必有字段  备注：无
        var oldUserId: String?,                //类型：String?  必有字段  备注：无
        var roleList: String?,                //类型：String?  必有字段  备注：无
        var sellManName: String?,                //类型：String?  必有字段  备注：无
        var sellManTel: String?,                //类型：String?  必有字段  备注：无
        var buserInfo: UserProfile?,
        var proInfo: String
)

data class UserLevel(
    var level: String?,
    var day: String?
)

data class HomeAdsModelData(val code: String, val message: String, val data: HomeAdsModelDataReal)
data class HomeAdsModelDataReal(val defaultImg: String, val width: Float, val adList: List<HomeAdsModelDataItem>)
data class HomeAdsModelDataItem(val url: String)

data class HomePriceData(val currentPrice: String, val createTime: Long)

data class HomePriceMonthModel(val code: String, val message: String, val data: List<HomePriceMonthData>)
data class HomePriceMonthData(val endPrice: String, val logTime: Long, val startPrice: String, val minPrice: String, val maxPrice: String)


data class HomeSellerModel(val code: String, val message: String, val data: HomeSellerDataAll)
data class HomeSellerDataAll(val all: List<HomeSellerDataItem>, val day: List<HomeSellerDataItem>)
data class HomeSellerDataItem(val companyName: String, val day: String, val num: Int)


data class HomeMarketPriceModel(val code: String, val message: String, val data: List<HomeMarketPriceData>)
data class HomeMarketPriceData(val area: String, val price: String, val proPlace: String, val updateTime: Long, val createTime: Long, val ironType: String, val material: String, val surface: String, val gains: Int, val tranStatus: String, val height: String, val width: String)

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

data class CityModel(val id: String? = "",
                     val shortName: String? = "",
                     val name: String = "",
                     val levelType: Int = 0,
                     val mergeName: String = "",
                     val mergeShortName: String = "",
                     val parentId: String = "") : Serializable

data class BaseIronInfo(val name: String?, val id: String?) : Serializable

data class UnitModel(val weightUnitId: String?, val weightUnitCName: String?, val weightUnitEName: String?, val numUnitId: String?, val numUnitCName: String?, val numUnitEName: String?) : Serializable

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
        val id: String,                //类型：String  必有字段  备注：编号
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
        bean.time = createTime.toLong()
        return bean
    }

}

data class PostRequestServerBean(
        var ironTypeId: String? = "",
        var ironTypeName: String? = "",
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
                           var length: String? = "",
                           var width: String? = "",
                           var height: String? = "",
                           var specifications: String? = null,
                           var tolerance: String? = null,
                           var timeLimit: String? = null,
                           var numbers: String? = null,
                           var weights: String? = null,
                           var unitModel: UnitModel? = null,
                           var time: Long? = 0,
                           var id: String? = ""
) : Serializable {
    override fun equals(other: Any?): Boolean {
        return (other as PostRequestBean).localId == localId
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
                (24 * 60 * 60 * 1000).toString(),
                request?.numbers,
                request?.unitModel?.numUnitId,
                request?.unitModel?.numUnitCName,
                request?.weights,
                request?.unitModel?.weightUnitId,
                request?.unitModel?.weightUnitCName
        )
    }
}


data class IronBuyInfoData(
        var all: String?,                //类型：Number  必有字段  备注：所有求购数（仅在第一页是返回）
        var ing: String?,                //类型：Number  必有字段  备注：进行中求购数（仅在第一页是返回）
        var get: String?,                //类型：Number  必有字段  备注：成交求购数（仅在第一页是返回）
        var end: String?,                //类型：Number  必有字段  备注：失效求购数（仅在第一页是返回）
        var totalCount: String?,              //类型：Number  必有字段  备注：当前请求数据数
        var list: List<IronBuyInfo>?
)

data class IronBuyInfo(
        val ironTypeId: String?,                //类型：String?  必有字段  备注：品类编号
        val proPlacesId: String?,                //类型：String?  必有字段  备注：产地编号
        val surfaceName: String?,                //类型：String?  必有字段  备注：编号名称
        val numbers: String?,                //类型：Number  必有字段  备注：数量
        val editStatus: Int?,                //类型：String?  必有字段  备注：编辑状态（0可编辑 1不可编辑）
        val remark: String?,                //类型：String?  必有字段  备注：描述
        val appFlag: String?,                //类型：String?  必有字段  备注：来源（ 1:PC发布 2.H5 3.ios 4.android）
        val specifications: String?,                //类型：String?  必有字段  备注：规格
        val numberUnit: String?,                //类型：String?  必有字段  备注：数量单位
        val serveTime: Long?,                //类型：Number  必有字段  备注：服务器时间
        val numberUnitId: String?,                //类型：String?  必有字段  备注：数量单位编号
        val locationId: String?,                //类型：String?  必有字段  备注：区域编号
        val weightUnitId: String?,                //类型：String?  必有字段  备注：重量单位编号
        val id: String?,                //类型：String?  必有字段  备注：求购编号
        val proPlacesName: String?,                //类型：String?  必有字段  备注：产地名称
        val tolerance: String?,                //类型：String?  必有字段  备注：公差
        val height: String?,                //类型：String?  必有字段  备注：厚
        val locationName: String?,                //类型：String?  必有字段  备注：区域名称
        val length: String?,                //类型：String?  必有字段  备注：长
        val surfaceId: String?,                //类型：String?  必有字段  备注：表面编号
        val updateTime: Long?,                //类型：Number  必有字段  备注：更新时间
        val materialId: String?,                //类型：String?  必有字段  备注：材料编号
        val weights: String?,                //类型：Number  必有字段  备注：重量
        val timeLimit: String,                //类型：Number  必有字段  备注：有效时间
        val materialName: String?,                //类型：String?  必有字段  备注：材料名称
        val createTime: Long?,                //类型：Number  必有字段  备注：求购发布时间（用这个）
        val ironTypeName: String?,                //类型：String?  必有字段  备注：品类名称
        val width: String?,                //类型：String?  必有字段  备注：宽
        val buyStatus: Int?,                //类型：String?  必有字段  备注：求购状态（1进行中2成交3失效）
        val sellNum: String?,                //类型：Number  必有字段  备注：报价数（有几家公司与你竞价）
        val weightUnit: String?,               //类型：String?  必有字段  备注：重量单位
        val hasNewOffer: String?,
        val ironSell: IronSellerListInfo?

) : Serializable {
    fun toPostReuqestBean(): PostRequestBean {
        var bean = PostRequestBean()
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
        bean.time = updateTime?.toLong()
        bean.id = id
        return bean
    }
}

data class IronSellerListInfo(var missSell: List<IronBuySellerInfo>?, var validSell: List<IronBuySellerInfo>?) : Serializable

data class IronBuySellerInfo(
        var offerRemark: String?,                //类型：String  必有字段  备注：报价备注
        var offerPrice: String?,                //类型：Number  必有字段  备注：报价总价
        var companyName: String?,                //类型：String  必有字段  备注：报价公司名称
        var sellGetNum: String?,                //类型：Number  必有字段  备注：报价人报价中标次数
        var storeHouseName: String?,                //类型：String  必有字段  备注：报价人仓库地址
        var contactNum: String?,                //类型：String  必有字段  备注：报价人联系电话
        var baseUnit: String?,                //类型：String  必有字段  备注：报价计量单位
        var offerPlacesId: String?,                //类型：String  必有字段  备注：报价产地编号
        var hasNewOffer: String?,                //类型：String  必有字段  备注：是否新报价（0是1否）
        var contact: String?,                //类型：String  必有字段  备注：报价联系人
        var storeHousePlace: String?,                //类型：String  必有字段  备注：报价人仓库地址
        var fax: String?,                //类型：String  必有字段  备注：报价人传真
        var storeHouseId: String?,                //类型：String  必有字段  备注：仓库编号
        var day: String?,                //类型：String  必有字段  备注：报价人作为买家的活跃登记
        var tolerance: String?,                //类型：String  必有字段  备注：报价公差
        var offerUserId: String?,                //类型：String  必有字段  备注：报价人编号
        var QQ: String?,                //类型：String  必有字段  备注：报价人qq
        var ironBuyId: String?,                //类型：String  必有字段  备注：求购编号
        var address: String?,                //类型：String  必有字段  备注：报价人地址
        var baseUnitId: String?,                //类型：String  必有字段  备注：报价人编号
        var level: String?,                //类型：String  必有字段  备注：报价人作为卖家活跃等级
        var offerPerPrice: String?,                //类型：Number  必有字段  备注：报价单价
        var buyGetNum: String?,                //类型：Number  必有字段  备注：报价人发布求购成交数
        var offerStatus: Int?,                //类型：String  必有字段  备注：报价状态（1已报价2中标3未中标4放弃报价）
        var createTime: Long?,                //类型：Number  必有字段  备注：报价时间
        var isFaithUser: String?,                //类型：String  必有字段  备注：是否诚信商户（0否1是）
        var buyAllNum: String?,                //类型：Number  必有字段  备注：报价人发布的求购总数
        var ironSellId: String?,                //类型：String  必有字段  备注：报价编号
        var offerPlaces: String?,                //类型：String  必有字段  备注：报价产地
        var proInfo: String?,                //类型：String  必有字段  备注：报价人优惠信息
        var sellAllNum: String?,                //类型：Number  必有字段  备注：报价人总的报价次数
        var user: String?,                //类型：String  必有字段  备注：报价人编号
        var isGuaranteeUser: String?,                //类型：String  必有字段  备注：是否担保商户（0否1是）
        var ironSell: List<IronBuySellerOfferInfo>?
) : Serializable

data class IronBuySellerOfferInfo(
        var ironBuyId: String?,                //类型：String  必有字段  备注：求购编号
        var offerRemark: String?,                //类型：String  必有字段  备注：报价备注
        var offerPrice: String?,                //类型：Number  必有字段  备注：报价总价
        var baseUnitId: String?,                //类型：String  必有字段  备注：报价计量单位
        var offerPerPrice: String?,                //类型：Number  必有字段  备注：报价单价
        var offerStatus: String?,                //类型：String  必有字段  备注：报价状态（1已报价2中标3未中标4放弃报价）
        var baseUnit: String?,                //类型：String  必有字段  备注：报价计量单位
        var offerPlacesId: String?,                //类型：String  必有字段  备注：报价产地编号
        var hasNewOffer: String?,                //类型：String  必有字段  备注：是否新报价（0是1否）
        var createTime: Long?,                //类型：Number  必有字段  备注：报价时间
        var ironSellId: String?,                //类型：String  必有字段  备注：求购编号
        var offerPlaces: String?,                //类型：String  必有字段  备注：报价产地
        var tolerance: String?,                //类型：String  必有字段  备注：报价公差
        var offerUserId: String?                //类型：String  必有字段  备注：报价人编号
) : Serializable


data class SellerOfferInfo(
        var offer: String?,                //类型：Number  必有字段  备注：已报价（仅在第一页返回）
        var all: String?,                //类型：Number  必有字段  备注：所有（仅在第一页返回）
        var never: String?,                //类型：Number  必有字段  备注：未报价（仅在第一页返回）
        var deal: String?,                //类型：Number  必有字段  备注：已中标（仅在第一页返回）
        var totalCount: String?,                //类型：Number  必有字段  备注：当前请求下数据
        var miss: String?,           //类型：Number  必有字段  备注：未中标（仅在第一页返回）
        var list: List<SellerOfferInfoListItem>?
)

data class SellerOfferInfoListItem(
        var ironTypeId: String?,                //类型：String  必有字段  备注：品类编号
        var surfaceName: String?,                //类型：String  必有字段  备注：表面名称
        var companyName: String?,                //类型：String  必有字段  备注：求购人公司名称
        var numbers: String?,                //类型：Number  必有字段  备注：数量
        var specifications: String?,                //类型：String  必有字段  备注：规格
        var storeHouseName: String?,                //类型：String  必有字段  备注：求购人仓库地址
        var contactNum: String?,                //类型：String  必有字段  备注：求购人联系电话
        var numberUnitId: String?,                //类型：String  必有字段  备注：数量单位编号
        var contact: String?,                //类型：String  必有字段  备注：求购人联系人
        var id: String?,                //类型：String  必有字段  备注：求购编号
        var proPlacesName: String?,                //类型：String  必有字段  备注：产地名称
        var fax: String?,                //类型：String  必有字段  备注：传真
        var day: String?,                //类型：String  必有字段  备注：买家活跃等级
        var tolerance: String?,                //类型：String  必有字段  备注：公差
        var height: String?,                //类型：String  必有字段  备注：重量
        var QQ: String?,                //类型：String  必有字段  备注：无
        var level: String?,                //类型：String  必有字段  备注：卖家活跃等级
        var materialId: String?,                //类型：String  必有字段  备注：材质编号
        var weights: String?,                //类型：Number  必有字段  备注：重量
        var buyGetNum: Long?,                //类型：Number  必有字段  备注：求购成交数
        var offerStatus: Int?,                //类型：String  必有字段  备注：报价状态（1未报价2已报价3成交4放弃报价）
        var materialName: String?,                //类型：String  必有字段  备注：材质名称
        var ironTypeName: String?,                //类型：String  必有字段  备注：品类名称
        var buyAllNum: Long?,                //类型：Number  必有字段  备注：总求购数
        var buyStatus: String?,                //类型：String  必有字段  备注：求购状态（123，进行中/已成交/已失效）
        var sellAllNum: Long?,                //类型：Number  必有字段  备注：总报价数
        var sellNum: Long?,                //类型：Number  必有字段  备注：报价数量（竞争对手）
        var weightUnit: String?,                //类型：String  必有字段  备注：重量单位
        var proPlacesId: String?,                //类型：String  必有字段  备注：产地编号
        var sellGetNum: Long?,                //类型：Number  必有字段  备注：报价成交数
        var remark: String?,                //类型：String  必有字段  备注：备注
        var numberUnit: String?,                //类型：String  必有字段  备注：数量单位
        var serveTime: Long?,                //类型：Number  必有字段  备注：服务器时间
        var locationId: String?,                //类型：String  必有字段  备注：区域编号
        var weightUnitId: String?,                //类型：String  必有字段  备注：重量单位编号
        var storeHousePlace: String?,                //类型：String  必有字段  备注：仓库地址
        var storeHouseId: String?,                //类型：String  必有字段  备注：仓库编号
        var locationName: String?,                //类型：String  必有字段  备注：区域名称
        var address: String?,                //类型：String  必有字段  备注：求购人地址
        var length: String?,                //类型：String  必有字段  备注：长
        var surfaceId: String?,                //类型：String  必有字段  备注：表面编号
        var updateTime: Long?,                //类型：Number  必有字段  备注：更新时间
        var timeLimit: Long?,                //类型：Number  必有字段  备注：有效时间
        var createTime: Long?,                //类型：Number  必有字段  备注：求购时间
        var isFaithUser: String?,                //类型：String  必有字段  备注：是否诚信商户（0否1是）
        var width: String?,                //类型：String  必有字段  备注：宽
        var proInfo: String?,                //类型：String  必有字段  备注：求购商家优惠信息
        var user: String?,                //类型：String  必有字段  备注：求购人编号
        var isGuaranteeUser: String?,                //类型：String  必有字段  备注：是否担保商户（0否1是）
        var ironSell: List<IronBuySellerOfferInfo>?
) : Serializable

data class LocalRequests(var requests: MutableList<PostRequestBean>)

data class UserProfile(
        var id: String?,                //类型：String  必有字段  备注：无
        var companyName: String?,                //类型：String  必有字段  备注：无
        var regMoney: String?,                //类型：String  必有字段  备注：无
        var unit: String?,                //类型：String  必有字段  备注：无
        var contact: String?,                //类型：String  必有字段  备注：无
        var contactNum: String?,                //类型：String  必有字段  备注：无
        var fax: String?,                //类型：String  必有字段  备注：无
        var provinceId: String?,                //类型：String  必有字段  备注：无
        var provinceName: String?,                //类型：String  必有字段  备注：无
        var cityId: String?,                //类型：String  必有字段  备注：无
        var cityName: String?,                //类型：String  必有字段  备注：无
        var districtId: String?,                //类型：String  必有字段  备注：无
        var districtName: String?,                //类型：String  必有字段  备注：无
        var address: String?,                //类型：String  必有字段  备注：无
        var qq: String?,                //类型：String  必有字段  备注：无
        var sellerProfile: String?,                //类型：String  必有字段  备注：无
        var beBuserTime: String?,                //类型：Number  必有字段  备注：无
        var isFaithUser: String?,                //类型：String  必有字段  备注：无
        var isGuaranteeUser: String?,                //类型：String  必有字段  备注：无
        var beFaithUserTime: String?,                //类型：Number  必有字段  备注：无
        var beGuaranteeUserTime: String?,                //类型：Number  必有字段  备注：无
        var storeHouseId: String?,                //类型：String  必有字段  备注：无
        var storeHouseName: String?,                //类型：String  必有字段  备注：无
        var proInfo: String?,                //类型：String  必有字段  备注：无
        var flag: Boolean?,                //类型：Boolean  必有字段  备注：无
        var oldUserId: String?,                //类型：String  必有字段  备注：无
        var pass: String?,                //类型：String  必有字段  备注：无
        var allCer: String?,                //类型：String  必有字段  备注：无
        var bussinessLic: String?,                //类型：String  必有字段  备注：无
        var codeLic: String?,                //类型：String  必有字段  备注：无
        var financeLic: String?,                //类型：String  必有字段  备注：无
        var cover: String?,                //类型：String  必有字段  备注：无
        var userId: String?,                //类型：String  必有字段  备注：无
        var salesManMobile: String?,                //类型：String  必有字段  备注：无
        var salesManId: String?,                //类型：Number  必有字段  备注：无
        var salesManName: String?,                //类型：String  必有字段  备注：无
        var buserMobile: String?                //类型：String  必有字段  备注：无
)


data class ChargeData(var id: String, var info: String, var addTime: String)

data class ScopeData(var proPlace: List<BaseIronInfo>?, var surface: List<BaseIronInfo>?, var material: List<BaseIronInfo>?, var ironType: List<BaseIronInfo>?)

data class CityDescData(var label: String, var value: String, var children: List<CityDescData>?)

data class StoreData(var notice: String, var id: String)

data class BuyerData(var todayBuyRate: Double, var todayBuyTotal: Int, var todayBuyValid: Int, var todayBuyMiss:Int,
                     var todaySellRate: Double, var todaySellTotal: Int, var todaySellGet: Int, var todaySellMiss: Int,
                     var monthSellRate: Double, var monthSellTotal: Int, var monthSellGet: Int, var monthSellMiss: Int,
                     var allSellRate: Double, var allSellTotal: Int, var allSellGet: Int, var allSellMiss: Int, var cooperation : List<BuyerCompany>
)

data class SellerData(var todaySellQuote: Int, var todaySellValid: Int, var todaySellMiss: Int, var todaySellRate:Double,
                      var monthSellQuote: Int, var monthSellValid: Int, var monthSellMiss: Int, var monthSellRate:Double,
                      var sellQuote: Int, var sellValid: Int, var sellMiss: Int, var sellRate:Double,
                      var todayOfferQuote: Int, var todayOfferGet: Int, var todayOfferNever: Int, var todayOfferRate:Double,
                      var monthOfferAll: Int, var monthOfferGet: Int, var monthOfferNot: Int, var monthOfferRate:Double,
                      var offerAll: Int, var offerGet: Int, var offerNot: Int, var offerRate:Double
)


data class BuyerCompany(
        var companyName: String, var coopLveve: String
)

data class Youhui(var id: String?, var info: String?, var addTime: String?)

const val USER_LOGIN_INFO = "user_login_info"
const val LOGIN_USER = "login_user"
const val LAST_USER_MOBILE = "last_user_mobile"
const val LOCAL_REQUESTS = "local_requests"