package com.fesskiev.mediacenter.ui

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.NestedScrollView
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import android.view.View
import android.view.animation.DecelerateInterpolator
import com.fesskiev.mediacenter.R
import com.fesskiev.mediacenter.ui.media.MediaFragment
import com.fesskiev.mediacenter.widgets.nestedscrolling.CustomNestedScrollView2
import com.fesskiev.mediacenter.widgets.nestedscrolling.LoremIpsumAdapter
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var isShowingCardHeaderShadow: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        setupBottomSheetView()
        setupDrawer()
        setupMainNavView()
        setupMediaFragment(savedInstanceState)
    }

    private fun setupMediaFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.content, MediaFragment.newInstance(), MediaFragment::class.java.simpleName)
            transaction.commit()
        }
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
        customNestedScrollView2.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
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
}
