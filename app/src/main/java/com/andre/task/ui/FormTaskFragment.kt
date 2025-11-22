package com.andre.task.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.andre.task.R
import com.andre.task.data.model.Status
import com.andre.task.data.model.Task
import com.andre.task.databinding.FragmentFormTaskBinding
import com.andre.task.util.initToolbar
import com.andre.task.util.showBottomSheet
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database


class FormTaskFragment : Fragment() {
    private var _binding: FragmentFormTaskBinding? = null
    private val binding get() = _binding!!

    private lateinit var task: Task
    private var newTask: Boolean = true
    private var status: Status = Status.TODO
    private lateinit var reference: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private val args: FormTaskFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFormTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(binding.toolbar)

        reference = Firebase.database.reference
        auth = Firebase.auth

        getArgs()
        initListener()
    }

    private fun getArgs(){
        args.task.let {
            if (it != null){
                this.task = it
            }
        }
    }

    private fun configTask(){
        newTask = false
        status = task.status

        binding.editTextDescricao.setText(task.description)
        setStatus()
    }

    private fun setStatus(){
        val id = when (task.status){
            Status.TODO -> R.id.rbTodo
            Status.DOING -> R.id.rbDoing
            else -> R.id.rbDone
        }
        binding.radioGroup.check(id)
    }

    private fun initListener(){
        binding.buttonSave.setOnClickListener {
            valideData()
        }

        binding.radioGroup.setOnCheckedChangeListener{ _, id-> status =
            when(id){
                R.id.rbTodo -> Status.TODO
                R.id.rbTodo -> Status.DOING
                else -> Status.DONE
            }

        }
    }

    private fun valideData(){
        val description = binding.editTextDescricao.text.toString().trim()

        if (description.isNotBlank()){
            binding.progressBar.isVisible = true

            if(newTask) {
                task = Task()
                task.id = reference.database.reference.push().key ?: ""
            }

            task.description = description
            task.status = status

            saveTask()
        }else{
            showBottomSheet(message = getString(R.string.description_empty_form_task_fragment))
        }
    }

    private fun saveTask(){
        reference
            .child("tasks")
            .child(auth.currentUser?.uid ?: "")
            .child(task.id)
            .setValue(task).addOnCompleteListener { result ->
                if(result.isSuccessful){
                    Toast.makeText(
                        requireContext(),
                        R.string.text_save_sucess_form_task_fragment,
                        Toast.LENGTH_SHORT).show()

                    if(newTask){
                        findNavController().popBackStack()

                    }else{
                        binding.progressBar.isVisible = false
                    }
                }else{
                    binding.progressBar.isVisible = false
                    showBottomSheet(message = getString(R.string.error_generic))
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}