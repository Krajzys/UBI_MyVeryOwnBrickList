package com.ubiquitous.bricklist

import android.app.Activity
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_add_project.*
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL
import javax.xml.parsers.DocumentBuilderFactory


class AddProjectActivity : AppCompatActivity() {
    // TODO: Check if Project ID is a ID of legit LEGO set
    // TODO: If yes download XML File from the web and read it
    // TODO: If no then say so
    var URLGiven: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_project)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Add Project"
        addButton.isEnabled = false
        checkButton.isEnabled = true

        URLGiven = PreferenceManager.getDefaultSharedPreferences(this).getString("prefixURL", "")
        Toast.makeText(this, URLGiven, Toast.LENGTH_SHORT).show()
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

    private inner class XMLDownloader(var projectCode: String?) : AsyncTask<String, Int, String>() {

        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            loadData(projectCode + ".xml")
        }

        override fun doInBackground(vararg p0: String?): String {
            try {
                Log.w("test", "im in")
                val url = URL(URLGiven + projectCode + ".xml")
                val connection = url.openConnection()
                connection.connect()
                val lengthOfFile = connection.contentLength
                val isStream = url.openStream()
                val testDirectory = File("$filesDir/XML")
                if (!testDirectory.exists()) testDirectory.mkdir()
                val fos = FileOutputStream("$testDirectory/$projectCode.xml")
                val data = ByteArray(1024)
                var count = 0
                var total: Long = 0
                var progress = 0
                count = isStream.read(data)
                while (count != -1) {
                    total += count.toLong()
                    val progress_temp = total.toInt() * 100/ lengthOfFile
                    if (progress_temp % 10 == 0 && progress != progress_temp) {
                        progress = progress_temp
                    }
                    fos.write(data, 0, count)
                    count = isStream.read(data)
                }
                isStream.close()
                fos.close()
            } catch (e: MalformedURLException) {
                return "Malformed URL"
            } catch (e: FileNotFoundException) {
                return "File not found"
            } catch (e: IOException) {
                return "IO Exception"
            }
            return "success"
        }
    }

    // TODO: Load to database (FML...) ? based  on it add elements to View Project
    fun loadData(fileName: String?) {
        val path = filesDir
        val inDir = File(path, "XML")
        Log.w("file", fileName)

        if (inDir.exists()) {
            Log.w("dir", "dir exists")
            val file = File(inDir, fileName)
            if (file.exists()) {
                Log.w("file", "file exists")
                val xmlDoc: Document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file)

                xmlDoc.documentElement.normalize()

                val items: NodeList = xmlDoc.getElementsByTagName("ITEM")

                for (i in 0 until items.length) {
                    val itemNode: Node = items.item(i)
                    Log.w("Item", itemNode.nodeName)

                    if (itemNode.nodeType == Node.ELEMENT_NODE) {
                        val elem = itemNode as Element
                        val children = elem.childNodes

                        for (j in 0 until children.length) {
                            val node = children.item(j)
                            if (node.nodeName == "ITEMTYPE" ||
                                    node.nodeName == "ITEMID" ||
                                    node.nodeName == "QTY" ||
                                    node.nodeName == "COLOR" ||
                                    node.nodeName == "EXTRA")
                                Log.w("  Item children", node.nodeName + " " + node.textContent)
                        }

                    }
                }
            }
        }
    }

    fun checkClick(v: View) {
        // TODO: Check from database and fill the project name based on it
        if (projectID.text.toString() == "615" || projectID.text.toString() == "70403") {
            Toast.makeText(this, "LEGO set exists!", Toast.LENGTH_LONG).show()
            addButton.isEnabled = true
        }
        else {
            Toast.makeText(this, "This LEGO set doesn't exist!", Toast.LENGTH_LONG).show()
            addButton.isEnabled = false
        }
    }

    fun addClick(v: View) {
        Toast.makeText(this, URLGiven + projectID.text.toString() + ".xml", Toast.LENGTH_SHORT).show()
        val xmld = XMLDownloader(projectID.text.toString())
        xmld.execute()
    }
}
