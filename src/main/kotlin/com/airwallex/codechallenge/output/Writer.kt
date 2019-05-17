package com.airwallex.codechallenge.output

import com.airwallex.codechallenge.common.Marshalling

class Writer {
    private val mapper = Marshalling.mapper

    fun write(obj: Any): String {
        return mapper.writeValueAsString(obj)
    }
}