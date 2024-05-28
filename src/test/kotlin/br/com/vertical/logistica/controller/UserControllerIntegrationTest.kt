import br.com.vertical.logistica.VerticalLogisticaApplication
import br.com.vertical.logistica.dto.OrderDTO
import br.com.vertical.logistica.dto.ProductDTO
import br.com.vertical.logistica.dto.UserDTO
import br.com.vertical.logistica.service.UserService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [VerticalLogisticaApplication::class])
@AutoConfigureMockMvc
class UserControllerIntegrationTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var userService: UserService

    @Test
    fun `getAllOrdersByUsers should return list of users with orders when parameters are valid`() {
        val initDate = "2023-01-01"
        val finishDate = "2023-12-31"
        val usersWithOrders = listOf(UserDTO(1, "John Doe", listOf(OrderDTO(1, "100.00", "2023-01-01", listOf(ProductDTO(1, "50.00"))))))

        `when`(userService.areDatesValid(initDate, finishDate)).thenReturn(true)
        `when`(userService.getAllUsersWithOrders(initDate, finishDate)).thenReturn(usersWithOrders)

        mockMvc.perform(get("/users/orders")
            .param("init_date", initDate)
            .param("finish_date", finishDate)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.size()").value(usersWithOrders.size))
            .andExpect(jsonPath("$[0].name").value("John Doe"))
    }

    @Test
    fun `getUserByOrderId should return user by order ID`() {
        val orderId = 1L
        val user = UserDTO(1, "John Doe", listOf(OrderDTO(1, "100.00", "2023-01-01", listOf(ProductDTO(1, "50.00")))))

        `when`(userService.getByOrderId(orderId)).thenReturn(user)

        mockMvc.perform(get("/users/orders/{orderId}", orderId)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.user_id").value(1))
            .andExpect(jsonPath("$.name").value("John Doe"))
    }
}
