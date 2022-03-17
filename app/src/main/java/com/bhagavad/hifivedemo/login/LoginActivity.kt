package com.bhagavad.hifivedemo.login


import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.library.BuildConfig
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bhagavad.hifivedemo.BR
import com.bhagavad.hifivedemo.R
import com.bhagavad.hifivedemo.base.BaseActivity
import com.bhagavad.hifivedemo.dashboard.DashboardActivity
import com.bhagavad.hifivedemo.databinding.ActivityLoginBinding
import com.bhagavad.hifivedemo.databinding.DialogEnterCodeBinding
import com.bhagavad.hifivedemo.server.serverResponseNavigator
import com.bhagavad.hifivedemo.toolbar.ToolBarViewModel
import com.bhagavad.hifivedemo.util.AppConstants
import com.bhagavad.hifivedemo.util.AppUtil
import com.bhagavad.hifivedemo.util.DialogUtil
import com.bhagavad.hifivedemo.viewmodalfactory.ViewModelProviderFactory
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel, ToolBarViewModel>(),
    LoginNavigator, serverResponseNavigator {
    private lateinit var mViewModel: LoginViewModel
    private lateinit var mBinding: ActivityLoginBinding
    private lateinit var mContext: Context
    private lateinit var mActivity: Activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()

    }

    fun initView() {
        mContext = this@LoginActivity
        mActivity = this@LoginActivity
        mBinding = getViewDataBinding()
        mViewModel.setNavigator(this)
        getSig(mContext,"SHA1")
        getFirebaseToken()
        mViewModel.geResult().observe(this, Observer { result ->

            if (result.equals("")) {

                //AppUtil.startIntent(null, mActivity, DashboardActivity::class.java)

                showEnterCodePopup()
            } else {
                showMessage(result)
            }
        })
    }

    private fun getFirebaseToken(){
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.e("qwerty-->", "LmsMessagingService Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            val token = task.result
            Log.e("firebase_token-->", token.toString())
            AppConstants.DEVICE_TOKEN=token.toString()

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
                AppUtil.startIntent(null, mActivity, DashboardActivity::class.java)

                dialog.dismiss()
            }

        })

        binding.btnCancel.setOnClickListener(View.OnClickListener {

            dialog.dismiss()
        })

    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_login
    }


    /*
        * show all common message
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


    override fun getViewModel(): LoginViewModel {
        mViewModel = ViewModelProvider(this, ViewModelProviderFactory(application, this))
            .get(LoginViewModel::class.java)
        return mViewModel
    }

    override fun getToolBarViewModel(): ToolBarViewModel? {
        return null
    }


    override fun showLoaderOnRequest(isShowLoader: Boolean) {
        TODO("Not yet implemented")
    }

    override fun onResponse(eventType: String, response: String) {
        TODO("Not yet implemented")
    }

    override fun onRequestFailed(eventType: String, response: String) {
        TODO("Not yet implemented")
    }

    override fun onRequestRetry() {
        TODO("Not yet implemented")
    }

    override fun onSessionExpire() {
        TODO("Not yet implemented")
    }

    override fun onMinorUpdate() {
        TODO("Not yet implemented")
    }

    override fun onAppHardUpdate() {
        TODO("Not yet implemented")
    }

    override fun noNetwork() {
        TODO("Not yet implemented")
    }

    fun getSig(context: Context, key: String) {
        val info: PackageInfo
        try {
            info = packageManager.getPackageInfo(
                "com.bhagavad.hifivedemo", PackageManager.GET_SIGNATURES
            )
            for (signature in info.signatures) {
                var md: MessageDigest
                md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val hash_key: String = String(Base64.encode(md.digest(), 0))
                Log.e("sha1_key",hash_key)
              //  Toast.makeText(context,"-->"+hash_key,Toast.LENGTH_LONG).show()
            }
        } catch (e1: PackageManager.NameNotFoundException) {
            Toast.makeText(context,"-->"+e1.toString(),Toast.LENGTH_LONG).show()

        } catch (e: NoSuchAlgorithmException) {
            Toast.makeText(context,"-->"+e.toString(),Toast.LENGTH_LONG).show()

        } catch (e: java.lang.Exception) {
            Toast.makeText(context,"-->"+e.toString(),Toast.LENGTH_LONG).show()

        }


    }
}