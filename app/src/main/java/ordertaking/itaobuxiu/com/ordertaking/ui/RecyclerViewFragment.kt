package ordertaking.itaobuxiu.com.ordertaking.ui


import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_recycler_view.*
import ordertaking.itaobuxiu.com.ordertaking.BaseActivity

import ordertaking.itaobuxiu.com.ordertaking.R
import ordertaking.itaobuxiu.com.ordertaking.engine.Network
import android.support.v7.widget.RecyclerView
import ordertaking.itaobuxiu.com.ordertaking.apis.*


@SuppressLint("ValidFragment")
/**
 * A simple [Fragment] subclass.
 */
class RecyclerViewFragment(val buyStatus:Int, val today: Int) : Fragment() {

    var adapter: IronBuyInfoAdapter? = null
    var data: MutableList<IronBuyInfo> = mutableListOf()

    var currentPage = 1

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_recycler_view, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        var mLayoutManager = LinearLayoutManager(context)
        ironInfoDataRecycler.layoutManager = mLayoutManager
        adapter = IronBuyInfoAdapter()
        ironInfoDataRecycler.adapter = adapter

        ironInfoDataRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                if (dy > 0)
                {
                    var visibleItemCount = mLayoutManager.getChildCount()
                    var totalItemCount = mLayoutManager.getItemCount()
                    var pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition()

                    if (visibleItemCount + pastVisiblesItems >= totalItemCount) {
                        currentPage++
                        fetchData(false)
                    }
                }
            }
        })

        configSwipe(ironBuySwipeLayout)

        adapter?.setActionListener(object: OnIronBuyInfoActionListener {
            override fun onCopy(request: IronBuyInfo) {
                gotoNewRequest(context, request.toPostReuqestBean(), false)
            }

            override fun onDelete(request: IronBuyInfo) {
                AlertDialog.Builder(context).setMessage("确认删除")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", {dialog, which ->
                            (context as BaseActivity).showLoading()
                            doEditIronBuy(request, false)?.subscribe({
                                (context as BaseActivity).hideLoading()
                                (context as BaseActivity).toastInfo("删除成功")
                                BuyerFragment.notifyRefrsh(null)
                            }, { error ->
                                (context as BaseActivity).hideLoading()
                                (context as BaseActivity).toastInfo("删除失败：" + error.message)
                            })

                            dialog.dismiss()
                        }).show()
            }

            override fun onEdit(request: IronBuyInfo) {
                gotoNewRequest(context, request.toPostReuqestBean(), true)
            }

            override fun onContact(request: IronBuyInfo) {
                if (request.buyStatus == 2) {
                    (context as BaseActivity).showCall(request.ironSell?.validSell?.get(0)?.contactNum)
                }
            }

            override fun onItemClick(request: IronBuyInfo) {
                gotoIronBuyDetail(context, request)
            }

        })

        ironBuySwipeLayout.setOnRefreshListener {
            refreshData()
        }

        if (isLogin()) {
            ironBuySwipeLayout.isRefreshing = true
            fetchData(true)
        }

        BuyerFragment.addRefreshListener {
            refreshData()
        }
    }

    fun refreshData() {
        currentPage = 1
        fetchData(false)
    }

    fun fetchData(withLoading: Boolean) {
        if (withLoading) {
            (activity as BaseActivity).showLoading()
        }
        var startCurrentPage = currentPage
        networkWrap(Network.create(IronRequestService::class.java)?.getIronBuyInfo(currentPage ,15, buyStatus, today))
                ?.subscribe({ result ->
                    if (result.data.ing != null
                            || result.data.get != null
                            || result.data.end != null)
                    BuyerFragment.notify(result.data.ing, result.data.get, result.data.end, today)
                    if (result.data.list != null) {
                        if (startCurrentPage == 1) {
                            data.clear()
                        }
                        data.addAll(result.data.list!!)
                    }
                    updateData()

                    ironBuySwipeLayout.isRefreshing = false
                    (activity as BaseActivity).hideLoading()
                },
                { error ->
                    (activity as BaseActivity).hideLoading()
                    ironBuySwipeLayout.isRefreshing = false
                })
    }

    fun updateData() {
        if (data == null || data.isEmpty()) {
            emptyView.visibility = View.VISIBLE
            ironInfoDataRecycler.visibility = View.GONE
        } else {
            emptyView.visibility = View.GONE
            ironInfoDataRecycler.visibility = View.VISIBLE
            adapter?.updateData(data)
        }
    }

}
