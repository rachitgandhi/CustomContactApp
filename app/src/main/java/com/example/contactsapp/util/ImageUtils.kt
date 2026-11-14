package com.example.contactsapp.util

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object ImageUtils {

    fun createImageFile(context: Context): Pair<File, String>? {
        return try {
            val timestamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val file = File.createTempFile("JPEG_${timestamp}_", ".jpg", storageDir)
            Pair(file, file.absolutePath)
        } catch (e: Exception) {
            Toast.makeText(context, "Error occurred while creating the file", Toast.LENGTH_SHORT).show()
            null
        }
    }

    fun copyUriToInternalStorage(context: Context, uri: Uri, fileName: String): String? {
        val file = File(context.filesDir, fileName)
        return try {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                FileOutputStream(file).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun recognizeText(bitmap: Bitmap, context: Context, onSuccess: (String) -> Unit) {
        val image = InputImage.fromBitmap(bitmap, 0)
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

        recognizer.process(image).addOnSuccessListener { ocrText ->
            onSuccess(ocrText.text)
        }.addOnFailureListener {
            Toast.makeText(context, "Error reading text", Toast.LENGTH_SHORT).show()
        }
    }
}
