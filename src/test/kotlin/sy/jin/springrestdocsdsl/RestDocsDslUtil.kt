package sy.jin.springrestdocsdsl

import org.json.JSONObject
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.restdocs.headers.HeaderDescriptor
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.request.ParameterDescriptor
import org.springframework.restdocs.request.RequestPartDescriptor
import org.springframework.restdocs.snippet.Attributes
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder
import sy.jin.springrestdocsdsl.AttributeNames.FILE_EXTENSION_ALLOWANCES
import java.time.format.DateTimeFormatter
import java.time.temporal.Temporal

inline fun <reified T> FieldDescriptor.buildDescriptor(desc: String, sample: T) : FieldDescriptor {
    return this.description(desc)
        .attributes(
            Attributes.Attribute(AttributeNames.TYPE, T::class.simpleName),
            formatFrom(T::class.java),
            Attributes.Attribute(AttributeNames.EXAMPLE, sample),
        )
}

inline fun <reified T> ParameterDescriptor.buildDescriptor(desc: String, sample: T) : ParameterDescriptor {
    return this.description(desc)
        .attributes(
            Attributes.Attribute(AttributeNames.TYPE, T::class.simpleName),
            formatFrom(T::class.java),
            Attributes.Attribute(AttributeNames.EXAMPLE, sample),
        )
}
inline fun <reified T> HeaderDescriptor.buildDescriptor(desc: String, sample: T) : HeaderDescriptor {
    return this.description(desc)
        .attributes(
            Attributes.Attribute(AttributeNames.TYPE, T::class.simpleName),
            formatFrom(T::class.java),
            Attributes.Attribute(AttributeNames.EXAMPLE, sample),
        )
}

inline fun <reified T> RequestPartDescriptor.buildDescriptor(desc: String, sample: T, allowedFileExtensions: List<String>) : RequestPartDescriptor {
    return this.description(desc)
        .attributes(
            Attributes.Attribute(FILE_EXTENSION_ALLOWANCES, allowedFileExtensions),
            Attributes.Attribute(AttributeNames.EXAMPLE, sample)
        )
}

fun formatFrom(sampleClass: Class<*>): Attributes.Attribute {
    if (sampleClass.isEnum) {
        return Attributes.Attribute(AttributeNames.FORMAT, "<${sampleClass.simpleName}> ${sampleClass.enumConstants}")
    }
    if (Temporal::class.java.isAssignableFrom(sampleClass)) {
        return Attributes.Attribute(AttributeNames.FORMAT, "${DateTimeFormatter.ISO_DATE_TIME}")
    }

    return emptyFormat()
}

fun emptyFormat(): Attributes.Attribute {
    return Attributes.Attribute(AttributeNames.FORMAT, "")
}

fun MockHttpServletRequestBuilder.putQueryParams(queryParameterDescriptors: List<ParameterDescriptor>): MockHttpServletRequestBuilder {
    queryParameterDescriptors.forEach {
        this.queryParam(it.name, it.attributes[AttributeNames.EXAMPLE]!!.toString())
    }

    return this
}

fun MockHttpServletRequestBuilder.putJsonContent(requestBodySpec: RequestBodySpec?): MockHttpServletRequestBuilder {
    requestBodySpec?: return this

    val jsonObject = JSONObject()
    requestBodySpec.fieldDescriptors.forEach {
        val sample = it.attributes[AttributeNames.EXAMPLE]
        jsonObject.put(it.path, sample)
    }

    this.content(jsonObject.toString())
    this.contentType(MediaType.APPLICATION_JSON)

    return this
}

fun MockMultipartHttpServletRequestBuilder.putRequestParts(requestPartSpec: RequestPartSpec): MockMultipartHttpServletRequestBuilder {
    requestPartSpec.requestPartDescriptors.forEach {
        this.file(it.attributes[AttributeNames.EXAMPLE] as MockMultipartFile)
    }
    requestPartSpec.requestPartBodySpecs.forEach {
        this.file(it.sample as MockMultipartFile)
    }

    return this
}
