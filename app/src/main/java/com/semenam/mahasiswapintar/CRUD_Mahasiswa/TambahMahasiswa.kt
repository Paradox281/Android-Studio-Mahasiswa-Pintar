package com.semenam.mahasiswapintar.CRUD_Mahasiswa

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.net.Uri
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.radiobutton.MaterialRadioButton
import com.google.android.material.textfield.TextInputEditText
import com.semenam.mahasiswapintar.API.API_Mahasiswa
import com.semenam.mahasiswapintar.Model.Mahasiswa
import com.semenam.mahasiswapintar.Response.ResponseJurusan
import com.semenam.mahasiswapintar.databinding.FragmentTambahMahasiswaBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.Calendar

class TambahMahasiswa : Fragment() {

    private lateinit var binding: FragmentTambahMahasiswaBinding
    private var isEditMode = false
    private var mahasiswaId: Int = 0
    private lateinit var spinnerJurusan: Spinner
    private var jurusanArray: Array<String>? = null
    private lateinit var btnAmbilFoto: Button
    private lateinit var inputFotoMahasiswa: TextInputEditText
    private val TAKE_PHOTO_REQUEST = 2
    private val PICK_PHOTO_REQUEST = 3
    private var photoUri: String? = null
    private var year = 0
    private var month = 0
    private var day = 0
    private lateinit var radioLakiLaki: MaterialRadioButton
    private lateinit var radioPerempuan: MaterialRadioButton
    private var selectedGender: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTambahMahasiswaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnAmbilFoto = binding.btnAmbilFoto
        inputFotoMahasiswa = binding.inputfotomahasiswa

        btnAmbilFoto.setOnClickListener {
            if (isCameraPermissionGranted()) {
                val options = arrayOf<CharSequence>("Ambil Foto", "Pilih dari Galeri", "Batal")
                val builder = android.app.AlertDialog.Builder(requireContext())
                builder.setTitle("Tambah Foto")
                builder.setItems(options) { dialog, item ->
                    when {
                        options[item] == "Ambil Foto" -> {
                            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            startActivityForResult(takePictureIntent, TAKE_PHOTO_REQUEST)
                        }
                        options[item] == "Pilih dari Galeri" -> {
                            val pickPhotoIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                            startActivityForResult(pickPhotoIntent, PICK_PHOTO_REQUEST)
                        }
                        options[item] == "Batal" -> {
                            dialog.dismiss()
                        }
                    }
                }
                builder.show()
            } else {
                requestCameraPermission()
            }
        }

        spinnerJurusan = binding.spinnerjurusan

        loadJurusanData()

        binding.kembali1.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.inputtanggallahirmahasiswa.setOnClickListener {
            showDatePickerDialog()
        }
        radioLakiLaki = binding.radioLakiLaki
        radioPerempuan = binding.radioPerempuan


        if (arguments != null && arguments!!.containsKey("mahasiswaId")) {
            isEditMode = true
            mahasiswaId = arguments!!.getInt("mahasiswaId")
            val nim = arguments!!.getString("nim", "")
            val nama = arguments!!.getString("nama", "")
            val alamat = arguments!!.getString("alamat", "")
            val jeniskelamin = arguments!!.getString("jeniskelamin", "")
            val nohp = arguments!!.getString("nohp", "")
            val jurusan = arguments!!.getString("jurusan", "")
            val tanggallahir = arguments!!.getString("tanggallahir", "")
            val foto = arguments!!.getString("foto", "")

            binding.inputnimmahasiswa.setText(nim)
            binding.inputnamamahasiswa.setText(nama)
            binding.inputalamatmahasiswa.setText(alamat)
            binding.inputtanggallahirmahasiswa.setText(tanggallahir)
            binding.inputnohp.setText(nohp)
            inputFotoMahasiswa.setText(foto)

            jurusanArray?.let {
                val position = it.indexOf(jurusan)
                if (position >= 0) {
                    spinnerJurusan.setSelection(position)
                }
            }
            if (jeniskelamin == "Laki-Laki") {
                radioLakiLaki.isChecked = true
                selectedGender = "Laki-Laki"
            } else if (jeniskelamin == "Perempuan") {
                radioPerempuan.isChecked = true
                selectedGender = "Perempuan"
            }
            binding.btnsimpan.text = "Simpan Perubahan"
        }
        radioLakiLaki.setOnClickListener {
            radioLakiLaki.isChecked = true
            radioPerempuan.isChecked = false
            selectedGender = "Laki-Laki"
        }

        radioPerempuan.setOnClickListener {
            radioLakiLaki.isChecked = false
            radioPerempuan.isChecked = true
            selectedGender = "Perempuan"
        }

        binding.btnsimpan.setOnClickListener {
            binding.btnsimpan.isEnabled = false // Menonaktifkan tombol simpan setelah diklik

            val nim = binding.inputnimmahasiswa.text.toString()
            val nama = binding.inputnamamahasiswa.text.toString()
            val alamat = binding.inputalamatmahasiswa.text.toString()
            val nohp = binding.inputnohp.text.toString()
            val jurusan = spinnerJurusan.selectedItem.toString()
            val tanggallahir = binding.inputtanggallahirmahasiswa.text.toString()
            val foto = inputFotoMahasiswa.text.toString()
            val jeniskelamin = when {
                radioLakiLaki.isChecked -> "Laki-Laki"
                radioPerempuan.isChecked -> "Perempuan"
                else -> ""
            }
            if (isValidDateFormat(tanggallahir)) {
                if (nim.isNotEmpty() && nama.isNotEmpty() && alamat.isNotEmpty() && jeniskelamin.isNotEmpty() &&
                    nohp.isNotEmpty() && jurusan.isNotEmpty() && tanggallahir.isNotEmpty() && foto.isNotEmpty()) {
                    if (isEditMode) {
                        updateMahasiswa(Mahasiswa(mahasiswaId, nim, nama, alamat, jeniskelamin, nohp, jurusan, tanggallahir, foto, "", ""))
                    } else {
                        addMahasiswa(Mahasiswa(0, nim, nama, alamat, jeniskelamin, nohp, jurusan, tanggallahir, foto, "", ""))
                    }
                } else {
                    Toast.makeText(requireContext(), "Semua field harus diisi dengan benar", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Format tanggal harus yyyy-mm-dd", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isValidDateFormat(dateStr: String): Boolean {
        val regexPattern = Regex("""\d{4}-\d{2}-\d{2}""")
        return regexPattern.matches(dateStr)
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                year = selectedYear
                month = selectedMonth
                day = selectedDay
                val formattedDate = String.format("%d-%02d-%02d", year, month + 1, day)
                binding.inputtanggallahirmahasiswa.setText(formattedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun isCameraPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.CAMERA),
            TAKE_PHOTO_REQUEST
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                TAKE_PHOTO_REQUEST -> {
                    val photo = data?.extras?.get("data") as? Bitmap
                    if (photo != null) {
                        photoUri = saveBitmapToFile(photo).toString()
                        inputFotoMahasiswa.setText(photoUri)
                    }
                }
                PICK_PHOTO_REQUEST -> {
                    photoUri = data?.data.toString()
                    inputFotoMahasiswa.setText(photoUri)
                }
            }
        }
    }

    private fun saveBitmapToFile(bitmap: Bitmap): Uri {
        val file = File(requireContext().cacheDir, "${System.currentTimeMillis()}.jpg")
        try {
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return Uri.fromFile(file)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            TAKE_PHOTO_REQUEST -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(intent, TAKE_PHOTO_REQUEST)
                } else {
                    Toast.makeText(requireContext(), "Izin akses kamera ditolak", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun loadJurusanData() {
        val apiJurusan = API_Jurusan.create().getJurusan()
        apiJurusan.enqueue(object : Callback<ResponseJurusan> {
            override fun onResponse(call: Call<ResponseJurusan>, response: Response<ResponseJurusan>) {
                if (isAdded) {
                    if (response.isSuccessful) {
                        val jurusanList = response.body()?.data ?: emptyList()
                        jurusanArray = jurusanList.map { it.nama }.toTypedArray()
                        setupSpinner()
                    } else {
                        Toast.makeText(requireContext(), "Gagal memuat data jurusan", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<ResponseJurusan>, t: Throwable) {
                Toast.makeText(requireContext(), "Gagal memuat data jurusan", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupSpinner() {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, jurusanArray!!)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerJurusan.adapter = adapter
    }

    private fun addMahasiswa(mahasiswa: Mahasiswa) {
        val apiMahasiswa = API_Mahasiswa.create().addMahasiswa(mahasiswa)
        apiMahasiswa.enqueue(object : Callback<Mahasiswa> {
            override fun onResponse(call: Call<Mahasiswa>, response: Response<Mahasiswa>) {
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "Data mahasiswa berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                    requireActivity().onBackPressed()
                } else {
                    Toast.makeText(requireContext(), "Gagal menambahkan data mahasiswa", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Mahasiswa>, t: Throwable) {
                Toast.makeText(requireContext(), "Gagal menambahkan data mahasiswa", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateMahasiswa(mahasiswa: Mahasiswa) {
        val apiMahasiswa = API_Mahasiswa.create().updateMahasiswa(mahasiswaId, mahasiswa)
        apiMahasiswa.enqueue(object : Callback<Mahasiswa> {
            override fun onResponse(call: Call<Mahasiswa>, response: Response<Mahasiswa>) {
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "Data mahasiswa berhasil diperbarui", Toast.LENGTH_SHORT).show()
                    requireActivity().onBackPressed()
                } else {
                    Toast.makeText(requireContext(), "Gagal memperbarui data mahasiswa", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Mahasiswa>, t: Throwable) {
                Toast.makeText(requireContext(), "Gagal memperbarui data mahasiswa", Toast.LENGTH_SHORT).show()
            }
        })
    }
}