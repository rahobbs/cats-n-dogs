package com.rahobbs.cats_n_dogs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rahobbs.cats_n_dogs.ui.PhotoFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, PhotoFragment.newInstance())
                .commitNow()
        }
    }
}