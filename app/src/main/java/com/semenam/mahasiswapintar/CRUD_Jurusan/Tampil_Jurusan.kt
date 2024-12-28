package com.semenam.mahasiswapintar.CRUD_Jurusan

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.semenam.mahasiswapintar.Adapter.JurusanAdapter
import com.semenam.mahasiswapintar.Model.Jurusan
import com.semenam.mahasiswapintar.R
import com.semenam.mahasiswapintar.Response.ResponseJurusan
import com.semenam.mahasiswapintar.Response.SessionManager
import com.semenam.mahasiswapintar.databinding.FragmentTampilJurusanBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Tampil_Jurusan : Fragment() {

    private lateinit var binding: FragmentTampilJurusanBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: JurusanAdapter
    private lateinit var jurusanList: ArrayList<Jurusan>
    private lateinit var sessionManager: SessionManager
    private var userRole: String = "" // Untuk menyimpan role pengguna


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sessionManager = SessionManager.init(requireContext()) // Inisialisasi SessionManager
        userRole = sessionManager.getCurrentUserRole() ?: ""
        binding = FragmentTampilJurusanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.listDataJurusan
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        jurusanList = ArrayList()
        adapter = JurusanAdapter(jurusanList,
            { jurusan -> navigateToEditJurusan(jurusan) }, // Edit listener
            { jurusan -> deleteJurusan(jurusan) })       // Delete listener
        recyclerView.adapter = adapter

        fetchJurusans()

        binding.kembali.setOnClickListener {
            requireActivity().onBackPressed()
        }
        arguments?.let {
            userRole = it.getString("userRole", "")
        }
        binding.btnTambahJurusan.setOnClickListener {
            if (userRole == "Administrator") {
                view?.findNavController()?.navigate(R.id.action_tampilJurusanFragment_to_tambahJurusanFragment)
            } else {
                Toast.makeText(requireContext(), "Anda tidak memiliki izin untuk menambah Jurusan", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchJurusans() {
        val apiService = API_Jurusan.create().getJurusan()
        apiService.enqueue(object : Callback<ResponseJurusan> {
            override fun onResponse(
                call: Call<ResponseJurusan>,
                response: Response<ResponseJurusan>
            ) {
                if (response.isSuccessful) {
                    val responseData = response.body()
                    if (responseData != null && responseData.status) {
                        jurusanList.clear()
                        responseData.data?.let { jurusanList.addAll(it) }
                        adapter.notifyDataSetChanged()
                    } else {
                        Log.e("Tampil_Jurusan", "Response unsuccessful: ${response.message()}")
                    }
                } else {
                    Log.e("Tampil_Jurusan", "Response unsuccessful: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ResponseJurusan>, t: Throwable) {
                Log.e("Tampil_Jurusan", "API call failed: ${t.message}")
                Toast.makeText(context, "Gagal memuat data", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun deleteJurusan(jurusan: Jurusan) {
        val role = sessionManager.getCurrentUserRole()
        if (role == "Administrator") {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Konfirmasi")
            builder.setMessage("Apakah Anda yakin ingin menghapus jurusan ini?")
            builder.setPositiveButton("Ya") { dialog, which ->
                val apiService = API_Jurusan.create().deleteJurusan(jurusan.id)
                apiService.enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            Toast.makeText(context, "Berhasil menghapus jurusan", Toast.LENGTH_SHORT).show()
                            fetchJurusans() // Ambil ulang data setelah berhasil hapus
                        } else {
                            Toast.makeText(context, "Gagal menghapus jurusan", Toast.LENGTH_SHORT).show()
                            Log.e("Tampil_Jurusan", "Delete failed: ${response.message()}")
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(context, "Gagal menghapus jurusan: ${t.message}", Toast.LENGTH_SHORT).show()
                        Log.e("Tampil_Jurusan", "Delete failed: ${t.message}")
                    }
                })
            }
            builder.setNegativeButton("Batalkan") { dialog, which ->
                dialog.dismiss()
            }
            builder.show()
        } else {
            // Role bukan admin, mungkin perlu memberikan pesan atau tindakan lain
            Toast.makeText(context, "Anda tidak memiliki izin untuk menghapus jurusan", Toast.LENGTH_SHORT).show()
            Log.w("Tampil_Jurusan", "Pengguna dengan role '$role' tidak diizinkan untuk menghapus jurusan")
        }
    }

    private fun navigateToEditJurusan(jurusan: Jurusan) {
        if (userRole == "Administrator") {
            val bundle = Bundle().apply {
                putInt("jurusanId", jurusan.id)
                putString("kode", jurusan.kode)
                putString("nama", jurusan.nama)
                putString("kaprodi", jurusan.kaprodi)
            }
            Navigation.findNavController(requireView()).navigate(
                R.id.action_tampilJurusanFragment_to_tambahJurusanFragment,
                bundle
            )
        } else {
            Toast.makeText(
                requireContext(),
                "Anda tidak memiliki izin untuk mengakses fitur ini",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}