package com.bhagavad.hifivedemo.base

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.bhagavad.hifivedemo.toolbar.ToolBarNavigator
import io.reactivex.disposables.CompositeDisposable
import java.lang.ref.WeakReference


//declare open because this class inherited by other classes
public abstract class BaseViewModel<N>(application: Application) : AndroidViewModel(application)
{
    var mContext : Context;

    var mToolBarNavigator : ToolBarNavigator? = null ;
     var mCompositeDisposable: CompositeDisposable ;

    private var mNavigator: WeakReference<N>? = null

    init {
        mContext = application.applicationContext;
        this.mCompositeDisposable = CompositeDisposable()
    }


   public fun setToolBarNavigator(navigator: ToolBarNavigator)
    {
        mToolBarNavigator = navigator;
    }

    public fun getToolBarNavigator():ToolBarNavigator
    {
        return mToolBarNavigator!!;
    }

    public fun getStringfromVM( stringId : Int):String
    {
        return mContext.getString(stringId);
    }

    fun getCompositeDisposable(): CompositeDisposable {
        return mCompositeDisposable
    }


    fun getNavigator(): N {
        return mNavigator!!.get()!!
    }

    fun setNavigator(navigator: N) {
        this.mNavigator = WeakReference<N>(navigator)

    }





}