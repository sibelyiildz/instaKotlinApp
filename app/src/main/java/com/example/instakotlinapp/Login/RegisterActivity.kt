package com.example.instakotlinapp.Login

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.example.instakotlinapp.Model.Users
import com.example.instakotlinapp.R
import com.example.instakotlinapp.utils.EventbusDataEvents
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_register.*
import org.greenrobot.eventbus.EventBus

class RegisterActivity : AppCompatActivity(), FragmentManager.OnBackStackChangedListener {

    lateinit var manager: FragmentManager
    lateinit var mRef: DatabaseReference  //verileri databaseden okumak için bir referans oluşturmamız gerekiyor.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mRef =
            FirebaseDatabase.getInstance().reference  //yukarıda oluşturduğum referansı tanımdadım

        manager = supportFragmentManager
        manager.addOnBackStackChangedListener(this)

        init()
    }

    private fun init() {

        btnIleri.setOnClickListener {

            if (gecerliEmail(etEpostaGiris.text.toString())) {

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
                                var transaction = supportFragmentManager.beginTransaction()
                                transaction.replace(R.id.registerContainer, KayitFragment())
                                transaction.addToBackStack("EmailGirisFramentEklendi")
                                transaction.commit()

                                //Eventbus ile bilgileri gönderme
                                EventBus.getDefault()
                                    .postSticky(EventbusDataEvents.EmailGönder(etEpostaGiris.text.toString()))
                            }
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
    fun gecerliEmail(kontrolEdilecekMail: String): Boolean {

        if (kontrolEdilecekMail == null) {
            return false
        }
        return android.util.Patterns.EMAIL_ADDRESS.matcher(kontrolEdilecekMail).matches()

    }
}
