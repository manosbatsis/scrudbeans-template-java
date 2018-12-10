package mypackage;


import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import com.github.manosbatsis.scrudbeans.api.mdd.service.PersistableModelService;
import com.github.manosbatsis.scrudbeans.jpa.mdd.repository.ModelRepositoryFactoryBean;
import lombok.extern.slf4j.Slf4j;
import mypackage.model.OrderLine;
import mypackage.model.Product;
import mypackage.model.Order;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.format.Formatter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
// Remove security and error handling
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class, ErrorMvcAutoConfiguration.class})
// Enable transactions and auditing
@EnableTransactionManagement
@EnableJpaAuditing
// Scan for existing or runtime-generated (scrudbeans) components
@EntityScan({ScrudBeansSampleApplication.PACKAGE_NAME})
@EnableJpaRepositories(
		basePackages = {ScrudBeansSampleApplication.PACKAGE_NAME},
		repositoryFactoryBeanClass = ModelRepositoryFactoryBean.class
)
public class ScrudBeansSampleApplication {

	public static final String PACKAGE_NAME = "mypackage";

	public static void main(String[] args) {
		SpringApplication.run(ScrudBeansSampleApplication.class, args);
	}

	/**
	 * Register a formatter for parsing LocalDateTime
	 * @return
	 */
	@Bean
	public Formatter<LocalDateTime> localDateFormatter() {
		return new Formatter<LocalDateTime>() {
			@Override
			public LocalDateTime parse(String text, Locale locale) throws ParseException {
				return LocalDateTime.parse(text, DateTimeFormatter.ISO_DATE_TIME);
			}

			@Override
			public String print(LocalDateTime object, Locale locale) {
				return DateTimeFormatter.ISO_DATE_TIME.format(object);
			}
		};
	}

	/**
	 * Create sample products for demo purposes
	 */
	@Bean
	public CommandLineRunner demo(
			PersistableModelService<Order, String> orderService,
			PersistableModelService<OrderLine, String> orderLineService,
			PersistableModelService<Product, String> productService) {
		return (args) -> {
			// save a few products
			productService.create(new Product("Systemantics", "How Systems Work and Especially How They Fail", BigDecimal.valueOf(126.95)));
			productService.create(new Product("Design Patterns", "Elements of Reusable Object-Oriented Software", BigDecimal.valueOf(42.93)));
			productService.create(new Product("XML Topic Maps", "Creating and Using Topic Maps for the Web", BigDecimal.valueOf(3.44)));
			// fetch all customers
			// fetch all customers
			log.info("Customers found with findAll():");
			log.info("-------------------------------");

			Order order = orderService.create(Order.builder().email("foo@bar.baz").build());
			for (Product p : productService.findAll()) {
				OrderLine orderLine = OrderLine.builder()
						.order(order)
						.product(p)
						.quantity(2).build();
				orderLine = orderLineService.create(orderLine);
			}
			log.info("");

		};
	}
}

