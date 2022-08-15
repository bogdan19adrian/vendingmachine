package machine.controller;

import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import machine.api.ProductDTO;
import machine.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/{apiVersion}/product", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class ProductController {

    private ProductService productService;

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody @Valid ProductDTO request, @PathVariable String apiVersion) {
        log.info("Create product request received with {} on API version {}", request, apiVersion);
        final ProductDTO productDTO = productService.createProduct(request);

        return new ResponseEntity<>(productDTO, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable("id") String id, @PathVariable String apiVersion) {
        log.info("Get  product request received with id {} on API version {}", id, apiVersion);
        return new ResponseEntity<>(productService.getProductById(id), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") String id, @PathVariable String apiVersion) {
        log.info("Delete product request received with id {} on API version {}", id, apiVersion);
        productService.deleteProduct(id);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable("id") String id,
            @RequestBody ProductDTO request,
            @PathVariable String apiVersion) {
        log.info("Update product request received with {} on API version {}", request, apiVersion);
        request.setId(id);
        return new ResponseEntity(productService.updateProduct(request), HttpStatus.OK);
    }


}
