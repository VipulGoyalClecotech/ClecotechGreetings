package com.bhagavad.hifivedemo.create


import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.bhagavad.hifivedemo.BR
import com.bhagavad.hifivedemo.R
import com.bhagavad.hifivedemo.base.BaseActivity
import com.bhagavad.hifivedemo.country.UsersDialogListFragment
import com.bhagavad.hifivedemo.country.bean.UsersAndGreetingListResponse
import com.bhagavad.hifivedemo.create.adapter.SelectedFriendsListAdapter
import com.bhagavad.hifivedemo.create.bean.CreateResponse
import com.bhagavad.hifivedemo.databinding.ActivityCreateBinding
import com.bhagavad.hifivedemo.greetingdialog.GreetingDialogListFragment
import com.bhagavad.hifivedemo.server.serverResponseNavigator
import com.bhagavad.hifivedemo.toolbar.ToolBarNavigator
import com.bhagavad.hifivedemo.toolbar.ToolBarViewModel
import com.bhagavad.hifivedemo.util.DialogUtil
import com.bhagavad.hifivedemo.viewmodalfactory.ViewModelProviderFactory
import com.google.gson.Gson
import org.json.JSONArray


class CreateActivity : BaseActivity<ActivityCreateBinding, CreateViewModel, ToolBarViewModel>(),
    CreateNavigator, serverResponseNavigator, ToolBarNavigator {
    private lateinit var mViewModel: CreateViewModel
    private lateinit var mBinding: ActivityCreateBinding
    private lateinit var mContext: Context
    private lateinit var mActivity: Activity
    private var listType: String = ""
    lateinit var mToolBarViewModel: ToolBarViewModel
    lateinit var mGreetingList: ArrayList<UsersAndGreetingListResponse.GreetingData>
    lateinit var mFriendsList: ArrayList<UsersAndGreetingListResponse.UserData>
    lateinit var mSelectedFriendsList: ArrayList<UsersAndGreetingListResponse.UserData>
    lateinit var mLayoutManager: LinearLayoutManager
    lateinit var mAdapter: SelectedFriendsListAdapter
    private var mShowNetworkDialog: Dialog? = null
    private  var member_ids = ""
    private  var greeting_id = ""
    var jsonArray = JSONArray()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()

    }

    fun initView() {
        mContext = this@CreateActivity
        mActivity = this@CreateActivity
        mBinding = getViewDataBinding()
        mViewModel.setNavigator(this)
        mGreetingList = ArrayList()
        mFriendsList = ArrayList()
        mSelectedFriendsList = ArrayList()
        mBinding.toolBar.collapseToolBarModel = mToolBarViewModel
        mToolBarViewModel!!.setToolBarNavigator(this)
        mBinding.toolBar.ibLeft.visibility = View.VISIBLE
        mBinding.toolBar.tvTitle.setText(getString(R.string.create))


        showLoaderOnRequest(true)
        mViewModel.userListingAPI()

        //these is adapter initialization of selected friends list
        mLayoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        mBinding.rvFriendsList.setHasFixedSize(true)
        mBinding.rvFriendsList.setLayoutManager(mLayoutManager)
        mBinding.rvFriendsList.setItemAnimator(DefaultItemAnimator())
        mAdapter = SelectedFriendsListAdapter(mContext, mSelectedFriendsList, mViewModel, this)
        mBinding.rvFriendsList.setAdapter(mAdapter)

        mViewModel.geResult().observe(this, Observer { result ->

            if (result.equals("")) {
                if((!mSelectedFriendsList.isNullOrEmpty())&&(mSelectedFriendsList.size>0))
                {member_ids=""
                    for (i in 0..(mSelectedFriendsList.size-1))
                    {

                        jsonArray.put(mSelectedFriendsList.get(i).id)
                        if(i==0)
                            member_ids=mSelectedFriendsList.get(i).id
                        else
                            member_ids=member_ids+","+mSelectedFriendsList.get(i).id
                    }

                }
                if (member_ids.isNullOrEmpty()) {

                    showMessage(getAppString(R.string.please_select_member))
                }
                else if (greeting_id.isNullOrEmpty()) {

                    showMessage(getAppString(R.string.please_select_greeting))
                } else {



                    showLoaderOnRequest(true)
                    mViewModel.createGreetingAPI(member_ids, greeting_id)
                }

            } else {
                showMessage(result)
            }
        })
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_create
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

    fun showSuccessMessage(msg: String) {
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


    override fun getViewModel(): CreateViewModel {
        mViewModel = ViewModelProvider(this, ViewModelProviderFactory(application, this))
            .get(CreateViewModel::class.java)
        return mViewModel
    }

    override fun getToolBarViewModel(): ToolBarViewModel? {
        mToolBarViewModel =
            ViewModelProvider(this, ViewModelProviderFactory(application, this)).get(
                ToolBarViewModel::class.java
            )
        return mToolBarViewModel
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
        if (eventType == "getUserListingResponse") {

            val bean = Gson().fromJson(response, UsersAndGreetingListResponse.ListDataBean::class.java)
            mFriendsList.addAll(bean.data.user)
            mGreetingList.addAll(bean.data.Greeting)

        }
        else if (eventType == "createGreetingResponse") {

            val bean = Gson().fromJson(response, CreateResponse.CreateBean::class.java)

            showSuccessMessage(bean.message)
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

    override fun backClick() {
        finish()
    }

    override fun onSelectFriendsClick() {

        listType = "Friends"

        showFriendListDialog(mFriendsList, getAppString(R.string.search))
    }

    override fun onSelectGreetingClick() {
        listType = "Greetings"

         showGreetingListDialog(mGreetingList, getAppString(R.string.search))
    }

    override fun listItemClick(position: Int) {
        mSelectedFriendsList.removeAt(position)
        mAdapter.notifyDataSetChanged()
    }

    private fun showFriendListDialog(
        list: java.util.ArrayList<UsersAndGreetingListResponse.UserData>,
        placeHolder: String
    ) {
        val dialogFrag = UsersDialogListFragment.newInstance(
            getAppString(R.string.app_name),
            list,
            placeHolder
        )

        dialogFrag.show(supportFragmentManager, "countrystate-dialog")
        dialogFrag.setCancelable(false)

        dialogFrag.setOnDialogClickedListener(object :
            UsersDialogListFragment.OnDialogClickedListener {
            override fun onDialogYes() {
                dialogFrag.dismiss()

            }

            override fun onDialogNo() {
                dialogFrag.dismiss()

            }

            override fun selectedDialogItem(
                dialogPos: Int,
                selectedData: UsersAndGreetingListResponse.UserData
            ) {

                dialogFrag.dismiss()
                if (listType.equals("Friends")) {
                    //  mBinding.tvSelectFriends.setText(selectedData.name)
                    mSelectedFriendsList.add(selectedData)
                    mAdapter.notifyDataSetChanged()
                }
            }
        })
    }


     private fun showGreetingListDialog(
         list: java.util.ArrayList<UsersAndGreetingListResponse.GreetingData>,
         placeHolder: String
     ) {
         val dialogFrag = GreetingDialogListFragment.newInstance(
             getAppString(R.string.app_name),
             list,
             placeHolder
         )

         dialogFrag.show(supportFragmentManager, "countrystate-dialog")
         dialogFrag.setCancelable(false)

         dialogFrag.setOnDialogClickedListener(object :
             GreetingDialogListFragment.OnDialogClickedListener {
             override fun onDialogYes() {
                 dialogFrag.dismiss()

             }

             override fun onDialogNo() {
                 dialogFrag.dismiss()

             }

             override fun selectedDialogItem(
                 dialogPos: Int,
                 selectedData: UsersAndGreetingListResponse.GreetingData
             ) {

                 dialogFrag.dismiss()
               if (listType.equals("Greetings")) {
                     mBinding.tvSelectGreeting.setText(selectedData.name)
                   greeting_id=selectedData.id
                 }
             }
         })
     }
}