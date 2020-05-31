package com.ubiquitous.bricklist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val REQUEST_CODE = 10000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Project List"
        // TODO: Connect to database
        // TODO: Fill the main page with buttons from project database (FML...)

        fab.setOnClickListener { view ->
            showActivity("Add Project")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                showActivity("Settings")
                return true
            }
            R.id.home -> {
                this.finish()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun showActivity(whichClass: String, intent: Intent? = null) {
        if (intent == null) {
            var i: Intent? = null
            if (whichClass == "Settings")
                i = Intent(this, SettingsActivity::class.java)
            if (whichClass == "Add Project")
                i = Intent(this, AddProjectActivity::class.java)
            if (whichClass == "View Project")
                i = Intent(this, ViewProjectActivity::class.java)
            startActivityForResult(i, REQUEST_CODE)
            return
        }
        Toast.makeText(this, "Rozpoczęto nową aktywność", Toast.LENGTH_LONG).show()
        startActivityForResult(intent, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Toast.makeText(this, "Powrócono z aktywności", Toast.LENGTH_LONG).show()
    }

    fun viewProject(v: View) {
        var projectName = (v as Button).text
        var i = Intent(this, ViewProjectActivity::class.java)
        i.putExtra("projectname", projectName)
        showActivity("View Project", i)
    }
}
