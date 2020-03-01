package myjavapackage.model;

import com.github.manosbatsis.scrudbeans.model.AbstractEmbeddableManyToManyIdentifier;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Sample composite identifier for manyToMany relationship entities.
 * @see AbstractEmbeddableManyToManyIdentifier
 */
@Embeddable
@Schema(name = "ProductRelationshipIdentifier",
        description = "A composite identifier used an ID in ProductRelationship entities")
public class ProductRelationshipIdentifier extends AbstractEmbeddableManyToManyIdentifier<Product, String, Product, String> {

	@Override
	public Product buildLeft(Serializable left) {
		Product product = new Product();
		product.setId(left.toString());
		return product;
	}

	@Override
	public Product buildRight(Serializable right) {
		Product product = new Product();
		product.setId(right.toString());
		return product;
	}

}
