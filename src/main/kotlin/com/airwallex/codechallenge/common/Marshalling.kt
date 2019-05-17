package com.airwallex.codechallenge.common

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

class Marshalling {
    companion object Factory {
        val mapper = jacksonObjectMapper()
            .registerModule(JavaTimeModule())
            .configure(WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, true)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)!!
    }
}