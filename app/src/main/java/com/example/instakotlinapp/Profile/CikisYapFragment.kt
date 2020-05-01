package com.example.instakotlinapp.Profile

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth

/**
 * A simple [Fragment] subclass.
 */
class CikisYapFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var alert = AlertDialog.Builder(this.activity!!)
            .setTitle("Uygulamadan Çıkış Yap")
            .setMessage("Emin Misin")
            .setPositiveButton("Çıkış Yap", object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    FirebaseAuth.getInstance().signOut()
                    activity!!.finish()
                }
            })
            .setNegativeButton("iptal", object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    dismiss()  //pencereyi kapat
                }

            }).create()

        return alert
    }

}
