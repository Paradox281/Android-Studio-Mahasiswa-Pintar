package com.semenam.mahasiswapintar.CRUD_Mahasiswa

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bumptech.glide.Glide
import com.semenam.mahasiswapintar.databinding.FragmentDetailMahasiswaBinding


class Detail_Mahasiswa : Fragment() {
    private lateinit var binding: FragmentDetailMahasiswaBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailMahasiswaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mahasiswaId = arguments?.getInt("mahasiswaId", 0)
        val nim = arguments?.getString("nim")
        val nama = arguments?.getString("nama")
        val alamat = arguments?.getString("alamat")
        val jeniskelamin = arguments?.getString("jeniskelamin")
        val nohp = arguments?.getString("nohp")
        val jurusan = arguments?.getString("jurusan")
        val tanggallahir = arguments?.getString("tanggallahir")
        val foto = arguments?.getString("foto")


        binding.tvNimValue.text = nim
        binding.tvNamaValue.text = nama
        binding.tvAlamatValue.text = alamat
        binding.tvKelaminValue.text = jeniskelamin
        binding.tvNoHpValue.text = nohp
        binding.tvJurusanValue.text = jurusan
        binding.tvTanggalLahirValue.text = tanggallahir

        // Load image using Glide
        Glide.with(requireContext())
            .load(foto)
            .into(binding.ivFoto)
        binding.kembali5.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

}