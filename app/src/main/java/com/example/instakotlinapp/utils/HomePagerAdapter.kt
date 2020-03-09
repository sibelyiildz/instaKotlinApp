package com.example.instakotlinapp.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter


/**     Code with ❤
╔════════════════════════════╗
║  Created by Sibel YILDIZ   ║
╠════════════════════════════╣
║ sibelyldz2012@gmail.com    ║
╠════════════════════════════╣
║     07/03/2020 - 13:22     ║
╚════════════════════════════╝
 */
class HomePagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(
    fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {

    private var mFragmentList: ArrayList<Fragment> =
        ArrayList()   // bütün fargmentleri tutacak olan arraylist

    override fun getItem(position: Int): Fragment {
        return mFragmentList.get(position)
    }

    override fun getCount(): Int {   //kaç fragment sayısının olduğunu dönen metot
        return mFragmentList.size
    }


    // fragmentlerimizi listeye eklediğimiz metot
    fun addFragment(fragment: Fragment) {
        mFragmentList.add(fragment)
    }
}