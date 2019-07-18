package com.example.multipdfswipelayout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.multipdfswipeview_library.PDFConfig
import com.example.multipdfswipeview_library.PDFMultiSwipeView
import java.util.ArrayList

class MainActivity : AppCompatActivity() {
    private val pdfConfigs = ArrayList<PDFConfig>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var config = PDFConfig()
        config.id = "1"
        config.name = "pdfSample1"
        config.pdfUrl = "https://www.antennahouse.com/XSLsample/pdf/sample-link_1.pdf"
        pdfConfigs.add(config)

        config = PDFConfig()
        config.id = "2"
        config.name = "pdfSample2"
        config.pdfUrl = "http://www.africau.edu/images/default/sample.pdf"
        pdfConfigs.add(config)

        config = PDFConfig()
        config.id = "3"
        config.name = "pdfSample3"
        config.pdfUrl = "http://www.pdf995.com/samples/pdf.pdf"
        pdfConfigs.add(config)

        config = PDFConfig()
        config.id = "4"
        config.name = "pdfSample4"
        config.pdfUrl = "https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf"
        pdfConfigs.add(config)

        val pdfView:PDFMultiSwipeView = findViewById(R.id.pdfView)

        pdfView.with(this)
            .build(pdfConfigs, PDFMultiSwipeView.TYPE_ONLINE)
    }
}
