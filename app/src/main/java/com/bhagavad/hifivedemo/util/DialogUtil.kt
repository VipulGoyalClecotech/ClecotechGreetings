package com.bhagavad.hifivedemo.util

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import androidx.databinding.DataBindingUtil
import com.bhagavad.hifivedemo.R
import com.bhagavad.hifivedemo.databinding.DialogOkCancelBinding

public class DialogUtil {

    companion object {

        /*
        * ok cancel dialog
        * */
        public fun okCancelDialog(
            context: Context, title: String, msg: String, okText: String, cancelText: String,
            isOk: Boolean, isCancel: Boolean, selectOkCancelListener: selectOkCancelListener
        ): Dialog {

            val dialog = Dialog(context)//, R.style.AppListDialogTheme)

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

            dialog.getWindow()!!.getAttributes().windowAnimations = R.style.Dialog_WindowAnimation;

            val binding = DataBindingUtil.inflate<DialogOkCancelBinding>(
                LayoutInflater.from(context),
                R.layout.dialog_ok_cancel, null, false
            )
            dialog.window!!.decorView.setBackgroundResource(android.R.color.transparent)
            dialog.setContentView(binding.getRoot())

            dialog.setCancelable(false)
            dialog.setCanceledOnTouchOutside(false)


            if (!TextUtils.isEmpty(msg)) {
                dialog.show()
            } else {
                dialog.dismiss()
            }


            if (TextUtils.isEmpty(title)) {
                binding.tvTitle.setVisibility(View.GONE)
            } else {
                binding.tvTitle.setText(title)
            }
            if (!TextUtils.isEmpty(msg)) {
                binding.tvMessage.setText(msg)
            }


            if (!TextUtils.isEmpty(okText)) {
                binding.btnOk.setText(okText)
            }
            if (!TextUtils.isEmpty(cancelText)) {
                binding.btnCancel.setText(cancelText)
            }

            if (isOk == false) {
                binding.btnOk.setVisibility(View.GONE)
                binding.viewSeparator.setVisibility(View.GONE)
            }

            if (isCancel == false) {
                binding.viewSeparator.setVisibility(View.GONE)
                binding.btnCancel.setVisibility(View.GONE)
            }

            binding.btnOk.setOnClickListener(View.OnClickListener {
                selectOkCancelListener.okClick()
                dialog.dismiss()
            })

            binding.btnCancel.setOnClickListener(View.OnClickListener {
                selectOkCancelListener.cancelClick()
                dialog.dismiss()
            })

            return dialog
        }


        interface selectOkCancelListener {
            public fun okClick()
            public fun cancelClick()
        }


        // loader dialog
        fun showLoader(context: Context): Dialog {
            var loaderDialog = Dialog(context, R.style.full_screen_dialog)
            loaderDialog.setContentView(R.layout.progress_loader)
            loaderDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            loaderDialog.setCancelable(false)
            loaderDialog.setCanceledOnTouchOutside(false)
            loaderDialog.show()
            return loaderDialog;
        }

        fun hideLoaderDialog(dialog: Dialog) {
            if(dialog!=null)
            dialog.dismiss()

        }


    }

}