package ordertaking.itaobuxiu.com.ordertaking.engine

import android.util.Log
import com.orhanobut.hawk.Hawk
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.Buffer
import ordertaking.itaobuxiu.com.ordertaking.BuildConfig
import ordertaking.itaobuxiu.com.ordertaking.apis.USER_LOGIN_INFO
import ordertaking.itaobuxiu.com.ordertaking.apis.UserLoginData
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

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
                .baseUrl("http://120.55.63.70/")
                .addConverterFactory(GsonConvert.create())
                .client(OkHttpClient.Builder()
                        .addInterceptor { chain ->
                            try {
                                var loginData: UserLoginData? = Hawk.get(USER_LOGIN_INFO)
                                var builder = chain?.request()
                                        ?.newBuilder()
                                        ?.addHeader("loginId", if (loginData?.loginId == null) "" else loginData?.loginId)
                                        ?.addHeader("authorization", if (loginData?.authorization == null) "" else loginData?.authorization)

                                if (Hawk.get<String>("SessionID") != null) {
                                    builder?.addHeader("Cookie", Hawk.get<String>("SessionID"))
                                }

                                var request = builder?.build()
                                if (BuildConfig.DEBUG) {
                                    try {
                                        val copy = request?.newBuilder()?.build()
                                        val buffer = Buffer()
                                        copy?.body()!!.writeTo(buffer)
                                        Log.e("HTTPResponseRequest", "${request?.url()} ${buffer.readUtf8()}")
                                    } catch (e: Exception) {
                                    }
                                }
                                chain?.proceed(request)!!
                            } catch (e: Exception) {
                                e.printStackTrace()
                                chain?.proceed(chain?.request())!!
                            }
                        }
                        .build())
                .build()


    }

    fun <T> create(service: Class<T>): T? {
        return retrofit?.create(service)
    }

}