package com.andre.task.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.andre.task.data.model.Task
import androidx.lifecycle.MutableLiveData

class TaskViewModel : ViewModel(){
    private val _taskUpdate = MutableLiveData<Task>()
    val taskUpdate: LiveData<Task> = _taskUpdate

    fun setUpdateTask(task: Task){
        _taskUpdate.value = task
    }
}