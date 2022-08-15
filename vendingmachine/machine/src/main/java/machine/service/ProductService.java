package machine.service;


import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
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
        productRepository.save(productDocument);
        return modelMapper.map(productDocument, ProductDTO.class);
    }

    public ProductDTO updateProduct(ProductDTO request) {
        AtomicReference<ProductDTO> updated = new AtomicReference<>();
        Optional<ProductDocument> productDocument = productRepository.findById(request.getId());
        productDocument.ifPresentOrElse(doc -> {
                    final ProductDTO destination = modelMapper.map(doc, ProductDTO.class);
                    updated.set(mapForUpdateService.mapForUpdate(request, destination));
                    productRepository.save(modelMapper.map(updated, ProductDocument.class));
                },
                () -> {
                    log.error(Message.PRODUCT_NOT_FOUND.with(request.getId()));
                    throw new BadRequestException(Message.PRODUCT_NOT_FOUND.with(request.getId()));
                });

        return updated.get();
    }

    public void deleteProduct(String id){
        productRepository.deleteById(id);
    }

    public ProductDTO getProductById(String id){
        return modelMapper.map(productRepository.findById(id), ProductDTO.class);
    }
}
