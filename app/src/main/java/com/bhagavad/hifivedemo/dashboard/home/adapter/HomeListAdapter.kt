package com.bhagavad.hifivedemo.dashboard.home.adapter

import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bhagavad.hifivedemo.R
import com.bhagavad.hifivedemo.BR
import com.bhagavad.hifivedemo.dashboard.home.HomeListFragmentNavigator
import com.bhagavad.hifivedemo.dashboard.home.HomeListFragmentViewModel
import com.bhagavad.hifivedemo.dashboard.home.bean.HomeListResponse
import com.bhagavad.hifivedemo.databinding.ItemHomeListBinding
import com.bhagavad.hifivedemo.util.AppConstants
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import java.lang.Appendable


class HomeListAdapter(
    context: Context,
    list: ArrayList<HomeListResponse.Greeting>,
    viewModel: HomeListFragmentViewModel,
    navigator: HomeListFragmentNavigator
) :
    RecyclerView.Adapter<HomeListAdapter.ViewHolder>() {
    lateinit var mContext: Context
    lateinit var mNavigator: HomeListFragmentNavigator
    lateinit var mViewModel: HomeListFragmentViewModel
    lateinit var mList: ArrayList<HomeListResponse.Greeting>


    init {
        mNavigator = navigator
        mContext = context
        mViewModel = viewModel
        mList = list
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = DataBindingUtil.inflate<ItemHomeListBinding>(
            LayoutInflater.from(mContext),
            R.layout.item_home_list, parent, false
        );
        return ViewHolder(view.root)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var data = mList.get(position)

        holder.binding!!.tvSenderName.setText(Html.fromHtml("<font color=#cc0029>"+mContext.getString(R.string.from)+"</font> "+data.sender.email))
        holder.binding!!.tvReceiverName.setText(Html.fromHtml("<font color=#3AEF1F>"+mContext.getString(R.string.to)+"</font> "+data.receiver.email))
       holder.binding!!.tvTitle.setText(data.title)
        holder.binding!!.tvMessage.setText(data.message)


        val requestOptions = RequestOptions()
        requestOptions.placeholder(R.drawable.default_profile)
        requestOptions.error(R.drawable.default_profile)
        Glide.with(mContext)
            .setDefaultRequestOptions(requestOptions)
            .load(AppConstants.IMAGE_BASE_URL+data.image)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(holder.binding!!.ivGreetingImage)


        holder.itemView.setOnClickListener(View.OnClickListener {

        })

        holder.setViewModel(position, mViewModel)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding = DataBindingUtil.bind<ItemHomeListBinding>(itemView)
        fun setViewModel(position: Int, viewModel: HomeListFragmentViewModel) {
            binding!!.position = position
            binding!!.viewModel = viewModel
            binding!!.setVariable(BR.viewModel, viewModel)
            binding!!.executePendingBindings()
        }
    }


}