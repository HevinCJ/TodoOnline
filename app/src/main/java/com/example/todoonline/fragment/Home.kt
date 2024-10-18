package com.example.todoonline.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoonline.R
import com.example.todoonline.adapter.HomeItemAdapter
import com.example.todoonline.databinding.FragmentHomeBinding
import com.example.todoonline.models.Todo
import com.example.todoonline.utils.FirebaseResult
import com.example.todoonline.viewmodel.AddTodoViewModel
import com.example.todoonline.viewmodel.HomeViewModel
import com.google.android.material.snackbar.Snackbar


class Home : Fragment() {

   private var home:FragmentHomeBinding?=null
    private val binding get() = home!!

    private val homeViewModel:HomeViewModel by viewModels()
    private val addTodoViewModel:AddTodoViewModel by viewModels()

    private val homeItemAdapter:HomeItemAdapter by lazy { HomeItemAdapter() }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        home = FragmentHomeBinding.inflate(inflater,container,false)
        observeGetAllData()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRcViewHome()
        actionToAddFrag()
        swipeToDelete(binding.rcviewhome)
        observeDeletedData()
    }

    private fun observeDeletedData() {
        homeViewModel.deleteddata.observe(viewLifecycleOwner){result->
         when(result){
             is FirebaseResult.Error -> {
                 Toast.makeText(requireContext(),result.message, Toast.LENGTH_SHORT).show()
             }
             is FirebaseResult.Loading -> {

             }
             is FirebaseResult.Success -> {
                 showSnackBar(result.data)
             }
         }
        }
    }

    private fun showSnackBar(todo:Todo?) {
        todo?.let {
           val snackbar =   Snackbar.make(requireView(), "Deleted ${todo.task}", Snackbar.LENGTH_LONG)
            snackbar.setAction("Undo"){
                addTodoViewModel.saveTodoToFirebase(todo)
            }.show()
        }
    }

    private fun swipeToDelete(rcviewhome: RecyclerView) {

        val swipeToDelete = object :ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
               try {
                   val currentItem = viewHolder.adapterPosition
                   val todo = homeItemAdapter.getTodo()[currentItem]
                   homeViewModel.deleteDataFromFirebase(todo)
               }catch (e:Exception){

               }
            }

        }

        val itemTouchHelper = ItemTouchHelper(swipeToDelete)
        itemTouchHelper.attachToRecyclerView(rcviewhome)
    }

    private fun actionToAddFrag() {
        binding.apply {
            fltactionbtn.setOnClickListener {
                findNavController().navigate(R.id.action_home_to_addTodo)
            }

        }
    }

    private fun observeGetAllData() {
        homeViewModel.getAllDataFromFirebase()
        homeViewModel._getalldata.observe(viewLifecycleOwner){result->
            when (result){
                is FirebaseResult.Error -> {

                }
                is FirebaseResult.Loading -> {

                }
                is FirebaseResult.Success -> {
                  result.data?.let {todo->
                      homeItemAdapter.setTodo(todo)
                  }
                }
            }
        }
    }


    private fun setRcViewHome(){
        binding.apply {
            rcviewhome.adapter = homeItemAdapter
            rcviewhome.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        }
    }

}