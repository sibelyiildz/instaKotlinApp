package com.example.instakotlinapp.Profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.instakotlinapp.Model.Users
import com.example.instakotlinapp.R
import com.example.instakotlinapp.utils.EventbusDataEvents
import com.example.instakotlinapp.utils.UniversalImageLoader
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_profil_duzenle.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

/**
 * A simple [Fragment] subclass.
 */
class ProfilDuzenleFragment : Fragment() {

    lateinit var cicleProfileImageFragment: CircleImageView
    lateinit var gelenKullaniciBilgileri: Users

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

        return view
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
