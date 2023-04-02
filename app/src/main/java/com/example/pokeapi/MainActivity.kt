package com.example.pokeapi
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.bumptech.glide.Glide
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import java.util.*

private val client = AsyncHttpClient()

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val getPokeData = findViewById<EditText>(R.id.search)
        val searchButton = findViewById<Button>(R.id.search_button)
        val pokePic = findViewById<ImageView>(R.id.display)
        val pokeName = findViewById<TextView>(R.id.name)
        val pokeType = findViewById<TextView>(R.id.type)

        searchButton.setOnClickListener {

            val pokemonName = getPokeData.text.toString().lowercase(Locale.getDefault())
            val url = "https://pokeapi.co/api/v2/pokemon/$pokemonName"

            client.get(url, object : JsonHttpResponseHandler() {
                override fun onFailure(
                    statusCode: Int,
                    headers: Headers?,
                    response: String?,
                    throwable: Throwable?
                ) {
                    Log.d("Fail","API request failed")
                }

                override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON?) {


                    val pokeImgUrl =
                        json?.jsonObject?.getJSONObject("sprites")?.getString("front_default")

                    Glide.with(this@MainActivity)
                        .load(pokeImgUrl)
                        .into(pokePic)

                        val name = json?.jsonObject?.getString("name")
                        pokeName.text = name

                        val typesArray = json?.jsonObject?.getJSONArray("types")
                        val typesList = mutableListOf<String>()
                        if (typesArray != null) {
                            for (i in 0 until typesArray.length()) {
                                val type = typesArray.getJSONObject(i).getJSONObject("type").getString("name")
                                typesList.add(type)
                            }
                        }
                        pokeType.text = typesList.joinToString(" / ")
                    }
                })
            }
        }
    }

