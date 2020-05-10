package com.example.instakotlinapp.Share

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.instakotlinapp.Login.LoginActivity
import com.example.instakotlinapp.R
import com.example.instakotlinapp.utils.SharePagerAdapter
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_share.*

class ShareActivity : AppCompatActivity() {


    lateinit var mAuthListener: FirebaseAuth.AuthStateListener   //oturum açma işlemlerini dinleyen bir yapı


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)

        setupAuthListener()    //Kullanıcının oturum açıp açmadığı ile ilgili veriler

        //Bu metosun içinde viewPager ayarlamaları yapılıyor
        setupShareViewPager()
    }

    private fun setupShareViewPager() {

        var tabAdlari = ArrayList<String>()
        tabAdlari.add("GALERİ")
        tabAdlari.add("FOTOĞRAF")


        //PagerAdapter sınıfından nesne oluşturuyoruz ve supportFragmentManager paremetresi veriyoruz
        var sharePagerAdapter = SharePagerAdapter(supportFragmentManager, tabAdlari)

        //İlgili fragmentleri viewPager adpterine ekliyoruz
        sharePagerAdapter.addFragment(ShareGaleriFragment())
        sharePagerAdapter.addFragment(ShareKameraFragment())


        //Yukarıda adaptere koyduğumuz fragmentleri ViewPagerda gösteriyoruz
        shareViewPager.adapter = sharePagerAdapter

        //Tabların aktif olarak gösterilmesi
        shareTabLayout.setupWithViewPager(shareViewPager)
    }

    override fun onBackPressed() {
        anaLayout.visibility = View.VISIBLE
        fragmentContainerLayout.visibility = View.GONE
        super.onBackPressed()
    }

    //Kullanıcının oturum açıp açmadığı ile ilgili verileri tutan listener   Eğer daha önce oturum açmadıysa login activity'e gidecek
    private fun setupAuthListener() {

        mAuthListener = object : FirebaseAuth.AuthStateListener {
            override fun onAuthStateChanged(p0: FirebaseAuth) {

                var user = FirebaseAuth.getInstance().currentUser

                if (user == null) {  //Kulanıcı yoksa

                    var intent = Intent(this@ShareActivity, LoginActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    finish()  //Actifityi bitiriyoruz ki geriye basıldığında dönmesin
                } else {

                }
            }

        }
    }
}
