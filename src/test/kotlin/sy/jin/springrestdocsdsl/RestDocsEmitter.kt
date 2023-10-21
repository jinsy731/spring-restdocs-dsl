package sy.jin.springrestdocsdsl

import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.restdocs.request.ParameterDescriptor
import org.springframework.restdocs.request.RequestDocumentation.*
import org.springframework.restdocs.request.RequestPartDescriptor
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder

class RestDocsEmitter {
    fun emit(mockMvc: MockMvc, docSpec: DocumentSpec) {
        mockMvc.perform(resolveRequestBuilder(docSpec))
            .andDocument(
                docSpec.documentName,
                queryParametersSnippet(docSpec.requestLineSpec.queryParameterDescriptors),
                pathParametersSnippet(docSpec.requestLineSpec.pathVariableDescriptors),
                requestFieldsSnippet(docSpec.requestBodySpec.fieldDescriptors),
                responseFieldsSnippet(docSpec.responseBodySpec.fieldDescriptors),
                requestPartsSnippet(docSpec.requestPartSpec.requestPartDescriptors),
                *requestPartFieldSnippet(docSpec.requestPartSpec.requestPartBodySpecs)
            )
    }

    private fun resolveRequestBuilder(docSpec: DocumentSpec) : MockHttpServletRequestBuilder {
        val requestBuilder = when(docSpec.requestPartSpec.isMultipartFormRequest()) {
            true -> RestDocumentationRequestBuilders.multipart(docSpec.url, *(docSpec.requestLineSpec.parsePathVariables()))
                .putRequestParts(docSpec.requestPartSpec)

            false -> RestDocumentationRequestBuilders.request(docSpec.method, docSpec.url, *(docSpec.requestLineSpec.parsePathVariables()))
                .putJsonContent(docSpec.requestBodySpec)

        }

        return requestBuilder
            .putQueryParams(docSpec.requestLineSpec.queryParameterDescriptors)
    }

    private fun queryParametersSnippet(queryParameterDescriptors: List<ParameterDescriptor>) : SnippetWrapper {
        return SnippetWrapper(
            snippet = queryParameters(queryParameterDescriptors),
            enabled = queryParameterDescriptors.isNotEmpty()
        )
    }

    private fun pathParametersSnippet(pathParameterDescriptors: List<ParameterDescriptor>) : SnippetWrapper {
        return SnippetWrapper(
            snippet = pathParameters(pathParameterDescriptors),
            enabled = pathParameterDescriptors.isNotEmpty()
        )
    }

    private fun requestFieldsSnippet(requestFieldDescriptors: List<FieldDescriptor>) : SnippetWrapper {
        return SnippetWrapper(
            snippet = requestFields(requestFieldDescriptors),
            enabled = requestFieldDescriptors.isNotEmpty()
        )
    }

    private fun responseFieldsSnippet(responseFieldDescriptors: List<FieldDescriptor>) : SnippetWrapper {
        return SnippetWrapper(
            snippet = responseFields(responseFieldDescriptors),
            enabled = responseFieldDescriptors.isNotEmpty()
        )
    }

    private fun requestPartsSnippet(requestPartsDescriptors: List<RequestPartDescriptor>) : SnippetWrapper {
        return SnippetWrapper(
            snippet = requestParts(requestPartsDescriptors),
            enabled = requestPartsDescriptors.isNotEmpty()
        )
    }

    private fun requestPartFieldSnippet(requestPartBodySpecs: List<RequestPartBodySpec>) : Array<SnippetWrapper> {
        return requestPartBodySpecs.map {
            SnippetWrapper(
                snippet = requestPartFields(it.name, it.fieldDescriptors),
                enabled = it.fieldDescriptors.isNotEmpty()
            )
        }.toTypedArray()
    }
}
