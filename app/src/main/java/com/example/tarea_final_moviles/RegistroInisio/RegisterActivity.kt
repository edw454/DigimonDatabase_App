package com.example.tarea_final_moviles.RegistroInisio

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
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
    //Se instancias FireBase Auth y Firestore
    private lateinit var auth: FirebaseAuth
    private var db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        NotificationClass(this).createNotificationChannel()
        //createNotificationChannel()
        setContent {
            Tarea_Final_MovilesTheme {
                Column (
                    Modifier.fillMaxSize()
                        .background(Color(0xFF80DAEB))
                ){
                    ScreenRegister()
                }
            }
        }
    }

    @Composable
    fun ScreenRegister(){
        //Se inicializan las variables de FireBase Auth y Firestore
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

            Column (Modifier.fillMaxSize()
                .padding(48.dp),
                horizontalAlignment = Alignment.CenterHorizontally){
                Text("Registrarse",
                    Modifier.padding(bottom = 16.dp),
                    style = TextStyle(fontSize = 24.sp)
                )
                //Se recogen los datos del usuario
                val name = registerUserName()
                val email = registerEmail()
                val password = registerPassword()
                val passwordConfirm = registerPasswordConfirm()
                //Se crea el usuario con los datos recogidos
                Button(onClick = {
                    if (password == passwordConfirm && checkEmpty(email, password, passwordConfirm, name)){
                        registerUser(email, password, name)
                    }else{
                        if (password != passwordConfirm){
                            toast("Las contraseñas no coinciden")
                        }else{
                            toast("Llene todos los campos")
                        }
                    }
                })
                { Text("Crear Cuenta") }
                //Se redirige al login
                Button(onClick = {val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                    startActivity(intent) })
                { Text("Iniciar sesión") }
            }

    }
    private fun toast(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }

    private fun registerUser(email: String, password: String, name: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this){task ->
                //Se crea el usuario con los datos recogidos
                if (task.isSuccessful){

                    val user = auth.currentUser
                    val uid = user?.uid
                    val currentTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                        Locale.getDefault()).format(Date())
                    //Se guarda la información del usuario en la base de datos
                    val userData = hashMapOf(
                        "user_name" to name,
                        "uid" to uid,
                        "register_time" to currentTime,
                        "last_login" to currentTime
                    )
                    //Si el uid no es nulo, se guarda la información del usuario en la base de datos
                    if (uid != null) {
                        db.collection("users").document(uid).set(userData)
                            .addOnSuccessListener {
                                println("User registered and data saved.")
                            }
                            .addOnFailureListener { e ->
                                println("Error saving data: $e")
                            }
                    }

                    toast("Usuario creado correctamemte")
                    NotificationClass(this).generateNotification("Registro exitoso", "Bienvenido $name")
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }else{
                    Toast.makeText(this, "No se pudo crear el usuario", Toast.LENGTH_SHORT).show()
                }
            }
    }

}

private fun checkEmpty(email: String, password: String, passwordConfirm: String, name: String): Boolean {
return email.isNotEmpty() && password.isNotEmpty() && passwordConfirm.isNotEmpty() && name.isNotEmpty()
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
fun registerUserName(): String {
    var name by remember { mutableStateOf("") }
    TextField(
        value = name,
        label = {Text("Nombre de usuario")},
        onValueChange = {name = it },
        placeholder = { Text("Juan") }
    )
    return name
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
fun RegisterPreview() {
    Tarea_Final_MovilesTheme {
        Column (Modifier.fillMaxSize()
            .padding(48.dp),
        horizontalAlignment = Alignment.CenterHorizontally){
            Text("Registrarse",
                Modifier.padding(bottom = 16.dp),
                style = TextStyle(fontSize = 24.sp)
            )
            registerUserName()
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
