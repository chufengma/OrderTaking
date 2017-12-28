package ordertaking.itaobuxiu.com.ordertaking.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.widget.DatePicker
import android.widget.TimePicker
import kotlinx.android.synthetic.main.activity_qu_helper.*
import ordertaking.itaobuxiu.com.ordertaking.BaseActivity
import ordertaking.itaobuxiu.com.ordertaking.R
import ordertaking.itaobuxiu.com.ordertaking.apis.*
import ordertaking.itaobuxiu.com.ordertaking.engine.Network
import java.text.SimpleDateFormat
import java.util.*

class QuHelperActivity : BaseActivity() {

    var currentDate: Calendar? = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qu_helper)

        dateLayout.setOnClickListener {
            var picker = DatePickerDialog(this, object:DatePickerDialog.OnDateSetListener{
                override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                    currentDate?.set(Calendar.YEAR, year)
                    currentDate?.set(Calendar.MONTH, month)
                    currentDate?.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                    date?.text = SimpleDateFormat("yyyy-MM-dd").format(currentDate?.timeInMillis) + " 00:00"
                }
            }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
            picker.datePicker.minDate = System.currentTimeMillis()
            picker.show()
        }

        timeLayout.setOnClickListener {
            TimePickerDialog(this, object:TimePickerDialog.OnTimeSetListener{
                override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                    currentDate?.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    currentDate?.set(Calendar.MINUTE, minute)
                    time?.text = SimpleDateFormat("HH:mm").format(currentDate?.timeInMillis)
                    date?.text = SimpleDateFormat("yyyy-MM-dd HH:mm").format(currentDate?.timeInMillis)
                }
            }, Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), true).show()
        }

        addressLayout.setOnClickListener {
            var dialog = SelectAreaDialog(this)
            dialog.setOnCitySelectedListener(object: SelectAreaDialog.OnCityDescALlClickListener {
                override fun onSelected(currentProvice: CityDescData?, currentCity: CityDescData?, currentDis: CityDescData?) {
                    address.setText(currentProvice?.label + " " + if (currentCity?.label.isNullOrBlank()) "" else currentCity?.label + " " + if (currentDis?.label.isNullOrBlank()) "" else currentDis?.label)
                }
            })
            dialog.show()
        }

        cancle.setOnClickListener {
            onBackPressed()
        }

        postEdit.setOnClickListener {
            if (date.text.toString().isNullOrBlank() || time.text.toString().isNullOrBlank()) {
                toastInfo("请选择时间")
                return@setOnClickListener
            }
            if (address.text.toString().isNullOrBlank() || addressDesc.text.toString().isNullOrBlank()) {
                toastInfo("请输入地址")
                return@setOnClickListener
            }
            if (contact.text.toString().isNullOrBlank()) {
                toastInfo("请输入联系人")
                return@setOnClickListener
            }
            if (companyName.text.toString().isNullOrBlank()) {
                toastInfo("请输入公司名称")
                return@setOnClickListener
            }
            if (tel.text.toString().isNullOrBlank()) {
                toastInfo("请输入联系电话")
                return@setOnClickListener
            }
            showLoading()
            networkWrap(Network.create(UserApiService::class.java)?.postQu(currentDate?.timeInMillis.toString(),
                    address.text.toString() + " " + addressDesc.text.toString(),
                    companyName.text.toString(), contact.text.toString(), tel.text.toString()))?.subscribe({
                hideLoading()
                toastInfo("提交成功")
                finish()
            }, { error ->
                hideLoading()
                toastInfo("提交失败：" + error.message)
            })
        }
    }


    override fun onBackPressed() {
        AlertDialog.Builder(this)
                .setMessage("本条质检尚未提交，确定要离开吗？")
                .setPositiveButton("确定") { dialog, which ->
                    finish()
                }
                .setNegativeButton("取消") { dialog, which ->
                    dialog.dismiss()
                }.show()
    }
}
