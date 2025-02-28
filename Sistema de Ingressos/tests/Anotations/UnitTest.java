package Anotations;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD) // Só para métodos
@Retention(RetentionPolicy.RUNTIME) // Disponível em tempo de execução
@Test
@Tag("Unit test")
public @interface UnitTest {
}
