package mypackage.test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import com.github.manosbatsis.scrudbeans.hypermedia.hateoas.ModelResource;
import com.github.manosbatsis.scrudbeans.hypermedia.hateoas.ModelResources;
import com.github.manosbatsis.scrudbeans.hypermedia.hateoas.PagedModelResources;
import com.github.manosbatsis.scrudbeans.test.AbstractRestAssueredIT;
import lombok.extern.slf4j.Slf4j;
import mypackage.ScrudBeansSampleApplication;
import mypackage.model.Order;
import mypackage.model.OrderLine;
import mypackage.model.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ScrudBeansSampleApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RestServicesIntegrationTest extends AbstractRestAssueredIT {


	@Test
	public void searchProducts_thenNoExceptions() {
		PagedProductResources products = given().log().all()
				.spec(defaultSpec())
				.get("/products")
				.then().log().all()
				.statusCode(200).extract().as(PagedProductResources.class);
	}


	@Test
	public void createAndUpdateOrder_thenNoExceptions() {
		// Get all products
		ProductResources products = given().log().all()
				.spec(defaultSpec())
				.queryParam("page", "no")
				.get("/products")
				.then().log().all()
				.statusCode(200).extract().as(ProductResources.class);

		// We have no auth mechanism so we'll
		// create and use an actual order as a basket
		Order order = Order.builder().email("foo@bar.baz").build();
		order = given().log().all()
				.spec(defaultSpec())
				.body(order)
				.post("/orders")
				.then().log().all()
				.statusCode(201).extract().as(Order.class);

		// Add order items (lines)
		for (ModelResource<Product> p : products.getContent()){

			OrderLine orderLine = OrderLine.builder()
					.order(order)
					.product(p.getContent())
					.quantity(2).build();
			given().log().all()
					.spec(defaultSpec())
					.body(orderLine)
					.post("/orderLines")
					.then().log().all()
					.statusCode(201).extract().as(OrderLine.class);
		}

		// Load a page of orders made today
		LocalDate localDate = LocalDate.now(ZoneId.systemDefault());
		LocalDateTime startOfDay = localDate.atStartOfDay();
		LocalDateTime endOfDay = localDate.atTime(LocalTime.MAX);

		PagedOrderResources ordersOfTheDay = given().log().all()
				.spec(defaultSpec())
				// rsql >= day-start and <= day-end
				.param("filter",
						"createdDate=ge=" + startOfDay + ";createdDate=le=" + endOfDay)
				.get("/orders")
				.then().log().all()
				.statusCode(200).extract().as(PagedOrderResources.class);

		// expecting 2 orders, one creatinjg on startup and one from this test
		assertEquals(2, ordersOfTheDay.getMetadata().getTotalElements());

	}

	// Help RestAssured's ObjectMapper
	public static class PagedProductResources extends PagedModelResources<Product>{	}
	public static class ProductResources extends ModelResources<Product> {	}
	public static class PagedOrderResources extends PagedModelResources<Order>{	}
	public static class OrderResources extends ModelResources<Order> {	}


}
