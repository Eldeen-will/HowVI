package com.igrejaSantoAntonio.appadm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.igrejaSantoAntonio.appadm.databinding.ActivityMainBinding
import kotlin.concurrent.timer
import kotlin.concurrent.timerTask
import kotlin.time.measureTime
import android.text.method.Touch.scrollTo as scrollTo

class MainActivity : AppCompatActivity() {
    // Declaração de variáveis
    private lateinit var binding: ActivityMainBinding // Definindo binding para manipular os componentes do XML
    private val db = FirebaseFirestore.getInstance() // Declarando a instância do banco de dados

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater) // Inflando layout para uso dos componentes
        setContentView(binding.root)

        supportActionBar!!.hide() // Tirando a barra de ação da view

        binding.btnPublicar.setOnClickListener{ // Definindo um evento ao clicar no botão "Publicar"
            // Coletando o que foi escrito nos Textview
            val titulo = binding.editTitulo.text.toString()
            val noticia = binding.editNoticia.text.toString()
            val data = binding.editData.text.toString()
            val autor = binding.editAutor.text.toString()

            if(titulo.isEmpty() || noticia.isEmpty() || data.isEmpty() || autor.isEmpty()) // Caso algum campo esteja vazio, mostrará a mensagem a seguir
            {
                binding.mensagem.setText("Preencha todos os campos!")
            }
            else
            {
                salvarNoticia(titulo, noticia, data, autor) // Caso todos os campos estiverem preenchidos, será realizado a chamada do metódo para salvar a notícia no banco
            }
        }
    }

    private fun salvarNoticia(titulo: String, noticia: String, data: String, autor: String){ // A princípio com um único path, na entrega final criará documentos acumulando dinamicamente, ao invés de sobrescrever o mesmo

        val mapNoticias = hashMapOf( // definição do "schema"
            "titulo" to titulo,
            "noticia" to noticia,
            "data" to data,
            "autor" to autor
        )

        db.collection("noticias").document("noticia") // Definindo o caminho de onde será escrito os registros
            .set(mapNoticias).addOnCompleteListener{ tarefa -> // Lampda para definir
                if (tarefa.isSuccessful){ // Só será adicionado ao banco a notícia caso tenha sucesso no acesso ao banco de dados.
                    binding.mensagem.setText("Notícia publicada com sucesso!") // Mostrando mensagem para feedback ao usuário
                    binding.Scroll.scrollTo(0,0) // Restaurando a posição do scroll para o início da página
                    limparCampos() // Chamada da função para limpar os campos e ficar pronto para escrever uma outra notícia
                }
            }
    }

    private fun limparCampos(){ // Definindo todos os campos como string vazia
        binding.editTitulo.setText("")
        binding.editNoticia.setText("")
        binding.editData.setText("")
        binding.editAutor.setText("")
    }
}

