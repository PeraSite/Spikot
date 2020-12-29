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

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.CompositeEncoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule

class SingleStringEncoder : Encoder {
    var encoded: String? = null
        get() {
            if (field == null) {
                throw IllegalStateException("Single string encoder encode nothing.")
            }
            return field
        }
        set(value) {
            if (field != null) {
                throw IllegalStateException("Single string encoder accept only one encoding.")
            }
            field = value
        }

    override val serializersModule: SerializersModule
        get() = EmptySerializersModule

    override fun beginStructure(descriptor: SerialDescriptor): CompositeEncoder {
        throw UnsupportedOperationException("Cannot use structure in single string encoder")
    }

    override fun encodeBoolean(value: Boolean) {
        encoded = value.toString()
    }

    override fun encodeByte(value: Byte) {
        encoded = value.toString()
    }

    override fun encodeChar(value: Char) {
        encoded = value.toString()
    }

    override fun encodeDouble(value: Double) {
        encoded = value.toString()
    }

    override fun encodeEnum(enumDescriptor: SerialDescriptor, index: Int) {
        encoded = index.toString()
    }

    override fun encodeFloat(value: Float) {
        encoded = value.toString()
    }

    override fun encodeInt(value: Int) {
        encoded = value.toString()
    }

    override fun encodeLong(value: Long) {
        encoded = value.toString()
    }

    override fun encodeNotNullMark() {
        throw UnsupportedOperationException("Cannot mark not null in single string encoder")
    }

    override fun encodeNull() {
        encoded = ""
    }

    override fun encodeShort(value: Short) {
        encoded = value.toString()
    }

    override fun encodeString(value: String) {
        encoded = value
    }
}
