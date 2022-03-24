package com.wisal.android.practices.filesharing.server

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.lang.IllegalArgumentException

class FileSelectActivity : AppCompatActivity() {

    private val TAG = "FileSelectActivity"

    private lateinit var privateRootDir: File
    private lateinit var imagesDir: File
    private lateinit var imagesFiles: Array<File>
    private lateinit var imagesFilenames: Array<String>

    private lateinit var listItemsAdapter: ListItemsAdapter
    private lateinit var resultInent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        listItemsAdapter = ListItemsAdapter()
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.adapter = listItemsAdapter

        resultInent = Intent("com.wisal.android.practices.filesharing.server.ACTION_RETURN_FILE")

        privateRootDir = filesDir
        imagesDir = File(privateRootDir,"images")
        imagesFiles = imagesDir.listFiles() ?: arrayOf()

        imagesFilenames = imagesFiles.map { it.name }.toTypedArray()
        setResult(Activity.RESULT_CANCELED,null)


        val rowItems = imagesFiles.map { RowItem(it.name,it.toUri()) }
        
        listItemsAdapter.submitData(rowItems)
        
        listItemsAdapter.addItemClickHandler { index ->
            val requestFile = File(imagesDir,imagesFilenames[index])

            val fileUri: Uri? = try {
                FileProvider.getUriForFile(
                    this,
                    "com.wisal.android.practices.filesharing.server.fileprovider",
                    requestFile
                )
            } catch (e: IllegalArgumentException) {
                Log.e("File Selector",
                    "The selected file can't be shared: $requestFile")
                e.printStackTrace()
                null
            }

            if(fileUri != null) {
                resultInent.apply {
                    flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                    setDataAndType(fileUri,contentResolver.getType(fileUri))
                    setResult(Activity.RESULT_OK,resultInent)
                    Toast.makeText(this@FileSelectActivity,"${imagesFilenames[index]} selected",Toast.LENGTH_SHORT).show()
                    this@FileSelectActivity.finish()
                }
            } else {
                resultInent.apply {
                    setDataAndType(null,"")
                    setResult(RESULT_CANCELED,resultInent)
                }
            }
            Log.d("Debug","Content uri for the file: ${fileUri.toString()}")
        }

    }
}