package com.bhagavad.hifivedemo.base

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.AndroidViewModel
import com.bhagavad.hifivedemo.R
import com.bhagavad.hifivedemo.toolbar.ToolBarViewModel
import com.bhagavad.hifivedemo.util.AppConstants
import com.bhagavad.hifivedemo.util.DialogUtil
import com.bhagavad.hifivedemo.util.LocaleHelper
import com.bhagavad.hifivedemo.util.SessionPreferences
import java.io.File
import java.util.*

abstract class BaseActivity<T : ViewDataBinding, V : AndroidViewModel, N : ToolBarViewModel>
    : AppCompatActivity() {

    private var mResources: Resources? = null
    private var mViewDataBinding: T? = null
    private var mViewModel: V? = null

    private var mToolBarViewModel: N? = null

    private var TAG = BaseActivity::class.java.simpleName

    var isFullView = false

    private var mToolBarBinding: T? = null

    /**
     * Override for set binding variable
     *
     * @return variable id
     */
    abstract fun getBindingVariable(): Int

    private val REQUEST_DIALOG_ID_FOR_UPDATE = 165


    /**
     * Override for set binding variable
     *
     * @return variable id
     */
//    abstract fun getToolBarBindingVariable(): Int

    /**
     * @return layout resource id
     */
    @LayoutRes
    abstract fun getLayoutId(): Int


    /**
     * Override for set view model
     *
     * @return view model instance
     */


    override fun onCreate(savedInstanceState: Bundle?) {
        var languageToLoad: String = SessionPreferences.loadStringPref(
            this,
            AppConstants.KEY_LANGUAGE
        )!!
        if (languageToLoad.isNullOrEmpty()) {
            languageToLoad = "en"
            SessionPreferences.saveStringPref(this, AppConstants.KEY_LANGUAGE, languageToLoad)
        }
        val locale = Locale(languageToLoad)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        baseContext.resources.updateConfiguration(
            config,
            baseContext.resources.displayMetrics
        )
        super.onCreate(savedInstanceState)
        performDataBinding()
        val contextlang: Context? = LocaleHelper.setLocale(this, languageToLoad)
        mResources = contextlang?.resources


    }

    fun getViewDataBinding(): T {
        return mViewDataBinding!!
    }

    fun getViewToolDataBinding(): T? {
        return mViewDataBinding!!
    }

    private fun performDataBinding() {
        mViewDataBinding = DataBindingUtil.setContentView<T>(this, getLayoutId())
        this.mViewModel = if (mViewModel == null) getViewModel() else mViewModel
        mViewDataBinding!!.setVariable(getBindingVariable(), mViewModel)
        mViewDataBinding!!.executePendingBindings()
        performToolBarBinding()
    }

    // if(mToolBarViewModel!=null)
    private fun performToolBarBinding() {

        Log.e(TAG, "TTTTTTT BaseActivity performToolBarBinding")
        if (getToolBarViewModel() != null) {
            this.mToolBarViewModel =
                if (mToolBarViewModel == null) getToolBarViewModel() as N? else mToolBarViewModel

        }
    }

    abstract fun getViewModel(): V


    abstract fun getToolBarViewModel(): ToolBarViewModel?


    /*
    * get string from edit text
    * */
    fun getEtString(et: EditText): String {
        val str = et.text.toString().trim()
        return str
    }


    /*
    *
    * */
    fun getAppString(id: Int): String {
        if (mResources != null)
            return mResources!!.getString(id)
        else
            return getString(id)

    }


    /*
    * define static methods , call any where as per need
    * */
    companion object {

        /*
         * get color from colors.xml
         * */
        fun getColor(context: Context, id: Int): Int {
            val version = Build.VERSION.SDK_INT
            return if (version >= 23) {
                ContextCompat.getColor(context, id)
            } else {
                context.resources.getColor(id)
            }
        }


        /*
     * get color code
     * */
        fun getParseColor(colorcode: String): Int {
            return Color.parseColor(colorcode)
        }


        /*****************
         * Enable/disable views
         */
        //Set enable / disable view  - true means enable else false means disable
        fun setSelection(view: View, visibility: Boolean) {
            view.isEnabled = visibility
            for (i in 0 until (view as ViewGroup).childCount) {
                val child = view.getChildAt(i)
                if (child is ViewGroup) {
                    setSelection(child, visibility)
                } else {
                    child.isEnabled = visibility
                }
            }
        }


        /*
* show common messages
* */
        fun showErrorMessage(
            context: Context?,
            message: String,
            isExit: Boolean
        ) {

            if (context != null) {
                DialogUtil.okCancelDialog(context,
                    context.resources.getString(R.string.app_name), message,
                    context.resources.getString(R.string.ok),
                    "", true, false, object : DialogUtil.Companion.selectOkCancelListener {
                        override fun okClick() {
                            if (isExit) {
                                SessionPreferences.clearAllPreferenceData(context)
/*
                                var intent = LoginActivity.getIntent(context);
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_CLEAR_TOP or
                                            Intent.FLAG_ACTIVITY_NEW_TASK or
                                            Intent.FLAG_ACTIVITY_CLEAR_TASK

                                (context as Activity).startActivity(intent)*/
                            }

                        }

                        override fun cancelClick() {
                        }


                    })
            }

        }


        /* show hard update messages
        * */
        fun showHardUpdateMessage(
            context: Context?,
            message: String,
            isExit: Boolean
        ) {

            if (context != null) {
                DialogUtil.okCancelDialog(context,
                    context.resources.getString(R.string.app_name), message,
                    context.resources.getString(R.string.update),
                    "", true, false, object : DialogUtil.Companion.selectOkCancelListener {
                        override fun okClick() {
                            val appPackageName: String =
                                context.getPackageName() // getPackageName() from Context or Activity object

                            try {
                                context.startActivity(
                                    Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("market://details?id=$appPackageName")
                                    )
                                )
                            } catch (anfe: ActivityNotFoundException) {
                                context.startActivity(
                                    Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                                    )
                                )
                            }
                        }

                        override fun cancelClick() {
                        }


                    })
            }

        }


        /* show minor update messages
               * */
        fun showMinorUpdateMessage(
            context: Context?,
            message: String,
            isExit: Boolean
        ) {

            if (context != null) {
                DialogUtil.okCancelDialog(context,
                    context.resources.getString(R.string.app_name),
                    message,
                    context.resources.getString(R.string.update),
                    context.resources.getString(R.string.cancel),
                    true,
                    true,
                    object : DialogUtil.Companion.selectOkCancelListener {
                        override fun okClick() {
                            val appPackageName: String =
                                context.getPackageName() // getPackageName() from Context or Activity object

                            try {
                                context.startActivity(
                                    Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("market://details?id=$appPackageName")
                                    )
                                )
                            } catch (anfe: ActivityNotFoundException) {
                                context.startActivity(
                                    Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                                    )
                                )
                            }
                        }

                        override fun cancelClick() {
                        }


                    })
            }

        }

        fun getFileSizeKiloBytes(file: File): Double {
            var t = file.length().toDouble() / 1024
            return t;// ( "$t  kb")
        }
    }


    open fun getDeviceWidth(context: Context): Int {
        val displayMetrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels
        return width
    }


    open fun pxFromDp(context: Context, dp: Float): Float {
        return dp * context.resources.displayMetrics.density
    }


}