package ordertaking.itaobuxiu.com.ordertaking.apis

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import com.orhanobut.hawk.Hawk
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ordertaking.itaobuxiu.com.ordertaking.engine.Network
import java.util.*
import android.support.v4.content.ContextCompat.startActivity
import com.google.gson.Gson
import ordertaking.itaobuxiu.com.ordertaking.engine.MainApplication
import ordertaking.itaobuxiu.com.ordertaking.ui.*

var gson = Gson()

fun toJsonString(any:Any): String {
    return gson.toJson(any)
}

/**
 * Created by dev on 2017/11/25.
 */
fun doLogin(mobile: String, password: String): Observable<Response<UserLoginData>>? {
    return networkWrap(Network.create(UserApiService::class.java)?.login(mobile, password))
            ?.observeOn(Schedulers.io())
            ?.map {
                response ->
                    Hawk.put(USER_LOGIN_INFO, response.data)

                networkWrap(Network.create(UserApiService::class.java)?.getUserInfo("123"))?.subscribe({ result: Response<UserInfo> ->
                    Hawk.put(LOGIN_USER, result.data)
                })

                response
            }
            ?.observeOn(AndroidSchedulers.mainThread())
}

fun isLogin(): Boolean {
    return Hawk.get<UserLoginData>(USER_LOGIN_INFO) != null
}

fun clearLogin() {
    Hawk.delete(USER_LOGIN_INFO)
    Hawk.delete(LOGIN_USER)
}

fun gotoPostRequest(context: Context) {
    context.startActivity(Intent(context, RequestsActivity::class.java))
}

fun gotoIronBuyDetail(context: Context, ironBuyInfo: IronBuyInfo) {
    var intent = Intent(context, IronBuyDetailActivity::class.java)
    intent.putExtra("ironBuyInfo", ironBuyInfo)
    context.startActivity(intent)
}

fun gotoIronOfferDetail(context: Context, sellerOffer: SellerOfferInfoListItem) {
    var intent = Intent(context, IronOfferActivity::class.java)
    intent.putExtra("sellerOffer", sellerOffer)
    context.startActivity(intent)
}

fun gotoHistoryPostRequest(context: Context) {
    context.startActivity(Intent(context, RequestHistoryActivity::class.java))
}

fun gotoNewRequest(context: Context, postRequestBean: PostRequestBean?, edit: Boolean) {
    var intent = Intent(context, NewRequestActivity::class.java)
    if (postRequestBean != null) {
        intent.putExtra("postRequestBean", postRequestBean)
        intent.putExtra("isEdit", edit)
    }
    context.startActivity(intent)
}

fun getLocalRuquests(): LocalRequests {
    var request = Hawk.get<LocalRequests>(LOCAL_REQUESTS)
    if (request == null) {
        request = LocalRequests(mutableListOf())
        Hawk.put(LOCAL_REQUESTS, request)
    }
    return request
}

fun saveRequest(newRequest: PostRequestBean?) {
    var localRequests = getLocalRuquests()
    var request = localRequests.requests.find { request ->
        return@find request.localId == newRequest?.localId
    }
    if (request != null) {
        localRequests.requests.remove(request)
    }
    if (newRequest != null) {
        localRequests.requests.add(0, newRequest)
    }
    Hawk.put(LOCAL_REQUESTS, localRequests)
}

fun deleteRequest(newRequest: PostRequestBean?) {
    var localRequests = getLocalRuquests()
    localRequests.requests.remove(newRequest)
    Hawk.put(LOCAL_REQUESTS, localRequests)
}

fun doPostReuqest(request: PostRequestBean?): Observable<Response<Object>>? {
    return networkWrap(Network.create(IronRequestService::class.java)?.postRequest(
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
    ))
}

fun doEditIronBuy(request: IronBuyInfo, edit: Boolean): Observable<Response<Object>>?  {
    return networkWrap(Network.create(IronRequestService::class.java)?.editIronBuy(
            request?.ironTypeId,
            request?.ironTypeName,
            request?.materialId,
            request?.materialName,
            request?.surfaceId,
            request?.surfaceName,
            request?.proPlacesId,
            request?.proPlacesName,
            request?.locationId,
            request?.locationName,
            request?.remark,
            request?.length,
            request?.width,
            request?.height,
            request?.specifications,
            request?.tolerance,
            (24*60*60*1000).toString(),
            request?.numbers,
            request?.numberUnitId,
            request?.numberUnit,
            request?.weights,
            request?.weightUnitId,
            request?.weightUnit,
            request?.id,
            if (edit) "1" else "0"
    ))
}

fun doEditIronBuy(request: PostRequestBean?): Observable<Response<Object>>? {
    return networkWrap(Network.create(IronRequestService::class.java)?.editIronBuy(
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
            request?.unitModel?.weightUnitCName,
            request?.id,
            "1"
    ))
}


fun id(): String {
    return UUID.randomUUID().toString()
}