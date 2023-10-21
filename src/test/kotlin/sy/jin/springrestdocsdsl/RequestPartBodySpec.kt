package sy.jin.springrestdocsdsl

import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.web.multipart.MultipartFile

class RequestPartBodySpec(val name: String, val sample: MultipartFile) {

    val fieldDescriptors: MutableList<FieldDescriptor> = mutableListOf()

    inline fun <reified T> field(name: String, desc: String, _sample: T) {
        fieldDescriptors.add(
            fieldWithPath(name).buildDescriptor(desc, _sample)
        )
    }
    inline fun <reified T> optionalField(name: String, desc: String, _sample: T) {
        fieldDescriptors.add(
            fieldWithPath(name).buildDescriptor(desc, _sample).optional()
        )
    }
}
