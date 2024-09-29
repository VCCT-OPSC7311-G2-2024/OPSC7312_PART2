package com.example.opsc7312poe

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MoodTrack : AppCompatActivity() {

    private var user: FirebaseUser? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var moodChart: BarChart
    private lateinit var timeFrameSpinner: Spinner
    private lateinit var userMoodsRef: DatabaseReference
    private lateinit var loadingIndicator: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mood_track)

        // Initialize UI components
        moodChart = findViewById(R.id.barchart)
        loadingIndicator = findViewById(R.id.loading_indicator)
        timeFrameSpinner = findViewById(R.id.time_frame_spinner) // Initialize your Spinner here

        // Adjust padding for system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize Firebase Authentication and Database
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser ?: run {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val userId = user!!.uid
        val database = FirebaseDatabase.getInstance("https://opsc7311poe-fd06a-default-rtdb.europe-west1.firebasedatabase.app")
        userMoodsRef = database.getReference("users").child(userId).child("moods")

        // Set up spinner adapter and listener
        timeFrameSpinner.adapter = ArrayAdapter.createFromResource(
            this,
            R.array.time_frame_options,
            android.R.layout.simple_spinner_item
        )
        timeFrameSpinner.setSelection(0)
        timeFrameSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                when (parent.getItemAtPosition(position).toString()) {
                    "Last 12 Days" -> getMoodsForTimeFrame(getLast12Days(), showWeekly = false)
                    "Weekly" -> getMoodsForTimeFrame(getLastWeekDays(), showWeekly = true)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // Load mood data for the initial selection
        getMoodsForTimeFrame(getLast12Days(), showWeekly = false)

        // Navigation buttons
        val navHome: ImageButton = findViewById(R.id.nav_home)
        navHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish() // Optional: Call finish() if you want to remove the current activity from the back stack
        }

        val navMood: ImageButton = findViewById(R.id.nav_mood)
        navMood.setOnClickListener {
            val intent = Intent(this, MoodTrack::class.java)
            startActivity(intent)
        }
    }


    private fun getMoodsForTimeFrame(dates: List<String>, showWeekly: Boolean) {
        loadingIndicator.visibility = View.VISIBLE

        userMoodsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val moodData = mutableListOf<BarEntry>()
                val moodValues = mutableListOf<Int>() // To calculate average

                dates.forEachIndexed { index, date ->
                    val moodValue = dataSnapshot.child(date).child("mood").getValue(String::class.java)
                    moodValue?.let {
                        val moodIndex = Mood.fromString(it).value
                        moodData.add(BarEntry(index.toFloat(), moodIndex.toFloat()))
                        moodValues.add(moodIndex) // Collect mood values for average calculation
                    }
                }

                if (showWeekly) {
                    val averageMood = calculateAverageMood(moodValues)
                    val averageEntry = BarEntry(moodData.size.toFloat(), averageMood.toFloat())
                    moodData.add(averageEntry) // Add average mood as the last bar
                }

                displayMoodGraph(moodData, dates, showWeekly)
                loadingIndicator.visibility = View.GONE
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@MoodTrack, "Failed to load mood data: ${databaseError.message}", Toast.LENGTH_SHORT).show()
                loadingIndicator.visibility = View.GONE
            }
        })
    }

    private fun calculateAverageMood(moodValues: List<Int>): Int {
        return if (moodValues.isNotEmpty()) {
            moodValues.sum() / moodValues.size // Calculate average mood
        } else {
            0 // Return 0 if no mood values available
        }
    }

    private fun getLast12Days(): List<String> {
        return getDaysFromNow(12)
    }

    private fun getLastWeekDays(): List<String> {
        return getDaysFromNow(7)
    }

    private fun getDaysFromNow(days: Int): List<String> {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance()
        return (0 until days).map {
            formatter.format(calendar.time).also { calendar.add(Calendar.DAY_OF_MONTH, -1) }
        }.reversed()
    }

    private fun displayMoodGraph(moodData: List<BarEntry>, lastDays: List<String>, showWeekly: Boolean) {
        moodChart.clear()

        val dataSet = BarDataSet(moodData, "Moods").apply {
            colors = listOf(
                resources.getColor(R.color.awful, theme),
                resources.getColor(R.color.bad, theme),
                resources.getColor(R.color.neutral, theme),
                resources.getColor(R.color.good, theme),
                resources.getColor(R.color.rad, theme)
            )
        }

        moodChart.data = BarData(dataSet)
        moodChart.xAxis.valueFormatter = if (showWeekly) {
            WeeklyAxisValueFormatter(lastDays)
        } else {
            DayAxisValueFormatter(lastDays)
        }

        // Configure Y-axis
        moodChart.axisLeft.apply {
            isEnabled = true
            setDrawGridLines(true)
            axisMinimum = 0f // Set minimum value
            axisMaximum = 5f // Set maximum value
            labelCount = 5 // Set number of labels
            granularity = 1f // Ensure grid lines are drawn at each integer value
        }

        // Right Y-axis settings (mirrored with the left Y-axis)
        moodChart.axisRight.apply {
            isEnabled = false // Disable right Y-axis
        }

        // X-axis configuration
        moodChart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            granularity = 1f // Ensure steps of 1 day
            setDrawGridLines(false)
        }

        moodChart.description.isEnabled = false // Disable the description label
        moodChart.invalidate() // Refresh the chart
    }

    class DayAxisValueFormatter(private val dates: List<String>) : ValueFormatter() {
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            return dates.getOrNull(value.toInt()) ?: ""
        }
    }

    class WeeklyAxisValueFormatter(private val dates: List<String>) : ValueFormatter() {
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            return if (value < dates.size) "Week $value" else "Avg"
        }
    }

    enum class Mood(val value: Int) {
        RAD(5),
        GOOD(4),
        MEH(3),
        BAD(2),
        AWFUL(1);

        companion object {
            fun fromString(mood: String): Mood {
                return when (mood.lowercase()) {
                    "rad" -> RAD
                    "good" -> GOOD
                    "meh" -> MEH
                    "bad" -> BAD
                    "awful" -> AWFUL
                    else -> MEH // Default to neutral mood if unknown
                }
            }
        }
    }
}
