package ordertaking.itaobuxiu.com.ordertaking.apis

import android.util.Log
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ordertaking.itaobuxiu.com.ordertaking.engine.MainApplication
import retrofit2.http.*


/**
 * Created by dev on 2017/11/20.
 */
interface HomeApiService {

    @GET("/api/ad/findByGroupId?groupId=3202")
    fun getHomeAdd(): Observable<Response<HomeAdsModelData>>


    @GET("/api/main/photoLME")
    fun getPriceToday(): Observable<Response<List<HomePriceData>>>


    @GET("/api/main/threeMonthLME")
    fun getPriceMonth(): Observable<HomePriceMonthModel>


    @GET("/api/main/countSellActive")
    fun getSellers(): Observable<HomeSellerModel>

    @GET("/api/main/queryIronNew")
    fun getMarketPrice(): Observable<HomeMarketPriceModel>

    @GET("/api/main/queryIronInfo")
    fun getIronInfos(): Observable<IronInfoModel>
}

interface UserApiService {

    @FormUrlEncoded
    @POST("/login/userLogin")
    fun login(@Field("mobile") mobile: String , @Field("password") password: String): Observable<Response<UserLoginData>>

    @FormUrlEncoded
    @POST("/api/user/findCurrentUser")
    fun getUserInfo(@Field("mobile") mobile: String) : Observable<Response<UserInfo>>
}

interface IronRequestService {

    @GET("/api/query/findProvince")
    fun getProvices(): Observable<Response<List<CityModel>>>


    @GET("/api/query/findCity")
    fun getCity(@Query("id") id: String): Observable<Response<List<CityModel>>>


    @GET("/api/query/findIronTypes")
    fun getIronTypes(): Observable<Response<List<BaseIronInfo>>>

    @GET("/api/query/findMaterials")
    fun getMaterials(): Observable<Response<List<BaseIronInfo>>>

    @GET("/api/query/findProPlaces")
    fun getProPlaces(): Observable<Response<List<BaseIronInfo>>>

    @GET("/api/query/findSurFace")
    fun getSurfaces(): Observable<Response<List<BaseIronInfo>>>

    @GET("/api/query/findIronAndUnitByIronId")
    fun getUnits(@Query("ironId") ironId: String): Observable<Response<UnitModel>>

    @GET("/api/query/findIronAndSurfaceAndSpecificationlist")
    fun getSuggestSpec(@Query("surface") surface: String, @Query("ironType") ironType: String): Observable<Response<List<SuggestSpecModel>>>

    @FormUrlEncoded
    @POST("/demands/ironBuy/saveIronBuyList")
    fun postAllRequest(@Field("ironBuyInfos") ironBuyInfos: String ?) : Observable<Response<Object>>

    @FormUrlEncoded
    @POST("/demands/ironBuy/saveAndUpdateIronBuy")
    fun postRequest(@Field("ironTypeId") ironTypeId: String ?,
                    @Field("ironTypeName") ironTypeName: String ?,
                    @Field("materialId") materialId: String?,
                    @Field("materialName") materialName: String?,
                    @Field("surfaceId") surfaceId: String?,
                    @Field("surfaceName") surfaceName: String?,
                    @Field("proPlacesId") proPlacesId: String?,
                    @Field("proPlacesName") proPlacesName: String?,
                    @Field("locationId") locationId: String?,
                    @Field("locationName") locationName: String?,
                    @Field("remark") remark: String?,
                    @Field("length") length: String?,
                    @Field("width") width: String?,
                    @Field("height") height: String?,
                    @Field("specifications") specifications: String?,
                    @Field("tolerance") tolerance: String?,
                    @Field("timeLimit") timeLimit: String?,
                    @Field("numbers") numbers: String?,
                    @Field("numberUnitId") numberUnitId: String?,
                    @Field("numberUnit") numberUnit: String?,
                    @Field("weights") weights: String?,
                    @Field("weightUnitId") weightUnitId: String?,
                    @Field("weightUnit") weightUnit: String?,
                    @Field("appFlag") appFlag: String = "4") : Observable<Response<Object>>

    @FormUrlEncoded
    @POST("/demands/ironBuy/saveAndUpdateIronBuy")
    fun editIronBuy(@Field("ironTypeId") ironTypeId: String ?,
                    @Field("ironTypeName") ironTypeName: String ?,
                    @Field("materialId") materialId: String?,
                    @Field("materialName") materialName: String?,
                    @Field("surfaceId") surfaceId: String?,
                    @Field("surfaceName") surfaceName: String?,
                    @Field("proPlacesId") proPlacesId: String?,
                    @Field("proPlacesName") proPlacesName: String?,
                    @Field("locationId") locationId: String?,
                    @Field("locationName") locationName: String?,
                    @Field("remark") remark: String?,
                    @Field("length") length: String?,
                    @Field("width") width: String?,
                    @Field("height") height: String?,
                    @Field("specifications") specifications: String?,
                    @Field("tolerance") tolerance: String?,
                    @Field("timeLimit") timeLimit: String?,
                    @Field("numbers") numbers: String?,
                    @Field("numberUnitId") numberUnitId: String?,
                    @Field("numberUnit") numberUnit: String?,
                    @Field("weights") weights: String?,
                    @Field("weightUnitId") weightUnitId: String?,
                    @Field("weightUnit") weightUnit: String?,
                    @Field("id") id: String? = null,
                    @Field("status") status: String? = "1",
                    @Field("appFlag") appFlag: String = "4") : Observable<Response<Object>>

    @GET("/demands/ironBuy/queryIronBuyInfo")
    fun getRequestHistory(): Observable<Response<List<PostRequestHistoryBean>>>

    @FormUrlEncoded
    @POST("/demands/ironBuy/queryIronBuyAllInfo")
    fun getIronBuyInfo(@Field("currentPage") currentPage: Int, @Field("pageSize") pageSize: Int,  @Field("buyStatus") buyStatus: Int, @Field("today") today: Int) : Observable<Response<IronBuyInfoData>>

    @FormUrlEncoded
    @POST("/demands/ironBuy/getIronSell")
    fun chooseSeller(@Field("ironBuyId") ironBuyId: String, @Field("ironSellId") ironSellId: String): Observable<Response<Object>>
}

interface IronBuyOfferService {
    @FormUrlEncoded
    @POST("/demands/ironBuy/queryIronSellerInfoPage")
    fun getIronBuyOffer(@Field("currentPage") currentPage: Int, @Field("pageSize") pageSize: Int,  @Field("offerStatus") buyStatus: Int, @Field("today") today: Int) : Observable<Response<SellerOfferInfo>>


    @FormUrlEncoded
    @POST("/demands/ironBuy/saveIronSellInfo")
    fun missOffer(@Field("ironBuyId") ironBuyId: String, @Field("flag") flag : String = "0"): Observable<Response<Object>>

}

fun <T> networkWrap(observable: Observable<Response<T>>?) : Observable<Response<T>>? {
    return observable?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.map({ response ->
                Log.e("HTTPResponse", Gson().toJson(response))
                when(response.code) {
                    "403","401" -> {
                        MainApplication.instance()?.getCurrentActivity()?.gotoLoginActivity()
                        clearLogin()
                        throw ResponseReturnErrorException(response.code, "请先登录")
                    }
                    "1002" -> {
                        throw ResponseReturnErrorException(response.code, "没有权限")
                    }
                    "1001" -> {
                        throw ResponseReturnErrorException(response.code, response.message)
                    }
                    "1000" -> {
                        // success
                    }
                    else -> {
                        throw ResponseReturnErrorException(response.code, response.message)
                    }
                }
                response
            })
}

class ResponseReturnErrorException(code: String, message: String?) : Exception(message) {

}