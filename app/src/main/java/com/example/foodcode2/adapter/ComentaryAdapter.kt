package com.example.foodcode2.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodcode2.R
import com.example.foodcode2.data.UserComentary
import com.example.foodcode2.databinding.ComentaryItemBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ComentaryAdapter(
    private var _comentaryList: MutableList<UserComentary>,
) : RecyclerView.Adapter<ComentaryAdapter.ComentaryViewHolder>() {

    class ComentaryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ComentaryItemBinding.bind(view)

        fun bind(comentary: UserComentary) {
            binding.tvUserComent.text = comentary.user
            binding.tvComentText.text = comentary.comentary
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComentaryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ComentaryViewHolder(layoutInflater.inflate(R.layout.comentary_item, parent, false))
    }

    override fun getItemCount(): Int {
        return _comentaryList.size
    }

    override fun onBindViewHolder(holder: ComentaryViewHolder, position: Int) {
        holder.bind(_comentaryList[position])
    }

    fun setComentsList(coment: List<UserComentary>) {
        _comentaryList = coment.toMutableList()
    }
}