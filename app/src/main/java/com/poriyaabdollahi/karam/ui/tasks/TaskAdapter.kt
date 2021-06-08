package com.poriyaabdollahi.karam.ui.tasks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.poriyaabdollahi.karam.R
import com.poriyaabdollahi.karam.data.Task
import com.poriyaabdollahi.karam.databinding.ItemTaskBinding

class TaskAdapter (private val listener:OnItemClickListener): ListAdapter<Task, TaskAdapter.TaskViewHolder> (DiffCallback()){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {

        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
       val currentItem = getItem(position)
        holder.bind(currentItem)
    }

 inner   class TaskViewHolder(private val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.apply {
                root.setOnClickListener{
                    val  position = adapterPosition
                    if (position != RecyclerView.NO_POSITION){
                        val task = getItem(position)
                        listener.onItemClicked(task)
                    }
                }
                checkboxCompleted.setOnClickListener{
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION){
                        val task = getItem(position)
                        listener.onCheckBoxClicked(task,checkboxCompleted.isChecked)
                    }
                }

            }
        }
        fun bind(task: Task) {
            binding.apply {
                checkboxCompleted.isChecked = task.completed
                textNameView.text = task.name
                textNameView.paint.isStrikeThruText = task.completed
                labelPriority.isVisible = task.important

                when (task.color) {

                    "black_light" -> taskItem.setBackgroundResource(R.drawable.task_back_black_light)
                    "pallet_violet" -> taskItem.setBackgroundResource(R.drawable.task_back_pallet_violet)
                    "caroline_blue" -> taskItem.setBackgroundResource(R.drawable.task_back_caroline_blue)
                    "pallet_sea_green" -> taskItem.setBackgroundResource(R.drawable.task_back_pallet_sea_green)
                    "pallet_saffron_yellow" -> taskItem.setBackgroundResource(R.drawable.task_back_pallet_saffron_yellow)
                    "indian_red" -> taskItem.setBackgroundResource(R.drawable.task_back_indian_red)
                    "dark_green" -> taskItem.setBackgroundResource(R.drawable.task_back_dark_green)
                    "violet" -> taskItem.setBackgroundResource(R.drawable.task_back_violet)
                    "green_white" -> taskItem.setBackgroundResource(R.drawable.task_back_green_white)
                    "dark_pink" -> taskItem.setBackgroundResource(R.drawable.task_back_dark_pink)
                }
            }
        }
    }
    interface OnItemClickListener{
        fun onItemClicked(task :Task)
        fun onCheckBoxClicked(task :Task,isChecked : Boolean)
    }
    class DiffCallback : DiffUtil.ItemCallback<Task>(){
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean =
            oldItem == newItem


    }
}