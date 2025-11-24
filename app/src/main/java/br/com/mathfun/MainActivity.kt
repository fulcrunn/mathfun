package br.com.mathfun

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import br.com.mathfun.contagem.ContagemActivity
import br.com.mathfun.aritmetica.AritmeticaActivity

class MainActivity : AppCompatActivity() { // Sua tela herda de AppCompatActivity (tela básica do Android)
    // lateinit indica que a inicialização é após o oncreate
    private lateinit var btnContagem: Button
    private lateinit var btnAritmetica: Button
    private lateinit var btnMaiorNumero: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        btnContagem = findViewById(R.id.btnContagem)
        btnAritmetica = findViewById(R.id.btnAritmetica)
        btnMaiorNumero = findViewById(R.id.btnMaiorNumero)

        btnContagem.setOnClickListener {
            // criar o Intent para abrir ContagemActivity
            val intent = Intent(this, ContagemActivity::class.java)
            startActivity(intent)
        }

        btnAritmetica.setOnClickListener {
            val intent = Intent(this, AritmeticaActivity::class.java)
            startActivity(intent)
        }

        btnMaiorNumero.setOnClickListener {
            // TODO: Implementar depois
        }
    } // end onCreate
}