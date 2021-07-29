package edu.kamshanski.tpuclassschedule.model.loader.responses

import kotlinx.datetime.Instant

class TimeReferenceResponse(val year: Int, val week: Int, val date: Instant) {
}