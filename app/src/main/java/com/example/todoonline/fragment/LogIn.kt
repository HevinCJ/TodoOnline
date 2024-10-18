package com.example.todoonline.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.todoonline.R
import com.example.todoonline.databinding.FragmentLogInBinding
import com.google.firebase.auth.FirebaseAuth

class LogIn : Fragment() {
    private var login:FragmentLogInBinding?=null
    private val binding get() = login!!

    private lateinit var auth:FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       login = FragmentLogInBinding.inflate(layoutInflater,container,false)
        auth = FirebaseAuth.getInstance()
      return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            loginbtn.setOnClickListener {
                LoginFirebaseUser()
            }



            txtviewsignup.setOnClickListener {
                findNavController().navigate(R.id.action_logIn_to_signUp)
            }

        }
    }

    private fun LoginFirebaseUser() {

        binding.apply {
            val email = edttextemail.text.toString().trim()
            val password = edttxtpassword.text.toString().trim()

            if (email.isNotEmpty()&&password.isNotEmpty()){
                    auth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
                        if (it.isSuccessful){

                            val navOptions = NavOptions.Builder().setPopUpTo(R.id.logIn,true).build()

                            findNavController().navigate(R.id.action_logIn_to_home,null,navOptions)
                            Toast.makeText(requireContext(),"Signed In",Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(requireContext(),it.exception?.message,Toast.LENGTH_SHORT).show()
                        }
                    }


            }else{
                Toast.makeText(requireContext(),"Please Fill the Fields",Toast.LENGTH_SHORT).show()
            }
        }
    }


}