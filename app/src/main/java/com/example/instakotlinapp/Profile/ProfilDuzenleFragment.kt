package com.example.instakotlinapp.Profile


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.instakotlinapp.Model.Users
import com.example.instakotlinapp.R
import com.example.instakotlinapp.utils.EventbusDataEvents
import com.example.instakotlinapp.utils.UniversalImageLoader
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_profil_duzenle, container, false)
        //Fragmentin içindekilere öğelere erişmek istiyorsak bu inflate işlemini bir değişkene atamamız gerek.

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

        return view
    }

    //galerinin açılıp resmin seçildiği kısım
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RESIM_SEC && resultCode == AppCompatActivity.RESULT_OK && data!!.data != null) {  //galeri açılmıştır, kullanıcı bir resim seçmiştir

            var profilResimURI =
                data.data  //Özel bir yapı, Androidin içinden bir şeylere ulaştığınızda onun adresini temsil eden yapı

            circleProfileImage.setImageURI(profilResimURI)
        }
    }

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
