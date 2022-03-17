package com.bhagavad.hifivedemo.viewmodalfactory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bhagavad.hifivedemo.create.CreateViewModel
import com.bhagavad.hifivedemo.dashboard.DashboardViewModel
import com.bhagavad.hifivedemo.dashboard.home.HomeListFragmentViewModel
import com.bhagavad.hifivedemo.dashboard.profile.ProfileFragmentViewModel
import com.bhagavad.hifivedemo.dashboard.setting.SettingFragmentViewModel
import com.bhagavad.hifivedemo.login.LoginViewModel

import com.bhagavad.hifivedemo.server.serverResponseNavigator
import com.bhagavad.hifivedemo.splash.SplashViewModel
import com.bhagavad.hifivedemo.toolbar.ToolBarViewModel


class ViewModelProviderFactory(
    private val application: Application,
    serverResponse: serverResponseNavigator
)

    : ViewModelProvider.AndroidViewModelFactory(application) {


    lateinit var serverResponse: serverResponseNavigator;

    init {
        this.serverResponse = serverResponse;
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SplashViewModel::class.java)) {
            return SplashViewModel(application, serverResponse) as T
        }
        else if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(application, serverResponse) as T
        }
        else if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
            return DashboardViewModel(application, serverResponse) as T
        }
        else if (modelClass.isAssignableFrom(HomeListFragmentViewModel::class.java)) {
            return HomeListFragmentViewModel(application, serverResponse) as T
        }
        else if (modelClass.isAssignableFrom(ProfileFragmentViewModel::class.java)) {
            return ProfileFragmentViewModel(application, serverResponse) as T
        }
        else if (modelClass.isAssignableFrom(SettingFragmentViewModel::class.java)) {
            return SettingFragmentViewModel(application, serverResponse) as T
        }
        else if (modelClass.isAssignableFrom(CreateViewModel::class.java)) {
            return CreateViewModel(application, serverResponse) as T
        }


        else if (modelClass.isAssignableFrom(ToolBarViewModel::class.java)) {
             return ToolBarViewModel(application) as T
         }

        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name) as Throwable

    }

}
