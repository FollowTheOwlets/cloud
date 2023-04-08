package ru.fedorichev.diplom;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.fedorichev.diplom.component.JwtRequest;
import ru.fedorichev.diplom.component.Login;
import ru.fedorichev.diplom.controller.AuthController;
import ru.fedorichev.diplom.entity.User;
import ru.fedorichev.diplom.repository.UserRepository;

@Testcontainers
@SpringBootTest()
@ContextConfiguration(initializers = DiplomApplicationTests.EnvInitializer.class)
class DiplomApplicationTests {
    @Autowired
    private UserRepository repository;

    @Autowired
    private AuthController authController;

    private static final PostgreSQLContainer<?> psql = new PostgreSQLContainer<>()
            .withUsername("postgres")
            .withPassword("root")
            .withExposedPorts(5432);

    static class EnvInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(@NotNull ConfigurableApplicationContext applicationContext) {
            TestPropertyValues.of(
                    String.format("spring.datasource.url=jdbc:postgresql://localhost:%d/postgres", psql.getFirstMappedPort()),
                    "spring.datasource.username=postgres",
                    "spring.datasource.password=root"
            ).applyTo(applicationContext);
        }
    }

    @BeforeAll
    public static void setUp() {
        psql.withReuse(true);
        psql.start();
    }

    @AfterAll
    public static void tearDown() {
        psql.stop();
    }

    @BeforeEach
    void init() {
        repository.save(
                User.builder()
                        .id(1L)
                        .login("test_user")
                        .password("test_password")
                        .build()
        );
    }

    @AfterEach
    void clear() {
        repository.deleteAll();
    }

    @DisplayName("JUnit test for testLoginController")
    @Test
    void testLoginController() {
        ResponseEntity<Login> login = authController.login(new JwtRequest("test_user","test_password"));
        Assertions.assertNotNull(login.getBody().getAuthToken());
    }
}
