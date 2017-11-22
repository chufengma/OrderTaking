package ordertaking.itaobuxiu.com.ordertaking.apis

import io.reactivex.Observable
import retrofit2.http.GET

/**
 * Created by dev on 2017/11/20.
 */
interface HomeApiService {

    @GET("/api/ad/findByGroupId?groupId=3202")
    fun getHomeAdd(): Observable<HomeAdsModel>


    @GET("/api/main/photoLME")
    fun getPriceToday(): Observable<HomePriceModel>


    @GET("/api/main/threeMonthLME")
    fun getPriceMonth(): Observable<HomePriceMonthModel>


    @GET("/api/main/countSellActive")
    fun getSellers(): Observable<HomeSellerModel>

    @GET("/api/main/queryIronNew")
    fun getMarketPrice(): Observable<HomeMarketPriceModel>

    @GET("/api/main/queryIronInfo")
    fun getIronInfos(): Observable<IronInfoModel>
}