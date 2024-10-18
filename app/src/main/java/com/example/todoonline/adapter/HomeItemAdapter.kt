    package com.example.todoonline.adapter

    import android.util.Log
    import android.view.LayoutInflater
    import android.view.ViewGroup
    import androidx.recyclerview.widget.RecyclerView
    import androidx.recyclerview.widget.RecyclerView.ViewHolder
    import com.example.todoonline.databinding.HomeitemadapterlayoutBinding
    import com.example.todoonline.models.Todo

    class HomeItemAdapter:RecyclerView.Adapter<HomeItemAdapter.HomeItemViewHolder>() {

        private var todoItems:List<Todo> = emptyList()

        class HomeItemViewHolder(private val binding:HomeitemadapterlayoutBinding):ViewHolder(binding.root){

            fun BindTodo(todo: Todo){
                binding.apply {
                  tododata = todo
                    executePendingBindings()
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeItemViewHolder {
           return HomeItemViewHolder(HomeitemadapterlayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
        }

        override fun getItemCount(): Int {
            return todoItems.size
        }

        override fun onBindViewHolder(holder: HomeItemViewHolder, position: Int) {
           val currentTodo = todoItems[position]
            holder.BindTodo(currentTodo)
            Log.d("AdapterTodo", "Todo ID: ${currentTodo.id}, Task: ${currentTodo.task}")
        }

        fun setTodo(todo:List<Todo>){
            this.todoItems = todo
            notifyDataSetChanged()
        }

        fun getTodo():List<Todo>{
            return todoItems
        }
    }