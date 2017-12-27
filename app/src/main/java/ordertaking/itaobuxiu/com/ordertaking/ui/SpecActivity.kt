package ordertaking.itaobuxiu.com.ordertaking.ui

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spec)
        useNormalBack()

        var adpater = SpecAdapter()
        suggestRecycler.layoutManager = LinearLayoutManager(this)
        suggestRecycler.adapter = adpater

        var ironid = intent.getStringExtra("ironId")
        var surfaceId = intent.getStringExtra("surfaceId")

        var weightStr: String = intent.getStringExtra("weight")
        var heightStr: String = intent.getStringExtra("height")
        var lengthStr: String = intent.getStringExtra("length")

        height.setText(heightStr)
        weight.setText(weightStr)
        length.setText(lengthStr)

        banjuan = intent.getBooleanExtra("banjuan", banjuan)

        banjuanLayout.visibility = if (banjuan) View.VISIBLE else View.GONE
        specLayout.visibility = if (banjuan) View.GONE else View.VISIBLE

        if (banjuan && ironid.isNotBlank() && surfaceId.isNotBlank()) {
            networkWrap(Network.create(IronRequestService::class.java)?.getSuggestSpec(surfaceId, ironid))
                    ?.subscribe { result ->
                        adpater.updateData(result.data)
                    }
        }

        adpater.setOnSpecSelectedListener(object: OnSpecItemClickListener {
            override fun specSelected(spec: SuggestSpecModel?) {
                height.setText(spec?.height)
                weight.setText(spec?.weight)
                length.setText(spec?.length)
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
    }
}
