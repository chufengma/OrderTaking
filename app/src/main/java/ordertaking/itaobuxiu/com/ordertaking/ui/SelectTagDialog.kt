package ordertaking.itaobuxiu.com.ordertaking.ui

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import io.reactivex.Observable
import kotlinx.android.synthetic.main.select_tag_dialog.*
import ordertaking.itaobuxiu.com.ordertaking.R
import ordertaking.itaobuxiu.com.ordertaking.apis.BaseIronInfo
import ordertaking.itaobuxiu.com.ordertaking.apis.IronRequestService
import ordertaking.itaobuxiu.com.ordertaking.apis.Response
import ordertaking.itaobuxiu.com.ordertaking.apis.networkWrap
import ordertaking.itaobuxiu.com.ordertaking.engine.Network
import org.jetbrains.anko.find

/**
 * Created by dev on 2017/11/25.
 */
class SelectTagDialog(val index: Int, context: Context?) : Dialog(context, R.style.Dialog) {

    var listener: OnTagItemClickListener? = null

    interface OnTagItemClickListener {
        fun tagSelected(ironType: BaseIronInfo?)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.select_tag_dialog)

        close.setOnClickListener {
            dismiss()
        }

        var ob: Observable<Response<List<BaseIronInfo>>>? = when (index) {
            0 -> {
                title.text = "选择品名"
                networkWrap(Network.create(IronRequestService::class.java)?.getIronTypes())
            }
            1 -> {
                title.text = "选择材质"
                networkWrap(Network.create(IronRequestService::class.java)?.getMaterials())
            }
            2 -> {
                title.text = "选择产地"
                networkWrap(Network.create(IronRequestService::class.java)?.getProPlaces())
            }
            else -> {
                title.text = "选择表面"
                networkWrap(Network.create(IronRequestService::class.java)?.getSurfaces())
            }
        }

        ob?.subscribe { result ->
            for (ironType in result.data) {
                tagGroup.addView(createTag(ironType))
            }
        }
    }

    fun createTag(ironType: BaseIronInfo): View {
        val view = LayoutInflater.from(context).inflate(R.layout.tag_item_layout, null)
        val tag = view.find<TextView>(R.id.tag)
        tag.text = ironType.name
        tag.tag = ironType
        tag.setOnClickListener {
            listener?.tagSelected(ironType)
            dismiss()
        }
        return view
    }

    fun setOnTagSelectedListener(listener: OnTagItemClickListener) {
        this.listener = listener
    }

    override fun show() {
        super.show()

        val layoutParams = window!!.attributes
        layoutParams.gravity = Gravity.BOTTOM
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        window!!.decorView.setPadding(0, 0, 0, 0)
        window!!.attributes = layoutParams
    }


}