package ordertaking.itaobuxiu.com.ordertaking.engine

import android.util.Log
import com.orhanobut.hawk.Hawk
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.Buffer
import ordertaking.itaobuxiu.com.ordertaking.BuildConfig
import ordertaking.itaobuxiu.com.ordertaking.apis.ResponseReturnErrorException
import ordertaking.itaobuxiu.com.ordertaking.apis.USER_LOGIN_INFO
import ordertaking.itaobuxiu.com.ordertaking.apis.UserLoginData
import ordertaking.itaobuxiu.com.ordertaking.apis.id
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.nio.charset.Charset

/**
 * Created by dev on 2017/11/20.
 */
object Network {

    var retrofit: Retrofit? = null

    init {
        retrofit = Retrofit.Builder()
                .addCallAdapterFactory(
                        RxJava2CallAdapterFactory.create())
//                .addConverterFactory(
//                        GsonConverterFactory.create())
//        .baseUrl("http://jiedan8.cn/")
//        120.55.63.70
//        111.231.134.170
                .baseUrl("http://111.231.134.170/")
                .addConverterFactory(GsonConvert.create())
                .client(OkHttpClient.Builder()
                        .addInterceptor { chain ->
                            var id = id()
                                var loginData: UserLoginData? = Hawk.get(USER_LOGIN_INFO)
                                var builder = chain?.request()
                                        ?.newBuilder()
                                        ?.addHeader("fromAndroid", "thisIsTestToken")
                                        ?.addHeader("loginId", if (loginData?.loginId == null) "" else loginData?.loginId)
                                        ?.addHeader("authorization", if (loginData?.authorization == null) "" else loginData?.authorization)

                                if (Hawk.get<String>("SessionID") != null) {
                                    builder?.addHeader("Cookie", Hawk.get<String>("SessionID"))
                                }

                                var request = builder?.build()
                            try {
                                if (BuildConfig.DEBUG) {
                                    try {
                                        val copy = request?.newBuilder()?.build()
                                        val buffer = Buffer()
                                        copy?.body()!!.writeTo(buffer)
                                        Log.e("HTTPResponseRequest", "${request?.url()} ${buffer.readUtf8()}")
                                        Log.e("LOGINOUT_CHECK", "${id} ${request?.url()} ${buffer.readUtf8()}  ${if (loginData?.loginId == null) "" else loginData?.loginId} ${if (loginData?.authorization == null) "" else loginData?.authorization}")

                                    } catch (e: Exception) {
                                        Log.e("LOGINOUT_CHECK_TIMEOUT", "${id} ${request?.url()}")
                                    }
                                }
                                var response = chain?.proceed(request)!!
                                // response.body()?.source()?.request(Long.MAX_VALUE)
                                // var str = response.body()?.source()?.buffer()?.clone()?.readString(Charset.forName("UTF-8"))
                                // var json = JSONObject(str)
                                // var code = json.optString("code", "-111111")
                                response
                            } catch (e: Exception) {
                                e.printStackTrace()
                                throw ResponseReturnErrorException("6666", "请求超时，请重试")
                            }
                        }
                        .build())
                .build()
    }

    fun <T> create(service: Class<T>): T? {
        return retrofit?.create(service)
    }

}