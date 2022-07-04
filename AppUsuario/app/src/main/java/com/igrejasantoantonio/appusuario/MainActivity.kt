package com.igrejasantoantonio.appusuario

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.firestore.FirebaseFirestore
import com.igrejasantoantonio.appusuario.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // Declaração de variáveis
    private lateinit var binding: ActivityMainBinding // Definindo binding para manipular os componentes do XML
    private val db = FirebaseFirestore.getInstance() // Declarando a instância do banco de dados

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater) // Inflando layout para uso dos componentes
        setContentView(binding.root)

        supportActionBar!!.hide()
        buscarNoticiaBanco() // Chamada da função para buscar as informações do banco de dados
    }

    private fun buscarNoticiaBanco(){ // Até o momento apenas busca do document "noticia", no projeto final buscará de todas existentes na coleção

        db.collection("noticias").document("noticia").get() // Definindo o caminho de onde será lido os registros
            .addOnCompleteListener{ documento -> // Lampda para definir
                if(documento.isSuccessful){ // Só será adicionado ao aplicativo a notícia caso tenha sucesso no acesso ao banco de dados.
                    // tratamento dos registros
                    val titulo = documento.result.get("titulo").toString()
                    val noticia = documento.result.get("noticia").toString()
                    val data = documento.result.get("data").toString()
                    val autor = documento.result.get("autor").toString()
                    // definindo na view a todas as informações coletadas
                    binding.titulo.text = titulo
                    binding.noticia.text = noticia
                    binding.data.text = data
                    binding.autor.text = autor
                }
            }
    }
}