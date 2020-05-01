package com.example.instakotlinapp.Login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.instakotlinapp.Home.HomeActivity
import com.example.instakotlinapp.Model.Users
import com.example.instakotlinapp.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {


    lateinit var mRef: DatabaseReference    // veritabanı referans nesnesi
    lateinit var mAuth: FirebaseAuth       //Oturum açma için referans nesnesi
    lateinit var mAuthListener: FirebaseAuth.AuthStateListener   //oturum açma işlemlerini dinleyen bir yapı

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setupAuthListener()    //Kullanıcının oturum açıp açmadığı ile ilgili veriler

        mAuth = FirebaseAuth.getInstance()    //ilk atamalarını burda yapıyoruz
        //mAuth.signOut()
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

        tvGirisYap.setOnClickListener {
            var intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun oturumAcacakKullaniciyiDenetle(emailUserName: String, sifre: String) {

        var kullaniciBulundu = false

        mRef.child("users").orderByChild("email")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {

                    for (ds in p0.children) {

                        var okunanKullanici = ds.getValue(Users::class.java)

                        if (okunanKullanici!!.email!!.toString().equals(emailUserName)) {

                            oturumAc(okunanKullanici, sifre)
                            kullaniciBulundu = true
                            break

                        } else if (okunanKullanici.user_name!!.toString().equals(emailUserName)) {

                            oturumAc(okunanKullanici, sifre)
                            kullaniciBulundu = true
                            break
                        }
                    }
                    if (kullaniciBulundu == false) {
                        Toast.makeText(
                            this@LoginActivity,
                            "Kullanıcı Bulunamadı",
                            Toast.LENGTH_SHORT
                        ).show()
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

    //Kullanıcının oturum açıp açmadığı ile ilgili verileri tutan listener   Eğer daha önce oturum açtıysa tekrar homeactivity'i açılacak
    private fun setupAuthListener() {
        mAuthListener = object : FirebaseAuth.AuthStateListener {
            override fun onAuthStateChanged(p0: FirebaseAuth) {

                var user = FirebaseAuth.getInstance().currentUser

                if (user != null) {

                    var intent = Intent(this@LoginActivity, HomeActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

                    startActivity(intent)
                    finish()
                } else {

                }
            }

        }
    }

    override fun onStart() {
        super.onStart()
        mAuth.addAuthStateListener(mAuthListener)
    }

    override fun onStop() {
        super.onStop()
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener)
        }
    }
}
