package com.example.todoonline.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.todoonline.R
import com.example.todoonline.databinding.FragmentSplashBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

class Splash : Fragment() {
   private var splash:FragmentSplashBinding?=null
    private val binding get() = splash!!

    private lateinit var auth:FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        splash = FragmentSplashBinding.inflate(inflater,container,false)
        auth = FirebaseAuth.getInstance()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Handler(Looper.myLooper()!!).postDelayed(Runnable {

            val currentUser = auth.currentUser

            val navOptions = NavOptions.Builder().setPopUpTo(R.id.splash,true).build()

            if (currentUser!=null ){
                findNavController().navigate(R.id.action_splash_to_home,null,navOptions)
            }else{
                findNavController().navigate(R.id.action_splash_to_logIn,null,navOptions)
            }
        }, 2000)



    }


}