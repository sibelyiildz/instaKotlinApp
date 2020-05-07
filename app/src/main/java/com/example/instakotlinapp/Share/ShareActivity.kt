package com.example.instakotlinapp.Share

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.instakotlinapp.R
import com.example.instakotlinapp.utils.SharePagerAdapter
import kotlinx.android.synthetic.main.activity_share.*

class ShareActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)

        //Bu metosun içinde viewPager ayarlamaları yapılıyor
        setupShareViewPager()
    }

    private fun setupShareViewPager() {

        var tabAdlari = ArrayList<String>()
        tabAdlari.add("GALERİ")
        tabAdlari.add("FOTOĞRAF")
        tabAdlari.add("VİDEO")

        //PagerAdapter sınıfından nesne oluşturuyoruz ve supportFragmentManager paremetresi veriyoruz
        var sharePagerAdapter = SharePagerAdapter(supportFragmentManager, tabAdlari)

        //İlgili fragmentleri viewPager adpterine ekliyoruz
        sharePagerAdapter.addFragment(ShareGaleriFragment())
        sharePagerAdapter.addFragment(ShareKameraFragment())
        sharePagerAdapter.addFragment(ShareVideoFragment())

        //Yukarıda adaptere koyduğumuz fragmentleri ViewPagerda gösteriyoruz
        shareViewPager.adapter = sharePagerAdapter

        //Tabların aktif olarak gösterilmesi
        shareTabLayout.setupWithViewPager(shareViewPager)
    }
}
