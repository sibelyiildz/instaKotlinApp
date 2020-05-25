package com.example.instakotlinapp.Share

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.instakotlinapp.Login.LoginActivity
import com.example.instakotlinapp.R
import com.example.instakotlinapp.utils.SharePagerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.DexterError
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.PermissionRequestErrorListener
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_share.*

class ShareActivity : AppCompatActivity() {


    lateinit var mAuthListener: FirebaseAuth.AuthStateListener   //oturum açma işlemlerini dinleyen bir yapı


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)

        setupAuthListener()    //Kullanıcının oturum açıp açmadığı ile ilgili veriler

        storageVeKameraIzniIste()

        //Bu metosun içinde viewPager ayarlamaları yapılıyor
        //setupShareViewPager()

    }

    private fun storageVeKameraIzniIste() {

        Dexter.withActivity(this)
            .withPermissions(
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {

                    //Bütün izinler verildiyse burası testiklenecek
                    if (report!!.areAllPermissionsGranted()) {
                        Log.e("HATA", "Tüm izinler verilmiş")

                        //Bu metosun içinde viewPager ayarlamaları yapılıyor
                        setupShareViewPager()
                    }

                    //Eğer herhangi bir izin reddedilip bana bunu bir daha sorma dendiyse
                    if (report.isAnyPermissionPermanentlyDenied) {
                        Log.e("HATA", "İzinlerden birine bir daha sorma denmiş")

                        var builder = AlertDialog.Builder(this@ShareActivity)
                        builder.setTitle("İzin Gerekli")
                        builder.setTitle("Ayarlar kısmından uygulamaya izin vermeniz gerekiyor")
                        builder.setPositiveButton(
                            "AYARLARA GİT",
                            object : DialogInterface.OnClickListener {
                                override fun onClick(dialog: DialogInterface?, p1: Int) {
                                    dialog!!.cancel()

                                    //AYARLARA GÖNDERDİĞİM KISIM
                                    var intent =
                                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                    var uri = Uri.fromParts("package", packageName, null)
                                    intent.data = uri
                                    startActivity(intent)
                                    finish()

                                }

                            })
                        builder.setNegativeButton(
                            "REDDET",
                            object : DialogInterface.OnClickListener {
                                override fun onClick(dialog: DialogInterface?, p1: Int) {
                                    dialog!!.cancel()
                                    finish()
                                }

                            })
                        builder.show()
                    }

                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    Log.e("HATA", "İzinlerden biri reddedilmiş, kullanıcıyı ikna et")

                    var builder = AlertDialog.Builder(this@ShareActivity)
                    builder.setTitle("İzin Gerekli")
                    builder.setTitle("Uygulamaya izin vermeniz gerekiyor. Onaylar mısınız?")
                    builder.setPositiveButton("ONAY VER", object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface?, p1: Int) {
                            dialog!!.cancel()
                            token!!.continuePermissionRequest()  //Bir daha izin iste
                        }

                    })
                    builder.setNegativeButton("REDDET", object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface?, p1: Int) {
                            dialog!!.cancel()
                            token!!.cancelPermissionRequest()
                            finish()
                        }

                    })
                    builder.show()

                }

            })
            .withErrorListener(object : PermissionRequestErrorListener {
                override fun onError(error: DexterError?) {
                    Log.e("HATA", error!!.toString())
                }

            }).check()
    }

    private fun setupShareViewPager() {

        var tabAdlari = ArrayList<String>()
        tabAdlari.add("GALERİ")


        //PagerAdapter sınıfından nesne oluşturuyoruz ve supportFragmentManager paremetresi veriyoruz
        var sharePagerAdapter = SharePagerAdapter(supportFragmentManager, tabAdlari)

        //İlgili fragmentleri viewPager adpterine ekliyoruz
        sharePagerAdapter.addFragment(ShareGaleriFragment())


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
