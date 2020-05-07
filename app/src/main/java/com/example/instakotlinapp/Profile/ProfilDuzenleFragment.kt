package com.example.instakotlinapp.Profile


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.instakotlinapp.Model.Users
import com.example.instakotlinapp.R
import com.example.instakotlinapp.utils.EventbusDataEvents
import com.example.instakotlinapp.utils.UniversalImageLoader
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_profil_duzenle.*
import kotlinx.android.synthetic.main.fragment_profil_duzenle.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

/**
 * A simple [Fragment] subclass.
 */
class ProfilDuzenleFragment : Fragment() {

    lateinit var cicleProfileImageFragment: CircleImageView
    lateinit var gelenKullaniciBilgileri: Users
    val RESIM_SEC = 100  //herhangi bir sayı
    lateinit var mDatabaseRef: DatabaseReference
    lateinit var mStoreRef: StorageReference

    var profilFotoUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_profil_duzenle, container, false)
        //Fragmentin içindekilere öğelere erişmek istiyorsak bu inflate işlemini bir değişkene atamamız gerek.

        mDatabaseRef = FirebaseDatabase.getInstance().reference
        mStoreRef = FirebaseStorage.getInstance().reference

        setupKullaniciBilgileri(view)


        // fragmentin içerisinde  bulunan öğelerle işlem yapcaksak return etmeden önce yapmak lazım.
        view.imgClose.setOnClickListener {
            activity?.onBackPressed()  //Çarpı ikonuna basıldığında telefonun geri tuşuna basıyormuş gibi geri gelmesi için.
        }

        //kullanıcının galeriyi açıp resim seçmesi
        view.tvFotografiDegistir.setOnClickListener {

            // resim seçme gibi işlemleri intentler ile yapıyoruz
            var intent = Intent()
            intent.type = "image/*"     //ne tür resimler gösterilsin(imagenin her türü)
            intent.action =
                Intent.ACTION_PICK        //butona tıklanıldığında ne yapmak istiyorsun(seçmek)
            startActivityForResult(intent, RESIM_SEC)   //geri kalan bütün işlemleri burda yapıyoruz
        }


        //Değişiklik yapılan bilgileri veri tabanına kaydettiğimiz kısım
        view.imgBtnDegisiklikleriKaydet.setOnClickListener {


            if (profilFotoUri != null) {  //kullanıcı galeriye girip bir resim seçtiyse, bunu storage atmak lazım

                var dialogYukleniyor = YukleniyorFragment()
                dialogYukleniyor.show(activity!!.supportFragmentManager, "YukleniyorFragmenti")

                dialogYukleniyor.isCancelable = false


                val profilePicReference = mStoreRef.child("users").child(
                    gelenKullaniciBilgileri.user_id!!
                )
                    .child(profilFotoUri!!.lastPathSegment!!)
                val uploadTask = profilePicReference.putFile(profilFotoUri!!)

                uploadTask.addOnFailureListener {
                    Toast.makeText(
                        activity,
                        "Profil resmi yüklenirken problem oldu",
                        Toast.LENGTH_SHORT
                    ).show()
                    kullaniciAdiniGuncelle(this.view!!, false) //profil resmi değiştirilememiştir
                }.addOnSuccessListener { it ->
                    var urlTask = uploadTask.continueWithTask { task ->
                        if (!task.isSuccessful) {
                            throw task.exception!!
                        }
                        profilePicReference.downloadUrl
                    }.addOnCompleteListener {
                        if (it.isSuccessful) {
                            profilFotoUri = null
                            mDatabaseRef.child("users").child(gelenKullaniciBilgileri.user_id!!)
                                .child("kullanici_detaylari")
                                .child("profilResmi").setValue(it.result.toString())


                            if (activity != null) {
                                dialogYukleniyor.dismiss()
                                kullaniciAdiniGuncelle(
                                    this.view!!,
                                    true
                                ) //profil resmi başarı bir şekilde değişmiştir

                            }
                        }
                    }
                }

            } else {

                //profil fotografını değiştirmemiştir
                kullaniciAdiniGuncelle(view, null)
            }

        }

        return view
    }

    //profilResmiDegisti
    //true: basarılı bir şekilde resim storage yüklenmiş ve veri tabanına yazılmıştır.
    //false: resim yüklenirken hata oluşmuştur
    //null: kullanıcı resmini değiştirmek istememiştir
    private fun kullaniciAdiniGuncelle(view: View, profilResmiDegisti: Boolean?) {

        if (!gelenKullaniciBilgileri.user_name!!.equals(view.etProfilKullaniciAdi.text.toString())) {
            mDatabaseRef.child("users").orderByChild("user_name")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                    }

                    override fun onDataChange(p0: DataSnapshot) {

                        var userNameKullanimdaMi = false
                        for (ds in p0.children) {
                            var okunanKullaniciAdi = ds!!.getValue(Users::class.java)!!.user_name

                            if (okunanKullaniciAdi!!.equals(view.etProfilKullaniciAdi.text.toString())) {
                                //Toast.makeText(activity,"Kullanıcı adı kullanımda.",Toast.LENGTH_SHORT).show()
                                userNameKullanimdaMi = true
                                profilBilgileriniGuncelle(view, profilResmiDegisti, false)
                                break
                            }
                        }
                        if (userNameKullanimdaMi == false) {

                            mDatabaseRef.child("users").child(gelenKullaniciBilgileri.user_id!!)
                                .child("user_name")
                                .setValue(view.etProfilKullaniciAdi.text.toString())

                            profilBilgileriniGuncelle(view, profilResmiDegisti, true)

                        }
                    }

                })
        } else {
            profilBilgileriniGuncelle(view, profilResmiDegisti, null)

        }
    }

    private fun profilBilgileriniGuncelle(
        view: View,
        profilResmiDegisti: Boolean?,
        userNameDegisti: Boolean?
    ) {

        var profilGuncellendiMi: Boolean? = null

        if (!gelenKullaniciBilgileri.adi_soyadi!!.equals(view.etProfilAdi.text.toString())) {   //ilk değer ile keydet butonuna basıldığı anki değer aynı değilse
            mDatabaseRef.child("users").child(gelenKullaniciBilgileri.user_id!!)
                .child("adi_soyadi").setValue(view.etProfilAdi.text.toString())
            profilGuncellendiMi = true
        }
        if (!gelenKullaniciBilgileri.kullanici_detaylari!!.biyografi.equals(view.etProfilBiyografi.text.toString())) {
            mDatabaseRef.child("users").child(gelenKullaniciBilgileri.user_id!!)
                .child("kullanici_detaylari").child("biyografi")
                .setValue(view.etProfilBiyografi.text.toString())
            profilGuncellendiMi = true
        }

        if (profilResmiDegisti == null && userNameDegisti == null && profilGuncellendiMi == null) {
            Toast.makeText(activity, "Değişiklik yok", Toast.LENGTH_SHORT).show()
        } else if (userNameDegisti == false && (profilGuncellendiMi == true || profilResmiDegisti == true)) {
            Toast.makeText(
                activity,
                "Bilgiler güncellendi ama KULLANICI ADI KULLANIMDA",
                Toast.LENGTH_SHORT
            ).show()
        } else if (userNameDegisti == false) {
            Toast.makeText(activity, " KULLANICI ADI KULLANIMDA", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(activity, "Kullanıcı Güncellendi", Toast.LENGTH_SHORT).show()
            activity!!.onBackPressed()
        }

    }

    //galerinin açılıp resmin seçildiği kısım
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RESIM_SEC && resultCode == AppCompatActivity.RESULT_OK && data!!.data != null) {  //galeri açılmıştır, kullanıcı bir resim seçmiştir

            profilFotoUri =
                data.data!! //Özel bir yapı, Androidin içinden bir şeylere ulaştığınızda onun adresini temsil eden yapı

            circleProfileImage.setImageURI(profilFotoUri)
        }
    }

    //kullanıcı bilgilerininin profilden alınıp bu kısma yazdırıldığı kısım
    private fun setupKullaniciBilgileri(view: View?) {

        view!!.etProfilAdi.setText(gelenKullaniciBilgileri.adi_soyadi)
        view.etProfilKullaniciAdi.setText(gelenKullaniciBilgileri.user_name)

        if (!gelenKullaniciBilgileri.kullanici_detaylari!!.biyografi.isNullOrEmpty()) {  //Null veya boş değilse
            view.etProfilBiyografi.setText(gelenKullaniciBilgileri.kullanici_detaylari!!.biyografi)
        }

        var imgUrl = gelenKullaniciBilgileri.kullanici_detaylari!!.profilResmi
        UniversalImageLoader.setImage(imgUrl!!, view.circleProfileImage, null, "")


    }


    //////////////////////////////////////////////  EVENTBUS  /////////////////////////////////////////////

    //Gelen yayını dinleyip verileri alması için erekli metot
    @Subscribe(sticky = true)
    internal fun onKullaniciBilgileriEvent(kullaniciBilgileri: EventbusDataEvents.KullaniciBilgileriniGonder) {

        gelenKullaniciBilgileri = kullaniciBilgileri.kullanici!!
    }

    //EventBus'ı eklemek için gerekli metotlar(Eventbustan yanın beklediğini söyleyen metotlar)
    override fun onAttach(context: Context) {
        super.onAttach(context)
        EventBus.getDefault().register(this)
    }

    override fun onDetach() {
        super.onDetach()
        EventBus.getDefault().unregister(this)
    }



}
