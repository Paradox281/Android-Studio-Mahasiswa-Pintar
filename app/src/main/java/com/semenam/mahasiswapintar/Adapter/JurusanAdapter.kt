package com.semenam.mahasiswapintar.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.semenam.mahasiswapintar.Model.Jurusan
import com.semenam.mahasiswapintar.R


class JurusanAdapter(
    private val jurusanList: ArrayList<Jurusan>,
    private val editListener: (Jurusan) -> Unit,
    private val deleteListener: (Jurusan) -> Unit
) : RecyclerView.Adapter<JurusanAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val kode: TextView = view.findViewById(R.id.kode1)
        val code : TextView = view.findViewById(R.id.codejurusan)
        val nama: TextView = view.findViewById(R.id.namajurusan)
        val kaprodi: TextView = view.findViewById(R.id.ketujurusan)
        val ketua: TextView = view.findViewById(R.id.ketuajurusan)
        val editButton: ImageView = view.findViewById(R.id.img_edit)
        val deleteButton: ImageView = view.findViewById(R.id.img_deletejurusan)

        init {
            editButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    editListener(jurusanList[position])
                }
            }

            deleteButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    deleteListener(jurusanList[position])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_jurusan, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val jurusan = jurusanList[position]
        holder.kode.text = jurusan.kode
        holder.nama.text = jurusan.nama
        holder.kaprodi.text = jurusan.kaprodi
        holder.ketua.text = "Ketua Prodi :"
        holder.code.text = "Kode Jurusan :"

    }

    override fun getItemCount(): Int {
        return jurusanList.size
    }
}