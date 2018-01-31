package com.fesskiev.mediacenter.ui.media

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.AppCompatImageView
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.fesskiev.mediacenter.R
import com.fesskiev.mediacenter.ui.media.audio.AudioFragment
import com.fesskiev.mediacenter.ui.media.files.FilesFragment
import com.fesskiev.mediacenter.ui.media.video.VideoFragment
import kotlinx.android.synthetic.main.fragment_media.*
import java.util.ArrayList


class MediaFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    companion object {
        fun newInstance(): MediaFragment {
            return MediaFragment()
        }
    }

    private var adapter: ViewPagerAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_media, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()
        setupTabs()
        setupSwipeRefreshLayout()
    }

    override fun onRefresh() {

    }

    private fun setupSwipeRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener(this)
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(context!!.applicationContext,
                R.color.primary_light))
        swipeRefreshLayout.setProgressViewOffset(false, 0,
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24f, resources.displayMetrics).toInt())
    }

    private fun setupTabs() {
        for (i in 0 until tabLayout.tabCount) {
            val tab = tabLayout.getTabAt(i)
            if (tab != null) {
                if (i == 0) {
                    tab.customView = adapter?.getTabView(getImagesIds()[i], getTitles()[i],
                            ContextCompat.getColor(context!!.applicationContext, R.color.yellow))
                } else {
                    tab.customView = adapter?.getTabView(getImagesIds()[i], getTitles()[i],
                            ContextCompat.getColor(context!!.applicationContext, R.color.white))
                }
            }
        }
    }

    private fun setupViewPager() {
        adapter = ViewPagerAdapter(context, fragmentManager)
        val fragments = getPagerFragments()

        viewPager.offscreenPageLimit = 2
        for (fragment in fragments) {
            adapter!!.addFragment(fragment)
        }
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            internal var currentPosition: Int = 0

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                Log.d("test", "onPageScrolled")
            }

            override fun onPageSelected(position: Int) {
                Log.d("test", "onPageSelected: " + position)
                currentPosition = position
            }

            @SuppressLint("RestrictedApi")
            override fun onPageScrollStateChanged(state: Int) {
                if (ViewPager.SCROLL_STATE_IDLE == state) {
                    val titleTexts = adapter!!.getTitleTextViews()
                    val titleImages = adapter!!.getTitleImageViews()
                    for (i in titleTexts.indices) {
                        val textView = titleTexts[i]
                        val imageView = titleImages[i]
                        if (currentPosition == i) {
                            textView.setTextColor(ContextCompat.getColor(context!!.applicationContext,
                                    R.color.yellow))
                            imageView.supportBackgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context!!.applicationContext, R.color.yellow))
                        } else {
                            textView.setTextColor(ContextCompat.getColor(context!!.applicationContext,
                                    R.color.white))
                            imageView.supportBackgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context!!.applicationContext, R.color.white))
                        }
                    }
                }
            }
        })
        viewPager.setOnTouchListener { v, event ->
            swipeRefreshLayout.isEnabled = false
            when (event.action) {
                MotionEvent.ACTION_UP -> swipeRefreshLayout.isEnabled = true
            }
            false
        }
    }

    private fun getPagerFragments(): Array<Fragment> {
        return arrayOf(AudioFragment.newInstance(), VideoFragment.newInstance(), FilesFragment.newInstance())
    }

    private fun getTitles(): Array<String> {
        return arrayOf(getString(R.string.tab_audio), getString(R.string.tab_video), getString(R.string.tab_files))
    }

    //TODO change icons!
    private fun getImagesIds(): Array<Int> {
        return arrayOf(R.drawable.icon_albums, R.drawable.icon_albums,
                R.drawable.icon_albums)
    }

    private class ViewPagerAdapter(val context: Context?, fm: FragmentManager?) : FragmentStatePagerAdapter(fm) {

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
            Log.d("test", "instantiateItem")
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
}