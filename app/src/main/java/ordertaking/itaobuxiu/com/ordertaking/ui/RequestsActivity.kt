package ordertaking.itaobuxiu.com.ordertaking.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_requests.*
import ordertaking.itaobuxiu.com.ordertaking.BaseActivity
import ordertaking.itaobuxiu.com.ordertaking.MainActivity
import ordertaking.itaobuxiu.com.ordertaking.R
import ordertaking.itaobuxiu.com.ordertaking.apis.*
import ordertaking.itaobuxiu.com.ordertaking.engine.*
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.inputmethod.InputMethodManager


class RequestsActivity : BaseActivity() {

    var adapter: PostRequestAdapter? = null
    var deleteFlag: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_requests)
        useNormalBack()

        addFromEmpty.setOnClickListener { gotoNewRequest(this, null, false) }
        addNewForNormal.setOnClickListener { gotoNewRequest(this, null, false) }
        selectFromHistoryForNormal.setOnClickListener { gotoHistoryPostRequest(this) }
        addFromHistory.setOnClickListener { gotoHistoryPostRequest(this) }

        adapter = PostRequestAdapter()

        requestRecycler.layoutManager = LinearLayoutManager(this)
        requestRecycler.adapter = adapter

        adapter?.setActionListener(object : OnPostRequestActionListener {

            override fun onItemClick(request: PostRequestBean) {
                gotoNewRequest(this@RequestsActivity, request, false)
            }

            override fun onDelete(request: PostRequestBean) {
                runOnUiThread {
                    deleteRequest(request)
                    updateData()
                }
            }

            override fun onCheckChange(request: PostRequestBean, check: Boolean) {
                checkData(true)
            }

            override fun onCopy(request: PostRequestBean) {
                runOnUiThread {
                    gotoNewRequest(this@RequestsActivity, request.copy(id()), false)
                }
            }
        })

        selectAll.setOnCheckedChangeListener { buttonView, isChecked ->
            var checkedCount = adapter?.getAllData()?.count { request -> request.localCheck }
//            if (checkedCount == adapter?.getAllData()?.size || checkedCount == 0) {
//                adapter?.getAllData()?.forEach { item ->
//                    item.localCheck = isChecked
//                }
//            }
            if (checkedCount != adapter?.getAllData()?.size && !isChecked) {
                return@setOnCheckedChangeListener
            }

            adapter?.getAllData()?.forEach { item ->
                item.localCheck = isChecked
            }
            postALl.postDelayed({
                adapter?.notifyDataSetChanged()
            }, 200)

            checkData(false)
        }

        postALl.setOnClickListener {
            if (deleteFlag) {
                deleteAll()
                deleteFlag = false
                updateData()
            } else {
                var serversBean: List<PostRequestServerBean>? = adapter?.getAllData()?.filter { it.localCheck }?.map { it.trans() }
                var dataStr = Gson().toJson(serversBean)
                showLoading()
                networkWrap(Network.create(IronRequestService::class.java)?.postAllRequest(dataStr))
                        ?.subscribe(
                                {
                                    hideLoading()
                                    toastInfo("发布成功")
                                    deleteAll()
                                    updateData()

                                    val intent1 = Intent(this@RequestsActivity, MainActivity::class.java)
                                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                    intent1.putExtra("innerCode", "1")
                                    startActivity(intent1)

                                    finish()
                                },
                                { error ->
                                    hideLoading()
                                    toastInfo("发布失败：" + error.message)
                                })
            }
        }

        right.setOnClickListener {
            deleteFlag = !deleteFlag
            updateDeletFlag()
            adapter?.setNeedCopy(!deleteFlag)
            if (deleteFlag) {
                toastInfo("进入删除模式")
            } else {
                toastInfo("退出删除模式")
            }
        }
    }

    fun deleteAll() {
        var checkedCount = adapter?.getAllData()?.count { request -> request.localCheck }
        if (checkedCount != null && checkedCount > 0) {
            for (request: PostRequestBean in adapter?.getAllData()?.filter { it.localCheck }!!) {
                deleteRequest(request)
            }
        }
    }

    fun checkData(refreshAll: Boolean) {
        var count = checkCount()
        if (refreshAll) {
            selectAll.isChecked = count == adapter?.getAllData()?.size
        }
    }

    fun checkCount(): Int {
        var checkedCount = adapter?.getAllData()?.count { request -> request.localCheck }
        if (checkedCount == null) {
            checkedCount = 0
        }
        postALl.isEnabled = checkedCount > 0
        selectAllText.text = "全选" + if (checkedCount > 0) "(${checkedCount})" else ""
        return checkedCount
    }

    fun updateData() {
        if (getLocalRuquests().requests.isEmpty()) {
            emptyLayout.visibility = View.VISIBLE
            normalLayout.visibility = View.GONE
            right.visibility = View.INVISIBLE
        } else {
            emptyLayout.visibility = View.GONE
            normalLayout.visibility = View.VISIBLE
            right.visibility = View.VISIBLE
        }

        checkCount()
        var list: MutableList<PostRequestBean> = getLocalRuquests().requests;
        list.forEach { a ->
            adapter?.getAllData()?.forEach { b->
                if (a.localId.equals(b.localId)) {
                    a.localCheck = b.localCheck
                }
            }
        }

        adapter?.updateData(list)
        checkData(false)
        updateDeletFlag()
    }

    fun updateDeletFlag() {
        if (deleteFlag) {
            right.text = "取消"
            postALl.text = "删除"
        } else {
            right.text = "编辑"
            postALl.text = "发布"
        }
    }

    override fun onStart() {
        super.onStart()
        updateData()
    }
}
