package com.semenam.mahasiswapintar.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.semenam.mahasiswapintar.Model.Mahasiswa
import com.semenam.mahasiswapintar.R

class MahasiswaAdapter(
    private val mahasiswaList: ArrayList<Mahasiswa>,
    private val editListener: (Mahasiswa) -> Unit,
    private val deleteListener: (Mahasiswa) -> Unit,
    private val detailListener: (Mahasiswa) -> Unit // Listener untuk detail button
) : RecyclerView.Adapter<MahasiswaAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nim: TextView = view.findViewById(R.id.nim)
        val nama: TextView = view.findViewById(R.id.nama)
        val jurusan: TextView = view.findViewById(R.id.jurusannya)
        val jeniskelamin: TextView = view.findViewById(R.id.kelamin)
        val gambar: ImageView = view.findViewById(R.id.gambar)
        val editButton: ImageView = view.findViewById(R.id.img_editmahasiswa)
        val deleteButton: ImageView = view.findViewById(R.id.img_deletemahasiswa)
        val detailButton: ImageView = view.findViewById(R.id.img_detail) // Detail button

        init {
            editButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    editListener(mahasiswaList[position])
                }
            }

            deleteButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    deleteListener(mahasiswaList[position])
                }
            }
            detailButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    detailListener(mahasiswaList[position])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_mahasiswa, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mahasiswa = mahasiswaList[position]
        holder.nim.text = mahasiswa.nim
        holder.nama.text = "Nama: ${mahasiswa.nama}"
        holder.jeniskelamin.text = "Jenis Kelamin: ${mahasiswa.jeniskelamin}"
        holder.jurusan.text = "Jurusan: ${mahasiswa.jurusan}"

        // Load image using Glide
        Glide.with(holder.itemView.context)
            .load(mahasiswa.foto)
            .into(holder.gambar)
    }

    override fun getItemCount(): Int {
        return mahasiswaList.size
    }
}