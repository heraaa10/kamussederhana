package com.example.kamussederhana

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class CariFragment : Fragment(R.layout.fragment_cari) {

    private var searchJob: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dao = KamusDatabase.getInstance(requireContext()).kataDao()
        val etCari = view.findViewById<EditText>(R.id.etCari)
        val txtKosong = view.findViewById<TextView>(R.id.txtKosong)
        val rvHasil = view.findViewById<RecyclerView>(R.id.rvHasil)

        val adapter = KataAdapter()
        rvHasil.layoutManager = LinearLayoutManager(requireContext())
        rvHasil.adapter = adapter

        // Pencarian langsung dengan TextWatcher
        etCari.addTextChangedListener { editable ->
            val teks = editable?.toString()?.trim().orEmpty()

            // Batalkan query sebelumnya jika user masih mengetik cepat
            searchJob?.cancel()
            searchJob = viewLifecycleOwner.lifecycleScope.launch {
                if (teks.isEmpty()) {
                    adapter.submitList(emptyList())
                    txtKosong.visibility = View.GONE
                } else {
                    val hasil = dao.cari("%$teks%")
                    adapter.submitList(hasil)
                    txtKosong.visibility = if (hasil.isEmpty()) View.VISIBLE else View.GONE
                }
            }
        }
    }
}