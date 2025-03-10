package shop.alten.shop;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasSize;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
// import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import shop.alten.shop.Product.exception.ProductNotFoundException;
import shop.alten.shop.Product.model.Product;
import shop.alten.shop.Product.service.ProductService;


@AutoConfigureMockMvc
@SpringBootTest
// @ActiveProfiles("test")
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService; // Mock the ProductService

    private final Product createdProduct = new Product(
        1L,                                  // id
        "P12345",                            // code
        "Smartphone",                        // name
        "Latest model with 128GB storage.",  // description
        "https://example.com/smartphone.jpg",// image URL
        "Electronics",                       // category
        599.99,                              // price
        50,                                  // quantity
        "REF-001",                         // internalReference
        123456L,                               // shellId
        Product.InventoryStatus.INSTOCK,     // inventoryStatus
        4.5,                                 // rating
        Instant.now(),                       // createdAt
        Instant.now()                        // updatedAt
    );

    private final String paylaod = "{"
        + "\"code\": \"P12345\","
        + "\"name\": \"Smartphone\","
        + "\"description\": \"Latest model with 128GB storage\","
        + "\"image\": \"https://example.com/sample-product.jpg\","
        + "\"category\": \"Electronics\","
        + "\"price\": 599.99,"
        + "\"quantity\": 50,"
        + "\"internalReference\": \"REF-001\","
        + "\"shellId\": 1234567,"
        + "\"inventoryStatus\": \"INSTOCK\","
        + "\"rating\": 4.5"
        + "}";

    @BeforeEach
    void setUp() {
        // Mock the return of the productService.createProduct() method because i haven't enable a test database
        when(productService.createProduct(any(Product.class))).thenReturn(createdProduct);

        // Mock the return of the productService.getAllProducts() method because i haven't enable a test database
        List<Product> productsList = new ArrayList<>();
        productsList.add(createdProduct); 
        productsList.add(createdProduct);
        when(productService.getAllProducts()).thenReturn(productsList);
    }

    @Test
    public void testCreateProduct_Success() throws Exception{
        // Given

        // When
        ResultActions response = this.mockMvc.perform(post("/products")
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .accept(MediaType.APPLICATION_JSON)
        .content(paylaod));
        
        // Then
        response.andExpect(status().isCreated())
        .andExpect(jsonPath("$.code").value(createdProduct.getCode()))
        .andExpect(jsonPath("$.name").value(createdProduct.getName()))
        .andExpect(jsonPath("$.shellId").value(createdProduct.getShellId())); 
    }


    @Test
    public void testCreateProduct_BadRequest() throws Exception {
        // Given
        final String wrong_payload = "{\"code\": \"P12345\"}";

        // When / Then
        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(wrong_payload))
                .andExpect(status().isBadRequest()); // 400 expected
    }

    @Test
    public void getAllProducts() throws Exception {
        // Given
        
        // when / then
        mockMvc.perform(get("/products"))
        .andExpect(status().isOk()) // 200 expected
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].code").value(createdProduct.getCode()))
        .andExpect(jsonPath("$[0].name").value(createdProduct.getName()))
        .andExpect(jsonPath("$[1].code").value(createdProduct.getCode()))
        .andExpect(jsonPath("$[1].name").value(createdProduct.getName()));
    }

    @Test
    public void testGetProductByCode_Success() throws Exception {
        // Given
        when(productService.getProductByCode(1L)).thenReturn(createdProduct);

        // When
        ResultActions response = mockMvc.perform(get("/products/1"));

        // Then
        verify(productService, times(1)).getProductByCode(1L);
        response.andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(createdProduct.getId()))
            .andExpect(jsonPath("$.code").value(createdProduct.getCode()))
            .andExpect(jsonPath("$.name").value(createdProduct.getName()))
            .andExpect(jsonPath("$.price").value(createdProduct.getPrice()));
    }

    @Test
    public void testGetProductByCode_NotFound() throws Exception {
        // Given
        when(productService.getProductByCode(1L)).thenThrow(new ProductNotFoundException(1L));

        // When / Then
        mockMvc.perform(get("/products/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Product with id 1 not found."));

    }

    @Test
    public void testGetProductByCode_InternalServerError() throws Exception {
        // Given
        when(productService.getProductByCode(1L)).thenThrow(new RuntimeException("Unexpected 1"));

        // When / Then
        mockMvc.perform(get("/products/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("An unexpected error occurred."));
    }

}