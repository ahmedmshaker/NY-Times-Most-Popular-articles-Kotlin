package com.example.nytime.util

import com.example.nytime.util.ISO8601Converter
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.TimeZone

class CalendarISO8601Converter : ISO8601Converter {

    override fun fromTimestamp(timestamp: Long): String {
        val tz = TimeZone.getTimeZone("UTC")
        val calendar = Calendar.getInstance(tz)
        calendar.timeInMillis = timestamp

        // Quoted "Z" to indicate UTC, no timezone offset
        val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        df.timeZone = tz

        return df.format(calendar.time)
    }

    @Throws(ParseException::class) override fun toTimestamp(date: String): Long {
        val tz = TimeZone.getTimeZone("UTC")

        // Quoted "Z" to indicate UTC, no timezone offset
        val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        df.timeZone = tz
        return df.parse(date).time
    }
}