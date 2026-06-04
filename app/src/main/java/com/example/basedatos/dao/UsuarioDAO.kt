package com.example.basedatos.dao

import androidx.room.Insert
import androidx.room.Query
import com.example.basedatos.model.Usuario

interface UsuarioDao {
    @Insert
    suspend fun insertarUsuario(usuario: Usuario)
    @Query("SELECT * FROM usuarios_table")
            suspend fun obtenerUsuarios():
            List<Usuario>
}