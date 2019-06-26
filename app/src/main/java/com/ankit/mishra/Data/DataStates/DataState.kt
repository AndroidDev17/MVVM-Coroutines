package com.ankit.mishra.Data.DataStates

import androidx.annotation.IntDef

@Retention(AnnotationRetention.SOURCE)
@IntDef(
    DataStateConstants.EMAIL,
    DataStateConstants.MID,
    DataStateConstants.PHONE
)
annotation class UserIdType

class DataStateConstants {
    companion object {
        const val EMAIL = 0
        const val MID = 1
        const val PHONE = 2
    }
}