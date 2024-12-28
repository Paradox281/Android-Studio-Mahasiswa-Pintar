package com.semenam.mahasiswapintar.CRUD_Jurusan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.semenam.mahasiswapintar.Model.Jurusan
import com.semenam.mahasiswapintar.databinding.FragmentTambahJurusanBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Tambah_Jurusan : Fragment() {
    private lateinit var binding: FragmentTambahJurusanBinding
    private var isEditMode = false // Flag untuk menandai apakah dalam mode edit
    private var jurusanId: Int = 0 // Id jurusan yang akan di-edit

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment using data binding
        binding = FragmentTambahJurusanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.kembali1.setOnClickListener {
            requireActivity().onBackPressed()
        }

        // Check apakah fragment dijalankan dalam mode edit atau tambah
        if (arguments != null && arguments!!.containsKey("jurusanId")) {
            isEditMode = true
            jurusanId = arguments!!.getInt("jurusanId")
            val kode = arguments!!.getString("kode", "")
            val nama = arguments!!.getString("nama", "")
            val kaprodi = arguments!!.getString("kaprodi", "")

            // Set nilai awal untuk form jika dalam mode edit
            binding.inputKodeJurusan.setText(kode)
            binding.inputNamaJurusan.setText(nama)
            binding.inputKetuaProdi.setText(kaprodi)
            binding.btnSimpanJurusan.text = "Simpan Perubahan"
        }

        binding.btnSimpanJurusan.setOnClickListener {
            val kode = binding.inputKodeJurusan.text.toString()
            val nama = binding.inputNamaJurusan.text.toString()
            val kaprodi = binding.inputKetuaProdi.text.toString()

            if (kode.isNotEmpty() && nama.isNotEmpty() && kaprodi.isNotEmpty()) {
                if (isEditMode) {
                    // Jika dalam mode edit, panggil fungsi updateJurusan
                    updateJurusan(Jurusan(jurusanId, kode, nama, kaprodi, "", ""))
                } else {
                    addJurusan(Jurusan(0, kode, nama, kaprodi, "", ""))
                }
                } else {
                    Toast.makeText(requireContext(), "Semua field harus diisi", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

    private fun addJurusan(jurusan: Jurusan) {
        binding.btnSimpanJurusan.isEnabled = false
        val apiJurusan = API_Jurusan.create().addJurusan(jurusan)
        apiJurusan.enqueue(object : Callback<Jurusan> {
            override fun onResponse(call: Call<Jurusan>, response: Response<Jurusan>) {
                binding.btnSimpanJurusan.isEnabled = true
                if (response.isSuccessful) {
                    activity?.let {
                        Toast.makeText(it, "Jurusan berhasil ditambahkan", Toast.LENGTH_SHORT)
                            .show()
                        it.onBackPressed()
                    }
                } else {
                    Toast.makeText(requireContext(), "Gagal menambahkan jurusan", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Jurusan>, t: Throwable) {
                binding.btnSimpanJurusan.isEnabled = true // Aktifkan kembali tombol simpan
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
    private fun updateJurusan(jurusan: Jurusan) {
        binding.btnSimpanJurusan.isEnabled = false
        val apiJurusan = API_Jurusan.create().updateJurusan(jurusan.id, jurusan)
        apiJurusan.enqueue(object : Callback<Jurusan> {
            override fun onResponse(call: Call<Jurusan>, response: Response<Jurusan>) {
                binding.btnSimpanJurusan.isEnabled = true
                if (response.isSuccessful) {
                    activity?.let {
                        Toast.makeText(it, "Jurusan berhasil diperbarui", Toast.LENGTH_SHORT)
                            .show()
                        it.onBackPressed()
                    }
                } else {
                    Toast.makeText(requireContext(), "Gagal memperbarui jurusan", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Jurusan>, t: Throwable) {
                binding.btnSimpanJurusan.isEnabled = true // Aktifkan kembali tombol simpan
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