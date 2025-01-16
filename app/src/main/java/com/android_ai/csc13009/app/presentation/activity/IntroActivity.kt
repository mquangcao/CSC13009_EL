package com.android_ai.csc13009.app.presentation.activity

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android_ai.csc13009.R

class IntroActivity : AppCompatActivity() {
    private var progress = 0
    private var maxProgress = 3
    private lateinit var images: List<Int>
    private lateinit var titles: List<String>
    private lateinit var descriptions: List<String>

    private lateinit var image: ImageView
    private lateinit var title: TextView
    private lateinit var description: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_intro)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        image = findViewById(R.id.intro_image_iv)
        title = findViewById(R.id.intro_title_tv)
        description = findViewById(R.id.intro_description_tv)
        progressBar = findViewById(R.id.intro_progress_pb)
        button = findViewById(R.id.intro_button_b)

        titles = resources.getStringArray(R.array.intro_titles).toList()
        descriptions = resources.getStringArray(R.array.intro_descriptions).toList()
        maxProgress = titles.size - 1

        val image1 = R.drawable.thumb_learn_activity
        val image2 = R.drawable.img_thumb_accout
        val image3 = R.drawable.thumb_chapter_activity
        images = listOf(image1, image2, image3)


        button.setOnClickListener {
            if (progress < maxProgress) {
                progress++
                loadProgress()
            } else {
                val intent = Intent(this, LoginActivity::class.java)
//                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, *pairs.map { androidx.core.util.Pair(it.first, it.second) }.toTypedArray())
                startActivity(intent)
            }
        }

//        resources.getResouceArray(R.array.intro_images)
        onBackPressedDispatcher.addCallback(this) {
            if (progress > 0) {
                progress--
                loadProgress()
            } else {
                finish()
            }
        }

        loadProgress()
    }

    private fun loadProgress() {
        title.text = titles[progress]
        description.text = descriptions[progress]
        image.setImageResource(images[progress])

        progressBar.progress = ((progress + 1) * 100) / (maxProgress + 1)

        if (progress < maxProgress) {
            button.text = "Next"
        } else {
            button.text = "Get started"
        }

        val fadeInAnimation = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
        title.startAnimation(fadeInAnimation)
        description.startAnimation(fadeInAnimation)
        image.startAnimation(fadeInAnimation)
    }
}