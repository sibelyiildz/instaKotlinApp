package com.example.instakotlinapp.Profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.instakotlinapp.R
import com.example.instakotlinapp.utils.BottomNavigationViewHelper
import com.example.instakotlinapp.utils.UniversalImageLoader
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {

    private val ACTIVITY_NO = 2
    private val TAG = "ProfileActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

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
}
