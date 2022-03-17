package com.bhagavad.hifivedemo.country


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bhagavad.hifivedemo.R
import com.bhagavad.hifivedemo.country.bean.UsersAndGreetingListResponse
import com.bhagavad.hifivedemo.databinding.DialogItemRowBinding
import java.util.*



class UsersDialogListAdapter(
    private val mContext: Context,
    internal var dialogListener: dialogItemSelectedInterface
) : RecyclerView.Adapter<UsersDialogListAdapter.ViewHolder>() {

    private var TAG = UsersDialogListAdapter::class.java.simpleName
    private lateinit var mDataArr: ArrayList<UsersAndGreetingListResponse.UserData>


    init {

    }


    fun setList(optArr: ArrayList<UsersAndGreetingListResponse.UserData>) {
        mDataArr = ArrayList<UsersAndGreetingListResponse.UserData>()
        mDataArr = optArr
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_country_row, parent, false);
        val binding = DataBindingUtil.inflate<DialogItemRowBinding>(
            LayoutInflater.from(mContext),
            R.layout.dialog_item_row, parent, false
        )
        return ViewHolder(binding.getRoot())
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = mDataArr!!.get(position)
        holder.binding!!.tvItem.setText(model.name)
        holder.binding!!.tvCode.setText("" + model.employee_id)




        holder.binding!!.rowRl.setOnClickListener(View.OnClickListener {
            dialogListener.dialogItem(
                position
            )
        })
    }

    override fun getItemCount(): Int {
        return mDataArr!!.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val binding: DialogItemRowBinding?

        init {
            binding = DataBindingUtil.bind<DialogItemRowBinding>(itemView) as DialogItemRowBinding?
        }
    }

}
