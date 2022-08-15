package machine.service;


import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import machine.api.ProductDTO;
import machine.dao.document.ProductDocument;
import machine.dao.repository.ProductRepository;
import machine.exception.BadRequestException;
import machine.exception.BadRequestException.Message;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final MapForUpdateService mapForUpdateService;


    public ProductDTO createProduct(ProductDTO request) {
        final ProductDocument productDocument = modelMapper.map(request, ProductDocument.class);
        productDocument.setId(UUID.randomUUID().toString());
        productDocument.setAmountAvailable(request.getAmount());
        productRepository.save(productDocument);
        return modelMapper.map(productRepository.save(productDocument), ProductDTO.class);
    }

    public ProductDTO updateProduct(ProductDTO request) {
        Optional<ProductDocument> productDocument = productRepository.findById(request.getId());

        if (productDocument.isEmpty()) {
            log.error(Message.PRODUCT_NOT_FOUND.with(request.getId()));
            throw new BadRequestException(Message.PRODUCT_NOT_FOUND.with(request.getId()));
        }

        final ProductDTO destination = modelMapper.map(productDocument.get(), ProductDTO.class);

        return modelMapper.map(
                productRepository.save(
                        modelMapper.map(
                                mapForUpdateService.mapForUpdate(request, destination), ProductDocument.class)),
                ProductDTO.class);

    }

    public void deleteProduct(String id) {
        productRepository.deleteById(id);
    }

    public ProductDTO getProductById(String id) {
        Optional<ProductDocument> document = productRepository.findById(id);
        if (document.isEmpty()) {
            log.error(Message.PRODUCT_NOT_FOUND.with(id));
            throw new BadRequestException(Message.PRODUCT_NOT_FOUND.with(id));
        }
        return modelMapper.map(document.get(), ProductDTO.class);


    }
}
