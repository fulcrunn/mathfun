package br.com.mathfun.maiorNumero

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import br.com.mathfun.R
import kotlin.random.Random

class MaiorNumeroActivity : AppCompatActivity() {

    private lateinit var tvProgresso: TextView
    private lateinit var tvDigitos: TextView // Apenas um TextView p os 3
    private lateinit var etResposta: EditText
    private lateinit var btnVerificar: Button
    private lateinit var btnVoltar: ImageButton

    private var questaoAtual = 1
    private val totalQuestoes = 5
    private var acertos = 0
    private var respostaCorreta = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_maior_numero)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        tvProgresso = findViewById(R.id.tvProgressoMaiorNumero)
        tvDigitos = findViewById(R.id.tvDigitos) // Referência ao novo TextView
        etResposta = findViewById(R.id.etRespostaMaiorNumero)
        btnVerificar = findViewById(R.id.btnVerificarMaiorNumero)
        btnVoltar = findViewById(R.id.btnVoltarMaiorNumero)

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
        etResposta.text.clear()

        val digitos = listOf(
            Random.nextInt(0, 10),
            Random.nextInt(0, 10),
            Random.nextInt(0, 10)
        )

        tvDigitos.text = "${digitos[0]}  ${digitos[1]}  ${digitos[2]}"

        val digitosOrdenados = digitos.sortedDescending()
        respostaCorreta = "${digitosOrdenados[0]}${digitosOrdenados[1]}${digitosOrdenados[2]}".toInt()
    }

    private fun verificarResposta() {
        val textoDigitado = etResposta.text.toString()

        if (textoDigitado.isEmpty()) {
            Toast.makeText(this, "Digite uma resposta!", Toast.LENGTH_SHORT).show()
            return
        }

        val respostaUsuario = textoDigitado.toInt()
        val acertou = (respostaUsuario == respostaCorreta)

        if (acertou) {
            acertos++
        }

        mostrarDialogoResultado(acertou)
    }

    private fun mostrarDialogoResultado(acertou: Boolean) {
        val titulo = if (acertou) "Parabéns!!! \uD83C\uDF89" else "Errou!!! \uD83D\uDE15"
        val mensagem = if (acertou) "Você acertou!" else "A resposta correta era $respostaCorreta."

        AlertDialog.Builder(this)
            .setTitle(titulo)
            .setMessage(mensagem)
            .setCancelable(false)
            .setPositiveButton("Próxima") { _, _ ->
                if (questaoAtual < totalQuestoes) {
                    questaoAtual++
                    gerarNovaQuestao()
                } else {
                    finalizarJogo()
                }
            }
            .show()
    }

    private fun finalizarJogo() {
        val nota = (acertos.toDouble() / totalQuestoes.toDouble()) * 100

        AlertDialog.Builder(this)
            .setTitle("Jogo Finalizado! \uD83C\uDFC6")
            .setMessage("\nSua nota final é: ${nota.toInt()}")
            .setCancelable(false)
            .setPositiveButton("Voltar ao Menu") { _, _ ->
                finish()
            }
            .show()
    }
}