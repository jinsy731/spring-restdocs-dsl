package sy.jin.springrestdocsdsl

import org.springframework.http.HttpMethod

class DocumentSpec(val documentName: String) {

    lateinit var requestLineSpec: RequestLineSpec
    var requestHeaderSpec: RequestHeaderSpec = RequestHeaderSpec()
    var requestBodySpec: RequestBodySpec = RequestBodySpec()
    var responseBodySpec: ResponseBodySpec = ResponseBodySpec()
    var requestPartSpec: RequestPartSpec = RequestPartSpec()

    val method: HttpMethod
        get() = requestLineSpec.method
    val url: String
        get() = requestLineSpec.url

    fun requestLine(method: HttpMethod, url: String, parameterDescriptorBlock: RequestLineSpec.() -> Unit) {
        this.requestLineSpec = RequestLineSpec(method, url).apply(parameterDescriptorBlock)
    }

    fun requestHeader(headerBlock: RequestHeaderSpec.() -> Unit) {
        this.requestHeaderSpec = RequestHeaderSpec().apply(headerBlock)
    }

    fun requestBody(bodyBlock: RequestBodySpec.() -> Unit) {
        this.requestBodySpec = RequestBodySpec().apply(bodyBlock)
    }

    fun responseBody(bodyBlock: ResponseBodySpec.() -> Unit) {
        this.responseBodySpec = ResponseBodySpec().apply(bodyBlock)
    }

    fun requestPart(partBlock: RequestPartSpec.() -> Unit) {
        this.requestPartSpec = RequestPartSpec().apply(partBlock)
    }
}
