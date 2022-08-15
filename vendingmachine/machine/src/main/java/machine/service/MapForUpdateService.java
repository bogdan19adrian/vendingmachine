package machine.service;

import machine.api.ProductDTO;
import org.springframework.stereotype.Service;

@Service
public class MapForUpdateService {

    public ProductDTO mapForUpdate(ProductDTO source, ProductDTO destination){
        if(source == null){
            return null;
        }
        if (source.getCost() !=null) {
            destination.setCost(source.getCost());
        }
        if(source.getProductName() != null){
            destination.setProductName(source.getProductName());
        }
        if(source.getAmount() != null) {
            destination.setAmountAvailable(destination.getAmountAvailable() + source.getAmount());
        }
        return destination;
    }
}
