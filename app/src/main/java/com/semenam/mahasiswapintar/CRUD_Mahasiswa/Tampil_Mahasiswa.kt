package com.semenam.mahasiswapintar.CRUD_Mahasiswa

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
import com.semenam.mahasiswapintar.API.API_Mahasiswa
import com.semenam.mahasiswapintar.Adapter.MahasiswaAdapter
import com.semenam.mahasiswapintar.Model.Mahasiswa
import com.semenam.mahasiswapintar.R
import com.semenam.mahasiswapintar.Response.ResponseMahasiswa
import com.semenam.mahasiswapintar.Response.SessionManager
import com.semenam.mahasiswapintar.databinding.FragmentTampilMahasiswaBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Tampil_Mahasiswa : Fragment() {

    private lateinit var binding: FragmentTampilMahasiswaBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MahasiswaAdapter
    private lateinit var mahasiswaList: ArrayList<Mahasiswa>
    private lateinit var sessionManager: SessionManager
    private var userRole: String = "" // Untuk menyimpan role pengguna


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sessionManager = SessionManager.init(requireContext()) // Inisialisasi SessionManager
        userRole = sessionManager.getCurrentUserRole() ?: ""
        binding = FragmentTampilMahasiswaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager = SessionManager(requireContext())
        recyclerView = binding.listDataMahasiswa
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        mahasiswaList = ArrayList()
        adapter = MahasiswaAdapter(mahasiswaList,
            { mahasiswa -> navigateToEditMahasiswa(mahasiswa) }, // Edit listener
            { mahasiswa -> deleteMahasiswa(mahasiswa) },
            { mahasiswa -> navigateToDetailMahasiswa(mahasiswa) } )
        recyclerView.adapter = adapter

        fetchMahasiswa()

        binding.kembali.setOnClickListener {
            requireActivity().onBackPressed()
        }
        arguments?.let {
            userRole = it.getString("userRole", "")
        }
        binding.btnTambahMahasiswa.setOnClickListener {
            if (userRole == "Administrator") {
                view?.findNavController()?.navigate(R.id.action_tampilMahasiswaFragment_to_tambahMahasiswaFragment)
            } else {
                Toast.makeText(requireContext(), "Anda tidak memiliki izin untuk menambah mahasiswa", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchMahasiswa() {
        val apiService = API_Mahasiswa.create().getMahasiswa()
        apiService.enqueue(object : Callback<ResponseMahasiswa> {
            override fun onResponse(call: Call<ResponseMahasiswa>, response: Response<ResponseMahasiswa>) {
                if (response.isSuccessful) {
                    val responseData = response.body()
                    if (responseData != null && responseData.status) {
                        mahasiswaList.clear()
                        responseData.data?.let { mahasiswaList.addAll(it) }
                        adapter.notifyDataSetChanged()
                    } else {
                        Log.e("Tampil_Mahasiswa", "Response unsuccessful: ${response.message()}")
                    }
                } else {
                    Log.e("Tampil_Mahasiswa", "Response unsuccessful: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ResponseMahasiswa>, t: Throwable) {
                Log.e("Tampil_Mahasiswa", "API call failed: ${t.message}")
                Toast.makeText(context, "Gagal memuat data", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun deleteMahasiswa(mahasiswa: Mahasiswa) {
        val role = sessionManager.getCurrentUserRole()
        if (role == "Administrator") {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Konfirmasi")
            builder.setMessage("Apakah Anda yakin ingin menghapus mahasiswa ini?")
            builder.setPositiveButton("Ya") { dialog, which ->
                val apiService = API_Mahasiswa.create().deleteMahasiswa(mahasiswa.id)
                apiService.enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            Toast.makeText(context, "Berhasil menghapus mahasiswa", Toast.LENGTH_SHORT).show()
                            fetchMahasiswa() // Ambil ulang data setelah berhasil hapus
                        } else {
                            Toast.makeText(context, "Gagal menghapus mahasiswa", Toast.LENGTH_SHORT).show()
                            Log.e("Tampil_Mahasiswa", "Delete failed: ${response.message()}")
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(context, "Gagal menghapus mahasiswa: ${t.message}", Toast.LENGTH_SHORT).show()
                        Log.e("Tampil_Mahasiswa", "Delete failed: ${t.message}")
                    }
                })
            }
            builder.setNegativeButton("Batalkan") { dialog, which ->
                dialog.dismiss()
            }
            builder.show()
        } else {
            // Role bukan admin, mungkin perlu memberikan pesan atau tindakan lain
            Toast.makeText(context, "Anda tidak memiliki izin untuk menghapus mahasiswa", Toast.LENGTH_SHORT).show()
            Log.w("Tampil_Mahasiswa", "Pengguna dengan role '$role' tidak diizinkan untuk menghapus mahasiswa")
        }
    }

    private fun navigateToEditMahasiswa(mahasiswa: Mahasiswa) {
        if (userRole == "Administrator") {
            val bundle = Bundle().apply {
            putInt("mahasiswaId", mahasiswa.id)
            putString("nim", mahasiswa.nim)
            putString("nama", mahasiswa.nama)
            putString("alamat", mahasiswa.alamat)
            putString("jeniskelamin", mahasiswa.jeniskelamin)
            putString("nohp", mahasiswa.nohp)
            putString("jurusan", mahasiswa.jurusan)
            putString("tanggallahir", mahasiswa.tanggallahir)
            putString("foto", mahasiswa.foto)
        }
        Navigation.findNavController(requireView()).navigate(
            R.id.action_tampilMahasiswaFragment_to_tambahMahasiswaFragment,
            bundle
        )
        } else {
            Toast.makeText(requireContext(), "Anda tidak memiliki izin untuk mengedit mahasiswa", Toast.LENGTH_SHORT).show()
        }
    }
    private fun navigateToDetailMahasiswa(mahasiswa: Mahasiswa) {
        val bundle = Bundle().apply {
            putInt("mahasiswaId", mahasiswa.id)
            putString("nim", mahasiswa.nim)
            putString("nama", mahasiswa.nama)
            putString("alamat", mahasiswa.alamat)
            putString("jeniskelamin", mahasiswa.jeniskelamin)
            putString("nohp", mahasiswa.nohp)
            putString("jurusan", mahasiswa.jurusan)
            putString("tanggallahir", mahasiswa.tanggallahir)
            putString("foto", mahasiswa.foto)
        }
        Navigation.findNavController(requireView()).navigate(
            R.id.action_tampilMahasiswaFragment_to_detailMahasiswaFragment,
            bundle
        )
    }
}
