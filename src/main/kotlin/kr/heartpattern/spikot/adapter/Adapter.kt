package kr.heartpattern.spikot.adapter

import kr.heartpattern.spikot.module.IModule
import kr.heartpattern.spikot.plugin.FindAnnotation
import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.TYPE)
@FindAnnotation(impl = [IAdapter::class])
annotation class Adapter(
    val target: KClass<*> = Nothing::class
)

/**
 * Adapter implementation
 */
interface IAdapter : IModule