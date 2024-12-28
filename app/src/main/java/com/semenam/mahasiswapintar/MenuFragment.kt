package com.semenam.mahasiswapintar

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.semenam.mahasiswapintar.Response.SessionManager
import com.semenam.mahasiswapintar.databinding.FragmentMenuBinding

class MenuFragment : Fragment() {

    private lateinit var binding: FragmentMenuBinding
    private lateinit var sharedPreferences: SharedPreferences
    private val session by lazy { SessionManager.init(requireContext()) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment using data binding
        binding = FragmentMenuBinding.inflate(inflater, container, false)
        sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set click listener using data binding
        binding.mahasiswa.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_menuFragment_to_tampilMahasiswaFragment)
        }
        binding.jurusan.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_menuFragment_to_tampilJurusanFragment)
        }

        // Implementasi setOnClickListener untuk navigasi ke tampilMataKuliahFragment
        binding.mataKuliah.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_menuFragment_to_tampilMataKuliahFragment)
        }
        binding.settings.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_menuFragment_to_about_Me)
        }
        // Set click listener for logout button
        binding.btnlogout.setOnClickListener {
            session.logout()
            val intent = Intent(activity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    private fun moveIntent() {
        val intent = Intent(requireActivity(), MainActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }
}
