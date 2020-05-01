package com.example.instakotlinapp.Login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.example.instakotlinapp.Home.HomeActivity
import com.example.instakotlinapp.Model.Users
import com.example.instakotlinapp.R
import com.example.instakotlinapp.utils.EventbusDataEvents
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_register.*
import org.greenrobot.eventbus.EventBus

class RegisterActivity : AppCompatActivity(), FragmentManager.OnBackStackChangedListener {

    lateinit var manager: FragmentManager
    lateinit var mRef: DatabaseReference  //verileri databaseden okumak için bir referans oluşturmamız gerekiyor.
    lateinit var mAuth: FirebaseAuth       //Oturum açma için referans nesnesi
    lateinit var mAuthListener: FirebaseAuth.AuthStateListener   //oturum açma işlemlerini dinleyen bir yapı

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        setupAuthListener()
        mAuth = FirebaseAuth.getInstance()
        mRef =
            FirebaseDatabase.getInstance().reference  //yukarıda oluşturduğum referansı tanımdadım


        manager = supportFragmentManager
        manager.addOnBackStackChangedListener(this)

        init()
    }

    private fun init() {

        tvGirisYap.setOnClickListener {
            var intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnIleri.setOnClickListener {

            if (gecerliEmail(etEpostaGiris.text.toString())) {

/*

                registerRoot.visibility = View.GONE
                var transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.registerContainer, KayitFragment())
                transaction.addToBackStack("EmailGirisFramentEklendi")
                transaction.commit()

                //Eventbus ile bilgileri gönderme
                EventBus.getDefault().postSticky(EventbusDataEvents.EmailGönder(etEpostaGiris.text.toString()))
*/

                var emailKullanimdaMi = false

                //Verileri databaseden okuma
                mRef.child("users").addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {  //şuan bütün veriler p0'ın içinde
                        if (p0.value != null) {

                            for (user in p0.children) {
                                var okunanKullanici = user.getValue(Users::class.java)
                                if (okunanKullanici!!.email!!.equals(etEpostaGiris.text.toString())) {
                                    Toast.makeText(
                                        this@RegisterActivity,
                                        "Bu email kullanılıyor",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    emailKullanimdaMi = true
                                    break
                                }
                            }
                            if (emailKullanimdaMi == false) {

                                registerRoot.visibility = View.GONE
                                registerContainer.visibility = View.VISIBLE
                                var transaction = supportFragmentManager.beginTransaction()
                                transaction.replace(R.id.registerContainer, KayitFragment())
                                transaction.addToBackStack("EmailGirisFramentEklendi")
                                transaction.commit()

                                //Eventbus ile bilgileri gönderme
                                EventBus.getDefault()
                                    .postSticky(EventbusDataEvents.EmailGönder(etEpostaGiris.text.toString()))
                            }
                        } else {

                            //Eğer veri tabanında hiç veri yoksa bile açılsın.
                            registerRoot.visibility = View.GONE
                            registerContainer.visibility = View.VISIBLE
                            var transaction = supportFragmentManager.beginTransaction()
                            transaction.replace(R.id.registerContainer, KayitFragment())
                            transaction.addToBackStack("EmailGirisFramentEklendi")
                            transaction.commit()

                            //Eventbus ile bilgileri gönderme
                            EventBus.getDefault()
                                .postSticky(EventbusDataEvents.EmailGönder(etEpostaGiris.text.toString()))
                        }
                    }
                })
            } else {
                Toast.makeText(this, "Lütfen geçerli bir email adresi giriniz.", Toast.LENGTH_SHORT)
                    .show()
            }

        }

    }


/*
    //telefonun geri tuşuna basıldığında bir önceki activitye dönme
    override fun onBackPressed() {
        registerRoot.visibility = View.VISIBLE
        super.onBackPressed()
    }
*/

    //telefonun geri tuşuna basıldığında bir önceki activitye dönme
    override fun onBackStackChanged() {
        val elemanSayisi = manager.backStackEntryCount

        if (elemanSayisi == 0) {
            registerRoot.visibility = View.VISIBLE
        }
    }

    //girilen email adresinin geçerliliğinin kontrolü
    private fun gecerliEmail(kontrolEdilecekMail: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(kontrolEdilecekMail).matches()
    }

    //Kullanıcının oturum açıp açmadığı ile ilgili verileri tutan listener   Eğer daha önce oturum açtıysa tekrar homeactivity'i açılacak
    private fun setupAuthListener() {
        mAuthListener = object : FirebaseAuth.AuthStateListener {
            override fun onAuthStateChanged(p0: FirebaseAuth) {

                var user =
                    FirebaseAuth.getInstance().currentUser  //oturum açmış bir kullanıcı var mı

                if (user != null) {

                    var intent = Intent(this@RegisterActivity, HomeActivity::class.java)
                    //Çıkış yapıldıktan sonra geri tuşuna basıldığında eski activitylere dönmeye çalışmasın diye
                    //Sanki ilk defa programı burdan başlatmışız gibi, backstacktaki verileri siliyor
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
