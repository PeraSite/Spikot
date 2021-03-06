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

package kr.heartpattern.spikot.persistence.storage.file

import kotlinx.serialization.*
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.nullable
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule

@OptIn(ExperimentalSerializationApi::class)
class SingleStringDecoder(
    value: String
) : Decoder {
    private var accessed = false
    private val value: String = value
        get() {
            if (accessed)
                throw java.lang.IllegalStateException("Single string decoder cannot decode ")
            return field
        }

    override val serializersModule: SerializersModule
        get() = EmptySerializersModule

    override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder {
        throw UnsupportedOperationException("Cannot use structure in single string decoder")
    }

    override fun decodeBoolean(): Boolean {
        return value.toBoolean()
    }

    override fun decodeByte(): Byte {
        return value.toByte()
    }

    override fun decodeChar(): Char {
        return value[0]
    }

    override fun decodeDouble(): Double {
        return value.toDouble()
    }

    override fun decodeEnum(enumDescriptor: SerialDescriptor): Int {
        return value.toInt()
    }

    override fun decodeFloat(): Float {
        return value.toFloat()
    }
    
    override fun decodeInline(inlineDescriptor: SerialDescriptor): Decoder {
        throw UnsupportedOperationException("Cannot decode not inline in single string decoder")
    }
    
    override fun decodeInt(): Int {
        return value.toInt()
    }

    override fun decodeLong(): Long {
        return value.toLong()
    }

    override fun decodeNotNullMark(): Boolean {
        throw UnsupportedOperationException("Cannot decode not null mark in single string decoder")
    }

    override fun decodeNull(): Nothing? {
        accessed = true
        return null
    }

    override fun decodeShort(): Short {
        return value.toShort()
    }

    override fun decodeString(): String {
        return value
    }

}
