package com.rahobbs.cats_n_dogs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.rahobbs.cats_n_dogs.ui.PhotoFragment

class MainActivity : AppCompatActivity() {

    companion object {
        const val PHOTO_FRAGMENT_TAG = "photo_fragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val fragment: Fragment = if (savedInstanceState == null) {
            PhotoFragment()
        } else {
            supportFragmentManager.getFragment(savedInstanceState, PHOTO_FRAGMENT_TAG)
                ?: PhotoFragment()
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment, PHOTO_FRAGMENT_TAG)
            .commitNow()
    }
}