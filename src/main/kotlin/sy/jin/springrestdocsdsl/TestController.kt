package sy.jin.springrestdocsdsl

import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/test")
class TestController {

    @PostMapping("/{id}")
    fun test(@RequestBody req: TestDTO, @PathVariable id: Long, param1: String, param2: Int) {
        println("req = ${req}")
        println("id = ${id}")
        println("param1 = ${param1}")
        println("param2 = ${param2}")
    }

    @PostMapping("/form/{id}")
    fun formTest(
        @RequestPart("req", name = "req") req: TestDTO,
        @RequestPart("doc") doc: MultipartFile,
        @RequestPart("file") file: MultipartFile,
        @RequestParam("param") param: String,
        @PathVariable id: Long
        ) {
        println("req = ${req}")
        println("doc.name = ${doc.name}")
        println("file.name = ${file.name}")
        println("param1 = ${param}")
        println("id = ${id}")
    }
    data class TestDTO(
        val name: String,
        val email: String,
        val age: Int
    )
}
