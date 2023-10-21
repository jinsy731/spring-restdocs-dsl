package sy.jin.springrestdocsdsl

import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.operation.preprocess.Preprocessors.*
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions

fun ResultActions.andDocument(identifier: String, vararg snippetWrappers: SnippetWrapper): ResultActions {
    val enabledSnippets = snippetWrappers
        .filter { it.enabled }
        .map { it.snippet }
        .toTypedArray()

    return this.andDo(
        document(
            identifier,
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            *enabledSnippets
        )
    )
}

fun documentation(documentName: String, mockMvc: MockMvc, specCustomizer: DocumentSpec.() -> Unit) {
    val documentSpec = DocumentSpec(documentName).also { specCustomizer(it) }
    RestDocsEmitter().emit(mockMvc, documentSpec)
}
