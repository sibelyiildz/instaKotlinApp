package com.example.instakotlinapp.utils

import android.content.Context
import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import com.example.instakotlinapp.R
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.nostra13.universalimageloader.core.assist.FailReason
import com.nostra13.universalimageloader.core.assist.ImageScaleType
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener


/**     Code with ❤
╔════════════════════════════╗
║  Created by Sibel YILDIZ   ║
╠════════════════════════════╣
║ sibelyldz2012@gmail.com    ║
╠════════════════════════════╣
║     15/04/2020 - 17:01     ║
╚════════════════════════════╝
 */


//Bu yaptığımız resimlerin ayarları ile ilgili
//Kullanmak istediğim zaman bunu çağıran activity'i parametre olarak göndericem
class UniversalImageLoader(val mContext: Context) {

    val config: ImageLoaderConfiguration
        get() {  //getter yazdık. Çağırırken getconfig() yazmak yeterli

            val defaultOptions = DisplayImageOptions.Builder()
                .showImageOnLoading(defaultImage)  //Resim yüklenirken bunu göster
                .showImageForEmptyUri(defaultImage)  //verilen uri'da resim yoksa
                .showImageOnFail(defaultImage)  //Resim yüklenirkern bir hata olursa
                .cacheOnDisk(true)
                .cacheInMemory(true)  //diske resimin cache'sini alsın, her seferinde yüklemeye çalışmasın
                .cacheOnDisk(true)
                .resetViewBeforeLoading(true) //Yüklemeden önce var olanı sıfırlasın
                .imageScaleType(ImageScaleType.EXACTLY)
                .considerExifParams(true) //Fotografı kendine göre otomatık yana kaydırmaz
                .bitmapConfig(Bitmap.Config.RGB_565) //Bellekten resmi getirirken hafızadan çok yer almamsı için daha az kaliterli gelcek
                .displayer(FadeInBitmapDisplayer(400))
                .build()  //Bu bir builder olduğu için de bütün bunları build() ile onaylıyorum

            return ImageLoaderConfiguration.Builder(mContext)
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(WeakMemoryCache())
                .diskCacheSize(100 * 1024 * 1024).build()
        }

    companion object {
        private val defaultImage = R.drawable.ic_profile

        //Sınıftan nesne üretmeden metotu kullanabilmek için Compain obje içine yazıyoruz
        fun setImage(
            imgUrel: String,
            imageView: ImageView,
            mProgressBar: ProgressBar?,
            ilkKisim: String?
        ) {

            //imgUrl: facebook.com/images/logo,jpeg
            //ilkKısım: http://

            val imageLoader = ImageLoader.getInstance()
            imageLoader.displayImage(ilkKisim + imgUrel, imageView, object : ImageLoadingListener {
                override fun onLoadingComplete(
                    imageUri: String?,
                    view: View?,
                    loadedImage: Bitmap?
                ) {
                    if (mProgressBar != null) {
                        mProgressBar.visibility = View.GONE
                    }
                }

                //Resim yükleme başlatıldığında progressBarr görüncek diğer zamanlarda görünmeyecek.
                override fun onLoadingStarted(imageUri: String?, view: View?) {
                    if (mProgressBar != null) {
                        mProgressBar.visibility = View.VISIBLE
                    }
                }

                override fun onLoadingCancelled(imageUri: String?, view: View?) {
                    if (mProgressBar != null) {
                        mProgressBar.visibility = View.GONE
                    }
                }

                override fun onLoadingFailed(
                    imageUri: String?,
                    view: View?,
                    failReason: FailReason?
                ) {
                    if (mProgressBar != null) {
                        mProgressBar.visibility = View.GONE
                    }
                }
            })

        }
    }

}