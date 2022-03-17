package com.bhagavad.hifivedemo.dashboard.setting

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bhagavad.hifivedemo.BR
import com.bhagavad.hifivedemo.R
import com.bhagavad.hifivedemo.base.BaseActivity
import com.bhagavad.hifivedemo.base.BaseFragment
import com.bhagavad.hifivedemo.dashboard.DashboardActivity
import com.bhagavad.hifivedemo.dashboard.setting.bean.LogoutResponse
import com.bhagavad.hifivedemo.databinding.DialogEnterCodeBinding
import com.bhagavad.hifivedemo.databinding.DialogLogoutBinding

import com.bhagavad.hifivedemo.databinding.FragmentSettingBinding
import com.bhagavad.hifivedemo.login.LoginActivity
import com.bhagavad.hifivedemo.server.serverResponseNavigator
import com.bhagavad.hifivedemo.splash.bean.LoginResponse
import com.bhagavad.hifivedemo.util.*
import com.bhagavad.hifivedemo.viewmodalfactory.ViewModelProviderFactory
import com.google.gson.Gson


class SettingFragment :
    BaseFragment<FragmentSettingBinding, SettingFragmentViewModel>(),
    serverResponseNavigator, SettingFragmentNavigator {
    private lateinit var mContext: Context
    private lateinit var mActivity: Activity
    lateinit var mViewModel: SettingFragmentViewModel
    var mShowNetworkDialog: Dialog? = null
    lateinit var mBinding: FragmentSettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }


    fun initViews() {
        mContext = this@SettingFragment.activity!!
        mActivity = this@SettingFragment.activity!!
        mBinding = getViewDataBinding()
        mViewModel.setNavigator(this)
        AppUtil.setupUI(mBinding.llMain, mActivity)

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
            SettingFragment().apply {

            }
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_setting
    }

    override fun getViewModel(): SettingFragmentViewModel {
        mViewModel =
            ViewModelProvider(this, ViewModelProviderFactory(activity!!.application, this)).get(
                SettingFragmentViewModel::class.java
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
        if (eventType == "getLogoutResponse") {

            val bean = Gson().fromJson(response, LogoutResponse.LogoutBean::class.java)
            SessionPreferences.clearAllPreferenceData(mContext)
            mActivity.finish()

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




    override fun onCreateClick() {

    }

    override fun onLogoutClick() {

        showLogoutPopup()
    }
    fun showLogoutPopup() {
        val dialog = Dialog(mContext)//, R.style.AppListDialogTheme)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        dialog.getWindow()!!.getAttributes().windowAnimations = R.style.Dialog_WindowAnimation;

        val binding = DataBindingUtil.inflate<DialogLogoutBinding>(
            LayoutInflater.from(mContext),
            R.layout.dialog_logout, null, false
        )
        dialog.window!!.decorView.setBackgroundResource(android.R.color.transparent)
        dialog.setContentView(binding.getRoot())
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()


        binding.btnOk.setOnClickListener(View.OnClickListener {
            showLoaderOnRequest(true)
            mViewModel.logoutAPI()




        })

        binding.btnCancel.setOnClickListener(View.OnClickListener {

            dialog.dismiss()
        })

    }

}
