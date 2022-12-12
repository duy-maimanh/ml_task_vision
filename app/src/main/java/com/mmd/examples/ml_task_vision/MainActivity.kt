package com.mmd.examples.ml_task_vision

import android.content.Context
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.mmd.ml_task_vision.core.FilterMode
import com.mmd.ml_task_vision.core.BaseOptions
import com.mmd.ml_task_vision.nsfw_classification.NsfwProcess
import com.mmd.ml_task_vision.nsfw_classification.NsfwProcess.NsfwProcessOptions
import java.io.IOException
import java.io.InputStream


class MainActivity : AppCompatActivity() {
    private lateinit var nsfwProcess: NsfwProcess
    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            // Handle the returned Uri
            benchmark {
                val result = nsfwProcess.detect(uri)
                Log.d("Result: ", result.highestResult.key)
                findViewById<ImageView>(R.id.imgShow).setImageBitmap(result.filteredImage)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val options = BaseOptions.builder()
            .isUseGPU(true)
            .build()

        val nsfwProcessOptions = NsfwProcessOptions.builder()
            .setBaseOptions(options)
            .setFilterMode(FilterMode.BLUR, 0.0)
            .build()

        nsfwProcess = NsfwProcess.create(this, nsfwProcessOptions)
        findViewById<Button>(R.id.btnSelectImage).setOnClickListener {
            getContent.launch("image/*")
        }
    }

    private fun getBitmapFromAsset(
        context: Context,
        filePath: String?
    ): Bitmap? {
        val assetManager: AssetManager = context.getAssets()
        val istr: InputStream
        var bitmap: Bitmap? = null
        try {
            istr = assetManager.open(filePath!!)
            bitmap = BitmapFactory.decodeStream(istr)
        } catch (e: IOException) {
            // handle exception
        }
        return bitmap
    }

    private inline fun benchmark(block: () -> Unit) {
        var timeRun = SystemClock.uptimeMillis()
        block()
        timeRun = SystemClock.uptimeMillis() - timeRun
        Log.d("Speed:", "$timeRun millis")
    }

    override fun onDestroy() {
        super.onDestroy()
        nsfwProcess.close()
    }
}