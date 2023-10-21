package sy.jin.springrestdocsdsl

import org.springframework.restdocs.request.RequestDocumentation.partWithName
import org.springframework.restdocs.request.RequestPartDescriptor
import org.springframework.web.multipart.MultipartFile

class RequestPartSpec {

    val requestPartDescriptors: MutableList<RequestPartDescriptor> = mutableListOf()
    val requestPartBodySpecs: MutableList<RequestPartBodySpec> = mutableListOf()

    fun file(name: String, desc: String, sample: MultipartFile, allowedFileExtensions: List<String>) {
        requestPartDescriptors.add(
            partWithName(name).buildDescriptor(desc, sample, allowedFileExtensions)
        )
    }
    fun optionalFile(name: String, desc: String, sample: MultipartFile, allowedFileExtensions: List<String>) {
        requestPartDescriptors.add(
            partWithName(name).buildDescriptor(desc, sample, allowedFileExtensions).optional()
        )
    }

    fun part(name: String, desc: String, sample: MultipartFile, requestPartBodyBlock: RequestPartBodySpec.() -> Unit) {
        requestPartDescriptors.add(
            partWithName(name).buildDescriptor(desc, sample, listOf())
        )
        requestPartBodySpecs.add(
            RequestPartBodySpec(name, sample).apply(requestPartBodyBlock)
        )
    }

    fun isMultipartFormRequest() : Boolean {
        return (requestPartBodySpecs.isNotEmpty() || requestPartDescriptors.isNotEmpty())
    }
}
