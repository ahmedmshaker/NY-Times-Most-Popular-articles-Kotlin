package com.example.nytime

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import kotlinx.android.synthetic.main.activity_main.view.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val host = NavHostFragment.create(R.navigation.navigation_graph)
        supportFragmentManager.beginTransaction().replace(R.id.container, host).setPrimaryNavigationFragment(host).commit()


    }
}
