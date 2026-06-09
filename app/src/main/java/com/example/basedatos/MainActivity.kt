package com.example.basedatos

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.basedatos.databinding.ActivityMainBinding
import com.example.basedatos.model.Usuario
import com.example.basedatos.repositories.UsuarioRepository
import com.example.basedatos.viewmodel.UsuarioViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var viewModel: UsuarioViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding =  ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val db = MyApplication.getDatabase(this)
        val repository = UsuarioRepository(db.usuarioDao())
        viewModel = UsuarioViewModel(repository)

        binding.btnRegistrar.setOnClickListener {
            val usuario = Usuario(0,binding.etUsuarioNombre.text.toString(),
                Integer.getInteger(binding.etUsuarioEdad.text.toString())!!)

            viewModel.agregarUsuarios(usuario)
        }

        binding.btnMostrarUsuarios.setOnClickListener {
            viewModel.cargarUsuarios()
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.usuarios.collect { listaDeUsuarios->
                    if (listaDeUsuarios.isEmpty()) {
                        binding.tvListaUsuarios.text = "La base de datos está vacía."
                    } else {
                        val stringBuilder = StringBuilder()
                        listaDeUsuarios.forEach { usuario ->
                            stringBuilder.append("ID: ${usuario.id} | Nombre: ${usuario.nombre} | Edad: ${usuario.edad}\n")
                        }
                        binding.tvListaUsuarios.text = stringBuilder.toString()
                    }
                }
            }
        }
    }
}