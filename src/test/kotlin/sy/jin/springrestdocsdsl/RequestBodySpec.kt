package sy.jin.springrestdocsdsl

import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.snippet.Attributes
import org.springframework.restdocs.snippet.Attributes.AttributeBuilder
import org.springframework.restdocs.snippet.Attributes.key
import javax.management.Attribute

class RequestBodySpec {

    val fieldDescriptors = mutableListOf<FieldDescriptor>()

    inline fun <reified T> field(name: String, desc: String, sample: T) {
        fieldDescriptors.add(
            fieldWithPath(name).buildDescriptor(desc, sample)
        )
    }

    inline fun <reified T> optionalField(name: String, desc: String, sample: T) {
        fieldDescriptors.add(
            fieldWithPath(name).optional().buildDescriptor(desc, sample)
        )
    }
}
