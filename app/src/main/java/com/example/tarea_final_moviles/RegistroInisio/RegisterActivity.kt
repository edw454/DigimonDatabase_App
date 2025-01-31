package com.example.tarea_final_moviles.RegistroInisio

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
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

class RegisterActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth
    private var db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        NotificationClass(this).createNotificationChannel()
        //createNotificationChannel()
        setContent {
            auth = FirebaseAuth.getInstance()
            db = FirebaseFirestore.getInstance()
            Tarea_Final_MovilesTheme {
                Column (Modifier.fillMaxSize()
                    .padding(48.dp),
                    horizontalAlignment = Alignment.CenterHorizontally){
                    Text("Registrarse",
                        Modifier.padding(bottom = 16.dp),
                        style = TextStyle(fontSize = 24.sp)
                    )
                    val email = registerEmail()
                    val password = registerPassword()
                    val passwordConfirm = registerPasswordConfirm()
                    Button(onClick = {
                        if (password == passwordConfirm && checkEmpty(email, password, passwordConfirm)){
                            registerUser(email, password)
                        }
                    })
                    { Text("Crear Cuenta") }

                    Button(onClick = {val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                        startActivity(intent) })
                    { Text("Iniciar sesión") }
                }
            }
        }

    }

    private fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this){task ->
                if (task.isSuccessful){

                    val user = auth.currentUser
                    val uid = user?.uid
                    val currentTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                        Locale.getDefault()).format(Date())

                    val userData = hashMapOf(
                        "uid" to uid,
                        "register_time" to currentTime,
                        "last_login" to currentTime
                    )
                    if (uid != null) {
                        db.collection("users").document(uid).set(userData)
                            .addOnSuccessListener {
                                println("User registered and data saved.")
                            }
                            .addOnFailureListener { e ->
                                println("Error saving data: $e")
                            }
                    }

                    Toast.makeText(this, "Usuario creado correctamente", Toast.LENGTH_SHORT).show()
                    NotificationClass(this).generateNotification("Registro exitoso", "Usuario creado correctamente")
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }else{
                    Toast.makeText(this, "No se pudo crear el usuario", Toast.LENGTH_SHORT).show()
                }
            }
    }

}

private fun checkEmpty(email: String, password: String, passwordConfirm: String): Boolean {
return email.isNotEmpty() && password.isNotEmpty() && passwordConfirm.isNotEmpty()
}

@Composable
fun registerEmail(): String {
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
fun registerPassword(): String {
    var password by remember { mutableStateOf("") }

    TextField(
        value = password,
        label = {Text("Contraseña")},
        onValueChange = {password = it },
        visualTransformation = PasswordVisualTransformation()
    )

    return password;
}

@Composable
fun registerPasswordConfirm(): String {
    var passwordConfirm by remember { mutableStateOf("") }

    TextField(
        value = passwordConfirm,
        label = {Text("Repetir Contraseña")},
        onValueChange = {passwordConfirm = it },
        visualTransformation = PasswordVisualTransformation()
    )
    return passwordConfirm;
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun registerPreview() {
    Tarea_Final_MovilesTheme {
        Column (Modifier.fillMaxSize()
            .padding(48.dp),
        horizontalAlignment = Alignment.CenterHorizontally){
            Text("Registrarse",
                Modifier.padding(bottom = 16.dp),
                style = TextStyle(fontSize = 24.sp)
            )
            registerEmail()
            registerPassword()
            registerPasswordConfirm()
            Button(onClick = {/*TODO*/
            })
            { Text("Crear Cuenta") }
            Button(onClick = { /*TODO*/ })
            { Text("Iniciar sesión") }
        }
    }
}
