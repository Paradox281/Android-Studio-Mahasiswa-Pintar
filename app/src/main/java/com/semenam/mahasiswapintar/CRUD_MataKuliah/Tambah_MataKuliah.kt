package com.semenam.mahasiswapintar.CRUD_MataKuliah

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.semenam.mahasiswapintar.API.API_MataKuliah
import com.semenam.mahasiswapintar.Model.MataKuliah
import com.semenam.mahasiswapintar.Response.ResponseJurusan
import com.semenam.mahasiswapintar.databinding.FragmentTambahMataKuliahBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Tambah_MataKuliah : Fragment() {
    private lateinit var binding: FragmentTambahMataKuliahBinding
    private var isEditMode = false // Flag untuk menandai apakah dalam mode edit
    private var mataKuliahId: Int = 0 // Id mata kuliah yang akan di-edit
    private lateinit var spinnerJurusan: Spinner
    private var jurusanArray: Array<String>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment using data binding
        binding = FragmentTambahMataKuliahBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        spinnerJurusan = binding.spinnerJurusanMK

        // Setup adapter for spinner (replace with your actual data and adapter)
        loadJurusanData()

        binding.kembali1.setOnClickListener {
            requireActivity().onBackPressed()
        }

        // Check apakah fragment dijalankan dalam mode edit atau tambah
        if (arguments != null && arguments!!.containsKey("mataKuliahId")) {
            isEditMode = true
            mataKuliahId = arguments!!.getInt("mataKuliahId")
            val kode = arguments!!.getString("kode", "")
            val nama = arguments!!.getString("nama", "")
            val semester = arguments!!.getString("semester", "")
            val sks = arguments!!.getInt("sks", 0)
            val jurusan = arguments!!.getString("jurusan", "")

            // Set nilai awal untuk form jika dalam mode edit
            binding.inputKodeMatkul.setText(kode)
            binding.inputNamaMatkul.setText(nama)
            binding.inputSemester.setText(semester)
            binding.inputSks.setText(sks.toString())
            // Ensure jurusanArray is not null before accessing it
            jurusanArray?.let {
                val position = it.indexOf(jurusan)
                if (position >= 0) {
                    spinnerJurusan.setSelection(position)
                }
            }
            binding.btnSimpanMatkul.text = "Simpan Perubahan"
        }

        binding.btnSimpanMatkul.setOnClickListener {
            val kode = binding.inputKodeMatkul.text.toString()
            val nama = binding.inputNamaMatkul.text.toString()
            val semester = binding.inputSemester.text.toString()
            val sks = binding.inputSks.text.toString().toInt()
            val jurusan = spinnerJurusan.selectedItem.toString()

            if (kode.isNotEmpty() && nama.isNotEmpty() && semester.isNotEmpty() && sks > 0 && jurusan.isNotEmpty()) {
                if (isEditMode) {
                    // Jika dalam mode edit, panggil fungsi updateMataKuliah
                    updateMataKuliah(MataKuliah(mataKuliahId, kode, nama, semester, sks, jurusan, "", ""))
                } else {
                    addMataKuliah(MataKuliah(0, kode, nama, semester, sks, jurusan, "", ""))
                }
            } else {
                Toast.makeText(requireContext(), "Semua field harus diisi dengan benar", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadJurusanData() {
        val apiJurusan = API_Jurusan.create().getJurusan()
        apiJurusan.enqueue(object : Callback<ResponseJurusan> {
            override fun onResponse(call: Call<ResponseJurusan>, response: Response<ResponseJurusan>) {
                if (response.isSuccessful) {
                    val jurusanList = response.body()?.data ?: emptyList()
                    jurusanArray = jurusanList.map { it.nama }.toTypedArray()
                    setupSpinner()
                } else {
                    Toast.makeText(requireContext(), "Gagal memuat data jurusan", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseJurusan>, t: Throwable) {
                Toast.makeText(requireContext(), "Kesalahan jaringan: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupSpinner() {
        jurusanArray?.let { array ->
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, array)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerJurusan.adapter = adapter
        }
    }

    private fun addMataKuliah(mataKuliah: MataKuliah) {
        binding.btnSimpanMatkul.isEnabled = false
        val apiMataKuliah = API_MataKuliah.create().addMatakuliah(mataKuliah)
        apiMataKuliah.enqueue(object : Callback<MataKuliah> {
            override fun onResponse(call: Call<MataKuliah>, response: Response<MataKuliah>) {
                binding.btnSimpanMatkul.isEnabled = true
                if (response.isSuccessful) {
                    activity?.let {
                        Toast.makeText(it, "Mata Kuliah berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                        it.onBackPressed()
                    }
                } else {
                    Toast.makeText(requireContext(), "Gagal menambahkan Mata Kuliah", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<MataKuliah>, t: Throwable) {
                binding.btnSimpanMatkul.isEnabled = true // Aktifkan kembali tombol simpan
                activity?.let {
                    Toast.makeText(
                        requireContext(),
                        "Kesalahan jaringan: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun updateMataKuliah(mataKuliah: MataKuliah) {
        binding.btnSimpanMatkul.isEnabled = false
        val apiMataKuliah = API_MataKuliah.create().updateMatakuliah(mataKuliah.id, mataKuliah)
        apiMataKuliah.enqueue(object : Callback<MataKuliah> {
            override fun onResponse(call: Call<MataKuliah>, response: Response<MataKuliah>) {
                binding.btnSimpanMatkul.isEnabled = true
                if (response.isSuccessful) {
                    activity?.let {
                        Toast.makeText(it, "Mata Kuliah berhasil diperbarui", Toast.LENGTH_SHORT).show()
                        it.onBackPressed()
                    }
                } else {
                    Toast.makeText(requireContext(), "Gagal memperbarui Mata Kuliah", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<MataKuliah>, t: Throwable) {
                binding.btnSimpanMatkul.isEnabled = true // Aktifkan kembali tombol simpan
                activity?.let {
                    Toast.makeText(
                        requireContext(),
                        "Kesalahan jaringan: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }
}