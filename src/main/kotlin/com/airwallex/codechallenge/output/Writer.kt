package com.airwallex.codechallenge.output

import com.airwallex.codechallenge.common.Marshalling
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter.FixedSpaceIndenter

class Writer {
    private val mapper = Marshalling.mapper

    fun write(obj: Any): String {
        return mapper
            .writer(DefaultPrettyPrinter().withObjectIndenter(FixedSpaceIndenter.instance))
            .writeValueAsString(obj)
    }
}