import br.com.vertical.logistica.VerticalLogisticaApplication
import br.com.vertical.logistica.entity.Message
import br.com.vertical.logistica.service.OrderService
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@ExtendWith(SpringExtension::class)
@AutoConfigureMockMvc
@SpringBootTest(classes = [VerticalLogisticaApplication::class])
class OrderControllerIntegrationTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockBean
    private lateinit var orderService: OrderService

    @Test
    fun `inputData should return 201 Created with success message`() {
        val fileString = "Valid file data"
        val successMessage = Message("Arquivo processado inteiramente com sucesso!")

        `when`(orderService.processOrder(fileString)).thenReturn(emptyList())

        mockMvc.perform(MockMvcRequestBuilders.post("/api/orders")
            .content(fileString)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(successMessage)))
    }

    @Test
    fun `inputData should return 201 Created with partial success message`() {
        val fileString = "Valid file data"
        val partialSuccessMessage = Message("Arquivo processado parcialmente com sucesso." +
                "Verifique os logs para mais informações.")

        `when`(orderService.processOrder(fileString)).thenReturn(listOf("Error line 1", "Error line 2"))

        mockMvc.perform(MockMvcRequestBuilders.post("/api/orders")
            .content(fileString)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(partialSuccessMessage)))
    }

    @Test
    fun `inputData should return 400 Bad Request when file data is empty`() {
        val emptyFileString = ""

        mockMvc.perform(MockMvcRequestBuilders.post("/api/orders")
            .content(emptyFileString)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun `inputData should return 400 Bad Request when file data is null`() {
        val nullFileString: String? = null

        nullFileString?.let {
            MockMvcRequestBuilders.post("/api/orders")
                .content(it)
                .contentType(MediaType.APPLICATION_JSON)
        }?.let {
            mockMvc.perform(it)
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
        }
    }
}
