package com.example.bookfilter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.Button
import android.widget.TextView
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val filterBtn = findViewById<Button>(R.id.btn)
        val resultView = findViewById<TextView>(R.id.resultView)
        resultView.movementMethod = ScrollingMovementMethod()
        val authorCondition = findViewById<TextInputLayout>(R.id.authorFilter)
        val countryCondition = findViewById<TextInputLayout>(R.id.countryFilter)
        val count = findViewById<TextView>(R.id.count)
        var result = mutableListOf<String>()

        filterBtn.setOnClickListener {
            val booksApp = application as BooksApp
            val httpApiService = booksApp.httpApiService

            CoroutineScope(Dispatchers.IO).launch {
                val decodedJsonResult = httpApiService.getBooks()

                withContext(Dispatchers.Main) {
                    resultView.text = ""
                    count.text = ""
                    result.clear()
                    decodedJsonResult.forEach {
                        if (authorCondition.editText?.text.toString() == it.author
                            && countryCondition.editText?.text.toString() == it.country
                        )
                            result.add(it.title)
                        else if (authorCondition.editText?.text.toString().isEmpty()
                            && countryCondition.editText?.text.toString() == it.country
                        )
                            result.add(it.title)
                        else if (authorCondition.editText?.text.toString() == it.author
                            && countryCondition.editText?.text.toString().isEmpty()
                        )
                            result.add(it.title)
                        else if (authorCondition.editText?.text.toString().isEmpty()
                            && countryCondition.editText?.text.toString().isEmpty()
                        )
                            result.add(it.title)
                    }
                    count.text = "Results:${result.count()}"
                    resultView.text = result.joinToString("\nResult: ", "Result: ", "", 3)
                }
            }

        }
    }
}