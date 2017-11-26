package ordertaking.itaobuxiu.com.ordertaking.engine

import com.google.gson.Gson
import ordertaking.itaobuxiu.com.ordertaking.apis.CityModel
import java.io.InputStreamReader
import com.google.gson.reflect.TypeToken



/**
 * Created by dev on 2017/11/25.
 */
object City {
    var provices: List<CityModel>? = null

    init {
        provices = Gson().
                fromJson(
                        InputStreamReader(MainApplication.instance()?.assets?.open("provinces")),
                        object : TypeToken<List<CityModel>>() {

                        }.type)
    }
}