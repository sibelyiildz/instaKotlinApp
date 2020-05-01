package com.example.instakotlinapp.Profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.instakotlinapp.Login.LoginActivity
import com.example.instakotlinapp.R
import com.example.instakotlinapp.utils.BottomNavigationViewHelper
import com.example.instakotlinapp.utils.UniversalImageLoader
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {

    private val ACTIVITY_NO = 2
    private val TAG = "ProfileActivity"

    lateinit var mAuth: FirebaseAuth       //Oturum açma için referans nesnesi
    lateinit var mAuthListener: FirebaseAuth.AuthStateListener   //oturum açma işlemlerini dinleyen bir yapı

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        setupAuthListener()    //Kullanıcının oturum açıp açmadığı ile ilgili veriler
        mAuth = FirebaseAuth.getInstance()    //ilk atamalarını burda yapıyoruz

        setupToolBar()
        setupNavigationView()
        setupProfilePhoto()
    }

    private fun setupProfilePhoto() {
        var imgURL = "www.apkindirsene.com/wp-content/uploads/2018/07/Android-PNG-Free-Download.png"
        UniversalImageLoader.setImage(imgURL, circleProfileImage, progressBar, "https://")
    }

    private fun setupToolBar() {

        imgProfileSettings.setOnClickListener {
            var intent = Intent(this, ProfileSettingActivity::class.java)
            startActivity(intent)
        }


        //profil kısmındaki profil düzenle butonuna basıldığıda gerekli fragmentin açıldığı kısım
        tvProfileDuzenleButon.setOnClickListener {
            profileRoot.visibility = View.GONE
            var transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.profileContainer, ProfilDuzenleFragment())
            transaction.addToBackStack("ProfilDüzenleFragment Eklendi")  //Geriye basıldığında bir öncesi fragmente dönmesi için stacke ekleme
            transaction.commit()
        }
    }

    //Geri tuşuna basıldığında Ayarlar kısmının layoutun görünürlüğü tekrar açılsın(yukarda kapatmıştık :) )
    override fun onBackPressed() {
        profileRoot.visibility = View.VISIBLE
        super.onBackPressed()
    }

    fun setupNavigationView() {
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationView)
        BottomNavigationViewHelper.setupNavigation(this, bottomNavigationView)

        //activity'in seçili olduğunu belirtmek
        var menu = bottomNavigationView.menu
        var menuItem = menu.getItem(ACTIVITY_NO)
        menuItem.isChecked = true
    }

    //Kullanıcının oturum açıp açmadığı ile ilgili verileri tutan listener   Eğer daha önce oturum açmadıysa login activity'e gidecek
    private fun setupAuthListener() {
        mAuthListener = object : FirebaseAuth.AuthStateListener {
            override fun onAuthStateChanged(p0: FirebaseAuth) {

                var user = FirebaseAuth.getInstance().currentUser

                if (user == null) {  //Kulanıcı yoksa

                    var intent = Intent(this@ProfileActivity, LoginActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    finish()  //Actifityi bitiriyoruz ki geriye basıldığında dönmesin
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
