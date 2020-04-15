package com.example.instakotlinapp.Profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.instakotlinapp.R
import com.example.instakotlinapp.utils.UniversalImageLoader
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_profil_duzenle.view.*

/**
 * A simple [Fragment] subclass.
 */
class ProfilDuzenleFragment : Fragment() {

    lateinit var cicleProfileImageFragment: CircleImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(
            R.layout.fragment_profil_duzenle,
            container,
            false
        )  //Fragmentin içindekilere öğelere erişmek istiyorsak bu inflate işlemini bir değişkene atamamız gerek.

        cicleProfileImageFragment =
            view.findViewById(R.id.circleProfileImage)  //fragmentte olduğumuz için.Acticitylerde buna gerek yok

        setupProfilePicture()

        // fragmentin içerisinde  bulunan öğelerle işlem yapcaksak return etmeden önce yapmak lazım.
        view.imgClose.setOnClickListener {
            activity?.onBackPressed()  //Çarpı ikonuna basıldığında telefonun geri tuşuna basıyormuş gibi geri gelmesi için.
        }

        return view
    }

    private fun setupProfilePicture() {

        //https://www.66pixel.com/wp-content/uploads/2016/03/ANDROID.png

        var imgURL = "www.66pixel.com/wp-content/uploads/2016/03/ANDROID.png"
        UniversalImageLoader.setImage(imgURL, cicleProfileImageFragment, null, "https://")
    }

}
