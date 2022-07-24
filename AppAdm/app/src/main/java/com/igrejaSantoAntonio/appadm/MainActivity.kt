package com.igrejaSantoAntonio.appadm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import com.google.firebase.firestore.FirebaseFirestore
import com.igrejaSantoAntonio.appadm.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    // Declaração de variáveis
    private lateinit var binding: ActivityMainBinding // Definindo binding para manipular os componentes do XML
    private val db = FirebaseFirestore.getInstance() // Declarando a instância do banco de dados

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater) // Inflando layout para uso dos componentes
        setContentView(binding.root)

        binding.btnProcurar.isVisible = false
        binding.procuraData.isVisible = false
        binding.procuraTitulo.isVisible = false

        supportActionBar!!.hide() // Tirando a barra de ação da view

        binding.btnPublicar.setOnClickListener{ // Definindo um evento ao clicar no botão "Publicar"
            // Coletando o que foi escrito nos Textview
            val titulo = binding.editTitulo.text.toString()
            val noticia = binding.editNoticia.text.toString()
            val data = binding.editData.text.toString()
            val autor = binding.editAutor.text.toString()

            if(titulo.isEmpty() || noticia.isEmpty() || data.isEmpty() || autor.isEmpty()) // Caso algum campo esteja vazio, mostrará a mensagem a seguir
            {
                binding.mensagem.text = "Preencha todos os campos!"
            }
            else
            {
                when (binding.btnPublicar.text){
                    "Publicar Notícia" ->  salvarNoticia(titulo, noticia, data, autor) // Caso todos os campos estiverem preenchidos, será realizado a chamada do metódo para salvar a notícia no banco
                    "Atualizar Notícia" -> salvarNoticia(titulo, noticia, data, autor) // O mesmo método usado para publicar também é usado para atualizar por conta de usar o método .set
                    "Deletar Notícia" ->  excluirNoticia(titulo, data) // Função para deletar a notícia escolhida
                }

            }
        }

        binding.btnAtualizar.setOnClickListener { // Configurações de telas para atualizar notícias
            limparCampos()
            binding.titulo.setText("Atualizar Notícia")
            binding.editData.isEnabled = false
            binding.editTitulo.isEnabled = false
            binding.btnProcurar.isVisible = true
            binding.procuraData.isVisible = true
            binding.procuraTitulo.isVisible = true
            binding.btnPublicar.text = "Atualizar Notícia"
        }

        binding.btnEscrever.setOnClickListener { // Configurações de telas para criar notícias
            limparCampos()
            binding.titulo.setText("Publicar Notícia")
            binding.editData.isEnabled = true
            binding.editTitulo.isEnabled = true
            binding.editAutor.isEnabled = true
            binding.editNoticia.isEnabled = true
            binding.btnProcurar.isVisible = false
            binding.procuraData.isVisible = false
            binding.procuraTitulo.isVisible = false
            binding.btnPublicar.text = "Publicar Notícia"
        }

        binding.btnDeletar.setOnClickListener { // Configurações de telas para excluir notícias
            limparCampos()
            binding.titulo.setText("Deletar Notícia")
            binding.editData.isEnabled = false
            binding.editTitulo.isEnabled = false
            binding.editAutor.isEnabled = false
            binding.editNoticia.isEnabled = false
            binding.btnProcurar.isVisible = true
            binding.procuraData.isVisible = true
            binding.procuraTitulo.isVisible = true
            binding.btnPublicar.text = "Deletar Notícia"
        }

        binding.btnProcurar.setOnClickListener { // Evento para procurar no banco a notícia baseado no título e data informados pelo usuário
            val titulo = binding.procuraTitulo.text.toString()
            val data = binding.procuraData.text.toString()
            // Verificação para os campos de busca
            if (titulo.isEmpty() || data.isEmpty()){
                binding.mensagem.text = "Preencha os campos para procurar a notícia."
            }
            else
            {
                val pathDocument = titulo + ": " + data.replace("/", "-")

                db.collection("noticias").document(pathDocument).addSnapshotListener { noticia, error -> // Evento para retornar a noticia e seus dados conforme caminho do documento passado
                    if (noticia != null) { // Caso ache a notícia, será feito o preenchimento dos campos
                        binding.editTitulo.setText(noticia.getString("titulo"))
                        binding.editNoticia.setText(noticia.getString("noticia"))
                        binding.editData.setText(noticia.getString("data"))
                        binding.editAutor.setText(noticia.getString("autor"))
                    }
                }
            }
        }

    }

    private fun excluirNoticia(titulo: String, data: String){ // // Método para excluir uma nova notícia

        val pathDocument = titulo + ": " + data.replace("/", "-")
        val reference = db.collection("noticias").document(pathDocument)

        reference.delete() // método de exclusão usado através da referência ao banco / caminho do documento a ser excluído
    }

    private fun salvarNoticia(titulo: String, noticia: String, data: String, autor: String){ // Método para criar uma nova notícia

        val mapNoticias = hashMapOf( // definição do "schema"
            "titulo" to titulo,
            "noticia" to noticia,
            "data" to data,
            "autor" to autor
        )

        val pathDocument = titulo + ": " + data.replace("/", "-") // Cria o nome do documento, tendo o titulo e a chave como "id"

        val reference = db.collection("noticias").document(pathDocument) // Definindo o caminho de onde será escrito os registros

            reference.set(mapNoticias).addOnCompleteListener{ tarefa -> // Lampda para passar os valores passados no schema
                if (tarefa.isSuccessful){ // Só será mostrado a mensagem tenha sucesso no acesso e gravação ao banco de dados.
                    binding.mensagem.text = "Notícia publicada com sucesso!" // Mostrando mensagem para feedback ao usuário
                    binding.Scroll.scrollTo(0,0) // Restaurando a posição do scroll para o início da página
                    limparCampos() // Chamada da função para limpar os campos e ficar pronto para escrever uma outra notícia
                }
            }
    }

    private fun limparCampos(){ // Definindo todos os campos como string vazia
        binding.procuraData.setText("")
        binding.procuraTitulo.setText("")
        binding.editTitulo.setText("")
        binding.editNoticia.setText("")
        binding.editData.setText("")
        binding.editAutor.setText("")
    }
}

