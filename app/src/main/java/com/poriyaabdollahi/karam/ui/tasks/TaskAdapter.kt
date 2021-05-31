package com.poriyaabdollahi.karam.ui.tasks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.poriyaabdollahi.karam.data.Task
import com.poriyaabdollahi.karam.databinding.ItemTaskBinding

import kotlin.coroutines.coroutineContext

class TaskAdapter (private val listener:onItemClickListener): ListAdapter<Task, TaskAdapter.TaskViewHolder> (DiffCallback()){
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
            }
        }
    }
    interface onItemClickListener{
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