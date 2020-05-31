package com.ubiquitous.bricklist

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem

class ViewProjectActivity : AppCompatActivity() {
    // TODO: Show list of all needed blocks with pictures
    // TODO: Maybe sorting ?
    // TODO: Make it possible to increase or decrease block count (+ / -)
    // TODO: Make it possible to save project to XML (based on database FML...)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_project)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "View Project"
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                setResult(Activity.RESULT_OK)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
