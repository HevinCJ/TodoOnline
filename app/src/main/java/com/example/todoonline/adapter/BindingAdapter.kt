package com.example.todoonline.adapter

import android.util.Log
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import com.example.todoonline.fragment.HomeDirections
import com.example.todoonline.models.Todo
import com.google.android.material.card.MaterialCardView


class BindingAdapter {

    companion object{


        @BindingAdapter("android:navigateToAddFragToUpdate")
        @JvmStatic
        fun navigateToAddFragToUpdate(view:MaterialCardView,todo: Todo){

            Log.d("BindingAdapterTodo", "Todo ID: ${todo.id}, Task: ${todo.task}")

            view.setOnClickListener {
                val action = HomeDirections.actionHomeToUpdateTodo(todo)
                it.findNavController().navigate(action)


            }
        }

        @BindingAdapter("android:CaptalizeText")
        @JvmStatic
        fun CaptitalizeText(view:TextView,todo: Todo){

            val capitalizedText = todo.task.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase() else it.toString()
            }
            view.text = capitalizedText
        }

    }

}