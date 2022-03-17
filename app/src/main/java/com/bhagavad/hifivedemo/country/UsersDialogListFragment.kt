package com.bhagavad.hifivedemo.country

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bhagavad.hifivedemo.R
import com.bhagavad.hifivedemo.base.BaseActivity
import com.bhagavad.hifivedemo.country.bean.UsersAndGreetingListResponse
import com.bhagavad.hifivedemo.databinding.DialogCountryViewBinding
import com.bhagavad.hifivedemo.util.AppUtil


class UsersDialogListFragment : DialogFragment(), dialogItemSelectedInterface {

    private val TAG = UsersDialogListFragment::class.java.simpleName

    private var mSearchView: SearchView? = null
    lateinit var mDataArrList: ArrayList<UsersAndGreetingListResponse.UserData>
    lateinit var builder: AlertDialog.Builder
    lateinit var rootView: View
    lateinit var mRecyclerView: RecyclerView

    //    private TextView mTitleTv;
    private var mBinding: DialogCountryViewBinding? = null
    private var mDialogAdapter: UsersDialogListAdapter? = null
    private var mTitleStr: String? = ""
    lateinit var mLayoutManager: RecyclerView.LayoutManager
    private var mDialogRl: LinearLayout? = null
    private var mContext: Context? = null
    private lateinit var mOkBtn: Button

    var callback: OnDialogClickedListener? = null


    internal lateinit var view: View

    internal lateinit var searchEditText: EditText
    internal var mCardView: CardView? = null
    private var mPlaceHolderStr: String? = ""

    private lateinit var mNewList: ArrayList<UsersAndGreetingListResponse.UserData>

    fun setOnDialogClickedListener(l: OnDialogClickedListener) {
        callback = l

    }

    override fun dialogItem(itemPos: Int) {

        /*
        * manage state selection through check box event
        * */
        AppUtil.hideSoftKeyBoard(mContext!!,mBinding!!.searchView)
        callback!!.selectedDialogItem(itemPos, mNewList!![itemPos])

    }

    /*
    * manage dialog item selection
    * */
    interface OnDialogClickedListener {
        fun onDialogYes()
        fun onDialogNo()
        fun selectedDialogItem(dialogPos: Int, selectedData: UsersAndGreetingListResponse.UserData)
    }


    /*
    * get current list - country/state or city to manage multiple selection
    * */
    public fun getList(): ArrayList<UsersAndGreetingListResponse.UserData> {

        return mDataArrList!!
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        view = inflater.inflate(R.layout.dialog_country_view, container, false)
        //        mBinding = DataBindingUtil.inflate(inflater , R.layout.dialog_country_view , container , false);
        //        view = mBinding.getRoot();
        return view
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = activity!!.layoutInflater
        mBinding = DataBindingUtil.inflate<ViewDataBinding>(
            inflater, R.layout.dialog_country_view, null, false
        ) as DialogCountryViewBinding?

        //        rootView = inflater.inflate(R.layout.dialog_country_view, null, false);
        rootView = mBinding!!.getRoot()
        builder = AlertDialog.Builder(activity!!, R.style.AppListDialogTheme)
        builder.setView(rootView)


        return builder.create()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        mContext = activity
        mDataArrList = ArrayList<UsersAndGreetingListResponse.UserData>()
        mRecyclerView = rootView.findViewById<View>(R.id.options_rv) as RecyclerView
        mDialogRl = rootView.findViewById<View>(R.id.rl_dialog) as LinearLayout
        mSearchView = rootView.findViewById<View>(R.id.searchView) as SearchView
        searchEditText = mSearchView!!.findViewById(R.id.search_src_text)
       // mOkBtn = rootView.findViewById<View>(R.id.btn_ok) as Button
        searchEditText.setHintTextColor(BaseActivity.getColor(mContext!!, R.color.hint_text_color))
        searchEditText.setTextColor(BaseActivity.getColor(mContext!!, R.color.black))

        mBinding!!.ivCross.setOnClickListener(this::crossClick)

        mLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        mRecyclerView.layoutManager = mLayoutManager
        mDialogAdapter = UsersDialogListAdapter(activity!!, this)
        mRecyclerView.adapter = mDialogAdapter
        AppUtil.hideSoftKeyBoard(mContext!!, mBinding!!.tvTitle)
        mDialogRl!!.requestFocus()
        mTitleStr = arguments!!.getString("title")

        if (arguments!!.getString("place-holder") != null) {
            mPlaceHolderStr = arguments!!.getString("place-holder")
            Log.e(TAG, "mPlaceHolderStr:::: " + mPlaceHolderStr)
            searchEditText.hint = mPlaceHolderStr
        }

        if (arguments!!.getParcelableArrayList<Parcelable>("country-list") != null) {
            mDataArrList =
                (arguments!!.getSerializable("country-list") as ArrayList<UsersAndGreetingListResponse.UserData>?)!!

        }

       /* mOkBtn.setOnClickListener {
            callback!!.onDialogYes()
        }*/

        val searchEditText = mBinding!!.searchView.findViewById<EditText>(R.id.search_src_text)
        //        searchEditText.setTextColor(Color.BLACK);
        searchEditText.setHintTextColor(BaseActivity.getColor(mContext!!, R.color.hint_text_color))
        searchEditText.setTextColor(BaseActivity.getColor(mContext!!, R.color.black))

        val tf = ResourcesCompat.getFont(mContext!!, R.font.sf_regular)
        searchEditText.setTypeface(tf)

        mNewList = ArrayList<UsersAndGreetingListResponse.UserData>()
        mNewList = mDataArrList
        mDialogAdapter!!.setList(ArrayList(mNewList))
        if (mTitleStr!!.isEmpty()) {
            mBinding!!.tvTitle.setVisibility(View.GONE)
        } else {
            mBinding!!.tvTitle.setVisibility(View.VISIBLE)
            mBinding!!.tvTitle.setText(mTitleStr)
        }

        searchData()
    }

    private fun crossClick(view: View) {
        callback!!.onDialogNo()
    }

    private fun searchData() {
        mSearchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                var newText = newText
                newText = newText.toLowerCase()
                mNewList = ArrayList<UsersAndGreetingListResponse.UserData>()
                for (userInfo in mDataArrList) {
                    val username = userInfo.name.toLowerCase()
                    if (username.contains(newText)) {
                        mNewList!!.add(userInfo)
                    }
                }

                mDialogAdapter!!.setList(mNewList!!)
                return true
            }
        })
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    companion object {
        internal lateinit var mDialogListFragment: UsersDialogListFragment
        //    private ImageView mCrossIv;

        fun newInstance(
            title: String,
            arrList: ArrayList<UsersAndGreetingListResponse.UserData>, placeHolder: String
        ): UsersDialogListFragment {
            mDialogListFragment = UsersDialogListFragment()
            val bundle = Bundle()
            bundle.putString("title", title)
            bundle.putString("place-holder", placeHolder)
            bundle.putSerializable("country-list", arrList)
            mDialogListFragment.arguments = bundle
            return mDialogListFragment
        }
    }

    /*override fun onStart() {
        val tabletSize = resources.getBoolean(R.bool.isTablet)
        if (tabletSize) {
            Log.e("isTablet--->", "true")
            getActivity()!!.setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            );
        }
        super.onStart()
    }*/


}