package com.example.todoonline.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoonline.utils.FirebaseResult
import com.example.todoonline.models.Todo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UpdateTodoViewModel:ViewModel() {

    private var databaseref: DatabaseReference
    private var firebaseAuth:FirebaseAuth

    init {
        firebaseAuth = FirebaseAuth.getInstance()
        databaseref= FirebaseDatabase.getInstance().getReference("Tasks").child(firebaseAuth.currentUser?.uid.orEmpty())
    }


    private var _updateddata = MutableLiveData<FirebaseResult<Boolean>>()
    val updateddata:LiveData<FirebaseResult<Boolean>> get() = _updateddata



    fun updateTodoToFirebase(todo: Todo){

        viewModelScope.launch(Dispatchers.Main) {

            _updateddata.value = FirebaseResult.Loading()

                val updatedTodo = mutableMapOf<String,Any>(
                    "id" to todo.id,
                    "task" to todo.task
                )
               withContext(Dispatchers.IO){
                   databaseref.child(todo.id).updateChildren(updatedTodo).addOnCompleteListener{result->
                       if (result.isSuccessful){
                           _updateddata.value = FirebaseResult.Success(true)
                       }else{
                           _updateddata.value = FirebaseResult.Error(data = null,message = result.exception?.message)
                       }
                   }
               }
        }
    }

}