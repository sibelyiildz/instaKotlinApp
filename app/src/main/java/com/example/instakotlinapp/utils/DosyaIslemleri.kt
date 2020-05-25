package com.example.instakotlinapp.utils

import java.io.File


/**     Code with ❤
╔════════════════════════════╗
║  Created by Sibel YILDIZ   ║
╠════════════════════════════╣
║ sibelyldz2012@gmail.com    ║
╠════════════════════════════╣
║     08/05/2020 - 17:08     ║
╚════════════════════════════╝
 */

class DosyaIslemleri {

    //Sınıftan nesne üretmeden içerisinde bulunan metodu kullanmak için companion obje kullanıyoruz
    companion object {

        fun klasördekiDosyalariGetir(klasörAdi: String): ArrayList<String> {

            var tumDosyalar = ArrayList<String>()

            var file = File(klasörAdi)

            //parametre olarak gönderdiğimiz klasördeki tüm dosyaların alınması
            var klasördekiTumDosyalar = file.listFiles()

            //parametre olarak gönderdiğimiz dosya yolunda klasör olup olmadığının kontrolü
            if (klasördekiTumDosyalar != null) {
/*
                //Galeriden getirilen resimlerin tarih sırasına göre listelenmesi
                if(klasördekiTumDosyalar.size>1){
                    Arrays.sort(klasördekiTumDosyalar, object: Comparator<File>{
                        override fun compare(p0: File?, p1: File?): Int {
                            if(p1!!.lastModified() > p0!!.lastModified()){
                                return -1
                            }else return 1
                        }

                    })
                }*/

                for (i in 0..klasördekiTumDosyalar.size - 1) {

                    //sadece dosyalara bakılır
                    if (klasördekiTumDosyalar[i].isFile) {

                        //okunan dosyanın telefondaki yeri ve adı
                        var okunanDosyaYolu =
                            klasördekiTumDosyalar[i].absolutePath  //O an okunan dosyanın bütün yolunu elde ettik

                        //okunan dosyanın tür kontrolünü yapmam lazım, pdf gibi istemiyorum
                        //files://root/Logo.png    -> noktadan sonrasını al dosyaTuru'ne ata
                        var dosyaTuru = okunanDosyaYolu.substring(okunanDosyaYolu.lastIndexOf("."))

                        if (dosyaTuru.equals(".jpeg") || dosyaTuru.equals(".jpg") || dosyaTuru.equals(
                                ".png"
                            ) || dosyaTuru.equals(".mp4")
                        ) {

                            tumDosyalar.add(okunanDosyaYolu)
                        }
                    }
                }
            }

            return tumDosyalar
        }


    }


}