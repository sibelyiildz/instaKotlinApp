package com.example.instakotlinapp.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.instakotlinapp.Model.UserPosts
import com.example.instakotlinapp.R
import kotlinx.android.synthetic.main.tek_post_recycler_item.view.*


/**     Code with ❤
╔════════════════════════════╗
║  Created by Sibel YILDIZ   ║
╠════════════════════════════╣
║ sibelyldz2012@gmail.com    ║
╠════════════════════════════╣
║     26/05/2020 - 15:20     ║
╚════════════════════════════╝
 */

class HomeActivityRecyclerAdapter(var context: Context, var tumGonderiler: ArrayList<UserPosts>) :
    RecyclerView.Adapter<HomeActivityRecyclerAdapter.MyViewHolder>() {


    override fun getItemCount(): Int {

        return tumGonderiler.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var viewHolder =
            LayoutInflater.from(context).inflate(R.layout.tek_post_recycler_item, parent, false)

        return MyViewHolder(viewHolder)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.setData(position, tumGonderiler.get(position))
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        var tumLayout = itemView as ConstraintLayout
        var profilImage = tumLayout.imgUserProfile
        var userNameTitle = tumLayout.tvKullaniciAdiBaslik
        var gonderi = tumLayout.imgPostResim
        var userName = tumLayout.tvKullaniciAdi
        var gonderiAciklama = tumLayout.tvPostAciklama

        fun setData(position: Int, oAnkiGonderi: UserPosts) {

            userNameTitle.text = oAnkiGonderi.userName
            UniversalImageLoader.setImage(oAnkiGonderi.postURL!!, gonderi, null, "")
            userName.text = oAnkiGonderi.userName
            gonderiAciklama.text = oAnkiGonderi.postAciklama
            UniversalImageLoader.setImage(oAnkiGonderi.userProfilFotoURL!!, profilImage, null, "")
        }
    }
}