package com.airwallex.codechallenge.output

import com.fasterxml.jackson.annotation.JsonProperty

enum class RateMoveAlertType {
    @JsonProperty("spotChange")
    SpotChange,
    @JsonProperty("failing")
    Failing,
    @JsonProperty("raising")
    Raising
}