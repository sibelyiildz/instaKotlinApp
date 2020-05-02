package com.example.instakotlinapp.Login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.instakotlinapp.Model.KullaniciDetaylari
import com.example.instakotlinapp.Model.Users
import com.example.instakotlinapp.R
import com.example.instakotlinapp.utils.EventbusDataEvents
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragmen_kayit.*
import kotlinx.android.synthetic.main.fragmen_kayit.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

/**
 * A simple [Fragment] subclass.
 */
class KayitFragment : Fragment() {

    var gelenEmail = ""
    lateinit var mAuth: FirebaseAuth  //Authaentication işlemleri bu nesne üzerinden
    lateinit var mRef: DatabaseReference  //veritatabanıişemleri bu nesne üzerinden

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.fragmen_kayit, container, false)


        mAuth = FirebaseAuth.getInstance()


        view.tvGirisYap.setOnClickListener {

            var intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)

        }



        mRef = FirebaseDatabase.getInstance().reference


        //Her 3 editText'e en az 6 karakter girilmeden button aktif olmamalı. Firebasete bunu istiyor
        view.etAdSoyad.addTextChangedListener(watcher)
        view.etProfilKullaniciAdi.addTextChangedListener(watcher)
        view.etSifre.addTextChangedListener(watcher)


        view.btnGiris.setOnClickListener {

            mRef.child("test").setValue(5)
            var kullaniciAdiKullanımdaMi = false
            mRef.child("users").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.value != null) {

                        for (user in p0.children) {
                            var okunanKullanici = user.getValue(Users::class.java)
                            if (okunanKullanici!!.user_name!!.equals(view.etProfilKullaniciAdi.text.toString())) {
                                Toast.makeText(
                                    activity,
                                    "Bu kullanıcı adı kullanılıyor",
                                    Toast.LENGTH_SHORT
                                ).show()
                                kullaniciAdiKullanımdaMi = true

                                break
                            }
                        }

                        if (kullaniciAdiKullanımdaMi == false) {

                            var sifre = view.etSifre.text.toString()
                            var adSoyad = view.etAdSoyad.text.toString()
                            var userName = view.etProfilKullaniciAdi.text.toString()

                            //Kullanıcı email ile oturum açıyor
                            mAuth.createUserWithEmailAndPassword(gelenEmail, sifre)
                                .addOnCompleteListener(object : OnCompleteListener<AuthResult> {
                                    override fun onComplete(p0: Task<AuthResult>) {

                                        if (p0.isSuccessful) {   //Kullanıcı bu bilgilerle başarılı giriş yapmış mı
                                            Toast.makeText(
                                                activity,
                                                "Oturum açıldı",
                                                Toast.LENGTH_SHORT
                                            ).show()

                                            var userID =
                                                mAuth.currentUser!!.uid.toString()  //Oturum açıldıktan sonra authenticationdan id yi aldık

                                            //oturum açan kullanıcının verilerini database'ye kaydetme
                                            var kaydedilecekKullaniciDetaylari =
                                                KullaniciDetaylari("0", "0", "0", "", "")
                                            var kaydedilecekKullanici = Users(
                                                gelenEmail,
                                                sifre,
                                                userName,
                                                adSoyad,
                                                userID,
                                                kaydedilecekKullaniciDetaylari
                                            )

                                            mRef.child("users").child(userID)
                                                .setValue(kaydedilecekKullanici)
                                                .addOnCompleteListener(object :
                                                    OnCompleteListener<Void> {
                                                    override fun onComplete(p0: Task<Void>) {
                                                        if (p0.isSuccessful) {
                                                            Toast.makeText(
                                                                activity,
                                                                "Kullanıcı kaydedildi.",
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                        } else {
                                                            //oturum açılıp veri kaydedilememişse var olan kullanıcı silinmeli
                                                            mAuth.currentUser!!.delete()
                                                                .addOnCompleteListener(object :
                                                                    OnCompleteListener<Void> {
                                                                    override fun onComplete(p0: Task<Void>) {
                                                                        if (p0.isSuccessful) {
                                                                            Toast.makeText(
                                                                                activity,
                                                                                "Kullanıcı kaydedilemedi.",
                                                                                Toast.LENGTH_SHORT
                                                                            ).show()
                                                                        }
                                                                    }

                                                                })

                                                        }
                                                    }

                                                })

                                        } else {

                                            Toast.makeText(
                                                activity,
                                                "Oturum açılamadı : " + p0.exception,
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }

                                })


                        }
                    } else {

                        var sifre = view.etSifre.text.toString()
                        var adSoyad = view.etAdSoyad.text.toString()
                        var userName = view.etProfilKullaniciAdi.text.toString()


                        var kaydedilecekKullaniciDetaylari =
                            KullaniciDetaylari("0", "0", "0", "", "")
                        var kaydedilecekKullanici = Users(
                            gelenEmail,
                            sifre,
                            userName,
                            adSoyad,
                            "cxvxcvxcvxcvxcvxc",
                            kaydedilecekKullaniciDetaylari
                        )

                        mRef.child("users").child("cxvxcvxcvxcvxcvxc")
                            .setValue(kaydedilecekKullanici)
                    }
                }

            })


        }

        return view
    }


    //Her 3 editText'e en az 6 karakter girilmeden button aktif olmamalı. Firebasete bunu istiyor
    var watcher: TextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s!!.length > 5) {
                if (etAdSoyad.text.toString().length > 5 && etProfilKullaniciAdi.text.toString().length > 5 && etSifre.text.toString().length > 5) {
                    btnGiris.isEnabled = true
                    btnGiris.setTextColor(ContextCompat.getColor(activity!!, R.color.beyaz))
                    btnGiris.setBackgroundResource(R.color.mavi)

                } else {
                    btnGiris.isEnabled = false
                    btnGiris.setTextColor(ContextCompat.getColor(activity!!, R.color.golgelik))
                    btnGiris.setBackgroundResource(R.color.button_pasif)
                }

            } else {
                btnGiris.isEnabled = false
                btnGiris.setTextColor(ContextCompat.getColor(activity!!, R.color.golgelik))
                btnGiris.setBackgroundResource(R.color.button_pasif)

            }
        }

    }

    //////////////////////////////////////////////  EVENTBUS  /////////////////////////////////////////////
    //Eventbus ile verileri aldık.
    @Subscribe(sticky = true)
    internal fun onEmailEvent(emailAdres: EventbusDataEvents.EmailGönder) {
        //E-mail bilgilerini al
        gelenEmail = emailAdres.email
        Log.e("sibel", "Gelen email" + gelenEmail)
        Toast.makeText(activity, "gelen email : " + gelenEmail, Toast.LENGTH_SHORT).show()

    }

    //EventBus'ı eklemek için gerekli metotlar
    override fun onAttach(context: Context) {
        super.onAttach(context)
        EventBus.getDefault().register(this)
    }

    override fun onDetach() {
        super.onDetach()
        EventBus.getDefault().unregister(this)
    }
    //////////////////////////////////////////////  EVENTBUS  /////////////////////////////////////////////

}
