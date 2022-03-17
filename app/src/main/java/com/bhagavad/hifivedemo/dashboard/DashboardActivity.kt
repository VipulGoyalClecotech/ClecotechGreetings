package com.bhagavad.hifivedemo.dashboard



import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.bhagavad.hifivedemo.BR
import com.bhagavad.hifivedemo.R
import com.bhagavad.hifivedemo.base.BaseActivity
import com.bhagavad.hifivedemo.dashboard.home.HomeListFragment
import com.bhagavad.hifivedemo.dashboard.profile.ProfileFragment
import com.bhagavad.hifivedemo.dashboard.setting.SettingFragment
import com.bhagavad.hifivedemo.databinding.ActivityDashboardBinding
import com.bhagavad.hifivedemo.server.serverResponseNavigator
import com.bhagavad.hifivedemo.splash.bean.LoginResponse
import com.bhagavad.hifivedemo.splash.bean.UserDataResponse
import com.bhagavad.hifivedemo.toolbar.ToolBarNavigator
import com.bhagavad.hifivedemo.toolbar.ToolBarViewModel
import com.bhagavad.hifivedemo.viewmodalfactory.ViewModelProviderFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

class DashboardActivity :  BaseActivity<ActivityDashboardBinding, DashboardViewModel, ToolBarViewModel>(),
    DashboardNavigator, serverResponseNavigator,  BottomNavigationView.OnNavigationItemSelectedListener,ToolBarNavigator {
    private lateinit var mViewModel: DashboardViewModel
    private lateinit var mBinding: ActivityDashboardBinding
    private lateinit var mContext: Context
    private lateinit var mActivity: Activity
    private lateinit var homeFrgment: HomeListFragment
    var mBottomTabSelectedId: Int? = null
    var mFragment: Fragment? = null
    val mBundle = Bundle()
    lateinit var mToolBarViewModel: ToolBarViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()

    }

    fun initView()
    {
        homeFrgment = HomeListFragment()

        mContext = this@DashboardActivity
        mActivity = this@DashboardActivity
        mBinding = getViewDataBinding()
        mViewModel.setNavigator(this)
        mBinding.toolBar.collapseToolBarModel = mToolBarViewModel
        mToolBarViewModel!!.setToolBarNavigator(this)
        // Get Intent Data
        val bundle = intent.extras
        var data=bundle!!.getParcelable<UserDataResponse.UserDataBean>("data")
        mBundle.putParcelable("data",data)
        mBinding.bottomTabView.setOnNavigationItemSelectedListener(this)
        mBinding.bottomTabView.selectedItemId = mBinding.bottomTabView.menu.getItem(0).itemId
        mBinding.bottomTabView.itemIconTintList = null
        mBinding.toolBar.tvTitle.setText(getString(R.string.home))

        mBinding.toolBar.ibRight1.setOnClickListener { homeFrgment.filterClickMethod(mContext) }
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getLayoutId(): Int {
       return R.layout.activity_dashboard
    }

    override fun getViewModel(): DashboardViewModel {
        mViewModel = ViewModelProvider(this, ViewModelProviderFactory(application, this))
            .get(DashboardViewModel::class.java)
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

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {

        if (mBottomTabSelectedId == menuItem.itemId) {
            return true;
        } else {

            mBottomTabSelectedId = menuItem.itemId;

        }

        when (menuItem.itemId) {


            R.id.tab1_fragment -> {
                mBinding.toolBar.tvTitle.setText(getString(R.string.home))
                mFragment = HomeListFragment.newInstance();
                mBinding.toolBar.ibRight1.visibility=View.INVISIBLE
            }

            R.id.tab2_fragment -> {
                mBinding.toolBar.tvTitle.setText(getString(R.string.my_profile))
                mFragment = ProfileFragment.newInstance();
                mBinding.toolBar.ibRight1.visibility=View.INVISIBLE
            }

           /* R.id.tab3_fragment -> {
                mBinding.toolBar.tvTitle.setText(getString(R.string.tab3))
                mFragment = HomeListFragment.newInstance();
                mBinding.toolBar.ibRight1.visibility=View.GONE
            }*/


            R.id.tab4_fragment -> {
                mBinding.toolBar.tvTitle.setText(getString(R.string.settings))
                mFragment = SettingFragment.newInstance();
                mBinding.toolBar.ibRight1.visibility=View.INVISIBLE
            }
        }


        return manageFragment()
    }

    /*
 * manage fragment as per bottom tab selection
 * */
    fun manageFragment(): Boolean {

        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction();

        if (mFragment != null) {

            mFragment!!.arguments = mBundle
            fragmentTransaction.replace(R.id.frame_layout, mFragment!!).commit()
            return true;
        }
        return false;
    }

    override fun backClick() {

    }



}