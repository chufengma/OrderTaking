package ordertaking.itaobuxiu.com.ordertaking.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_recycler_view_seller.*
import ordertaking.itaobuxiu.com.ordertaking.BaseActivity
import ordertaking.itaobuxiu.com.ordertaking.R
import ordertaking.itaobuxiu.com.ordertaking.apis.*
import ordertaking.itaobuxiu.com.ordertaking.engine.Network

/**
 * A simple [Fragment] subclass.
 */
class RecyclerViewSellerFragment : Fragment() {

    var adapter: IronOfferInfoAdapter? = null
    var data: MutableList<SellerOfferInfoListItem> = mutableListOf()

    var buyStatus:Int = 0
    var today:Int = 0
    var currentPage = 1

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_recycler_view_seller, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        buyStatus = arguments.getInt("buyStatus")
        today = arguments.getInt("today")

        Log.e("RecyclerViewSellerFragment", "$today $buyStatus")

        var mLayoutManager = LinearLayoutManager(context)
        ironInfoDataRecyclerSeller.layoutManager = mLayoutManager
        adapter = IronOfferInfoAdapter()
        ironInfoDataRecyclerSeller.adapter = adapter

        ironInfoDataRecyclerSeller.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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

        configSwipe(ironBuySwipeLayoutSeller)

        adapter?.setActionListener(object: OnIronOfferActionListener{

            override fun onOfferIt(request: SellerOfferInfoListItem) {
                gotoIronOfferDetail(context, request)
            }

            override fun onChangeOffer(request: SellerOfferInfoListItem) {
                gotoIronOfferDetail(context, request)
            }

            override fun onContactBuyer(request: SellerOfferInfoListItem) {
                (context as BaseActivity).showCall(request.contactNum, request.contact)
            }

            override fun onItemClick(request: SellerOfferInfoListItem) {
                gotoIronOfferDetail(context, request)
            }

            override fun onMiss(request: SellerOfferInfoListItem) {
                (activity as BaseActivity).showLoading()
                networkWrap(Network.create(IronBuyOfferService::class.java)?.missOffer(request?.id!!))
                        ?.subscribe({
                            (activity as BaseActivity).hideLoading()
                            (activity as BaseActivity). toastInfo("操作成功")
                            SellerFragment.notifyRefrsh(null)
                        }, { error ->
                            (activity as BaseActivity).hideLoading()
                            (activity as BaseActivity).toastInfo("操作失败：" + error.message)
                        })
            }

        })

        ironBuySwipeLayoutSeller.setOnRefreshListener {
            refreshData()
        }

        if (isLogin()) {
            ironBuySwipeLayoutSeller.isRefreshing = true
            fetchData(true)
        }

        SellerFragment.addRefreshListener {
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
        networkWrap(Network.create(IronBuyOfferService::class.java)?.getIronBuyOffer(currentPage, 15, buyStatus, today))
                ?.subscribe({ result ->
                    if (result.data.never != null
                            || result.data.offer != null
                            || result.data.miss != null
                            || result.data.deal != null)
                        SellerFragment.notify(result.data.never, result.data.offer, result.data.deal, result.data.miss, today)
                    if (result.data.list != null) {
                        if (startCurrentPage == 1) {
                            data.clear()
                        }
                        data.addAll(result.data.list!!)
                    }
                    updateData()

                    ironBuySwipeLayoutSeller.isRefreshing = false
                    (activity as BaseActivity).hideLoading()
                },
                { error ->
                    (activity as BaseActivity).hideLoading()
                    ironBuySwipeLayoutSeller.isRefreshing = false
                })
    }

    override fun onResume() {
        super.onResume()
        refreshData()
    }

    fun updateData() {
        if (data == null || data.isEmpty()) {
            emptyViewSeller.visibility = View.VISIBLE
            ironInfoDataRecyclerSeller.visibility = View.GONE
        } else {
            emptyViewSeller.visibility = View.GONE
            ironInfoDataRecyclerSeller.visibility = View.VISIBLE
            adapter?.updateData(data)
        }
    }

}