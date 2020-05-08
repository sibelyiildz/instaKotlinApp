package com.example.instakotlinapp.utils

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView


/**     Code with ❤
╔════════════════════════════╗
║  Created by Sibel YILDIZ  ║
╠════════════════════════════╣
║ sibelyldz2012@gmail.com ║
╠════════════════════════════╣
║     08/05/2020 - 19:29     ║
╚════════════════════════════╝
 */


//Resimleri gridView'e yerleştirmek için tek_sutun_grid layoutu oluşturduk fakat içindeki imageView'ı kullanınca resimlerin boyutu bozuk geliyor
//Resimlerin eşit ölçüde elmesi için kendi özel imageView sınıfını oluşturduk.
class GridImageView : AppCompatImageView {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }
}