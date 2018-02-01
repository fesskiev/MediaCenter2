package com.fesskiev.mediacenter.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v7.widget.AppCompatImageView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.fesskiev.mediacenter.R
import java.util.ArrayList

 class ViewPagerAdapter(val context: Context, fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    private val fragmentList = ArrayList<Fragment>()
    private val registeredFragments = ArrayList<Fragment>()
    private val titleTextViews = ArrayList<TextView>()
    private val titleImageViews = ArrayList<AppCompatImageView>()

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    fun addFragment(fragment: Fragment) {
        fragmentList.add(fragment)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val fragment = super.instantiateItem(container, position) as Fragment
        registeredFragments.add(fragment)
        return fragment
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        registeredFragments.removeAt(position)
        super.destroyItem(container, position, `object`)
    }

    fun getRegisteredFragments(): List<Fragment> {
        return registeredFragments
    }

    fun getTitleTextViews(): List<TextView> {
        return titleTextViews
    }

    fun getTitleImageViews(): List<AppCompatImageView> {
        return titleImageViews
    }

    @SuppressLint("RestrictedApi")
    fun getTabView(imageResId: Int, textTitle: String, tabTextColor: Int): View {
        val v = LayoutInflater.from(context).inflate(R.layout.custom_tab, null)
        val tv = v.findViewById<TextView>(R.id.titleTab)
        tv.text = textTitle
        tv.setTextColor(tabTextColor)
        titleTextViews.add(tv)

        val img = v.findViewById<AppCompatImageView>(R.id.imageTab)
        img.setBackgroundResource(imageResId)
        img.supportBackgroundTintList = ColorStateList.valueOf(tabTextColor)
        titleImageViews.add(img)
        return v
    }
}