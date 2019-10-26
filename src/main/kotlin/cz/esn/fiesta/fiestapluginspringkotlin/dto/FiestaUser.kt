package cz.esn.fiesta.fiestapluginspringkotlin.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty

data class FiestaUser(
        val firstname: String,
        val lastname: String,
        val gender: String,
        val email: String,
        val university: University,
        @JsonIgnore
        var token: String? = null
)

data class University(
        val name: String,
        val sectionShort: String,
        @field:JsonProperty("section_long")
        val sectionLong: String
)