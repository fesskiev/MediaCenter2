package com.fesskiev.mediacenter.ui.main

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v4.view.ViewPager
import android.support.v4.widget.NestedScrollView
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.SearchView
import android.text.InputType
import android.util.TypedValue
import android.view.*
import android.view.inputmethod.EditorInfo

import com.fesskiev.mediacenter.R
import com.fesskiev.mediacenter.services.ScanSystemService
import com.fesskiev.mediacenter.ui.adapters.ViewPagerAdapter
import com.fesskiev.mediacenter.ui.media.audio.AudioFoldersFragment
import com.fesskiev.mediacenter.ui.media.files.FilesFragment
import com.fesskiev.mediacenter.ui.media.folders.FoldersFragment
import com.fesskiev.mediacenter.ui.media.video.VideoFoldersFragment
import com.fesskiev.mediacenter.utils.PermissionsUtils.Companion.PERMISSION_STORAGE
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_playback.*
import javax.inject.Inject
import com.fesskiev.mediacenter.domain.entity.media.AudioFile
import com.fesskiev.mediacenter.domain.entity.media.AudioFolder
import com.fesskiev.mediacenter.domain.entity.media.VideoFile
import com.fesskiev.mediacenter.domain.entity.media.VideoFolder
import com.fesskiev.mediacenter.services.PlaybackService
import com.fesskiev.mediacenter.services.PlaybackService.Companion.ACTION_FINISH_APP
import com.fesskiev.mediacenter.ui.playlist.PlaylistActivity
import com.fesskiev.mediacenter.utils.*
import com.fesskiev.mediacenter.widgets.controls.AudioControlLayout
import com.fesskiev.mediacenter.widgets.dialogs.SimpleDialog

class MainActivity : DaggerAppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
        SwipeRefreshLayout.OnRefreshListener, MainContract.View {

    @Inject
    @JvmField
    var presenter: MainPresenter? = null

    @Inject
    @JvmField
    var permissionsUtils: PermissionsUtils? = null

    private lateinit var adapter: ViewPagerAdapter
    private var setupViews = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_main)
        val checkPermission = permissionsUtils?.checkPermissionsStorage(this)
        if (checkPermission != null && checkPermission) {
            setupViews()
            setupViews = true
        }

        PlaybackService.startPlaybackService(this)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val action = intent?.action
        if (action != null && action == ACTION_FINISH_APP) {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        presenter?.fetchSelectedMedia()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.detach()
    }

    private fun setupViews() {
        setSupportActionBar(toolbar)
        setupPlaybackView()
        setupDrawer()
        setupMainNavView()
        setupViewPager()
        setupTabs()
        setupSearchView()
        setupSwipeRefresh()
        presenter?.fetchMediaControl()
    }

    private fun setupSwipeRefresh() {
        swipeRefreshLayout.setOnRefreshListener(this)
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(applicationContext, R.color.primary_light),
                ContextCompat.getColor(applicationContext, R.color.primary),
                ContextCompat.getColor(applicationContext, R.color.yellow))
        swipeRefreshLayout.setProgressViewOffset(false, 0, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 128f,
                resources.displayMetrics).toInt())

        viewPager.setOnTouchListener { v, event ->
            swipeRefreshLayout.isEnabled = false
            when (event.action) {
                MotionEvent.ACTION_UP -> swipeRefreshLayout.isEnabled = true
            }
            false
        }
    }

    private fun setupSearchView() {
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = getString(R.string.search_hint)
        searchView.inputType = InputType.TYPE_TEXT_FLAG_CAP_WORDS
        searchView.imeOptions = searchView.imeOptions or EditorInfo.IME_ACTION_SEARCH or
                EditorInfo.IME_FLAG_NO_EXTRACT_UI or EditorInfo.IME_FLAG_NO_FULLSCREEN
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                return true
            }

            override fun onQueryTextChange(text: String): Boolean {
                if (searchView.width > 0) {
                    queryFiles(text)
                }
                return true
            }
        })
    }

    private fun setupDrawer() {
        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.wear -> {

            }
            R.id.effects -> {

            }
            R.id.settings -> {

            }
            R.id.about -> {

            }
            R.id.playlist -> openActivity(PlaylistActivity::class.java)
        }
        return false
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_search -> searchTracks()
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_STORAGE -> checkGrantResults(grantResults)
        }
    }

    private fun checkGrantResults(grantResults: IntArray) {
        if (grantResults.isNotEmpty()) {
            val checkPermission = permissionsUtils?.checkPermissionsResultGranted(grantResults)
            if (checkPermission != null && checkPermission) {
                if (!setupViews) {
                    setupViews()
                } else {
                    ScanSystemService.startFetchMedia(applicationContext)
                }
            } else {
                val showRationale = permissionsUtils?.shouldShowRequestStoragePermissionRationale(this)
                if (showRationale != null && showRationale) {
                    createExplanationPermissionDialog()
                } else {
                    permissionsDenied()
                }
            }
        }
    }

    override fun onRefresh() {
        val checkPermission = permissionsUtils?.checkPermissionsStorage(this)
        if (checkPermission != null && checkPermission) {
            val transaction = supportFragmentManager.beginTransaction()
            val dialog = SimpleDialog.newInstance(getString(R.string.dialog_search_title),
                    getString(R.string.dialog_search_message), R.drawable.ic_launch_splash)
            dialog.setPositiveListener(object : SimpleDialog.OnPositiveListener {
                override fun onClick() {
                    ScanSystemService.startFetchMedia(applicationContext)
                }
            })
            dialog.show(transaction, SimpleDialog::class.java.name)
        }
        swipeRefreshLayout.isRefreshing = false
    }

    override fun showAudioControl() {
        contentContainer.removeAllViews()
        contentContainer.addView(AudioControlLayout(this))
    }

    override fun showVideoControl() {

    }

    override fun updateSelectedAudioFile(audioFile: AudioFile) {
        playbackTitle.text = audioFile.audioFileArtist
        playbackDesc.text = audioFile.audioFileTitle
    }

    override fun updateSelectedVideoFile(videoFile: VideoFile) {

    }

    override fun updateSelectedAudioFolder(audioFolder: AudioFolder) {

    }

    override fun updateSelectedVideoFolder(videoFolder: VideoFolder) {

    }

    override fun showProgressBar() {

    }

    override fun hideProgressBar() {

    }

    fun queryFiles(query: String) {
        val fragments = adapter.getRegisteredFragments()
        fragments.filterIsInstance<FilesFragment>().single().queryFiles(query)
    }

    private fun searchTracks() {
        viewPager.setCurrentItem(3, true)
        visibleSearchView()
    }

    private fun visibleSearchView() {
        searchView.visible()
        searchView.isFocusable = true
        searchView.isIconified = false
    }

    private fun invisibleSearchView() {
        searchView.invisible()
    }

    private fun setupTabs() {
        for (i in 0 until tabLayout.tabCount) {
            val tab = tabLayout.getTabAt(i)
            if (tab != null) {
                if (i == 0) {
                    tab.customView = adapter.getTabView(getTitles()[i],
                            ContextCompat.getColor(applicationContext, R.color.yellow))
                } else {
                    tab.customView = adapter.getTabView(getTitles()[i],
                            ContextCompat.getColor(applicationContext, R.color.white))
                }
            }
        }
    }

    private fun setupViewPager() {
        adapter = ViewPagerAdapter(applicationContext, supportFragmentManager)
        val fragments = getPagerFragments()

        viewPager.offscreenPageLimit = 4
        for (fragment in fragments) {
            adapter.addFragment(fragment)
        }
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            internal var currentPosition: Int = 0

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                currentPosition = position
                if (position == 3) {
                    visibleSearchView()
                } else {
                    invisibleSearchView()
                }
            }

            @SuppressLint("RestrictedApi")
            override fun onPageScrollStateChanged(state: Int) {
                if (ViewPager.SCROLL_STATE_IDLE == state) {
                    val titleTexts = adapter.getTitleTextViews()
                    for (i in titleTexts.indices) {
                        val textView = titleTexts[i]
                        if (currentPosition == i) {
                            textView.setTextColor(ContextCompat.getColor(applicationContext, R.color.yellow))
                        } else {
                            textView.setTextColor(ContextCompat.getColor(applicationContext, R.color.white))
                        }
                    }
                }
            }
        })
    }

    private fun setupMainNavView() {
        navigationViewMain.setNavigationItemSelectedListener(this)
        navigationViewMain.itemIconTintList = null
        navigationViewMain.inflateHeaderView(R.layout.nav_header_main)
    }

    private fun setupPlaybackView() {
        nestedScrollview.overScrollMode = View.OVER_SCROLL_NEVER
        nestedScrollview.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY,
                                                                                             oldScrollX, oldScrollY ->
            val height = (v.getChildAt(0).measuredHeight - v.measuredHeight).toFloat()
            val value = scrollY / height
            animateTopView(1f - value)
        })
    }

    private fun animateTopView(value: Float) {
        fabPlayPause.animate().alpha(value)
    }

    private fun getPagerFragments(): Array<Fragment> {
        return arrayOf(AudioFoldersFragment.newInstance(), VideoFoldersFragment.newInstance(),
                FoldersFragment.newInstance(), FilesFragment.newInstance())
    }

    private fun getTitles(): Array<String> {
        return arrayOf(getString(R.string.tab_audio), getString(R.string.tab_video),
                getString(R.string.tab_folders), getString(R.string.tab_files))
    }

    private fun createExplanationPermissionDialog() {
        val transaction = supportFragmentManager.beginTransaction()
        val dialog = SimpleDialog.newInstance(getString(R.string.dialog_permission_title),
                getString(R.string.dialog_permission_message), R.drawable.ic_launch_splash)
        dialog.setPositiveListener(object : SimpleDialog.OnPositiveListener {
            override fun onClick() {
                permissionsUtils?.requestPermissionsStorage(this@MainActivity)
            }
        })
        dialog.setNegativeListener(object : SimpleDialog.OnNegativeListener {
            override fun onClick() {
                finish()
            }
        })
        dialog.show(transaction, SimpleDialog::class.java.name)
    }

    private fun permissionsDenied() {
        showToast(R.string.toast_permissions_denied_message)
        finish()
    }
}
