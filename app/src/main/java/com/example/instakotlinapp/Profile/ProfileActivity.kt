package com.example.instakotlinapp.Profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.instakotlinapp.Login.LoginActivity
import com.example.instakotlinapp.Model.Users
import com.example.instakotlinapp.R
import com.example.instakotlinapp.utils.BottomNavigationViewHelper
import com.example.instakotlinapp.utils.EventbusDataEvents
import com.example.instakotlinapp.utils.UniversalImageLoader
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_profile.*
import org.greenrobot.eventbus.EventBus

class ProfileActivity : AppCompatActivity() {

    private val ACTIVITY_NO = 2
    private val TAG = "ProfileActivity"

    lateinit var mAuth: FirebaseAuth       //Oturum açma için referans nesnesi
    lateinit var mAuthListener: FirebaseAuth.AuthStateListener   //oturum açma işlemlerini dinleyen bir yapı
    lateinit var mRef: DatabaseReference
    lateinit var mUser: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        setupAuthListener()    //Kullanıcının oturum açıp açmadığı ile ilgili veriler
        mAuth = FirebaseAuth.getInstance()    //referans atamasını yaptık
        mUser =
            mAuth.currentUser!!  //Şuan açık olam kullanıcınınid'si. databaseden verileri buna göre çekiyoruz
        mRef = FirebaseDatabase.getInstance().reference

        setupToolBar()
        setupNavigationView()
        kullaniciBilgileriniGetir()

    }

    private fun kullaniciBilgileriniGetir() {

        mRef.child("users").child(mUser.uid).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.value != null) {
                    var okunanKullaniciBilgileri =
                        p0.getValue(Users::class.java)  //Bütün verileri okuyup bu değişkene atadık

                    /* veri göndermek için evetbus kütüphanesini kullanıyoruz.
                    Yayın yapıyor, Bu bilgiler bir yerde duruyor. İlgili sınıf yayınları dinliyorsa bekleyen bilgileri alıp okuyor*/

                    EventBus.getDefault().postSticky(
                        EventbusDataEvents.KullaniciBilgileriniGonder(okunanKullaniciBilgileri)
                    )  //Bir dınıftan nesne oluşturmak gibi
                    tvProfilAdiToolbar.text = okunanKullaniciBilgileri!!.user_name
                    tvProfilGercekAdi.text = okunanKullaniciBilgileri.adi_soyadi
                    tvTakipciSayisi.text = okunanKullaniciBilgileri.kullanici_detaylari!!.takipci
                    tvTakipEdilenSayisi.text =
                        okunanKullaniciBilgileri.kullanici_detaylari!!.takipEdilen
                    tvGonderiSayisi.text = okunanKullaniciBilgileri.kullanici_detaylari!!.gönderi

                    var imgURL: String =
                        okunanKullaniciBilgileri.kullanici_detaylari!!.profilResmi!!
                    UniversalImageLoader.setImage(imgURL, circleProfileImage, progressBar, "")

                    //Biyografi kısmı boş veya null değilse atama yap
                    if (!okunanKullaniciBilgileri.kullanici_detaylari!!.biyografi.isNullOrEmpty()) {
                        tvBiyografi.visibility = View.VISIBLE
                        tvBiyografi.text = okunanKullaniciBilgileri.kullanici_detaylari!!.biyografi
                    }


                }
            }

        })
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
