package com.ankit.mishra.Services.Api

import com.ankit.mishra.Data.*
import kotlinx.coroutines.delay

class LoginService {
    private val validEmailAccount =Credentials(UserId.EmailId("abc@mailinator.com"),"Test@123")
    private val validMidAccount =Credentials(UserId.MID("Mabcdefg"),"Test@1234")
    private val validPhoneAccount =Credentials(UserId.PhoneNumber(9988776655),"Test@12345")

    suspend fun login(credentials: Credentials): Response<LoginSuccess, LoginError> {

        return when(credentials.userId){
            is UserId.EmailId-> validateAndRespond(credentials,validEmailAccount,5)
            is UserId.PhoneNumber->validateAndRespond(credentials,validPhoneAccount,10)
            is UserId.MID->validateAndRespond(credentials,validMidAccount,15)
        }
    }

    private suspend fun validateAndRespond(credential: Credentials, validCredential: Credentials, delay: Long): Response<LoginSuccess, LoginError> {
        delay(delay*1000)
        return when {
            credential == validCredential -> {
                 generateRespond(
                    LoginSuccess(getAccessToken(), getUserName()),
                    LoginError(), 200)
            }
            credential.userId==validCredential.userId->{
                generateRespond(
                    LoginSuccess(),
                    LoginError(100_002,getInvalidPasswordErrorMessage(credential)), 200)
            }
            credential.password==validCredential.password->{
                generateRespond(
                    LoginSuccess(),
                    LoginError(100_003,getInvalidUserIdErrorMessage(credential)), 200)
            }

            else -> {
                generateRespond(
                    LoginSuccess(),
                    LoginError(100_004,getUserNotExistErrorMessage(credential)), 200)
            }
        }
    }

    private fun getUserNotExistErrorMessage(credential: Credentials): String =
        when(credential.userId){
            is UserId.EmailId -> "No user exist with Email id : ${credential.userId.emailId}"
            is UserId.MID -> "No user exist with Mid : ${credential.userId.mid}"
            is UserId.PhoneNumber -> "No user exist with phone Number : ${credential.userId.phoneNumber}"
        }


    private fun getInvalidUserIdErrorMessage(credential: Credentials): String =
         when(credential.userId){
            is UserId.EmailId -> "you entered a wrong Email id : ${credential.userId.emailId}"
            is UserId.MID -> "you entered a wrong Mid : ${credential.userId.mid}"
            is UserId.PhoneNumber -> "you entered a wrong phone Number : ${credential.userId.phoneNumber}"
        }

    private fun getInvalidPasswordErrorMessage(credential: Credentials): String =
        when(credential.userId){
            is UserId.EmailId -> "you entered a wrong password for Email id : ${credential.userId.emailId}"
            is UserId.MID -> "you entered a wrong password for Mid : ${credential.userId.mid}"
            is UserId.PhoneNumber -> "you entered a wrong password for phone Number : ${credential.userId.phoneNumber}"
        }

    private fun generateRespond(success:LoginSuccess, error: LoginError,httpResponse:Int):Response<LoginSuccess,LoginError> =
        object :Response<LoginSuccess,LoginError> {
            override val httpResponseCode: Int
                get() = httpResponse
            override fun successResponse(): LoginSuccess =success
            override fun erroResponse(): LoginError =error

        }

    private fun getUserName():String="Valid User"

    private fun getAccessToken():String="Accewtfetydfiytfqu#$^$&%$%$&*$%*%kg3efugtco7fggv*&0787867665fgeiuywfqvycyfgfwftffwqcyfifiy"
}