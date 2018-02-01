package com.fesskiev.mediacenter.ui

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v4.view.ViewPager
import android.support.v4.widget.NestedScrollView
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.view.animation.DecelerateInterpolator

import com.fesskiev.mediacenter.R
import com.fesskiev.mediacenter.ui.adapters.ViewPagerAdapter
import com.fesskiev.mediacenter.ui.media.audio.AudioFragment
import com.fesskiev.mediacenter.ui.media.files.FilesFragment
import com.fesskiev.mediacenter.ui.media.video.VideoFragment
import com.fesskiev.mediacenter.widgets.nestedscrolling.CustomNestedScrollView2
import com.fesskiev.mediacenter.widgets.nestedscrolling.LoremIpsumAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var adapter: ViewPagerAdapter? = null
    private var isShowingCardHeaderShadow: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        setupBottomSheetView()
        setupDrawer()
        setupMainNavView()
        setupViewPager()
        setupTabs()
    }

    private fun setupDrawer() {
        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return false
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun setupTabs() {
        for (i in 0 until tabLayout.tabCount) {
            val tab = tabLayout.getTabAt(i)
            if (tab != null) {
                if (i == 0) {
                    tab.customView = adapter?.getTabView(getImagesIds()[i], getTitles()[i],
                            ContextCompat.getColor(applicationContext, R.color.yellow))
                } else {
                    tab.customView = adapter?.getTabView(getImagesIds()[i], getTitles()[i],
                            ContextCompat.getColor(applicationContext, R.color.white))
                }
            }
        }
    }

    private fun setupViewPager() {
        adapter = ViewPagerAdapter(applicationContext, supportFragmentManager)
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

            }

            override fun onPageSelected(position: Int) {
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
                            textView.setTextColor(ContextCompat.getColor(applicationContext, R.color.yellow))
                            imageView.supportBackgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(applicationContext, R.color.yellow))
                        } else {
                            textView.setTextColor(ContextCompat.getColor(applicationContext, R.color.white))
                            imageView.supportBackgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(applicationContext, R.color.white))
                        }
                    }
                }
            }
        })
    }

    private fun setupMainNavView() {
        val navigationViewMain = findViewById<NavigationView>(R.id.nav_view_main)
        navigationViewMain.setNavigationItemSelectedListener(this)
        navigationViewMain.itemIconTintList = null
        navigationViewMain.inflateHeaderView(R.layout.nav_header_main)
    }

    private fun setupBottomSheetView() {
        val recyclerView = findViewById<RecyclerView>(R.id.card_recyclerview)
        val linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = LoremIpsumAdapter(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this, linearLayoutManager.orientation))

        val cardHeaderShadow = findViewById<View>(R.id.card_header_shadow)
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                // Animate the shadow view in/out as the user scrolls so that it
                // looks like the RecyclerView is scrolling beneath the card header.
                val isRecyclerViewScrolledToTop = linearLayoutManager.findFirstVisibleItemPosition() == 0 &&
                        linearLayoutManager.findViewByPosition(0).top == 0
                if (!isRecyclerViewScrolledToTop && !isShowingCardHeaderShadow) {
                    isShowingCardHeaderShadow = true
                    showOrHideView(cardHeaderShadow, true)
                } else if (isRecyclerViewScrolledToTop && isShowingCardHeaderShadow) {
                    isShowingCardHeaderShadow = false
                    showOrHideView(cardHeaderShadow, false)
                }
            }
        })

        val customNestedScrollView2 = findViewById<CustomNestedScrollView2>(R.id.nestedscrollview)
        customNestedScrollView2.overScrollMode = View.OVER_SCROLL_NEVER
        customNestedScrollView2.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY,
                                                                                                    oldScrollX, oldScrollY ->
            if (scrollY == 0 && oldScrollY > 0) {
                // Reset the RecyclerView's scroll position each time the card
                // returns to its starting position.
                recyclerView.scrollToPosition(0)
                cardHeaderShadow.alpha = 0f
                isShowingCardHeaderShadow = false
            }
        })
    }

    private fun showOrHideView(view: View, shouldShow: Boolean) {
        view.animate().alpha(if (shouldShow) 1f else 0f)
                .setDuration(100).interpolator = DecelerateInterpolator()
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
}
