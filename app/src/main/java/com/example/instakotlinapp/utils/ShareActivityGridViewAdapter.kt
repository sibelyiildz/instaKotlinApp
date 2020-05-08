package com.example.instakotlinapp.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import com.example.instakotlinapp.R
import kotlinx.android.synthetic.main.tek_sutun_grid_resim.view.*


/**     Code with ❤
╔════════════════════════════╗
║  Created by Sibel YILDIZ   ║
╠════════════════════════════╣
║ sibelyldz2012@gmail.com    ║
╠════════════════════════════╣
║     08/05/2020 - 18:19     ║
╚════════════════════════════╝
 */

class ShareActivityGridViewAdapter(
    context: Context,
    resource: Int,
    var klasördekiDosylar: ArrayList<String>
) :
    ArrayAdapter<String>(context, resource, klasördekiDosylar) {

    var inflater: LayoutInflater
    var tekSutunResim: View? = null
    lateinit var viewHolder: ViewHolder

    init {   //inflater performansı ağırlaştırıyor o yüzden init blogu içerisine aldım
        inflater = LayoutInflater.from(context)
    }

    inner class ViewHolder {
        lateinit var imageView: GridImageView
        lateinit var progressBar: ProgressBar
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        tekSutunResim = convertView

        if (tekSutunResim == null) {

            tekSutunResim = inflater.inflate(R.layout.tek_sutun_grid_resim, parent, false)
            viewHolder = ViewHolder()
            viewHolder.imageView = tekSutunResim!!.imgTekSutunImage
            viewHolder.progressBar = tekSutunResim!!.progressBar

            tekSutunResim!!.tag = viewHolder
        } else {
            viewHolder = tekSutunResim!!.tag as ViewHolder
        }


        UniversalImageLoader.setImage(
            klasördekiDosylar.get(position),
            viewHolder.imageView,
            viewHolder.progressBar,
            "file:/"
        )

        return tekSutunResim!!
    }

}