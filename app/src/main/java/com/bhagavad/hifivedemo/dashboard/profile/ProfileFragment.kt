package com.bhagavad.hifivedemo.dashboard.profile

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.bhagavad.hifivedemo.BR
import com.bhagavad.hifivedemo.R
import com.bhagavad.hifivedemo.base.BaseActivity
import com.bhagavad.hifivedemo.base.BaseFragment
import com.bhagavad.hifivedemo.databinding.FragmentProfileBinding
import com.bhagavad.hifivedemo.server.serverResponseNavigator
import com.bhagavad.hifivedemo.splash.bean.UserDataResponse
import com.bhagavad.hifivedemo.util.*
import com.bhagavad.hifivedemo.viewmodalfactory.ViewModelProviderFactory


class ProfileFragment :
    BaseFragment<FragmentProfileBinding, ProfileFragmentViewModel>(),
    serverResponseNavigator, ProfileFragmentNavigator {
    private lateinit var mContext: Context
    private lateinit var mActivity: Activity
    lateinit var mViewModel: ProfileFragmentViewModel
    var mShowNetworkDialog: Dialog? = null
    lateinit var mBinding: FragmentProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }


    fun initViews() {
        mContext = this@ProfileFragment.activity!!
        mActivity = this@ProfileFragment.activity!!
        mBinding = getViewDataBinding()
        mViewModel.setNavigator(this)
        AppUtil.setupUI(mBinding.llMain, mActivity)

        // Gets the data from the passed bundle
        val bundle = arguments
        var data = bundle!!.getParcelable<UserDataResponse.UserDataBean>("data")

        mBinding.tvUserName.text=data!!.name
        mBinding.tvUniqueId.text=data!!.email

    }

    override fun onStart() {
        super.onStart()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = getViewDataBinding()
        mViewModel.setNavigator(this)
        initViews()
    }


    companion object {
        @JvmStatic
        fun newInstance() =
            ProfileFragment().apply {

            }
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_profile
    }

    override fun getViewModel(): ProfileFragmentViewModel {
        mViewModel =
            ViewModelProvider(this, ViewModelProviderFactory(activity!!.application, this)).get(
                ProfileFragmentViewModel::class.java
            )
        return mViewModel
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




    override fun onCreateClick() {

    }



}
