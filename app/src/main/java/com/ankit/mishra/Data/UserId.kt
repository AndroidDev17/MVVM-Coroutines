package com.ankit.mishra.Data

sealed class UserId {
    data class EmailId(val emailId: String) : UserId()
    data class PhoneNumber(val phoneNumber: Long) : UserId()
    data class MID(val mid: String) : UserId()
}