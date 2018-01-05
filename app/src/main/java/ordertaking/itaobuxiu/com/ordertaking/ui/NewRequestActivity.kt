package ordertaking.itaobuxiu.com.ordertaking.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import com.sdsmdg.tastytoast.TastyToast
import kotlinx.android.synthetic.main.activity_new_request.*
import ordertaking.itaobuxiu.com.ordertaking.BaseActivity
import ordertaking.itaobuxiu.com.ordertaking.R
import ordertaking.itaobuxiu.com.ordertaking.apis.*
import android.text.InputFilter
import android.view.View
import android.view.inputmethod.InputMethodManager
import ordertaking.itaobuxiu.com.ordertaking.MainActivity
import ordertaking.itaobuxiu.com.ordertaking.engine.*


class NewRequestActivity : BaseActivity() {

    var postRequestBean: PostRequestBean? = null
    var editFlag: Boolean = false
    var ironInfo: IronBuyInfo? = null

    var needGot = true
    var displayedBackDialog = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_request)
        useNormalBack()

        postRequestBean = intent.getSerializableExtra("postRequestBean") as PostRequestBean?

        var localRequests = getLocalRuquests()
        var request = localRequests.requests.find { request ->
            return@find request.localId == postRequestBean?.localId
        }

        if (request == null && localRequests.requests.size >= 6) {
            toastInfo("单次最多只能发布6条")
            needGot = false
            finish();
            return
        }

        ironInfo = intent.getSerializableExtra("ironInfo") as IronBuyInfo?
        if (postRequestBean == null) {
            postRequestBean = PostRequestBean()
        }

        editFlag = intent.getBooleanExtra("isEdit", false)

        if (postRequestBean?.localId.isNullOrBlank()) {
            postRequestBean?.localId = id()
        }

        cityLayout.setOnClickListener {
            var cityDialog = SelectCityDialog(this)
            cityDialog.show()
            cityDialog.setOnCitySelectedListener(object: OnCityItemClickListener {
                override fun citySelected(cityModel: CityModel?) {
                    this@NewRequestActivity.postRequestBean?.location = cityModel
                    city.text = cityModel?.shortName
                }
            })
        }

        if (editFlag) {
            postEdit.visibility = View.VISIBLE
            save.visibility = View.GONE
            justPost.visibility = View.GONE
        } else {
            postEdit.visibility = View.GONE
            save.visibility = View.VISIBLE
            justPost.visibility = View.VISIBLE
        }

        weightUnit.isEnabled = false
        numUnit.isEnabled = false
        to.isEnabled = false

        nameLayout.setOnClickListener {
            var dialog = SelectTagDialog(0, this)
            dialog.show()
            dialog.setOnTagSelectedListener(object : SelectTagDialog.OnTagItemClickListener {
                override fun tagSelected(ironType: BaseIronInfo?) {
                    this@NewRequestActivity.postRequestBean?.ironType = ironType
                    name.text = ironType?.name

                    if (ironType?.name == "不锈钢板" || ironType?.name == "不锈钢卷") {
                        to.isEnabled = true
                        to.hint = "请填写公差"
                    } else {
                        to.isEnabled = false
                        to.hint = "非板卷类不可填写"
                    }

                    this@NewRequestActivity.postRequestBean?.specifications = ""
                    this@NewRequestActivity.postRequestBean?.width = ""
                    this@NewRequestActivity.postRequestBean?.height = ""
                    this@NewRequestActivity.postRequestBean?.length = ""
                    this@NewRequestActivity.postRequestBean?.tolerance = ""

                    to.setText("")
                    spec.setText("")


                    networkWrap(Network.create(IronRequestService::class.java)?.getUnits(ironType?.id!!))?.subscribe({ result ->
                        this@NewRequestActivity.postRequestBean?.unitModel = result.data
                         weightUnit.text = result.data.weightUnitCName
                         numUnit.text = result.data.numUnitCName
                         weightUnit.isEnabled = true
                         numUnit.isEnabled = true
                    }, {})
                }
            })
        }

        materialLayout.setOnClickListener {
            var dialog = SelectTagDialog(1,this)
            dialog.show()
            dialog.setOnTagSelectedListener(object : SelectTagDialog.OnTagItemClickListener {
                override fun tagSelected(ironType: BaseIronInfo?) {
                    this@NewRequestActivity.postRequestBean?.materialModel = ironType
                    material.text = ironType?.name
                }
            })
        }

        proPlaceLayout.setOnClickListener {
            var dialog = SelectTagDialog(2,this)
            dialog.show()
            dialog.setOnTagSelectedListener(object : SelectTagDialog.OnTagItemClickListener {
                override fun tagSelected(ironType: BaseIronInfo?) {
                    this@NewRequestActivity.postRequestBean?.proPlaceModel = ironType
                    proPlace.text = ironType?.name
                }
            })
        }

        surfaceLayout.setOnClickListener {
            var dialog = SelectTagDialog(3, this)
            dialog.show()
            dialog.setOnTagSelectedListener(object : SelectTagDialog.OnTagItemClickListener {
                override fun tagSelected(ironType: BaseIronInfo?) {
                    this@NewRequestActivity.postRequestBean?.surfaceModel = ironType
                    surface.text = ironType?.name
                }
            })
        }

        weightLayout.setOnClickListener {
            if (postRequestBean?.unitModel == null) {
                TastyToast.makeText(this, "请先选择品名", TastyToast.LENGTH_SHORT, TastyToast.INFO)
                return@setOnClickListener
            }
        }

        numLayout.setOnClickListener {
            if (postRequestBean?.unitModel == null) {
                TastyToast.makeText(this, "请先选择品名", TastyToast.LENGTH_SHORT, TastyToast.INFO)
                return@setOnClickListener
            }
        }

        specLayout.setOnClickListener {
            var intent = Intent(this, SpecActivity::class.java)
            if (postRequestBean?.ironType == null || postRequestBean?.surfaceModel == null) {
                toast(this, "请先选择品名和表面", TastyToast.INFO)
                return@setOnClickListener
            }
            intent.putExtra("weight", if (postRequestBean?.width == null) "" else postRequestBean?.width)
            intent.putExtra("height", if (postRequestBean?.height == null) "" else postRequestBean?.height)
            intent.putExtra("length", if (postRequestBean?.length == null) "" else postRequestBean?.length)

            intent.putExtra("otherStr", if (postRequestBean?.specifications == null) "" else postRequestBean?.specifications)

            intent.putExtra("ironId", postRequestBean?.ironType?.id)
            intent.putExtra("surfaceId", postRequestBean?.surfaceModel?.id)
            intent.putExtra("banjuan", postRequestBean?.ironType?.name == "不锈钢板" || postRequestBean?.ironType?.name == "不锈钢卷")

            startActivityForResult(intent, 1000)
        }


        comment.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(35))

        fillData()

        justPost.setOnClickListener {
            if (doCheck()) {
                showLoading()
                doPostReuqest(postRequestBean)?.subscribe({
                    hideLoading()
                    deleteRequest(postRequestBean)
                    toastInfo("发布成功")
                    BuyerFragment.notifyRefrsh(null)

                    val intent1 = Intent(this@NewRequestActivity, MainActivity::class.java)
                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent1.putExtra("innerCode", "1")
                    startActivity(intent1)

                    finish()
                }, { error ->
                    hideLoading()
                    toastInfo("发布失败：" + error.message)
                })
            }
        }

        save.setOnClickListener {
            if (doCheck()) {
                if (saveRequest(postRequestBean)) {
                    toastInfo("保存成功")
                } else {
                    toastInfo("单次最多只能发布6条")
                }

                if (needGot) {
                    var intent = Intent(this, RequestsActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                }

                finish()
            }
        }

        postEdit.setOnClickListener {
            if (doCheck()) {
                showLoading()
                doEditIronBuy(postRequestBean)?.subscribe({
                    hideLoading()
                    deleteRequest(postRequestBean)
                    toastInfo("保存修改成功")
                    // TODO
                    BuyerFragment.notifyRefrsh(null)
                    finish()
                }, { error ->
                    hideLoading()
                    toastInfo("保存修改失败：" + error.message)
                })
            }
        }
    }

    fun fillData() {
        name.text = postRequestBean?.ironType?.name
        material.text = postRequestBean?.materialModel?.name
        surface.text = postRequestBean?.surfaceModel?.name
        proPlace.text = postRequestBean?.proPlaceModel?.name
        city.text = postRequestBean?.location?.shortName

        weightUnit.text = postRequestBean?.unitModel?.weightUnitCName
        numUnit.text = postRequestBean?.unitModel?.numUnitCName

        num.setText(if (postRequestBean?.numbers != null) postRequestBean?.numbers else "")
        weight.setText(if (postRequestBean?.weights != null) postRequestBean?.weights else "")

        spec.text = if (isBanjuan()) "${postRequestBean?.height}*${postRequestBean?.width}*${postRequestBean?.length}" else postRequestBean?.specifications

        comment.setText(if (postRequestBean?.remark != null) postRequestBean?.remark else "")

        to.setText(if (postRequestBean?.tolerance != null) postRequestBean?.tolerance else "")

        to.isEnabled = !postRequestBean?.tolerance.isNullOrBlank()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000 && resultCode == Activity.RESULT_OK) {
            postRequestBean?.height = data?.getStringExtra("height")
            postRequestBean?.width = data?.getStringExtra("weight")
            postRequestBean?.length = data?.getStringExtra("length")
            postRequestBean?.specifications = data?.getStringExtra("spec")
            spec.text = if (isBanjuan()) "${postRequestBean?.height}*${postRequestBean?.width}*${postRequestBean?.length}" else postRequestBean?.specifications
        }
    }

    fun isBanjuan() :Boolean {
        return postRequestBean?.ironType?.name == "不锈钢板" || postRequestBean?.ironType?.name == "不锈钢卷"
    }

    fun doCheck(): Boolean {
        if (postRequestBean?.location == null) {
            toastInfo("请选择货源地")
            return false
        }
        if (postRequestBean?.ironType == null) {
            toastInfo("请选择品名")
            return false
        }
        if (postRequestBean?.materialModel == null) {
            toastInfo("请选择材质")
            return false
        }
        if (postRequestBean?.surfaceModel == null) {
            toastInfo("请选择表面")
            return false
        }
        if (postRequestBean?.proPlaceModel == null) {
            toastInfo("请选择产地")
            return false
        }
        if (postRequestBean?.location == null) {
            toastInfo("请选择货源地")
            return false
        }
        if ((postRequestBean?.ironType?.name == "不锈钢板" || postRequestBean?.ironType?.name == "不锈钢卷")) {
            if(postRequestBean?.width.isNullOrBlank() || postRequestBean?.height.isNullOrBlank()
                    || postRequestBean?.length.isNullOrBlank()) {
                toastInfo("请填写长宽厚")
                return false
            }
        } else {
            if (postRequestBean?.specifications.isNullOrBlank()) {
                toastInfo("请填写规格")
                return false
            }
        }
        postRequestBean?.weights = weight.text.toString()
        postRequestBean?.numbers = num.text.toString()
        postRequestBean?.tolerance = to.text.toString()

        if (postRequestBean?.weights.isNullOrBlank() && postRequestBean?.numbers.isNullOrBlank()) {
            toastInfo("数量和重量，至少选择其中一项")
            return false
        }
        if (!postRequestBean?.weights.isNullOrBlank() && postRequestBean?.weights?.toDouble()!! <= 0) {
            toastInfo("数量和重量必须大于0")
            return false
        }
        if (!postRequestBean?.numbers.isNullOrBlank() && postRequestBean?.numbers?.toDouble()!! <= 0) {
            toastInfo("数量和重量必须大于0")
            return false
        }
        if ((postRequestBean?.ironType?.name == "不锈钢板" || postRequestBean?.ironType?.name == "不锈钢卷")
                && postRequestBean?.tolerance.isNullOrBlank()) {
            toastInfo("请填写公差")
            return false
        }
        postRequestBean?.remark = comment.text.toString().trim()
        return true
    }

    override fun onBackPressed() {
            AlertDialog.Builder(this)
                    .setMessage("本条求购尚未提交，确定要离开吗？")
                    .setPositiveButton("确定") { dialog, which ->
                        finish()
                    }
                    .setNegativeButton("取消") { dialog, which ->
                        displayedBackDialog = false
                    }.show()
    }

    override fun finish() {
        super.finish()

        try {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm!!.hideSoftInputFromWindow(comment.getWindowToken(), 0)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}
