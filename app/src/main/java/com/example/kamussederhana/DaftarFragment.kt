package com.example.kamussederhana

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class DaftarFragment : Fragment(R.layout.fragment_daftar) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dao = KamusDatabase.getInstance(requireContext()).kataDao()
        val txtJumlah = view.findViewById<TextView>(R.id.txtJumlah)
        val rvDaftar = view.findViewById<RecyclerView>(R.id.rvDaftar)

        // Fungsi pembantu agar kode hapus tidak ditulis dua kali
        val hapusKata: (Kata) -> Unit = { kataDihapus ->
            viewLifecycleOwner.lifecycleScope.launch {
                dao.hapus(kataDihapus)
                Toast.makeText(
                    requireContext(),
                    "'${kataDihapus.indonesia}' dihapus",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        // Adapter dengan tombol ikon hapus
        val adapter = KataAdapter { kataDihapus ->
            hapusKata(kataDihapus)
        }

        rvDaftar.layoutManager = LinearLayoutManager(requireContext())
        rvDaftar.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            dao.ambilSemua().collect { daftar ->
                adapter.submitList(daftar)
                txtJumlah.text = "Jumlah kata: ${daftar.size}"
            }
        }

        // Tetap mendukung hapus dengan geser ke kiri atau kanan
        val swipeHandler = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val posisi = viewHolder.bindingAdapterPosition
                if (posisi != RecyclerView.NO_POSITION) {
                    val kataDihapus = adapter.currentList[posisi]
                    hapusKata(kataDihapus)
                }
            }
        }

        ItemTouchHelper(swipeHandler).attachToRecyclerView(rvDaftar)
    }
}