package sy.jin.springrestdocsdsl

import org.springframework.restdocs.headers.HeaderDescriptor
import org.springframework.restdocs.headers.HeaderDocumentation.headerWithName

class RequestHeaderSpec {
    val headerDescriptor = mutableListOf<HeaderDescriptor>()

    inline fun <reified T> header(name: String, desc: String, sample: T) {
        headerDescriptor.add(headerWithName(name).buildDescriptor(desc, sample))
    }
    inline fun <reified T> optionalHeader(name: String, desc: String, sample: T) {
        headerDescriptor.add(headerWithName(name).optional().buildDescriptor(desc, sample))
    }
}
