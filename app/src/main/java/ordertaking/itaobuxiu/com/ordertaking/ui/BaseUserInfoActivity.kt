package ordertaking.itaobuxiu.com.ordertaking.ui

import android.os.Bundle
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.activity_base_user_info.*
import ordertaking.itaobuxiu.com.ordertaking.BaseActivity
import ordertaking.itaobuxiu.com.ordertaking.R
import ordertaking.itaobuxiu.com.ordertaking.apis.*
import ordertaking.itaobuxiu.com.ordertaking.engine.Network

class BaseUserInfoActivity : BaseActivity() {

    var contactStr: String? = ""
    var contactNumStr: String? = ""
    var qqStr: String? = ""
    var addressDesStr: String? = ""

    var proviceIdStr: String? = ""
    var proviceNameStr: String? = ""

    var cityIdStr: String? = ""
    var cityNameStr: String? = ""

    var disIdStr: String? = ""
    var disNameStr: String? = ""

    var currentStoreId: String? = ""
    var currentStoreName: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base_user_info)

        networkWrap(Network.create(UserApiService::class.java)?.getUserInfo("123"))?.subscribe({ result: Response<UserInfo> ->
            Hawk.put(LOGIN_USER, result.data)
            updateViews()
        }, {})
    }

    fun updateViews() {
        var userInfo: UserInfo = Hawk.get(LOGIN_USER)

        companyName.text = userInfo?.buserInfo?.companyName
        userName.text = userInfo?.mobile

        contact.setText(userInfo?.buserInfo?.contact)
        tel.setText(userInfo?.buserInfo?.contactNum)
        qq.setText(userInfo?.buserInfo?.qq)
        address.setText("${userInfo?.buserInfo?.provinceName} ${userInfo?.buserInfo?.cityName} ${userInfo?.buserInfo?.districtName}")
        addressDesc.setText(userInfo?.buserInfo?.address)
        store.setText(userInfo?.buserInfo?.storeHouseName)

        contactStr = userInfo?.buserInfo?.contact
        contactNumStr = userInfo?.buserInfo?.contactNum
        qqStr = userInfo?.buserInfo?.qq
        proviceIdStr = userInfo?.buserInfo?.provinceId
        proviceNameStr = userInfo?.buserInfo?.provinceName
        cityIdStr = userInfo?.buserInfo?.cityId
        cityNameStr = userInfo?.buserInfo?.cityName
        disIdStr = userInfo?.buserInfo?.districtId
        disNameStr = userInfo?.buserInfo?.districtName
        currentStoreId = userInfo?.buserInfo?.storeHouseId
        currentStoreName = userInfo?.buserInfo?.storeHouseName
        addressDesStr = userInfo?.buserInfo?.address

        addressLayout.setOnClickListener {
            var dialog = SelectAreaDialog(this)
            dialog.setOnCitySelectedListener(object: SelectAreaDialog.OnCityDescALlClickListener {
                override fun onSelected(currentProvice: CityDescData?, currentCity: CityDescData?, currentDis: CityDescData?) {
                    address.setText(currentProvice?.label + " " + if (currentCity?.label.isNullOrBlank()) "" else currentCity?.label + " " + if (currentDis?.label.isNullOrBlank()) "" else currentDis?.label)
                    proviceIdStr = currentProvice?.value
                    proviceNameStr = currentProvice?.label
                    cityIdStr = currentCity?.value
                    cityNameStr = currentCity?.label
                    disIdStr = currentDis?.value
                    disNameStr = currentDis?.label
                }
            })
            dialog.show()
        }

        storeLayout.setOnClickListener {
            var dialog = SelectStoreDialog(this)
            dialog.setOnStoreListener(object: SelectStoreDialog.OnStoreBackListener {
                override fun onSelected(storeDat: StoreData?) {
                    currentStoreId = storeDat?.id
                    currentStoreName = storeDat?.notice

                    store.setText(currentStoreName)
                }
            })
            dialog.show()
        }

        right.setOnClickListener {
            contactStr = contact.text.toString()
            contactNumStr = tel.text.toString()
            qqStr = qq.text.toString()
            addressDesStr = addressDesc.text.toString()

            showLoading()
            networkWrap(Network.create(UserApiService::class.java)?.saveBInfo(contactStr,
                    contactNumStr,
                    qqStr,
                    proviceIdStr,
                    proviceNameStr,
                    cityIdStr,
                    cityNameStr,
                    disIdStr,
                    disNameStr,
                    currentStoreId,
                    currentStoreName,
                    addressDesStr))?.subscribe({ result ->
                toastInfo("保存成功")
                hideLoading()
                finish()
            }, { error ->
                hideLoading()
                toastInfo("保存失败：" + error.message)
            })
        }
    }


}
