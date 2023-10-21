package sy.jin.springrestdocsdsl

import org.springframework.http.HttpMethod
import org.springframework.restdocs.request.ParameterDescriptor
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.test.web.servlet.MockHttpServletRequestDsl

class RequestLineSpec(
    val method: HttpMethod,
    val url: String
) {
    val pathVariableDescriptors = mutableListOf<ParameterDescriptor>()
    val queryParameterDescriptors = mutableListOf<ParameterDescriptor>()

    inline fun <reified T> pathVariable(name: String, desc: String, sample: T, isOptional: Boolean) {
        pathVariableDescriptors.add(
            parameterWithName(name).buildDescriptor(desc, sample).apply {
                if (isOptional) this.optional()
            }
        )
    }
    inline fun <reified T> queryParam(name: String, desc: String, sample: T, isOptional: Boolean) {
        queryParameterDescriptors.add(
            parameterWithName(name).buildDescriptor(desc, sample).apply {
                if (isOptional) this.optional()
            }
        )
    }

    fun parsePathVariables(): Array<Any> {
        return pathVariableDescriptors.mapNotNull { it.attributes["example"] }.toTypedArray()
    }

    fun parseQueryParameters(): MockHttpServletRequestDsl.() -> Unit {
        return {
            this@RequestLineSpec.queryParameterDescriptors.forEach {
                param(it.name, it.attributes["example"]!!.toString())
            }
        }
    }
}
