package com.daryl.mob23quizapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.daryl.mob23quizapp.data.models.User
import com.daryl.mob23quizapp.databinding.ItemStudentBinding

class StudentAdapter(
    private var students: List<User>
): RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder =
        StudentViewHolder(
            ItemStudentBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun getItemCount(): Int = students.size

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) =
        holder.bind(students[position])
    fun setStudents(students: List<User>) {
        this.students = students
        notifyDataSetChanged()
    }
    inner class StudentViewHolder(
        private val binding: ItemStudentBinding
    ): ViewHolder(binding.root) {
        fun bind(student: User) {
           binding.run {
               tvEmail.text = student.email
               tvUsername.text = student.username
           }
        }
    }
}