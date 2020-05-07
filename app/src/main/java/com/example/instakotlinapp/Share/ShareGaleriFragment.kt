package com.example.instakotlinapp.Share

import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.example.instakotlinapp.R
import kotlinx.android.synthetic.main.fragment_share_galeri.view.*

/**
 * A simple [Fragment] subclass.
 */
class ShareGaleriFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_share_galeri, container, false)

        var klasörlerYollari = ArrayList<String>()
        var klasörAdlari = ArrayList<String>()//Kullanıcıya göstereceğimiz isimler


        //Resimleri göstereceğim klasörlerin yolları
        var root = Environment.getExternalStorageDirectory().path
        var kameraResimleri = root + "/DCIM/Camera"
        var indirilenResimler = root + "/Download"
        var whatsappResimleri = root + "/Whatsapp/Media/Whatsapp Images"

        klasörlerYollari.add(kameraResimleri)
        klasörlerYollari.add(indirilenResimler)
        klasörlerYollari.add(whatsappResimleri)

        //Spinnerde gösterilecek adlar
        klasörAdlari.add("Kamera")
        klasörAdlari.add("İndirilenler")
        klasörAdlari.add("Whatsapp")

        //İsimleri spinnera adapter ile atamak
        var spinnerArrayAdapter =
            activity?.let { ArrayAdapter(it, R.layout.spinner_item, klasörAdlari) }
        spinnerArrayAdapter!!.setDropDownViewResource(R.layout.spinner_dropdown_item)

        //Oluşturduğumuz adapteri spinnere atıyoruz.
        view.spnKlasörAdlari.adapter = spinnerArrayAdapter

        return view
    }

}
