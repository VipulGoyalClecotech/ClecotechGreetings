package com.bhagavad.hifivedemo.dashboard.home

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.text.Html
import android.view.*

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.bhagavad.hifivedemo.BR
import com.bhagavad.hifivedemo.R
import com.bhagavad.hifivedemo.base.BaseActivity
import com.bhagavad.hifivedemo.base.BaseFragment
import com.bhagavad.hifivedemo.create.CreateActivity
import com.bhagavad.hifivedemo.dashboard.home.adapter.HomeListAdapter
import com.bhagavad.hifivedemo.dashboard.home.bean.HomeListResponse
import com.bhagavad.hifivedemo.databinding.DialogFilterPopupBinding
import com.bhagavad.hifivedemo.databinding.DialogShowGreetingBinding
import com.bhagavad.hifivedemo.databinding.FragmentHomeListBinding
import com.bhagavad.hifivedemo.server.serverResponseNavigator
import com.bhagavad.hifivedemo.splash.bean.UserDataResponse
import com.bhagavad.hifivedemo.util.AppConstants
import com.bhagavad.hifivedemo.util.AppUtil
import com.bhagavad.hifivedemo.util.DialogUtil
import com.bhagavad.hifivedemo.util.PaginationScrollListener
import com.bhagavad.hifivedemo.viewmodalfactory.ViewModelProviderFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson


class HomeListFragment :
    BaseFragment<FragmentHomeListBinding, HomeListFragmentViewModel>(),
    serverResponseNavigator, HomeListFragmentNavigator {
    private lateinit var mContext: Context
    private lateinit var mActivity: Activity
    lateinit var mViewModel: HomeListFragmentViewModel
    var mShowNetworkDialog: Dialog? = null
    lateinit var mBinding: FragmentHomeListBinding
    lateinit var mGreetingList: ArrayList<HomeListResponse.Greeting>
    lateinit var mAdapter: HomeListAdapter
    lateinit var mLayoutManager: LinearLayoutManager
    lateinit var userData: UserDataResponse.UserDataBean
    private var description = ""
    private var currentPage: Int = 1
    private var senderId: String = ""
    private var receiverId: String = ""
    private var isLoadMoreList: Boolean = false

    private var isLoadMoreClassesList: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    fun initViews() {
        mContext = this@HomeListFragment.activity!!
        mActivity = this@HomeListFragment.activity!!
        mBinding = getViewDataBinding()
        mViewModel.setNavigator(this)
        AppUtil.setupUI(mBinding.llMain, mActivity)
        mGreetingList = ArrayList()

        // Gets the data from the passed bundle
        val bundle = arguments
        userData = bundle!!.getParcelable<UserDataResponse.UserDataBean>("data")!!


        //these is adapter initialization of notification list
        mLayoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        mBinding.rvNotificationList.setHasFixedSize(true)
        mBinding.rvNotificationList.setLayoutManager(mLayoutManager)
        mBinding.rvNotificationList.setItemAnimator(DefaultItemAnimator())
        mAdapter = HomeListAdapter(mContext, mGreetingList, mViewModel, this)
        mBinding.rvNotificationList.setAdapter(mAdapter)

        // setting load more functionality
        settingGeetingsListLoadMore(mLayoutManager)
    }

    override fun onStart() {
        super.onStart()

    }

    fun filterClickMethod(context: Context) {
        showFilterPopup(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = getViewDataBinding()
        mViewModel.setNavigator(this)

        initViews()
    }


    override fun onResume() {
        super.onResume()
        showLoaderOnRequest(true)
        mViewModel.listingWithFilterAPI(currentPage.toString(), senderId, receiverId)

    }


    companion object {
        @JvmStatic
        fun newInstance() =
            HomeListFragment().apply {

            }
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_home_list
    }

    override fun getViewModel(): HomeListFragmentViewModel {
        mViewModel =
            ViewModelProvider(this, ViewModelProviderFactory(activity!!.application, this)).get(
                HomeListFragmentViewModel::class.java
            )
        return mViewModel
    }

    //override fun onPrepareOptionsMenu(menu: Menu) {
    //    menu.clear()
   // }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear();
    inflater?.inflate(R.menu.menu_filter, menu)


}



override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item?.itemId) {
        R.id.filter ->
        {
            showFilterPopup(mContext)
            return true
        }

        else -> return super.onOptionsItemSelected(item)
    }
}

    override fun showLoaderOnRequest(isShowLoader: Boolean) {
        if (isShowLoader && mShowNetworkDialog == null) {
            mShowNetworkDialog = DialogUtil.showLoader(mContext!!)
        } else if (isShowLoader && !mShowNetworkDialog!!.isShowing()) {
            mShowNetworkDialog = null
            mShowNetworkDialog = DialogUtil.showLoader(mContext!!)
        } else {
            if (mShowNetworkDialog != null && isShowLoader == false) {
                DialogUtil.hideLoaderDialog(mShowNetworkDialog!!)
                mShowNetworkDialog = null
            }
        }
    }

    override fun onResponse(eventType: String, response: String) {
        showLoaderOnRequest(false)
        if (eventType == "greetingListingResponse") {

            val bean = Gson().fromJson(response, HomeListResponse.HomeListBean::class.java)

            //showMessage(bean.message)


            if (currentPage == 1) {
                mGreetingList.clear()

                mBinding.tvNoRecord.visibility = View.GONE
                mBinding.rvNotificationList.visibility = View.VISIBLE
            }

            //setting data for subjects detail list
            var mList = bean.data.Greetings as ArrayList<HomeListResponse.Greeting>
            mGreetingList.addAll(mList)
            mAdapter.notifyDataSetChanged()


            //// setting is load more check
            isLoadMoreList = mList.size === AppConstants.STATIC_PAGECOUNT_CHECK_SIZE


            if ((mGreetingList == null) || (mGreetingList.size == 0)) {

                mBinding.tvNoRecord.visibility = View.VISIBLE
                mBinding.rvNotificationList.visibility = View.GONE
            }

        }
    }

    override fun onRequestFailed(eventType: String, response: String) {
        showLoaderOnRequest(false)
        showMessage(response)


    }

    override fun onRequestRetry() {
        showLoaderOnRequest(false)

    }

    override fun onSessionExpire() {
        showLoaderOnRequest(false)
        BaseActivity.showErrorMessage(
            mContext,
            getString(R.string.session_expire_msg),
            true
        )
    }

    override fun onMinorUpdate() {
        showLoaderOnRequest(false)
        BaseActivity.showMinorUpdateMessage(
            mContext,
            getString(R.string.new_update_available),
            false
        )
    }

    override fun onAppHardUpdate() {
        showLoaderOnRequest(false)
        BaseActivity.showHardUpdateMessage(
            mContext,
            getString(R.string.new_update_available),
            true
        )
    }

    override fun noNetwork() {
        showLoaderOnRequest(false)
        BaseActivity.showErrorMessage(
            mContext,
            getString(R.string.No_internet_connection),
            false
        )
    }

    /*
        * show all common message from one place
        * */
    fun showMessage(msg: String) {
        DialogUtil.okCancelDialog(mContext!!,
            getString(R.string.app_name), msg, getString(R.string.ok),
            "", true, false, object : DialogUtil.Companion.selectOkCancelListener {
                override fun okClick() {

                }

                override fun cancelClick() {

                }

            });
    }


    override fun listItemClick(position: Int) {
        showGreetingPopup(mGreetingList.get(position))

    }
    fun showGreetingPopup(data:HomeListResponse.Greeting) {
        val dialog = Dialog(mContext)//, R.style.AppListDialogTheme)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        dialog.getWindow()!!.getAttributes().windowAnimations = R.style.Dialog_WindowAnimation;

        val binding = DataBindingUtil.inflate<DialogShowGreetingBinding>(
            LayoutInflater.from(mContext),
            R.layout.dialog_show_greeting, null, false
        )
        dialog.window!!.decorView.setBackgroundResource(android.R.color.transparent)
        dialog.setContentView(binding.getRoot())
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()

            binding!!.tvSenderName.setText(Html.fromHtml("<font color=#cc0029>"+mContext.getString(R.string.from)+"</font> "+data.sender.email))
        binding!!.tvReceiverName.setText(Html.fromHtml("<font color=#3AEF1F>"+mContext.getString(R.string.to)+"</font> "+data.receiver.email))
        binding!!.tvTitle.setText(data.title)
        binding!!.tvMessage.setText(data.message)


        val requestOptions = RequestOptions()
        requestOptions.placeholder(R.drawable.default_profile)
        requestOptions.error(R.drawable.default_profile)
        Glide.with(mContext)
            .setDefaultRequestOptions(requestOptions)
            .load(AppConstants.IMAGE_BASE_URL+data.image)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(binding!!.ivGreetingImage)

        binding.btnCancel.setOnClickListener(View.OnClickListener {

            dialog.dismiss()

        })

    }

    override fun onNoRecordClick() {

    }

    override fun onCreateClick() {
        AppUtil.startIntent(null, mActivity, CreateActivity::class.java)

    }

    override fun onFilterClick() {
        showFilterPopup(mContext)
    }


    fun showFilterPopup(mContext: Context) {

        val dialog = Dialog(mContext)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        dialog.getWindow()!!.getAttributes().windowAnimations = R.style.Dialog_WindowAnimation;

        val binding = DataBindingUtil.inflate<DialogFilterPopupBinding>(
            LayoutInflater.from(mContext),
            R.layout.dialog_filter_popup, null, false
        )
        dialog.window!!.decorView.setBackgroundResource(android.R.color.transparent)
        dialog.setContentView(binding.getRoot())
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()


        binding.btnOk.setOnClickListener(View.OnClickListener {

            if (binding.switchSent.isChecked)
                senderId = "1"
            else
                senderId = ""

            if (binding.switchReceived.isChecked)
                receiverId = "1"
            else
                receiverId = ""

            currentPage = 1



            callApi(mContext)
            dialog.dismiss()

        })

        binding.btnCancel.setOnClickListener(View.OnClickListener {

            dialog.dismiss()
        })

    }
    private fun callApi(context: Context)
    {

        showLoaderOnRequest(true)
        mViewModel.listingWithFilterAPI(currentPage.toString(), senderId, receiverId)

    }
    private fun settingGeetingsListLoadMore(mLayoutManager: LinearLayoutManager) {
        mBinding.rvNotificationList.addOnScrollListener(object :
            PaginationScrollListener(mLayoutManager) {
            override fun loadMoreItems() {
                // making network delay for API call
                Handler().postDelayed({
                    if (isLoadMoreList) {
                        isLoadMoreList = false
                        currentPage = currentPage + 1

                        mViewModel.listingWithFilterAPI(currentPage.toString(), senderId,receiverId)

                    }
                }, 1000)
            }

            override val totalPageCount: Int
                get() = currentPage

            override val isLastPage: Boolean
                get() = false

            override val isLoading: Boolean
                get() = false
        })
    }



}
