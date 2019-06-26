package com.ankit.mishra.ViewModels

import android.text.Editable
import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.MutableLiveData
import com.ankit.mishra.Data.*
import com.ankit.mishra.Data.DataStates.DataStateConstants
import com.ankit.mishra.Data.DataStates.UserIdType
import com.ankit.mishra.Data.ViewStates.SpinnerImageHolder
import com.ankit.mishra.Data.ViewStates.ViewStateConstants
import com.ankit.mishra.R
import com.ankit.mishra.Services.Api.LoginService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

class LoginViewModel : BaseViewModel() {


    val adapter: SpinnerAdapter<AccountType> by lazy {
        SpinnerAdapter(mutableListOf(
            AccountType(R.drawable.ic_email, DataStateConstants.EMAIL),
            AccountType(R.drawable.ic_phone, DataStateConstants.PHONE),
            AccountType(R.drawable.ic_identity, DataStateConstants.MID)
        ),
            R.layout.spinner_image_item,
            object : SpinnerBinding<AccountType>() {
                override fun bindData(data: AccountType, view: View) {
                    if (view.tag != null) {
                        view.tag as SpinnerImageHolder
                    } else {
                        SpinnerImageHolder(view)
                    }.imageHolder.setImageResource(data.resourceId)
                }

            })

    }
    @UserIdType
    var selectedAccountType :  Int=0
        set(value) {
        if(value in 0..2)
            field=value
    }

    val progressState by lazy {
        MutableLiveData<@com.ankit.mishra.Data.ViewStates.LoginViewState Int>().apply{
            value=ViewStateConstants.RESUMED
        }
    }

//    val messageState by lazy {
//        MutableLiveData<String>()
//    }

    val messageChannel by lazy {Channel<String>()}

    val onItemSelectedListener by lazy {
        object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedAccountType = position
            }

        }
    }

    private val mLoginService by lazy {
        LoginService()
    }

    fun login(userId: Editable?, password: Editable?) {
        progressState.postValue(ViewStateConstants.LOADING)
        val userId= when(selectedAccountType){
            DataStateConstants.EMAIL->UserId.EmailId(userId.toString())
            DataStateConstants.MID->UserId.MID(userId.toString())
            else->UserId.PhoneNumber(userId.toString().toLong())
        }

        viewModeScope.launch(Dispatchers.IO) {
            var response:Response<LoginSuccess,LoginError>?=null
            try {
                 response = async {
                    mLoginService.login(Credentials(userId, password.toString()))
                }.await()

            } catch (e: Exception) {

            }
            uiScope.launch {
                progressState.postValue(ViewStateConstants.RESUMED)
            }
            viewModeScope.launch(Dispatchers.Default) {
                val messageFromResponse = getMessageFromResponse(response)
                if (messageFromResponse.isNotEmpty()) {
                    uiScope.launch {
                        messageChannel.send(messageFromResponse)
//                        messageState.postValue(messageFromResponse)
                    }
                }
            }
        }

    }

    private fun getMessageFromResponse(response: Response<LoginSuccess, LoginError>?) :String {
        val msg= response?.let {
            when{
                ! it.isHttpOk() -> "Unable to connect to server..."
                it.successResponse().accessToken.isNotEmpty() && it.successResponse().userName.isNotEmpty()-> "Welcome ${it.successResponse().userName}"
                it.erroResponse().errorCode!=-1 -> it.erroResponse().errorMsg
                else -> "Something went wrong please try again..."
            }
        }
        return msg?:""
    }

    override fun onCleared() {
        super.onCleared()
        messageChannel.close()
    }
}