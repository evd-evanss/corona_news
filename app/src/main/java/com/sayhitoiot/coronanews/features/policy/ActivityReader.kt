package com.sayhitoiot.coronanews.features.policy

import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.barteksc.pdfviewer.PDFView
import com.github.barteksc.pdfviewer.util.Constants
import com.google.android.material.button.MaterialButton
import com.sayhitoiot.coronanews.R
import kotlinx.android.synthetic.main.activity_reader.*


class ActivityReader : AppCompatActivity() {

    private var reader: PDFView? = null
    private var nextButton: MaterialButton? = null
    private var previousButton: MaterialButton? = null
    private var countPolicyPrivacy = 0
    private var textCount: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reader)
        supportActionBar?.hide()
        reader = activityReader_pdfView_reader
        setupPDFReader()
        renderTopic()
        nextButton = activityReader_button_next
        textCount = activityStatistic_textView_count
        nextButton?.setOnClickListener { nextPage() }
        previousButton = activityReader_button_previous
        previousButton?.setOnClickListener { previousPage() }
    }

    private fun setupPDFReader() {
        Constants.Pinch.MINIMUM_ZOOM = 1f
        Constants.Pinch.MAXIMUM_ZOOM = 1f
    }

    private fun renderTopic() {
        reader?.fromAsset("terms_and_condictions.pdf")
            ?.defaultPage(0)
            ?.enableDoubletap(false)
            ?.pageSnap ( true )
            ?.pageFling ( true )
            ?.enableSwipe(false)
            ?.swipeHorizontal(true)
            ?.enableAntialiasing(true)
            ?.load()
    }

    private fun nextPage() {

        if(countPolicyPrivacy == (reader?.pageCount!!)-1) {
            return
        }
        countPolicyPrivacy++

        if(countPolicyPrivacy == 42) {
            renderButtonOK()
        } else {
            renderButtonNext()
        }

        if(countPolicyPrivacy<=reader?.pageCount!!) {
            reader?.jumpTo(countPolicyPrivacy)
        }
        textCount?.text = "$countPolicyPrivacy/${reader?.pageCount!!-1}"
        Log.d("privacy", "$countPolicyPrivacy")
    }

    private fun previousPage() {
        if(countPolicyPrivacy == 0) {
            return
        }
        renderButtonNext()
        countPolicyPrivacy--

        if(countPolicyPrivacy>=0) {
            reader?.jumpTo(countPolicyPrivacy)
        }
        textCount?.text = "$countPolicyPrivacy/${reader?.pageCount!!-1}"
        Log.d("privacy", "$countPolicyPrivacy")
    }

    private fun renderButtonOK() {
        nextButton?.text = "OK"
        nextButton?.backgroundTintList =  ColorStateList
            .valueOf(ContextCompat.getColor(this, R.color.colorGreen))
        nextButton?.setOnClickListener {
            val result = Intent()
            result.putExtra("accept", true)
            setResult(Activity.RESULT_OK, result)
            finish()
        }
    }

    private fun renderButtonNext() {
        nextButton?.text = "Seguinte"
        nextButton?.backgroundTintList =  ColorStateList
            .valueOf(ContextCompat.getColor(this, R.color.colorRed))
        nextButton?.setOnClickListener { nextPage() }
    }

}
