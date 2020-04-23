package com.example.instakotlinapp.Login

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.example.instakotlinapp.R
import com.example.instakotlinapp.utils.EventbusDataEvents
import kotlinx.android.synthetic.main.activity_register.*
import org.greenrobot.eventbus.EventBus

class RegisterActivity : AppCompatActivity(), FragmentManager.OnBackStackChangedListener {

    lateinit var manager: FragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        manager = supportFragmentManager
        manager.addOnBackStackChangedListener(this)

        init()
    }

    private fun init() {

        btnIleri.setOnClickListener {

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
}
