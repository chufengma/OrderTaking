package ordertaking.itaobuxiu.com.ordertaking.ui

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.sdsmdg.tastytoast.TastyToast
import kotlinx.android.synthetic.main.activity_spec.*
import ordertaking.itaobuxiu.com.ordertaking.BaseActivity
import ordertaking.itaobuxiu.com.ordertaking.R
import ordertaking.itaobuxiu.com.ordertaking.apis.IronRequestService
import ordertaking.itaobuxiu.com.ordertaking.apis.SuggestSpecModel
import ordertaking.itaobuxiu.com.ordertaking.apis.networkWrap
import ordertaking.itaobuxiu.com.ordertaking.engine.Network

class SpecActivity : BaseActivity() {

    var banjuan: Boolean = false
    var ironid = ""
    var surfaceId = ""
    var adpater: SpecAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spec)
        useNormalBack()

        adpater = SpecAdapter()
        suggestRecycler.layoutManager = LinearLayoutManager(this)
        suggestRecycler.adapter = adpater

        ironid = intent.getStringExtra("ironId")
        surfaceId = intent.getStringExtra("surfaceId")

        var weightStr: String = intent.getStringExtra("weight")
        var heightStr: String = intent.getStringExtra("height")
        var lengthStr: String = intent.getStringExtra("length")

        var otherStr: String? = intent.getStringExtra("otherStr")

        height.setText(heightStr)
        weight.setText(weightStr)
        length.setText(lengthStr)

        spec.setText(otherStr)

        banjuan = intent.getBooleanExtra("banjuan", banjuan)

        banjuanLayout.visibility = if (banjuan) View.VISIBLE else View.GONE
        specLayout.visibility = if (banjuan) View.GONE else View.VISIBLE

        adpater?.setOnSpecSelectedListener(object : OnSpecItemClickListener {
            override fun specSelected(spec: SuggestSpecModel?) {
                if (!spec?.height.isNullOrBlank()) {
                    height.setText(spec?.height)
                }
                if (!spec?.width.isNullOrBlank()) {
                    weight.setText(spec?.width)
                }
                if (!spec?.length.isNullOrBlank()) {
                    length.setText(spec?.length)
                }
            }
        })

        right.setOnClickListener {
            if (banjuan && (height.text.toString().isNullOrBlank()
                    || weight.text.toString().isNullOrBlank()
                    || length.text.toString().isNullOrBlank())) {
                toast(this, "请输入完整规格", TastyToast.INFO)
                return@setOnClickListener
            }

            if (!banjuan && spec.text.toString().isNullOrBlank()) {
                toast(this, "请输入规格", TastyToast.INFO)
                return@setOnClickListener
            }

            intent.putExtra("weight", weight.text.toString())
            intent.putExtra("height", height.text.toString())
            intent.putExtra("length", length.text.toString())
            intent.putExtra("spec", spec.text.toString())
            setResult(Activity.RESULT_OK, intent)
            finish()
        }


        height.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                updateValues(height.text.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })

        height.postDelayed({
            updateValues(heightStr)
        }, 200)

        height.setOnFocusChangeListener { v, hasFocus ->
            updateValues(height.text.toString())
        }

        weight.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                updateValues(height.text.toString())
            }
        }

        length.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                updateValues(height.text.toString())
            }
        }
    }

    fun updateValues(text: String) {
        if (banjuan && ironid.isNotBlank() && surfaceId.isNotBlank()) {
            if (text.isNullOrBlank() || height.hasFocus()) {
                networkWrap(Network.create(IronRequestService::class.java)?.getSuggestSpec(surfaceId, ironid, weight.text.toString(),
                        height.text.toString(),
                        length.text.toString()
                        ))
                        ?.subscribe({ result ->
                            adpater?.updateData(result.data)
                        }, {})
            } else {
                networkWrap(Network.create(IronRequestService::class.java)?.getSuggestSpec2(surfaceId, ironid))
                        ?.subscribe({ result ->
                            adpater?.updateData(result.data)
                        }, {})
            }
        }
    }
}
