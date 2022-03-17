package com.bhagavad.hifivedemo.splash


import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Base64.encode
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.library.BuildConfig
import androidx.lifecycle.ViewModelProvider
import com.bhagavad.hifivedemo.BR
import com.bhagavad.hifivedemo.R
import com.bhagavad.hifivedemo.base.BaseActivity
import com.bhagavad.hifivedemo.dashboard.DashboardActivity
import com.bhagavad.hifivedemo.databinding.ActivitySplashBinding
import com.bhagavad.hifivedemo.databinding.DialogEnterCodeBinding
import com.bhagavad.hifivedemo.login.LoginActivity
import com.bhagavad.hifivedemo.server.serverResponseNavigator
import com.bhagavad.hifivedemo.splash.bean.LoginResponse
import com.bhagavad.hifivedemo.toolbar.ToolBarViewModel
import com.bhagavad.hifivedemo.util.AppConstants
import com.bhagavad.hifivedemo.util.AppUtil
import com.bhagavad.hifivedemo.util.DialogUtil
import com.bhagavad.hifivedemo.util.SessionPreferences
import com.bhagavad.hifivedemo.viewmodalfactory.ViewModelProviderFactory
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import java.net.URLEncoder.encode
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*


class SplashActivity : BaseActivity<ActivitySplashBinding, SplashViewModel, ToolBarViewModel>(),
    SplashNavigator, serverResponseNavigator {
    private lateinit var mViewModel: SplashViewModel
    private lateinit var mBinding: ActivitySplashBinding
    private lateinit var mContext: Context
    private lateinit var mActivity: Activity
    private var mShowNetworkDialog: Dialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()

    }

    fun initView() {
        mContext = this@SplashActivity
        mActivity = this@SplashActivity
        mBinding = getViewDataBinding()
        mViewModel.setNavigator(this)
        getFirebaseToken()

        Handler(Looper.getMainLooper()).postDelayed({
            var isLogin = SessionPreferences.loadStringPref(
                mContext,
                AppConstants.KEY_IS_LOGIN
            )
            if (!isLogin.equals("1"))
                showEnterCodePopup()
            else {
                var userData = SessionPreferences.loadUserDataPref(
                    mContext
                )
                AppConstants.LOGIN_TOKEN = userData!!.authentication_token

                val bundle = Bundle()
                bundle.putParcelable("data", userData)
                AppUtil.startIntent(bundle, mActivity, DashboardActivity::class.java)
                finish()
            }
            // AppUtil.startIntent(null, mActivity, LoginActivity::class.java)
            //finish()

        }, 4000)
    }

    private fun getFirebaseToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
            val token = task.result
            Log.e("firebase_token-->", token.toString())
            AppConstants.DEVICE_TOKEN = token.toString()

        })

    }

    fun showEnterCodePopup() {
        val dialog = Dialog(mContext)//, R.style.AppListDialogTheme)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        dialog.getWindow()!!.getAttributes().windowAnimations = R.style.Dialog_WindowAnimation;

        val binding = DataBindingUtil.inflate<DialogEnterCodeBinding>(
            LayoutInflater.from(mContext),
            R.layout.dialog_enter_code, null, false
        )
        dialog.window!!.decorView.setBackgroundResource(android.R.color.transparent)
        dialog.setContentView(binding.getRoot())
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()


        binding.btnOk.setOnClickListener(View.OnClickListener {
            if (binding.edtCode.text.toString().isNullOrEmpty()) {
                dialog.dismiss()
                showMessage(getAppString(R.string.please_enter_code))
            } else {
                showLoaderOnRequest(true)
                mViewModel.loginAPI(binding.edtCode.text.toString())
                dialog.dismiss()
            }

        })

        binding.btnCancel.setOnClickListener(View.OnClickListener {

            dialog.dismiss()
            finish()
        })

    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_splash
    }


    /*
        * show all common message
        * */
    fun showMessage(msg: String) {
        DialogUtil.okCancelDialog(mContext!!,
            getString(R.string.app_name), msg, getString(R.string.ok),
            "", true, false, object : DialogUtil.Companion.selectOkCancelListener {
                override fun okClick() {
                    finish()
                }

                override fun cancelClick() {

                }
            });
    }


    override fun getViewModel(): SplashViewModel {
        mViewModel = ViewModelProvider(this, ViewModelProviderFactory(application, this))
            .get(SplashViewModel::class.java)
        return mViewModel
    }

    override fun getToolBarViewModel(): ToolBarViewModel? {
        return null
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
        // Toast.makeText(mContext,"Response_came",Toast.LENGTH_LONG).show()
        if (eventType == "getLoginResponse") {

            val bean = Gson().fromJson(response, LoginResponse.LoginBean::class.java)
            AppConstants.LOGIN_TOKEN = bean.data.user.authentication_token
            SessionPreferences.saveStringPref(
                mContext,
                AppConstants.KEY_IS_LOGIN,
                "1"
            )
            SessionPreferences.saveUserDataPref(
                mContext,
                bean.data.user
            )
            val bundle = Bundle()

            bundle.putParcelable("data", bean.data.user)

            AppUtil.startIntent(bundle, mActivity, DashboardActivity::class.java)

            finish()
            /*   val bundle = Bundle()
               bundle.putString("mobile_number", mViewModel.mobilenumberStr)
               bundle.putString("country_code", mViewModel.codeStr)
               bundle.putString("name", bean.data.name)
               AppConstants.LOGIN_TOKEN = bean.data.login_token
               SessionPreferences.saveStringPref(mContext, AppConstants.KEY_LOGIN_TOKEN, bean.data.login_token)
               AppUtil.startIntent(bundle, mActivity, EnterPasswordActivity::class.java)*/
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
        TODO("Not yet implemented")
    }

    override fun onMinorUpdate() {
        showLoaderOnRequest(false)
    }

    override fun onAppHardUpdate() {
        showLoaderOnRequest(false)
    }

    override fun noNetwork() {
        showLoaderOnRequest(false)
    }


}