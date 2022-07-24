package com.igrejasantoantonio.appusuario

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.igrejasantoantonio.appusuario.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // Declaração de variáveis
    private lateinit var newRecyclerView: RecyclerView // Definindo uma nova instância do RecyclerView
    private lateinit var newArrayList: ArrayList<Noticias> // Definindo uma nova instância da classe Noticias para ser preenchida posteriormente
    private lateinit var binding: ActivityMainBinding // Definindo binding para manipular os componentes do XML
    private val db = FirebaseFirestore.getInstance() // Declarando a instância do banco de dados


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater) // Inflando layout para uso dos componentes
        setContentView(binding.root)

        newRecyclerView = findViewById(R.id.view_reutilizavel)
        newRecyclerView.layoutManager = LinearLayoutManager(this)
        newRecyclerView.setHasFixedSize(true)

        newArrayList = arrayListOf<Noticias>()
        buscarNoticiaBanco() // Chamada da função para buscar as informações do banco de dados
        supportActionBar!!.hide()
    }

    private fun buscarNoticiaBanco(){ // Até o momento apenas busca do document "noticia", no projeto final buscará de todas existentes na coleção

        db.collection("noticias").get() // Definindo o caminho de onde será lido os registros
            .addOnSuccessListener { resultado -> // Só será adicionado ao aplicativo a notícia caso tenha sucesso no acesso ao banco de dados.
                for (documento in resultado){ // Loop para ler todas as notícias armazenadas no banco
                    // Separadamente conforme o for, será armazenado os dados das notícias nas variáveis declaradas
                    val titulo = documento.get("titulo").toString()
                    val noticia = documento.get("noticia").toString()
                    val data = documento.get("data").toString()
                    val autor = documento.get("autor").toString()
                    // Usando a classe Noticia para criar uma lista de dados e depois adicionando no newArrayList que leva os dados preenchidos na view
                    val news = Noticias(titulo, noticia, data, autor)
                    newArrayList.add(news)
                }
                // Por fim, adicionando o array já com todas as notícias no recyclerView
                newRecyclerView.adapter = MyAdapter(newArrayList)
            }
    }
}