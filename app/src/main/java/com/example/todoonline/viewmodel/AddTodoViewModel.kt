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

class AddTodoViewModel:ViewModel() {



    private val firebaseAuth:FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val databaseRef:DatabaseReference by lazy {
        FirebaseDatabase.getInstance().getReference("Tasks").child(firebaseAuth.currentUser?.uid.orEmpty())
    }




    private val saveddata = MutableLiveData<FirebaseResult<Boolean>>()
    val _saveddata: LiveData<FirebaseResult<Boolean>> get() = saveddata



    fun saveTodoToFirebase(todo: Todo) {

        viewModelScope.launch(Dispatchers.IO){

            saveddata.postValue(FirebaseResult.Loading())

            databaseRef.child(todo.id).setValue(todo).addOnCompleteListener {
                if (it.isSuccessful){
                    saveddata.postValue(FirebaseResult.Success(true))
                }else{
                    saveddata.value = FirebaseResult.Error(null,it.exception?.message)
                }
            }.addOnFailureListener {
                saveddata.value = FirebaseResult.Error(null,it.message)
            }

        }


    }
}


