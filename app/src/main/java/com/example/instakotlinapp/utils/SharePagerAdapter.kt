package com.example.instakotlinapp.utils


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter


/**     Code with ❤
╔════════════════════════════╗
║  Created by Sibel YILDIZ  ║
╠════════════════════════════╣
║ sibelyldz2012@gmail.com ║
╠════════════════════════════╣
║     07/05/2020 - 18:29     ║
╚════════════════════════════╝
 */

class SharePagerAdapter(fm: FragmentManager, tabAdlari: ArrayList<String>) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {


    //Pager içinde gösterilecek fragtmentleri tutacak bir arraylist tanımlamamız gerekiyor.
    private var mFragmentList: ArrayList<Fragment> = ArrayList()

    private var tabAdlari: ArrayList<String> = tabAdlari


    override fun getItem(position: Int): Fragment {

        return mFragmentList.get(position)
    }

    override fun getCount(): Int {

        return mFragmentList.size
    }

    //PagerAdapterine fragmentleri eklerken
    fun addFragment(fragment: Fragment) {
        mFragmentList.add(fragment)
    }

    //O an hangi fragmentte ise onu gçsterir
    override fun getPageTitle(position: Int): CharSequence? {
        return tabAdlari.get(position)
    }

}