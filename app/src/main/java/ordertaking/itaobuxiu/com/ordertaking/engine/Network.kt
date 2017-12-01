package ordertaking.itaobuxiu.com.ordertaking.engine

import android.util.Log
import com.orhanobut.hawk.Hawk
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
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
                .baseUrl("http://111.231.134.170/")
                .addConverterFactory(GsonConvert.create())
                .client(OkHttpClient.Builder()
                        .addInterceptor { chain ->
                            try {
                                var loginData: UserLoginData? = Hawk.get(USER_LOGIN_INFO)
                                var request = chain?.request()
                                        ?.newBuilder()
                                        ?.addHeader("loginId", loginData?.loginId)
                                        ?.addHeader("authorization", loginData?.authorization)
                                        ?.build()
                                Log.e("HTTPResponseRequest", request.toString())
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