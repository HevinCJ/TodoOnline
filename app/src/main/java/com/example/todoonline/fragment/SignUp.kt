package com.example.todoonline.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.todoonline.R
import com.example.todoonline.databinding.FragmentSignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUp : Fragment() {
    private var signup:FragmentSignUpBinding?=null
    private val binding get() = signup!!

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        signup = FragmentSignUpBinding.inflate(inflater,container,false)
        auth = FirebaseAuth.getInstance()
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            signupbtn.setOnClickListener {
                createFirebaseUser()
            }

            txtviewlogin.setOnClickListener {
                findNavController().navigate(R.id.action_signUp_to_logIn)
            }
        }

    }

    private fun createFirebaseUser() {

       binding.apply {

           val email = edttextemail.text.toString().trim()
           val password = edttxtpassword.text.toString().trim()
           val confirmpass = edttxtconfirmpassword.text.toString().trim()

           if (email.isNotEmpty()&&password.isNotEmpty()&&confirmpass.isNotEmpty()){
               if (password==confirmpass){
                   auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
                       if (it.isSuccessful){
                           findNavController().navigate(R.id.action_signUp_to_home)
                           Toast.makeText(requireContext(),"Signed In",Toast.LENGTH_SHORT).show()
                       }else{
                           Toast.makeText(requireContext(),it.exception?.message,Toast.LENGTH_SHORT).show()
                       }
                   }
               }else{
                   Toast.makeText(requireContext(),"Password Incorrect",Toast.LENGTH_SHORT).show()
               }

           }else{
               Toast.makeText(requireContext(),"Please Fill the Fields",Toast.LENGTH_SHORT).show()
           }
       }
    }

}