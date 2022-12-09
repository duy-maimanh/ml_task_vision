package com.mmd.examples.ml_task_vision

import android.content.Context
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.mmd.ml_task_vision.core.FilterMode
import com.mmd.ml_task_vision.core.OutputImageOptions
import com.mmd.ml_task_vision.nsfw_classification.NsfwProcess
import com.mmd.ml_task_vision.nsfw_classification.NsfwProcess.NsfwProcessOptions
import java.io.IOException
import java.io.InputStream


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bitmap = getBitmapFromAsset(this, "girl.jpg")
        val options = OutputImageOptions.builder()
            .setTargetSize(100, 100)
            .setScaleRatio(0.1)
            .setRotationDegrees(90)
            .build()

        val nsfwProcessOptions = NsfwProcessOptions.builder()
            .setOutputImageOptions(options)
            .setFilterMode(FilterMode.BLUR, 0.0)
            .build()

        val nsfwProcess = NsfwProcess.create(this, nsfwProcessOptions)
        findViewById<Button>(R.id.btnReRun).setOnClickListener {
            benchmark {
                val result = nsfwProcess.process(bitmap)
                Log.d("NSFW detection:", result.highestResult.key)
            }
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
}