package com.example.todoonline.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoonline.utils.FirebaseResult
import com.example.todoonline.models.Todo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch


class HomeViewModel:ViewModel() {

    private var databaseref: DatabaseReference
    private var firebaseAuth:FirebaseAuth

    init {
        firebaseAuth = FirebaseAuth.getInstance()
        databaseref= FirebaseDatabase.getInstance().getReference("Tasks").child(firebaseAuth.currentUser?.uid.orEmpty())
    }

    private val getalldata = MutableLiveData<FirebaseResult<List<Todo>>>()
    val _getalldata:LiveData<FirebaseResult<List<Todo>>> get() = getalldata

    private val _deletedata = MutableLiveData<FirebaseResult<Todo>>()
    val deleteddata:LiveData<FirebaseResult<Todo>> get() = _deletedata

    fun getAllDataFromFirebase(){
        viewModelScope.launch {
            getalldata.value = FirebaseResult.Loading()

            databaseref.addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    val userdata = snapshot.children.mapNotNull { it.getValue(Todo::class.java) }
                    getalldata.value = FirebaseResult.Success(userdata)
                    Log.d("hometodoviemodel", listOf(userdata).toString())
                }

                override fun onCancelled(error: DatabaseError) {
                   getalldata.value = FirebaseResult.Error(message = error.message)
                }

            })

        }
    }

    fun deleteDataFromFirebase(todo:Todo){

        viewModelScope.launch {
            _deletedata.value = FirebaseResult.Loading()

            databaseref.child(todo.id).removeValue().addOnCompleteListener {task->
                if (task.isSuccessful){
                    _deletedata.value = FirebaseResult.Success(todo)
                }else{
                    _deletedata.value = FirebaseResult.Error(null, task.exception?.message)
                }
            }
        }


    }


}