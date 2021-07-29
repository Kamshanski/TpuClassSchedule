package edu.kamshanski.tpuclassschedule.model.loader.converters

import retrofit2.Converter
import kotlin.reflect.KClass

@kotlin.annotation.Target(AnnotationTarget.FUNCTION)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class Converter(val converterClass: KClass<out Converter<*,*>>)
