package com.ankit.mishra.UI

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ankit.mishra.Data.ViewStates.LoginViewState
import com.ankit.mishra.Data.ViewStates.ViewStateConstants
import com.ankit.mishra.R
import com.ankit.mishra.ViewModels.LoginViewModel
import com.ankit.mishra.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [LoginFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class LoginFragment : androidx.fragment.app.Fragment(),CoroutineScope  {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: LoginViewModel
    protected lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate<FragmentLoginBinding>(inflater, R.layout.fragment_login, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListeners()
        activity?.actionBar
    }

    private fun initListeners() {
        viewModel.progressState.observe(this, Observer<@LoginViewState Int> {
            when (it) {
                ViewStateConstants.LOADING -> {
                    binding.progressHorizontal.visibility = View.VISIBLE
                    setViewEnable(false)
                }
                ViewStateConstants.RESUMED -> {
                    binding.progressHorizontal.visibility = View.GONE
                    setViewEnable(true)
                }
            }
        })

        binding.button.setOnClickListener {
            viewModel.login(binding.editTextEmail.text,binding.editTextPassword.text)
        }

         launch(Dispatchers.Main){
            viewModel.messageChannel.consumeEach { Snackbar.make(binding.root,it,Snackbar.LENGTH_SHORT).show() }
        }
    }

    private fun initView() {
        binding.spinnerIdType.adapter = viewModel.adapter
        binding.spinnerIdType.onItemSelectedListener = viewModel.onItemSelectedListener
        binding.spinnerIdType.setSelection(viewModel.selectedAccountType)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LoginFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun setViewEnable(boolean: Boolean) {
        binding.spinnerIdType.isEnabled = boolean
        binding.editTextEmail.isEnabled = boolean
        binding.editTextPassword.isEnabled = boolean
        binding.button.isEnabled = boolean
    }
}
