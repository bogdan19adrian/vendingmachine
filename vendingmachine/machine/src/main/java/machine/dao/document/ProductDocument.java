package machine.dao.document;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("product")
public class ProductDocument {

    @Id
    private String id;
    private int amountAvailable;
    private int cost;
    private String productName;
    private String sellerId;
    private boolean addedInMachine;

}
