    package com.example.todoonline.fragment

    import android.os.Bundle
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import android.widget.Toast
    import androidx.fragment.app.DialogFragment
    import androidx.fragment.app.viewModels
    import androidx.navigation.fragment.findNavController
    import androidx.navigation.fragment.navArgs
    import com.example.todoonline.R
    import com.example.todoonline.databinding.FragmentUpdateTodoBinding
    import com.example.todoonline.utils.FirebaseResult
    import com.example.todoonline.models.Todo
    import com.example.todoonline.viewmodel.UpdateTodoViewModel

    class UpdateTodo : DialogFragment() {

        private var updateTodo:FragmentUpdateTodoBinding?=null
        private val binding get() = updateTodo!!

        private val updateTodoViewModel:UpdateTodoViewModel by viewModels()

        private val updateTodoArgs by navArgs<UpdateTodoArgs>()

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            updateTodo = FragmentUpdateTodoBinding.inflate(inflater,container,false)
            return binding.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            setUpdateArgsToEdittext()
            updateTodoToFirebase()
            observeUpdateDataFirebase()
        }

        private fun updateTodoToFirebase() {
           binding.apply {

               savebtn.setOnClickListener {
                   val newTask = edttexttitle.text.toString().trim()
                   if (newTask.isNotEmpty()){
                           updateTodoViewModel.updateTodoToFirebase(Todo(updateTodoArgs.todoData.id,newTask))

                   }else{
                       Toast.makeText(requireContext(),"Please fill required fields", Toast.LENGTH_SHORT).show()
                   }

               }
           }
        }

        private fun observeUpdateDataFirebase() {

            updateTodoViewModel.updateddata.observe(viewLifecycleOwner){data->
                when(data){
                    is FirebaseResult.Error -> {

                    }
                    is FirebaseResult.Loading -> {

                    }
                    is FirebaseResult.Success -> {
                        findNavController().navigate(R.id.action_updateTodo_to_home)
                        Toast.makeText(requireContext(),"Updated Todo", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        private fun setUpdateArgsToEdittext() {

            binding.apply {
                edttexttitle.setText(updateTodoArgs.todoData.task)
            }

        }
    }


