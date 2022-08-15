package machine.controller;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import machine.api.ProductDTO;
import machine.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/{api_version}/product")
@Slf4j
@RequiredArgsConstructor
public class ProductController {

    private ProductService productService;

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody @Valid ProductDTO request) {
        log.info("Create product request received with with {}", request);
        final ProductDTO productDTO =  productService.createProduct(request);
        return new ResponseEntity<>(productDTO, HttpStatus.CREATED);

    }

}
