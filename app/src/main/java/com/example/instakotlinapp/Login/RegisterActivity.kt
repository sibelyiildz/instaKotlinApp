package com.example.instakotlinapp.Login

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.instakotlinapp.R
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        init()
    }

    private fun init() {

        btnIleri.setOnClickListener {

            registerRoot.visibility = View.GONE
            var transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.registerContainer, EmailGirisFragment())
            transaction.addToBackStack("EmailGirisFramentEklendi")
            transaction.commit()
        }

    }


    //telefonun geri tuşuna basıldığında bir önceki activitye dönme
    override fun onBackPressed() {
        registerRoot.visibility = View.VISIBLE
        super.onBackPressed()
    }
}
