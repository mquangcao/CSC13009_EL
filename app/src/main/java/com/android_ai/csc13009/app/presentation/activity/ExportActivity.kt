package com.android_ai.csc13009.app.presentation.activity

import ExportGrammarQuestionAdapter
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.data.remote.model.FirestoreGrammarAnswer
import com.android_ai.csc13009.app.data.remote.model.FirestoreGrammarQuestion
import com.android_ai.csc13009.app.data.remote.repository.FirestoreLearningDetailRepository
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.core.View
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import android.view.View.MeasureSpec

class ExportActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var btnExport: Button
    private lateinit var userId: String
    private val repository = FirestoreLearningDetailRepository(FirebaseFirestore.getInstance())
    private val adapter = ExportGrammarQuestionAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_export)

        recyclerView = findViewById(R.id.recyclerView)
        btnExport = findViewById(R.id.btnExport)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        val firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser
        if (currentUser == null) {
            // Handle user not logged in case
            Toast.makeText(this, "User not logged in.", Toast.LENGTH_SHORT).show()
            return
        }
        userId = currentUser.uid

        // Lấy danh sách câu hỏi sai và hiển thị
        lifecycleScope.launch {
            val questionAnswerPairs = repository.getIncorrectGrammarQuestionsWithAnswers(userId)
            Log.d("ExportActivity", "Question-Answer Pairs: $questionAnswerPairs")
            adapter.submitList(questionAnswerPairs)
        }

        val backButton: ImageButton = findViewById(R.id.btnBack)
        backButton.setOnClickListener { finish() }

        // Xử lý khi nhấn nút export
        btnExport.setOnClickListener {
            lifecycleScope.launch {
                val pdfPath = exportIncorrectQuestionsToPDF(this@ExportActivity, adapter.questionAnswerPairs)

                if (pdfPath != null) {
                    // Mở file PDF ngay sau khi export
                    val pdfFile = File(pdfPath)
                    val pdfUri = FileProvider.getUriForFile(
                        this@ExportActivity,
                        "${applicationContext.packageName}.fileprovider",
                        pdfFile
                    )

                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        setDataAndType(pdfUri, "application/pdf")
                        flags = Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_GRANT_READ_URI_PERMISSION
                    }

                    try {
                        startActivity(Intent.createChooser(intent, "Open PDF"))
                    } catch (e: Exception) {
                        Toast.makeText(
                            this@ExportActivity,
                            "No PDF viewer available on this device.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    // Hiển thị SnackBar thông báo
                    Snackbar.make(
                        findViewById(R.id.main),
                        "Exported to $pdfPath",
                        Snackbar.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(this@ExportActivity, "No data to export!", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    private suspend fun exportIncorrectQuestionsToPDF(
        context: Context,
        data: List<Pair<FirestoreGrammarQuestion, List<FirestoreGrammarAnswer>>>
    ): String? {
        if (data.isEmpty()) return null

        val pdfDocument = PdfDocument()
        val paint = android.graphics.Paint()

        // Khởi tạo trang PDF
        var pageNumber = 1
        var yPosition = 50
        val pageWidth = 595
        val pageHeight = 842
        var page = pdfDocument.startPage(PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create())
        var canvas = page.canvas

        var index = 1
        val scaleFactor = 1.0f // Scale down the item view
        data.forEach { (question, answers) ->
            if (yPosition + 50 > pageHeight) {
                pdfDocument.finishPage(page)
                pageNumber++
                page = pdfDocument.startPage(PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create())
                canvas = page.canvas
                yPosition = 50
            }

            // Lấy itemView từ adapter
            val itemView = adapter.getItemViewAt(index - 1, recyclerView)
            itemView.measure(
                MeasureSpec.makeMeasureSpec(pageWidth, MeasureSpec.EXACTLY),
                MeasureSpec.UNSPECIFIED
            )
            itemView.layout(0, 0, itemView.measuredWidth, itemView.measuredHeight)

            // Scale the Bitmap to make it smaller
            val scaledWidth = (itemView.measuredWidth * scaleFactor).toInt()
            val scaledHeight = (itemView.measuredHeight * scaleFactor).toInt()
            val bitmap = Bitmap.createBitmap(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888)
            val itemCanvas = Canvas(bitmap)
            itemView.draw(itemCanvas)

            // Nếu không đủ không gian cho item, chuyển sang trang mới
            if (yPosition + bitmap.height > pageHeight) {
                pdfDocument.finishPage(page)
                pageNumber++
                page = pdfDocument.startPage(PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create())
                canvas = page.canvas
                yPosition = 50
            }

            // Vẽ bitmap lên canvas của trang PDF
            canvas.drawBitmap(bitmap, 0f, yPosition.toFloat(), null)
            yPosition += bitmap.height

            index++
        }
        pdfDocument.finishPage(page)

        // Lưu file PDF vào thư mục "Downloads"
        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

        // Handle file name conflict
        var pdfFile = File(downloadsDir, "IncorrectQuestions.pdf")
        var i = 1
        while (pdfFile.exists()) {
            val newFileName = "IncorrectQuestions($i).pdf"
            pdfFile = File(downloadsDir, newFileName)
            i++
        }

        // Write PDF to file
        pdfDocument.writeTo(FileOutputStream(pdfFile))
        pdfDocument.close()

        return pdfFile.absolutePath
    }
}
