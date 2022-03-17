package com.bhagavad.hifivedemo.create.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bhagavad.hifivedemo.BR
import com.bhagavad.hifivedemo.R
import com.bhagavad.hifivedemo.country.bean.UsersAndGreetingListResponse
import com.bhagavad.hifivedemo.create.CreateNavigator
import com.bhagavad.hifivedemo.create.CreateViewModel
import com.bhagavad.hifivedemo.databinding.ItemSelectedFriendsListBinding


class SelectedFriendsListAdapter(
    context: Context,
    list: ArrayList<UsersAndGreetingListResponse.UserData>,
    viewModel: CreateViewModel,
    navigator: CreateNavigator
) :
    RecyclerView.Adapter<SelectedFriendsListAdapter.ViewHolder>() {
    lateinit var mContext: Context
    lateinit var mNavigator: CreateNavigator
    lateinit var mViewModel: CreateViewModel
    lateinit var mList: ArrayList<UsersAndGreetingListResponse.UserData>


    init {
        mNavigator = navigator
        mContext = context
        mViewModel = viewModel
        mList = list
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = DataBindingUtil.inflate<ItemSelectedFriendsListBinding>(
            LayoutInflater.from(mContext),
            R.layout.item_selected_friends_list, parent, false
        );
        return ViewHolder(view.root)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var data = mList.get(position)

         holder.binding!!.tvFriendName.setText(data.name)


        holder.itemView.setOnClickListener(View.OnClickListener {

        })

        holder.setViewModel(position, mViewModel)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding = DataBindingUtil.bind<ItemSelectedFriendsListBinding>(itemView)
        fun setViewModel(position: Int, viewModel: CreateViewModel) {
            binding!!.position = position
            binding!!.viewModel = viewModel
            binding!!.setVariable(BR.viewModel, viewModel)
            binding!!.executePendingBindings()
        }
    }


}