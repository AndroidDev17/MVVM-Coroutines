package com.ankit.mishra.Data

import com.ankit.mishra.Data.DataStates.UserIdType

data class AccountType(val resourceId: Int, @UserIdType val userId: Int)

data class LoginSuccess(val accessToken: String = "", val userName: String = "")

data class LoginError(val errorCode: Int = -1, val errorMsg: String = "")