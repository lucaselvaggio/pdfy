package io.github.boldijar.pdfy

import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream

class PdfDownloader {

    companion object {
        fun downloadPdf(pdfUrl: String, headersMap: HashMap<String, String>? = null, outputFile: File) {
            val client = OkHttpClient()
            val requestBuilder = Request.Builder().url(pdfUrl)
            headersMap?.let { headers ->
                headers.forEach{
                    requestBuilder.addHeader(it.key, it.value)
                }
            }
            val request = requestBuilder.build()
            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val inputStream = response.body?.byteStream()
                val outputStream = FileOutputStream(outputFile)
                inputStream?.copyTo(outputStream)
                outputStream.flush()
                outputStream.close()
                inputStream?.close()
            } else {
                logError("Failed to download file: ${response.code} ${response.message}")
            }
        }
    }
}