package com.dailystudio.tflite.example.medata

import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.dailystudio.tensorflow.lite.viewer.AbsTFLiteModelViewerActivity
import com.dailystudio.tensorflow.lite.viewer.image.ImageInferenceInfo
import com.dailystudio.tflite.example.medata.fragment.ImageClassifierCameraFragment
import org.tensorflow.lite.support.label.Category
import kotlin.math.min
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class MainActivity : AbsTFLiteModelViewerActivity<ImageInferenceInfo, List<Category>>() {

    companion object {
        const val REPRESENTED_ITEMS_COUNT = 3
    }

    private var detectItemViews: Array<TextView?> =
        Array(REPRESENTED_ITEMS_COUNT) {null}
    private var detectItemValueViews: Array<TextView?> =
        Array(REPRESENTED_ITEMS_COUNT) {null}
    private var detectItemCalorieViews: Array<TextView?> =
        Array(REPRESENTED_ITEMS_COUNT) {null}


    override fun createBaseFragment(): Fragment {
        return ImageClassifierCameraFragment()
    }
    fun getCalories(query: String, header: String): String {
        val url = "https://api.calorieninjas.com/v1/nutrition?query=$query"
        val request = Request.Builder()
            .url(url)
            .header("X-Api-Key", header)
            .build()
        val client = OkHttpClient()
        val response = client.newCall(request).execute()
        if (response.isSuccessful){
            return "Success"
        }
//        val body = response.body?.string()
//        val json = JSONObject(body)
//        val food = json.getJSONArray("items")
//        for (i in 0 until food.length()) {
//            val foodName = food.getJSONObject(i).getString("name")
//            //if (foodName == query) {
//            return food.getJSONObject(i).getString("calories")
//            //}
//        }
        return "0"
    }

    



    override fun createResultsView(): View? {
        val resultsView = LayoutInflater.from(this).inflate(
                R.layout.layout_results, null)

        resultsView?.let {
            setupResultView(it)
        }

        return resultsView
    }

    private fun setupResultView(resultsView: View) {
        for (i in 0 until REPRESENTED_ITEMS_COUNT) {
            detectItemViews[i] = resultsView.findViewById(
                resources.getIdentifier("detected_item${i + 1}", "id", packageName)
            )

            detectItemValueViews[i] = resultsView.findViewById(
                resources.getIdentifier("detected_item${i + 1}_value", "id", packageName)
            )
            detectItemCalorieViews[i] = resultsView.findViewById(
                resources.getIdentifier("detected_item${i+1}_calorie","id", packageName)
            )
        }
    }

    override fun onResultsUpdated(results: List<Category>) {
        val itemCount = min(results.size, REPRESENTED_ITEMS_COUNT)
        val header = "jUJykEHjFjQ67E/gABnIzg==HxVIUbfONEAoFDM2"

        for (i in 0 until itemCount) {
            var label = results[i].displayName
            if (label.isNullOrEmpty()) {
                label = results[i].label
            }
            
            detectItemViews[i]?.text = label
            detectItemValueViews[i]?.text = "%.1f%%".format(results[i].score * 100)
            detectItemCalorieViews[i]?.text = getCalories(results[i].displayName, header)

        }
    }

    override fun getViewerAppName(): CharSequence? {
        return getString(R.string.app_name)
    }

    override fun getAboutIconResource(): Int {
        return R.mipmap.ic_launcher
    }

    override fun getAboutThumbResource(): Int {
        return R.drawable.app_thumb
    }

    override fun getViewerAppDesc(): CharSequence? {
        return getString(R.string.app_desc)
    }

}

