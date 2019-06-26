package com.ankit.mishra.Data.ViewStates

import androidx.annotation.IntDef


@Retention(AnnotationRetention.SOURCE)
@IntDef(ViewStateConstants.RESUMED, ViewStateConstants.LOADING)
annotation class LoginViewState

class ViewStateConstants {
    companion object {
        const val RESUMED = 0
        const val LOADING = 1
    }
}
