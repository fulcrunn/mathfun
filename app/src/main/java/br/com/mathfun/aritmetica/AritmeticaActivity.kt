package br.com.mathfun.aritmetica

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import br.com.mathfun.R
import kotlin.random.Random

class AritmeticaActivity : AppCompatActivity() {

    // Componentes de UI
    private lateinit var tvProgresso: TextView
    private lateinit var tvEquacao: TextView
    private lateinit var etResposta: EditText
    private lateinit var btnVerificar: Button

    // Variáveis do Jogo
    private var questaoAtual = 1
    private val totalQuestoes = 5
    private var acertos = 0
    private var respostaCorreta = 0

    // Histórico para evitar perguntas repetidas (Ex: "5+3")
    private val historicoEquacoes = mutableSetOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aritmetica)

        // Vincular componentes
        tvProgresso = findViewById(R.id.tvProgressoAritmetica)
        tvEquacao = findViewById(R.id.tvEquacao)
        etResposta = findViewById(R.id.etResposta)
        btnVerificar = findViewById(R.id.btnVerificarAritmetica)

        // Iniciar o jogo
        gerarNovaQuestao()

        btnVerificar.setOnClickListener {
            verificarResposta()
        }
    }

    private fun gerarNovaQuestao() {
        // Atualiza texto do progresso
        tvProgresso.text = "Questão $questaoAtual de $totalQuestoes"
        etResposta.text.clear() // Limpa o campo de texto

        var op1: Int
        var op2: Int
        var isSoma: Boolean
        var chaveEquacao: String

        // Loop para garantir que a equação não seja repetida
        do {
            op1 = Random.nextInt(0, 10) // 0 a 9
            op2 = Random.nextInt(0, 10) // 0 a 9
            isSoma = Random.nextBoolean() // true = soma, false = subtração

            val simbolo = if (isSoma) "+" else "-"
            chaveEquacao = "$op1$simbolo$op2" // Ex: "5+3"

        } while (historicoEquacoes.contains(chaveEquacao))

        // Adiciona ao histórico
        historicoEquacoes.add(chaveEquacao)

        // Calcula a resposta correta (internamente)
        respostaCorreta = if (isSoma) op1 + op2 else op1 - op2

        // Mostra na tela
        val simbolo = if (isSoma) "+" else "-"
        tvEquacao.text = "$op1 $simbolo $op2"
    }

    private fun verificarResposta() {
        val textoDigitado = etResposta.text.toString()

        if (textoDigitado.isEmpty()) {
            Toast.makeText(this, "Digite uma resposta!", Toast.LENGTH_SHORT).show()
            return
        }

        val respostaUsuario = textoDigitado.toInt()
        val acertou = respostaUsuario == respostaCorreta

        if (acertou) {
            acertos++
        }

        mostrarDialogoResultado(acertou)
    }

    private fun mostrarDialogoResultado(acertou: Boolean) {
        val titulo = if (acertou) "Parabéns! 🎉" else "Que pena! 😕"
        val mensagem = if (acertou) {
            "Você acertou a resposta!"
        } else {
            "Você errou. A resposta correta era $respostaCorreta."
        }

        val builder = AlertDialog.Builder(this)
        builder.setTitle(titulo)
        builder.setMessage(mensagem)
        builder.setCancelable(false) // Impede fechar clicando fora
        builder.setPositiveButton("Próxima") { dialog, _ ->
            if (questaoAtual < totalQuestoes) {
                questaoAtual++
                gerarNovaQuestao()
            } else {
                finalizarJogo()
            }
        }
        builder.show()
    }

    private fun finalizarJogo() {
        // Cálculo da nota (0 a 100)
        val nota = (acertos.toDouble() / totalQuestoes.toDouble()) * 100

        AlertDialog.Builder(this)
            .setTitle("Fim de Jogo!")
            .setMessage("Sua nota final é: ${nota.toInt()}")
            .setCancelable(false)
            .setPositiveButton("Voltar ao Menu") { _, _ ->
                finish() // Fecha a activity e volta para a Main
            }
            .show()
    }
}