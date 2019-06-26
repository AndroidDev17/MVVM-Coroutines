package com.ankit.mishra.Data

import android.view.View

abstract class SpinnerBinding <T> {
    abstract fun bindData( data:T, view: View)
}