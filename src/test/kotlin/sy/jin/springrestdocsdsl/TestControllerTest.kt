package sy.jin.springrestdocsdsl

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.operation.preprocess.Preprocessors.*
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.restdocs.request.RequestDocumentation.*
import org.springframework.restdocs.snippet.Attributes.key
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import java.io.InputStream

@SpringBootTest
@ExtendWith(RestDocumentationExtension::class)
class TestControllerTest @Autowired constructor(private val objectMapper: ObjectMapper) {
    private lateinit var mockMvc: MockMvc
    @BeforeEach
    fun setUp(wac: WebApplicationContext, restDocumentation: RestDocumentationContextProvider) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
            .apply<DefaultMockMvcBuilder?>(documentationConfiguration(restDocumentation))
            .build()
    }
    @Test
    fun `RestDocs DSL`() {
        //given
        val testDTO = TestController.TestDTO(
            name = "name",
            email = "email",
            age = 20
        )

        //when
        documentation("test", mockMvc) {
            requestLine(HttpMethod.POST, "/test/{id}") {
                pathVariable("id", "ID", 1L, false)
                queryParam("param1", "PARAM1", "SAMPLE_PARAM", false)
                queryParam("param2", "PARAM2", 30, false)
            }
            requestBody {
                field("name", "이름", "JIN")
                field("email", "이메일", "jinsy731@gmail.com")
                field("age", "나이", 20)
            }
        }
        //then
    }

    @Test
    fun `RestDocs DSL Multipart Form Request Test`() {
        //given
        val testDTO = TestController.TestDTO(
            name = "name",
            email = "email",
            age = 20
        )
        val req = MockMultipartFile("req", "", MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsBytes(testDTO))
        val doc = MockMultipartFile("doc", "doc.html", MediaType.APPLICATION_OCTET_STREAM_VALUE, InputStream.nullInputStream())
        val file = MockMultipartFile("file", "file.pdf", MediaType.APPLICATION_OCTET_STREAM_VALUE, InputStream.nullInputStream())
        //when

        documentation("multipart", mockMvc) {
            requestLine(HttpMethod.POST, "/test/form/{id}") {
                pathVariable("id", "ID", 1L, false)
                queryParam("param", "param", "sample", false)
            }
            requestPart {
                file("doc", "Document", doc, listOf("html, pdf, docx"))
                file("file", "Files", file, listOf("exe, hwp, txt"))
                part("req", "Request JSON Body", req) {
                    field("name", "name", "sample name")
                    field("email", "email", "abc@example.com")
                    field("age", "age", 20)
                }
            }
        }
        //then
    }
}
