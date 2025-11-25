package br.com.mathfun.contagem

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import br.com.mathfun.R
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog

// Estrutura simples para agrupar dados relacionados, lembra um DTO ou Struct
data class Pergunta(
    val imagemId: Int,
    val respostaCorreta: Int
)
class ContagemActivity : AppCompatActivity() {

    private lateinit var tvProgresso: TextView
    private lateinit var tvPergunta: TextView
    private lateinit var imgContagem: ImageView
    private lateinit var btnOpcao1: Button
    private lateinit var btnOpcao2: Button
    private lateinit var btnOpcao3: Button

    private lateinit var btnVoltar: ImageButton

    // Imagens
    private val todasImagens = listOf(
        Pergunta(R.drawable.fig_1, 7),
        Pergunta(R.drawable.fig_2, 3),
        Pergunta(R.drawable.fig_3, 8),
        Pergunta(R.drawable.fig_4, 4),
        Pergunta(R.drawable.fig_5, 2),
        Pergunta(R.drawable.fig_6, 9),
        Pergunta(R.drawable.fig_7, 6),
        Pergunta(R.drawable.fig_8, 3),
        Pergunta(R.drawable.fig_9, 5),
        Pergunta(R.drawable.fig_10, 2)
    )

    // Inicializacoa das variáveis
    private lateinit var perguntas: List<Pergunta>
    private var perguntaAtual = 0
    private var pontuacao = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_contagem)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Componentes da view
        tvProgresso = findViewById(R.id.tvProgresso)
        tvPergunta = findViewById(R.id.tvPergunta)
        imgContagem = findViewById(R.id.imgContagem)
        btnOpcao1 = findViewById(R.id.btnOpcao1)
        btnOpcao2 = findViewById(R.id.btnOpcao2)
        btnOpcao3 = findViewById(R.id.btnOpcao3)
        btnVoltar = findViewById(R.id.btnVoltarContagem)

        // Embaralhar e pegar 5 perguntas
        perguntas = todasImagens.shuffled().take(5)

        // Chama a função para mostrar primeira pergunta
        mostrarPergunta()

        // Configurar cliques dos botões
        btnOpcao1.setOnClickListener { verificarResposta(btnOpcao1.text.toString().toInt()) }
        btnOpcao2.setOnClickListener { verificarResposta(btnOpcao2.text.toString().toInt()) }
        btnOpcao3.setOnClickListener { verificarResposta(btnOpcao3.text.toString().toInt()) }

        btnVoltar.setOnClickListener {
            finish() // Fecha a activity atual e volta para a anterior
        }
    }// end onCreate

    private fun mostrarPergunta(){
        // Pega a primeira pergunta da lista, pos 0
        val pergunta = perguntas[perguntaAtual]
        // Atualiza meu textView pergunta
        tvProgresso.text = "Pergunta ${perguntaAtual + 1} de 5"
        // Mostrar a imagem da pergunta atual
        imgContagem.setImageResource(pergunta.imagemId)

        // Gera minha lista com as 3 opções
        val opcoes = gerarOpcoes(pergunta.respostaCorreta)

        // Faz a atribuição das opções nos botões
        btnOpcao1.text = opcoes[0].toString()
        btnOpcao2.text = opcoes[1].toString()
        btnOpcao3.text = opcoes[2].toString()

    } // end mostrarPergunta

    private fun gerarOpcoes(respCerta: Int): List<Int>{
        // Gera um objeto 'lista mutável', pq tem ferramentas
        val opcoes = mutableListOf<Int>()
        // Adiciono a respCerta na lista
        opcoes.add(respCerta)

        while (opcoes.size < 3){
            // Uso um lambda para gerar números, filtra o 0 e pega 1 aleatório
            val diferenca = (-3..3).filter { it != 0 }.random()
            // Estratégia para escolher as opçoes erradas
            val opcaoErrada = respCerta + diferenca

            // Se a opcao errada for única, jogo na lista
            if(opcaoErrada !in opcoes){
                opcoes.add(opcaoErrada)
            }
        } // end while
        // Para evitar a repetição das posições
        return opcoes.shuffled()
    } // end geraOpcoes

    //Método Principal
    private fun verificarResposta(respEscolhida: Int){
        // Seleciono o atributo 'respostaCorreta' do meu tipo pergunta da resposta atual
        val respCorreta = perguntas[perguntaAtual].respostaCorreta

        if(respCorreta == respEscolhida){
            // atualizo a pontuação
            pontuacao++
            // Chamo o AlertDialog
            mostraDialogo(
                titulo = "Parabéns!!!\uD83C\uDF89",
                mensagem = "Você acertou!",
            )
        } else {
            mostraDialogo(
                titulo = "Errou!!! \uD83D\uDE15",
                mensagem = "A resposta correta era: $respCorreta",
            )
        }
    } // end verificarResposta

    private fun mostraDialogo(titulo: String, mensagem: String){
        AlertDialog.Builder(this)
            .setTitle(titulo)
            .setMessage(mensagem)
            .setPositiveButton("Continuar"){ _, _ ->
                proximaPergunta()
            }
            .setCancelable(false)  // Não pode fechar clicando fora
            .show()
    } // end mostraDialogo

    private fun proximaPergunta(){
        // atualizo o indice da pergunta atual
        perguntaAtual++
        //testes
        if(perguntaAtual < 5){
            mostrarPergunta()
        } else {
            mostrarResultadoFinal()
        }
    } // end proximaPergunta

    private fun mostrarResultadoFinal(){
        // calcula a pontuação com base nos acertos
        val pontuacaoFinal = (pontuacao*20)

        AlertDialog.Builder(this)
            .setTitle("Jogo Finalizado!\uD83C\uDFC6")
            .setMessage("\nVocê acertou $pontuacao de 5 perguntas.\n\nSua nota: $pontuacaoFinal\n")
            .setPositiveButton("Voltar ao Menu"){ _, _ ->
                finish()  // Fecha esta Activity e volta para o menu
            }
                .setCancelable(false).show()
    }
}// end class