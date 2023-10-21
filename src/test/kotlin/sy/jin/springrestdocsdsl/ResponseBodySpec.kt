package sy.jin.springrestdocsdsl

import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath

class ResponseBodySpec {
    val fieldDescriptors = mutableListOf<FieldDescriptor>()

    inline fun <reified T> field(name: String, desc: String, sample: T) {
        fieldDescriptors.add(
            fieldWithPath(name).buildDescriptor(desc, sample)
        )
    }
}
