package com.example.kamussederhana

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class KataAdapter(
    private val onHapusClick: ((Kata) -> Unit)? = null
) : ListAdapter<Kata, KataAdapter.KataViewHolder>(DIFF) {

    class KataViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtIndonesia: TextView = view.findViewById(R.id.txtIndonesia)
        val txtInggris: TextView = view.findViewById(R.id.txtInggris)
        val btnHapus: ImageButton = view.findViewById(R.id.btnHapus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KataViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_kata, parent, false)
        return KataViewHolder(view)
    }

    override fun onBindViewHolder(holder: KataViewHolder, position: Int) {
        val kata = getItem(position)
        holder.txtIndonesia.text = kata.indonesia
        holder.txtInggris.text = kata.inggris

        if (onHapusClick != null) {
            holder.btnHapus.visibility = View.VISIBLE
            holder.btnHapus.setOnClickListener { onHapusClick.invoke(kata) }
        } else {
            holder.btnHapus.visibility = View.GONE
        }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Kata>() {
            override fun areItemsTheSame(lama: Kata, baru: Kata) = lama.id == baru.id
            override fun areContentsTheSame(lama: Kata, baru: Kata) = lama == baru
        }
    }
}