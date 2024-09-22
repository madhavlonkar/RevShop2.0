# RevShop 2.0 - E-commerce Application

This project was developed by a team of 5 developers. Due to security concerns, such as the exposure of API keys and secret keys during development, the entire codebase was pushed in a single commit to this repository. To maintain security, the repository is private.

If you need access to the repository or its commit history for reference, please email **madhavlonkar2@gmail.com**.

### Note:
The `application.properties` file is missing from the repository to avoid sharing sensitive information. However, the following keys are placeholders for configuration, and you will need to provide your own values:

```properties
spring.application.name=RevShopRevatureSpring
server.port=8081
spring.datasource.url=jdbc:mysql://localhost:3306/revshopdb
spring.datasource.username=root
spring.datasource.password=root

spring.jpa.hibernate.ddl-auto=update
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect

spring.mvc.view.prefix=/WEB-INF/
spring.mvc.view.suffix=.jsp

spring.jpa.show-sql=true

spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB

spring.mail.host=smtp-relay.sendinblue.com
spring.mail.port=587
spring.mail.username=
spring.mail.password=
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

spring.security.oauth2.client.registration.google.client-id=
spring.security.oauth2.client.registration.google.client-secret=
spring.security.oauth2.client.registration.google.scope=email, profile

logging.level.org.springframework.web.client.RestTemplate=DEBUG
