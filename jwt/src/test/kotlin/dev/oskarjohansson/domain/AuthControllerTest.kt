package dev.oskarjohansson.domain


import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import dev.oskarjohansson.api.dto.PublicKeyResponseDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithAnonymousUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import kotlin.test.Test
import kotlin.test.assertNotNull

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest @Autowired constructor(
    val mockMvc: MockMvc
) {
    @Test
    @WithAnonymousUser
    fun `Test that public key controller returns public RSA key`(
    ) {

        val response = mockMvc.get("/public-key-controller/v1/public-key")
            .andExpect {
                status { isOk() }
                content() { contentType(MediaType.APPLICATION_JSON) }
            }.andReturn()

        val mapper = ObjectMapper().registerModule(KotlinModule())
        val result: PublicKeyResponseDTO = mapper.readValue(response.response.contentAsString)

        assertNotNull(result.publicKey)
    }

}



