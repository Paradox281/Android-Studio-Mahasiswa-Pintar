package com.semenam.mahasiswapintar.CRUD_MataKuliah

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
import com.semenam.mahasiswapintar.API.API_MataKuliah
import com.semenam.mahasiswapintar.Adapter.MataKuliahAdapter
import com.semenam.mahasiswapintar.Model.MataKuliah
import com.semenam.mahasiswapintar.R
import com.semenam.mahasiswapintar.Response.ResponseMataKuliah
import com.semenam.mahasiswapintar.Response.SessionManager
import com.semenam.mahasiswapintar.databinding.FragmentTampilMataKuliahBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
class Tampil_MataKuliah : Fragment() {

    private lateinit var binding: FragmentTampilMataKuliahBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MataKuliahAdapter
    private lateinit var mataKuliahList: ArrayList<MataKuliah>
    private lateinit var sessionManager: SessionManager
    private var userRole: String = "" // Untuk menyimpan role pengguna

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTampilMataKuliahBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.listDataMataKuliah
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        mataKuliahList = ArrayList()
        adapter = MataKuliahAdapter(mataKuliahList,
            { mataKuliah -> navigateToEditMataKuliah(mataKuliah) }, // Edit listener
            { mataKuliah -> deleteMataKuliah(mataKuliah) })       // Delete listener
        recyclerView.adapter = adapter

        // Inisialisasi SessionManager untuk manajemen sesi pengguna
        sessionManager = SessionManager.init(requireContext())

        // Ambil role pengguna dari sesi
        userRole = sessionManager.getCurrentUserRole() ?: ""

        fetchMataKuliah()

        binding.kembali.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.btnTambahmatakuliah.setOnClickListener{
            if (userRole == "Administrator") {
                view?.findNavController()?.navigate(R.id.action_tampilMataKuliahFragment_to_tambahMataKuliahFragment)
            } else {
                Toast.makeText(requireContext(), "Anda tidak memiliki izin untuk menambah MataKuliah", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchMataKuliah() {
        val apiService = API_MataKuliah.create().getMatakuliah()
        apiService.enqueue(object : Callback<ResponseMataKuliah> {
            override fun onResponse(call: Call<ResponseMataKuliah>, response: Response<ResponseMataKuliah>) {
                if (response.isSuccessful) {
                    val responseData = response.body()
                    if (responseData != null && responseData.status) {
                        mataKuliahList.clear()
                        responseData.data?.let { mataKuliahList.addAll(it) }
                        adapter.notifyDataSetChanged()
                    } else {
                        Log.e("Tampil_MataKuliah", "Response unsuccessful: ${response.message()}")
                    }
                } else {
                    Log.e("Tampil_MataKuliah", "Response unsuccessful: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ResponseMataKuliah>, t: Throwable) {
                Log.e("Tampil_MataKuliah", "API call failed: ${t.message}")
                Toast.makeText(context, "Gagal memuat data", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun deleteMataKuliah(mataKuliah: MataKuliah) {
        val role = sessionManager.getCurrentUserRole()
        if (role == "Administrator") {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Konfirmasi")
            builder.setMessage("Apakah Anda yakin ingin menghapus mata kuliah ini?")
            builder.setPositiveButton("Ya") { dialog, which ->
                val apiService = API_MataKuliah.create().deleteMatakuliah(mataKuliah.id)
                apiService.enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            Toast.makeText(context, "Berhasil menghapus mata kuliah", Toast.LENGTH_SHORT).show()
                            fetchMataKuliah() // Ambil ulang data setelah berhasil hapus
                        } else {
                            Toast.makeText(context, "Gagal menghapus mata kuliah", Toast.LENGTH_SHORT).show()
                            Log.e("Tampil_MataKuliah", "Delete failed: ${response.message()}")
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(context, "Gagal menghapus MataKuliah: ${t.message}", Toast.LENGTH_SHORT).show()
                        Log.e("Tampil_Matakuliah", "Delete failed: ${t.message}")
                    }
                })
            }
            builder.setNegativeButton("Batalkan") { dialog, which ->
                dialog.dismiss()
            }
            builder.show()
        } else {
            // Role bukan admin, mungkin perlu memberikan pesan atau tindakan lain
            Toast.makeText(context, "Anda tidak memiliki izin untuk menghapus MataKuliah", Toast.LENGTH_SHORT).show()
            Log.w("Tampil_Matakuliah", "Pengguna dengan role '$role' tidak diizinkan untuk menghapus MataKuliah")
        }
    }

    private fun navigateToEditMataKuliah(mataKuliah: MataKuliah) {
        if (userRole == "Administrator") {
        val bundle = Bundle().apply {
            putInt("mataKuliahId", mataKuliah.id)
            putString("kode", mataKuliah.kode)
            putString("nama", mataKuliah.nama)
            putString("sks", mataKuliah.sks.toString())
            putString("semester", mataKuliah.semester)
            putString("jurusan", mataKuliah.jurusan)
        }
        Navigation.findNavController(requireView()).navigate(
            R.id.action_tampilMataKuliahFragment_to_tambahMataKuliahFragment,
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