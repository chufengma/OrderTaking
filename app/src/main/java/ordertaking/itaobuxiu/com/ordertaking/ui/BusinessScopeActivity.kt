package ordertaking.itaobuxiu.com.ordertaking.ui

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.CompoundButton
import android.widget.TextView
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import kotlinx.android.synthetic.main.activity_business_scope.*
import ordertaking.itaobuxiu.com.ordertaking.BaseActivity
import ordertaking.itaobuxiu.com.ordertaking.R
import ordertaking.itaobuxiu.com.ordertaking.apis.*
import ordertaking.itaobuxiu.com.ordertaking.engine.Network

class BusinessScopeActivity : BaseActivity() {

    var requestCount = 0

    var ironTypes: Array<BaseIronInfo>? = null
    var materials: Array<BaseIronInfo>? = null
    var surfaces: Array<BaseIronInfo>? = null
    var proPlaces: Array<BaseIronInfo>? = null

    var userData: ScopeData? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_business_scope)

        fetchData()

        right.setOnClickListener { doSave() }
    }

    fun fetchData() {
        showLoading()

        networkWrap(Network.create(UserApiService::class.java)?.findIronTypes(""))?.subscribe({ result ->
            ironTypes = result.data.toTypedArray()
            ironTypeFlowlayout.adapter = MyTagAdapter(ironTypes)
            checkRequestCount()
        }, {
            checkRequestCount()
        })

        networkWrap(Network.create(UserApiService::class.java)?.findMaterials(""))?.subscribe({ result ->
            materials = result.data.toTypedArray()
            materialFlowlayout.adapter = MyTagAdapter(materials)
            checkRequestCount()
        }, {
            checkRequestCount()
        })


        networkWrap(Network.create(UserApiService::class.java)?.findSurFace(""))?.subscribe({ result ->
            surfaces = result.data.toTypedArray()
            surfaceFlowlayout.adapter = MyTagAdapter(surfaces)
            checkRequestCount()
        }, {
            checkRequestCount()
        })


        networkWrap(Network.create(UserApiService::class.java)?.findProPlaces(""))?.subscribe({ result ->
            proPlaces = result.data.toTypedArray()
            proPlaceFlowlayout.adapter = MyTagAdapter(proPlaces)
            checkRequestCount()
        }, {
            checkRequestCount()
        })

        networkWrap(Network.create(UserApiService::class.java)?.findMyScopes(""))?.subscribe({ result ->
            userData = result.data
            if (userData?.ironType == null) {
                userData?.ironType = mutableListOf()
            }
            if (userData?.surface == null) {
                userData?.surface = mutableListOf()
            }
            if (userData?.material == null) {
                userData?.material = mutableListOf()
            }
            if (userData?.proPlace == null) {
                userData?.proPlace = mutableListOf()
            }
            checkRequestCount()
        }, {
            checkRequestCount()
        })


    }

    fun doSave() {
        if (ironTypes == null || materials == null || surfaces == null || proPlaces == null) {
            toastInfo("范围信息有误，无法保存")
            return
        }

        var ironTypeList = mutableListOf<BaseIronInfo>()
        ironTypeFlowlayout.selectedList.forEach { index ->
            ironTypeList.add(ironTypes?.get(index)!!)
        }

        var materialsList = mutableListOf<BaseIronInfo>()
        materialFlowlayout.selectedList.forEach { index ->
            materialsList.add(materials?.get(index)!!)
        }

        var surfacesList = mutableListOf<BaseIronInfo>()
        surfaceFlowlayout.selectedList.forEach { index ->
            surfacesList.add(surfaces?.get(index)!!)
        }

        var proPlacesList = mutableListOf<BaseIronInfo>()
        proPlaceFlowlayout.selectedList.forEach { index ->
            proPlacesList.add(proPlaces?.get(index)!!)
        }

        showLoading()
        networkWrap(Network.create(UserApiService::class.java)?.saveMyScopes(toJsonString(ironTypeList),
                toJsonString(surfacesList),
                toJsonString(materialsList),
                toJsonString(proPlacesList)))?.subscribe({ result ->
            toastInfo("保存成功")
            hideLoading()
            finish()
        }, { error ->
            hideLoading()
            toastInfo("保存失败：" + error.message)
        })
    }

    fun checkRequestCount() {
        if (requestCount >= 4) {
            hideLoading()
        } else {
            requestCount++
        }

        if (ironTypes != null && userData?.ironType != null) {
            var indexs = mutableSetOf<Int>()
            ironTypes?.forEachIndexed { index, baseIronInfo ->
                userData?.ironType?.forEach { it ->
                    Log.e("Scope", "${it.id}/${baseIronInfo.id}")
                    if (baseIronInfo.id?.equals(it.id)!!) {
                        Log.e("Scope", " same ${it.id}/${baseIronInfo.id}")
                        indexs.add(index)
                    }
                }
            }
            ironTypeFlowlayout.adapter.setSelectedList(indexs)
            ironTypeFlowlayout.adapter = ironTypeFlowlayout.adapter
            var i = object : CompoundButton.OnCheckedChangeListener {
                override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                    if (!isChecked) {
                        ironTypeFlowlayout.adapter.setSelectedList(mutableSetOf<Int>())
                    } else {
                        ironTypeFlowlayout.adapter.setSelectedList(IntRange(0, ironTypes?.size!! - 1).toMutableSet())
                    }
                }
            }
            ironTypeCheckBox.setOnCheckedChangeListener(i)
            ironTypeCheckBox.isChecked = indexs.size == ironTypes?.size
            ironTypeFlowlayout.setOnTagClickListener { view, position, parent ->
                ironTypeCheckBox.setOnCheckedChangeListener(null)
                ironTypeCheckBox.isChecked = ironTypeFlowlayout.selectedList.size == ironTypes?.size
                ironTypeCheckBox.setOnCheckedChangeListener(i)
                true
            }
        }

        if (materials != null && userData?.material != null) {
            var indexs = mutableSetOf<Int>()
            materials?.forEachIndexed { index, baseIronInfo ->
                userData?.material?.forEach { it ->
                    if (TextUtils.equals(baseIronInfo.id, it.id)) {
                        indexs.add(index)
                    }
                }
            }
            materialFlowlayout.adapter.setSelectedList(indexs)
            materialCheckBox.isChecked = indexs.size == materials?.size
            var m = object : CompoundButton.OnCheckedChangeListener {
                override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                    if (!isChecked) {
                        materialFlowlayout.adapter.setSelectedList(mutableSetOf<Int>())
                    } else {
                        materialFlowlayout.adapter.setSelectedList(IntRange(0, materials?.size!! - 1).toMutableSet())
                    }
                }
            }
            materialCheckBox.setOnCheckedChangeListener(m)

            materialFlowlayout.setOnSelectListener {
                materialCheckBox.setOnCheckedChangeListener(null)
                materialCheckBox.isChecked = materialFlowlayout.selectedList.size == materials?.size
                materialCheckBox.setOnCheckedChangeListener(m)
            }
        }

        if (surfaces != null && userData?.surface != null) {
            var indexs = mutableSetOf<Int>()
            surfaces?.forEachIndexed { index, baseIronInfo ->
                userData?.surface?.forEach { it ->
                    if (TextUtils.equals(baseIronInfo.id, it.id)) {
                        indexs.add(index)
                    }
                }
            }
            surfaceFlowlayout.adapter.setSelectedList(indexs)
            surfaceCheckBox.isChecked = indexs.size == surfaces?.size
            var s = object : CompoundButton.OnCheckedChangeListener {
                override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                    if (!isChecked) {
                        surfaceFlowlayout.adapter.setSelectedList(mutableSetOf<Int>())
                    } else {
                        surfaceFlowlayout.adapter.setSelectedList(IntRange(0, surfaces?.size!! - 1).toMutableSet())
                    }
                }
            }
            surfaceCheckBox.setOnCheckedChangeListener(s)

            surfaceFlowlayout.setOnTagClickListener { view, position, parent ->
                surfaceCheckBox.setOnCheckedChangeListener(null)
                surfaceCheckBox.isChecked = surfaceFlowlayout.selectedList.size == surfaces?.size
                surfaceCheckBox.setOnCheckedChangeListener(s)
                true
            }
        }

        if (proPlaces != null && userData?.proPlace != null) {
            var indexs = mutableSetOf<Int>()
            proPlaces?.forEachIndexed { index, baseIronInfo ->
                userData?.proPlace?.forEach { it ->
                    if (TextUtils.equals(baseIronInfo.id, it.id)) {
                        indexs.add(index)
                    }
                }
            }
            proPlaceFlowlayout.adapter.setSelectedList(indexs)
            proPlaceCheckBox.isChecked = indexs.size == proPlaces?.size
            var p = object : CompoundButton.OnCheckedChangeListener {
                override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                    if (!isChecked) {
                        proPlaceFlowlayout.adapter.setSelectedList(mutableSetOf<Int>())
                    } else {
                        proPlaceFlowlayout.adapter.setSelectedList(IntRange(0, proPlaces?.size!! - 1).toMutableSet())
                    }
                }
            }
            proPlaceCheckBox.setOnCheckedChangeListener(p)
            proPlaceFlowlayout.setOnTagClickListener { view, position, parent ->
                proPlaceCheckBox.setOnCheckedChangeListener(null)
                proPlaceCheckBox.isChecked = proPlaceFlowlayout.selectedList.size == proPlaces?.size
                proPlaceCheckBox.setOnCheckedChangeListener(p)
                true
            }
        }
    }

    class MyTagAdapter(datas: Array<BaseIronInfo>?) : TagAdapter<BaseIronInfo>(datas) {

        var views: MutableList<View> = mutableListOf()

        override fun getView(parent: FlowLayout?, position: Int, t: BaseIronInfo?): View {
            var view = LayoutInflater.from(parent?.context).inflate(R.layout.scope_text, null)
            (view.findViewById(R.id.text) as TextView).text = t?.name
            views.add(view)
            return view
        }

        override fun onSelected(position: Int, view: View?) {
            super.onSelected(position, view)
            (view?.findViewById(R.id.text) as TextView).isSelected = true
        }

        override fun unSelected(position: Int, view: View?) {
            super.unSelected(position, view)
            (view?.findViewById(R.id.text) as TextView).isSelected = false
        }

    }

}
