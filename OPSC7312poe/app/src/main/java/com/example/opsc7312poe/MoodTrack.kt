package com.example.opsc7312poe

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
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
        timeFrameSpinner = findViewById(R.id.time_frame_spinner)
        loadingIndicator = findViewById(R.id.loading_indicator)

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
        val database = FirebaseDatabase.getInstance()
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
            isEnabled = true
            setDrawGridLines(true)
            axisMinimum = 0f
            axisMaximum = 5f
            labelCount = 5
            granularity = 1f // Ensure grid lines are drawn at each integer value
        }

        // Configure X-axis
        moodChart.xAxis.apply {
            isEnabled = true
            setDrawGridLines(false)
            position = XAxis.XAxisPosition.BOTTOM
            labelCount = lastDays.size // Set to match the number of days displayed
            granularity = 1f // Ensure each label corresponds to one day
        }

        moodChart.invalidate() // Refresh the chart
    }

    inner class DayAxisValueFormatter(private val days: List<String>) : ValueFormatter() {
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            val index = value.toInt()
            return if (index in days.indices) {
                val date = SimpleDateFormat("EEE", Locale.getDefault()).format(
                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(days[index])!!
                )
                date.first().uppercase() // Return the first letter of the day (e.g., "M", "T", "W", ...)"
            } else {
                ""
            }
        }
    }

    inner class WeeklyAxisValueFormatter(private val days: List<String>) : ValueFormatter() {
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            val index = value.toInt()
            return if (index in days.indices) {
                val startDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(days[index])!!
                val calendar = Calendar.getInstance().apply { time = startDate }
                calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek) // Set to the first day of the week
                SimpleDateFormat("MM/dd", Locale.getDefault()).format(calendar.time) // Return the formatted date
            } else {
                ""
            }
        }
    }

    // Mood class to represent mood values
    enum class Mood(val value: Int) {
        AWFUL(1),
        BAD(2),
        NEUTRAL(3),
        GOOD(4),
        RAD(5);

        companion object {
            fun fromString(moodString: String): Mood {
                return when (moodString.lowercase(Locale.getDefault())) {
                    "awful" -> AWFUL
                    "bad" -> BAD
                    "neutral" -> NEUTRAL
                    "good" -> GOOD
                    "rad" -> RAD
                    else -> NEUTRAL // Default to neutral
                }
            }
        }
    }
}