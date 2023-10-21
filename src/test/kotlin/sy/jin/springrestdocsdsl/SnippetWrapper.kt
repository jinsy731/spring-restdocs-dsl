package sy.jin.springrestdocsdsl

import org.springframework.restdocs.snippet.Snippet

class SnippetWrapper(
    val snippet: Snippet,
    val enabled: Boolean
)
