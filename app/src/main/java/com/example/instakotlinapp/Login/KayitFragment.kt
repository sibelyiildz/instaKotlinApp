package com.example.instakotlinapp.Login

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.instakotlinapp.R
import com.example.instakotlinapp.utils.EventbusDataEvents
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

/**
 * A simple [Fragment] subclass.
 */
class KayitFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragmen_kayit, container, false)
    }

    //Eventbus ile verileri aldık.
    @Subscribe(sticky = true)
    internal fun onEmailEvent(emailAdres: EventbusDataEvents.EmailGönder) {
        //E-mail bilgilerini al
        var gelenEmail = emailAdres.email
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

}
