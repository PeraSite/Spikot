/*
 * Copyright 2020 Spikot project authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package kr.heartpattern.spikot.serialization

import com.charleskorn.kaml.Yaml
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kr.heartpattern.spikot.module.AbstractModule
import kr.heartpattern.spikot.module.ModulePriority
import kr.heartpattern.spikot.module.ServerModule
import kr.heartpattern.spikot.plugin.FindAnnotation
import kr.heartpattern.spikot.plugin.SpikotPluginManager
import kr.heartpattern.spikot.utils.getInstance

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@FindAnnotation(impl = [SerializersModule::class])
annotation class SerializationModule

val serializationModule: SerializersModule
    get() = SerializationModuleRegistry.serializationModule

val jsonSerializer: Json
    get() = SerializationModuleRegistry.jsonSerializer

val yamlSerializer: Yaml
    get() = SerializationModuleRegistry.yamlSerializer

@ServerModule(priority = ModulePriority.SYSTEM)
object SerializationModuleRegistry : AbstractModule() {
    lateinit var serializationModule: SerializersModule
        private set
    lateinit var jsonSerializer: Json
        private set
    lateinit var yamlSerializer: Yaml
        private set

    override fun onEnable() {
        serializationModule = SerializersModule {
            SpikotPluginManager.forEachAnnotation<SerializationModule> { (type, _, _) ->
                include(type.getInstance() as SerializersModule)
            }
        }
        jsonSerializer = Json {
            serializersModule = this@SerializationModuleRegistry.serializationModule
        }
        yamlSerializer = Yaml(serializationModule)
    }
}