package com.igrejasantoantonio.appusuario

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*

class MyAdapter(private val newList : ArrayList<Noticias>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder { // Adiciona o layout de notícias para cada notícia no banco
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.lista_noticias, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) { // Baseado na posição do item, é adicionado os textos recuperados do banco
        val currentItem = newList[position]
        holder.titulo.setText(currentItem.titulo)
        holder.noticia.setText(currentItem.noticia)
        holder.data.setText(currentItem.data)
        holder.autor.setText(currentItem.autor)
    }

    override fun getItemCount(): Int { // Retorna o tamanho total do arrayList

        return newList.size
    }

    class MyViewHolder(itemView : View) : ViewHolder(itemView){ // Classe para conectar Kotlin com os componentes do XML

        val titulo : TextView = itemView.findViewById(R.id.titulo)
        val noticia : TextView = itemView.findViewById(R.id.noticia)
        val data : TextView = itemView.findViewById(R.id.data)
        val autor : TextView = itemView.findViewById(R.id.autor)
    }
}