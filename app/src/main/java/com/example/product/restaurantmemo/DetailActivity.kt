package com.example.product.restaurantmemo

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // if (id == null) { id = -1 }
        val id = intent.getLongExtra(MainActivity.EXTRA_ID, -1)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.frame_fragment_detail, DetailFragment().createInstance(id))
        transaction.commit()

        fab_detail.setOnClickListener {
            val intent = Intent(this@DetailActivity, PostActivity::class.java)
            startActivity(intent)
        }
    }
}
