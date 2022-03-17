/*
 *  Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      https://mindorks.com/license/apache-v2
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License
 */

package com.bhagavad.hifivedemo.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel


/**
 */

abstract class BaseFragment<T : ViewDataBinding, V : AndroidViewModel> : Fragment() {

    private var viewDataBinding: T? = null
    private var mViewModel: V? = null

    val TAG = BaseFragment::class.java.simpleName
    /**
     * Override for set binding variable
     *
     * @return variable id
     */
//    abstract val bindingVariable: Int
    abstract fun getBindingVariable(): Int

    /**
     * @return layout resource id
     */
//    @get:LayoutRes
//    abstract val layoutId: Int

    @LayoutRes
    abstract fun getLayoutId(): Int

    /**
     * Override for set view model
     *
     * @return view model instance
     */
//    abstract val viewModel: V
    abstract fun getViewModel(): V

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = getViewModel()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        return viewDataBinding!!.root
    }


    fun getViewDataBinding(): T {
        return viewDataBinding!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.mViewModel = if (mViewModel == null) getViewModel() else mViewModel
        viewDataBinding?.setVariable(getBindingVariable(), mViewModel)
        viewDataBinding?.executePendingBindings()
    }


}
