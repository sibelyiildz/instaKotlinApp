package com.example.instakotlinapp.utils

import android.content.Context
import android.content.Intent
import android.view.MenuItem
import com.example.instakotlinapp.Home.HomeActivity
import com.example.instakotlinapp.News.NewsActivity
import com.example.instakotlinapp.Profile.ProfileActivity
import com.example.instakotlinapp.R
import com.example.instakotlinapp.Search.SearchActivity
import com.example.instakotlinapp.Share.ShareActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx


/**     Code with ❤
╔════════════════════════════╗
║  Created by Sibel YILDIZ   ║
╠════════════════════════════╣
║ sibelyldz2012@gmail.com    ║
╠════════════════════════════╣
║     05/03/2020 - 21:48     ║
╚════════════════════════════╝
 */
class BottomNavigationViewHelper {

    companion object {  //companion obje karşılığı statik (sınıftan herhangi bir nesne üretmeden içindeki metotlara ulaşabilirim)

        //BottomNavigationView'da default gelen özellikleri değiştirdiğimiz kısım, kütüphene yükledik
        fun setupBottomNavigationView(bottomNavigationViewEx: BottomNavigationViewEx) {

            bottomNavigationViewEx.enableAnimation(false)
            bottomNavigationViewEx.enableItemShiftingMode(false)
            bottomNavigationViewEx.enableShiftingMode(false)
            bottomNavigationViewEx.setTextVisibility(false)
        }

        //Activityler arası geçiş için
        fun setupNavigation(context: Context, bottomNavigationViewEx: BottomNavigationViewEx) {

            bottomNavigationViewEx.onNavigationItemSelectedListener =
                object : BottomNavigationView.OnNavigationItemSelectedListener {
                    override fun onNavigationItemSelected(item: MenuItem): Boolean {

                        when (item.itemId) {
                            R.id.ic_home -> {

                                val intent = Intent(
                                    context,
                                    HomeActivity::class.java
                                ).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                                context.startActivity(intent)
                                return true
                            }

                            R.id.ic_serach -> {

                                val intent = Intent(
                                    context,
                                    SearchActivity::class.java
                                ).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                                context.startActivity(intent)
                                return true
                            }

                            R.id.ic_share -> {

                                val intent = Intent(
                                    context,
                                    ShareActivity::class.java
                                ).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                                context.startActivity(intent)
                                return true
                            }

                            R.id.ic_news -> {

                                val intent = Intent(
                                    context,
                                    NewsActivity::class.java
                                ).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                                context.startActivity(intent)
                                return true
                            }

                            R.id.ic_profile -> {

                                val intent = Intent(
                                    context,
                                    ProfileActivity::class.java
                                ).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                                context.startActivity(intent)
                                return true
                            }
                        }
                        return false
                    }
                }

        }

    }
}