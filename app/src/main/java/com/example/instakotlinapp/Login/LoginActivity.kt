package com.example.instakotlinapp.Login

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.instakotlinapp.Model.Users
import com.example.instakotlinapp.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    lateinit var mAuth: FirebaseAuth       //Oturum açma için referans nesnesi
    lateinit var mRef: DatabaseReference    // veritabanı referans nesnesi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()    //ilk atamalarını burda yapıyoruz
        mRef = FirebaseDatabase.getInstance().reference
        init()
    }

    fun init() {

        btnGirisYapLogin.setOnClickListener {

            oturumAcacakKullaniciyiDenetle(
                etEmailLogin.text.toString(),
                etSifreLogin.text.toString()
            )
        }
    }

    private fun oturumAcacakKullaniciyiDenetle(emailUserName: String, sifre: String) {

        mRef.child("users").orderByChild("email")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {

                    for (ds in p0.children) {

                        var okunanKullanici = ds.getValue(Users::class.java)

                        if (okunanKullanici!!.email!!.toString().equals(emailUserName)) {

                            oturumAc(okunanKullanici, sifre)
                            break

                        } else if (okunanKullanici.user_name!!.toString().equals(emailUserName)) {

                            oturumAc(okunanKullanici, sifre)
                            break
                        }
                    }
                }


            })
    }

    private fun oturumAc(okunanKullanici: Users, sifre: String) {

        var girisYapacakEmail = okunanKullanici.email.toString()

        mAuth.signInWithEmailAndPassword(girisYapacakEmail, sifre)
            .addOnCompleteListener(object : OnCompleteListener<AuthResult> {
                override fun onComplete(p0: Task<AuthResult>) {
                    if (p0.isSuccessful) {
                        Toast.makeText(
                            this@LoginActivity,
                            "Oturum açıldı " + mAuth.currentUser!!,
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            "Kullanıcı adı veya şifre hatalı ",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            })

    }
}
