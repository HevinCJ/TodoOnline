package com.example.todoonline.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.todoonline.databinding.FragmentAddTodoBinding
import com.example.todoonline.utils.FirebaseResult
import com.example.todoonline.models.Todo
import com.example.todoonline.viewmodel.AddTodoViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddTodo : DialogFragment() {

    private var addtodo:FragmentAddTodoBinding?=null
    private val binding get() = addtodo!!

    private val addTodoViewModel:AddTodoViewModel by viewModels()
    private lateinit var auth:FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var key:String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        addtodo = FragmentAddTodoBinding.inflate(inflater,container,false)
        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("Tasks").child(auth.currentUser?.uid.orEmpty())
        key = databaseReference.push().key.toString()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            savebtn.setOnClickListener {
                saveTodoToFirebase()
            }

            observeSavedData()
        }


    }

    private fun observeSavedData() {
        addTodoViewModel._saveddata.observe(viewLifecycleOwner){result->
            when (result){
                is FirebaseResult.Error ->{
                    Toast.makeText(requireContext(),result.message.toString(),Toast.LENGTH_SHORT).show()
                }
                is FirebaseResult.Loading ->{
                    Toast.makeText(requireContext(),"Loading,please wait..",Toast.LENGTH_SHORT).show()
                }
                is FirebaseResult.Success -> {
                    dismiss()
                    Toast.makeText(requireContext(),"Saved Todo",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun saveTodoToFirebase() {

        binding.apply {


            val task = edttexttitle.text.toString().trim()
            if (task.isNotEmpty()){
                addTodoViewModel.saveTodoToFirebase(Todo(id = key, task = task))
            }else{
                Toast.makeText(requireContext(),"Fill required Fields",Toast.LENGTH_SHORT).show()
            }
        }


    }


}