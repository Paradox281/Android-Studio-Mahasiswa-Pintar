package com.semenam.mahasiswapintar.AboutMe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.semenam.mahasiswapintar.databinding.FragmentAboutMeBinding
import com.semenam.mahasiswapintar.databinding.FragmentTampilMahasiswaBinding

class About_Me : Fragment() {

    private lateinit var binding: FragmentAboutMeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAboutMeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.kembali1.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }
}
