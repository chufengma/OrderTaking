package ordertaking.itaobuxiu.com.ordertaking.apis

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.http.GET
import io.reactivex.functions.Function


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

class ResponseReturnErrorException(code: String, message: String?) : Exception(message) {

}

fun <T> networkWrap(observable: Observable<Response<T>>?) : Observable<Response<T>>? {
    return observable?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.map({ response ->
                if (response.code == "403") {
                    throw ResponseReturnErrorException(response.code, "请先登录")
                }
                response
            })
}