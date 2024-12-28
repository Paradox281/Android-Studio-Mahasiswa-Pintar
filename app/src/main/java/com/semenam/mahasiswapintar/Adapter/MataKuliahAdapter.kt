package com.semenam.mahasiswapintar.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.semenam.mahasiswapintar.Model.MataKuliah
import com.semenam.mahasiswapintar.R

class MataKuliahAdapter(
    private val mataKuliahList: ArrayList<MataKuliah>,
    private val editListener: (MataKuliah) -> Unit,
    private val deleteListener: (MataKuliah) -> Unit
) : RecyclerView.Adapter<MataKuliahAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val kode: TextView = view.findViewById(R.id.kode)
        val nama: TextView = view.findViewById(R.id.namamapel)
        val sks: TextView = view.findViewById(R.id.sk)
        val semester: TextView = view.findViewById(R.id.sem)
        val jurusan: TextView = view.findViewById(R.id.jurus)
        val editButton: ImageView = view.findViewById(R.id.img_editmk)
        val deleteButton: ImageView = view.findViewById(R.id.img_deletemk)

        init {
            editButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    editListener(mataKuliahList[position])
                }
            }

            deleteButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    deleteListener(mataKuliahList[position])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_matakuliah, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mataKuliah = mataKuliahList[position]
        holder.kode.text = mataKuliah.kode
        holder.nama.text = "Mata Kuliah: ${mataKuliah.nama}"
        holder.sks.text = "SKS: ${mataKuliah.sks}"
        holder.semester.text = "Semester: ${mataKuliah.semester}"
        holder.jurusan.text = "Jurusan: ${mataKuliah.jurusan}"
    }

    override fun getItemCount(): Int {
        return mataKuliahList.size
    }
}