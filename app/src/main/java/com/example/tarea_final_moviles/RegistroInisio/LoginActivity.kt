package com.example.tarea_final_moviles.RegistroInisio

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tarea_final_moviles.Main_Structure.MainActivity
import com.example.tarea_final_moviles.notification.NotificationClass
import com.example.tarea_final_moviles.ui.theme.Tarea_Final_MovilesTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class LoginActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private var db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            auth = FirebaseAuth.getInstance()
            db = FirebaseFirestore.getInstance()
            Tarea_Final_MovilesTheme {
                Column (
                    Modifier.fillMaxSize()
                    .padding(48.dp),
                    horizontalAlignment = Alignment.CenterHorizontally){
                    Text("Iniciar Sesión",
                        Modifier.padding(bottom = 16.dp),
                        style = TextStyle(fontSize = 24.sp)
                    )
                    val email = loginEmail()
                    val password = loginPassword()
                    Button(onClick = {
                        if (checkEmpty(email, password)){
                            logUser(email, password)
                        }
                    })
                    { Text("Iniciar sesión") }

                    Button(onClick = {val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                        startActivity(intent) })
                    { Text("Crear Cuenta") }
                }
            }
        }
    }

    private fun logUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this){task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val uid = user?.uid
                    val currentTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
                    if (uid != null) {
                        val userRef = db.collection("users").document(uid)
                        userRef.get()
                            .addOnSuccessListener { document ->
                                if (document != null) {
                                    val lastLogin = document.getString("last_login")
                                    NotificationClass(this).generateNotification("Last login time: ", "$lastLogin")

                                    userRef.update("last_login", currentTime)
                                        .addOnSuccessListener {
                                            println("Last login time updated.")
                                        }
                                        .addOnFailureListener { e ->
                                            println("Error updating last login time: $e")
                                        }
                                }
                            }
                    }
                            Toast.makeText(this, "Usuario creado correctamente", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }else{
                    Toast.makeText(this, "No se pudo iniciar sesión", Toast.LENGTH_SHORT).show()
                }
            }
    }
}

private fun checkEmpty(email: String, password: String): Boolean {
    return email.isNotEmpty() && password.isNotEmpty()
}

@Composable
fun loginEmail(): String {
    var email by remember { mutableStateOf("") }
    TextField(
        value = email,
        label = {Text("Email")},
        onValueChange = {email = it },
        placeholder = { Text("example@gmmail.com") }
    )
    return email
}

@Composable
fun loginPassword(): String {
    var password by remember { mutableStateOf("") }

    TextField(
        value = password,
        label = {Text("Contraseña")},
        onValueChange = {password = it },
        visualTransformation = PasswordVisualTransformation()
    )
    return password;
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun loginPreview() {
    Tarea_Final_MovilesTheme {
        Column (
            Modifier.fillMaxSize()
            .padding(48.dp),
            horizontalAlignment = Alignment.CenterHorizontally){
            Text("Iniciar Sesión",
                Modifier.padding(bottom = 16.dp),
                style = TextStyle(fontSize = 24.sp)
            )
            loginEmail()
            loginPassword()
            Button(onClick = {/*TODO*/ })
            { Text("Crear Cuenta") }
            Button(onClick = { /*TODO*/ })
            { Text("Iniciar sesión") }
        }
    }
}
