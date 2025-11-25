package br.com.mathfun.aritmetica

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import br.com.mathfun.R
import kotlin.random.Random

class AritmeticaActivity : AppCompatActivity() {

    private lateinit var tvProgresso: TextView
    private lateinit var tvEquacao: TextView
    private lateinit var textResposta: EditText
    private lateinit var btnVerificar: Button

    private lateinit var btnVoltar: ImageButton

    private var questaoAtual = 1
    private val totalQuestoes = 5
    private var acertos = 0
    private var respostaCorreta = 0

    private val historicoEquacoes = mutableSetOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aritmetica)

        tvProgresso = findViewById(R.id.tvProgressoAritmetica)
        tvEquacao = findViewById(R.id.tvEquacao)
        textResposta = findViewById(R.id.etResposta)
        btnVerificar = findViewById(R.id.btnVerificarAritmetica)
        btnVoltar = findViewById(R.id.btnVoltarAritmetica)

        gerarNovaQuestao()

        btnVerificar.setOnClickListener {
            verificarResposta()
        }

        btnVoltar.setOnClickListener {
            finish()
        }
    }

    private fun gerarNovaQuestao() {
        tvProgresso.text = "Questão $questaoAtual de $totalQuestoes"
        textResposta.text.clear()

        var op1: Int
        var op2: Int
        var isSoma: Boolean
        var chaveEquacao: String

        do {
            op1 = Random.nextInt(0, 10) // 0 a 9
            op2 = Random.nextInt(0, 10) // 0 a 9
            isSoma = Random.nextBoolean()

            val simbolo = if (isSoma) "+" else "-"
            chaveEquacao = "$op1$simbolo$op2"

        } while (historicoEquacoes.contains(chaveEquacao))

        historicoEquacoes.add(chaveEquacao)

        respostaCorreta = if (isSoma) op1 + op2 else op1 - op2

        val simbolo = if (isSoma) "+" else "-"
        tvEquacao.text = "$op1 $simbolo $op2"
    }

    private fun verificarResposta() {
        val textoDigitado = textResposta.text.toString()

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
        val titulo = if (acertou) "Parabéns!!! \uD83C\uDF89" else "Errou!!! \uD83D\uDE15"
        val mensagem = if (acertou) {
            "Você acertou!"
        } else {
            "A resposta correta era $respostaCorreta."
        }

        val builder = AlertDialog.Builder(this)
        builder.setTitle(titulo)
        builder.setMessage(mensagem)
        builder.setCancelable(false)
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
        val nota = (acertos.toDouble() / totalQuestoes.toDouble()) * 100

        AlertDialog.Builder(this)
            .setTitle("Jogo Finalizado!\uD83C\uDFC6")
            .setMessage("\nSua nota final é: ${nota.toInt()}")
            .setCancelable(false)
            .setPositiveButton("Voltar ao Menu") { _, _ ->
                finish()
            }
            .show()
    }
}