package com.example.instakotlinapp.Profile

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.instakotlinapp.R
import com.example.instakotlinapp.utils.BottomNavigationViewHelper
import kotlinx.android.synthetic.main.activity_profile_setting.*


class ProfileSettingActivity : AppCompatActivity() {

    private val ACTIVITY_NO = 2
    private val TAG = "ProfileActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_setting)

        setupNavigationView()
        setupToolBar()
        settingsFragmentGecisleri()
    }

    /*Profil ayarlarından herhangi bir ayar tıklandığında onun fragmenti açılacak.
    * ProfilSettingActivity'de ikitane layout kullandık. Herhangi bir fragment açıldığında
    * ayarların açık olduğu layout görünürlüğü kaybolup fragmentin göreneceği layout açılacak.*/
    private fun settingsFragmentGecisleri() {

        tvProfilDüzenleHesapAyarlari.setOnClickListener {
            profileSettingsRoot.visibility =
                View.GONE //Ayarlar kısmının layoutun görünürlüğünü kapattık
            var transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.profileSettingsContainer, ProfilDuzenleFragment())
            transaction.addToBackStack("ProfilDüzenleFragment Eklendi")  //Geriye basıldığında bir öncesi fragmente dönmesi
            transaction.commit()
        }

        tvCikisYap.setOnClickListener {
            profileSettingsRoot.visibility =
                View.GONE //Ayarlar kısmının layoutun görünürlüğünü kapattık
            var transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.profileSettingsContainer, CikisYapFragment())
            transaction.addToBackStack("cikisYapFragment Eklendi")
            transaction.commit()
        }
    }

    //Geri tuşuna basıldığında Ayarlar kısmının layoutun görünürlüğü tekrar açılsın(yukarda kapatmıştık :) )
    override fun onBackPressed() {
        profileSettingsRoot.visibility = View.VISIBLE
        super.onBackPressed()
    }

    private fun setupToolBar() {
        //Geri tuşu iconu basıldığında eski activitye dönmesi için
        imgGeri.setOnClickListener {
            onBackPressed()
        }
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
