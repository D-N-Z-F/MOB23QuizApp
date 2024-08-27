package com.daryl.mob23quizapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.daryl.mob23quizapp.R
import com.daryl.mob23quizapp.core.Constants.HOLDER_TYPE_1
import com.daryl.mob23quizapp.core.utils.ResourceProvider
import com.daryl.mob23quizapp.data.models.Quiz
import com.daryl.mob23quizapp.data.models.User
import com.daryl.mob23quizapp.databinding.ItemStudentBinding

class StudentAdapter(
    private var students: List<User>,
    private val holderType: Int,
    private val resourceProvider: ResourceProvider?
): RecyclerView.Adapter<StudentAdapter.BaseViewHolder>() {
    private var quiz: Quiz? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val binding = ItemStudentBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return when(holderType) {
            HOLDER_TYPE_1 -> StudentViewHolder(binding)
            else -> ParticipantViewHolder(binding)
        }
    }
    override fun getItemCount(): Int = students.size

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) =
        holder.bind(students[position])
    inner class StudentViewHolder(binding: ItemStudentBinding): BaseViewHolder(binding)
    inner class ParticipantViewHolder(
        private val binding: ItemStudentBinding
    ): BaseViewHolder(binding) {
        override fun bind(student: User) {
            super.bind(student)
            binding.run {
                quiz?.let { quiz ->
                    val participant = quiz.participants.find { it.studentEmail == student.email }
                    if(participant != null) {
                        tvTimeTaken.text = participant.timeTaken.toString()
                        mbScore.text = resourceProvider?.getString(
                            R.string.score, participant.score, quiz.questions.size
                        )
                        llDetails.visibility = View.VISIBLE
                    }
                }
            }
        }
    }
    abstract inner class BaseViewHolder(
        private val binding: ItemStudentBinding
    ): ViewHolder(binding.root) {
        open fun bind(student: User) {
            binding.run {
                tvEmail.text = student.email
                tvUsername.text = student.username
            }
        }
    }
    fun setStudents(students: List<User>) {
        // If teacher is viewing quiz participants, filter for students who participated in said quiz.
        this.students = if(resourceProvider != null) {
            val newData = mutableListOf<User>()
            quiz!!.participants.forEach {
                students.forEach { user ->
                    if(user.email == it.studentEmail) newData.add(user)
                }
            }
            newData
        } else students
        notifyDataSetChanged()
    }
    fun setQuiz(quiz: Quiz) { this.quiz = quiz }
}