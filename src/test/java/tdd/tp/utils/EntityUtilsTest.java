package tdd.tp.utils;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import tdd.tp.entity.Book;
import tdd.tp.entity.Subscriber;
import tdd.tp.enums.GenderEnum;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EntityUtilsTest {

    @Test
    void canReplaceValuesFromObjectWithAnotherObject() {
        Subscriber s1 = Subscriber.builder()
                .code("2")
                .gender(GenderEnum.MALE)
                .name("Al")
                .firstName("Hal")
                .birthdate(LocalDate.now())
                .mail("oui")
                .build();

        Subscriber s1FromWeb = Subscriber.builder()
                .code("2")
                .gender(GenderEnum.MALE)
                .name("Francois")
                .firstName("Jean")
                .birthdate(LocalDate.MAX)
                .mail("oui")
                .build();

        EntityUtils.replaceValuesFromFieldName(Arrays.stream(Subscriber.class.getDeclaredFields()).toList(), s1, s1FromWeb);

        assertNotEquals(s1, s1FromWeb);
        assertEquals(s1.getName(), s1FromWeb.getName());
        assertEquals(s1.getBirthdate(), s1FromWeb.getBirthdate());
        assertEquals(s1.getMail(), s1FromWeb.getMail());
    }

    @Test
    void areFieldsRecognizedAsNull() {
        Book book1 = Book.builder()
                .author("J. R. R. Tolkien")
                .editor("Editor 1")
                .isbn("2080421204")
                .title("")
                .nbPage(-458)
                //.size(BookSizeEnum.POCHE)
                .isAvailable(true)
                .build();

        List<Field> fields = EntityUtils.containsEmptyFieldsOrNegativeInteger(book1);
        List<String> fieldsName = new ArrayList<>();
        fieldsName.add("title");
        fieldsName.add("nbPage");
        fieldsName.add("size");

        assertEquals(fieldsName, fields.stream().map(Field::getName).collect(Collectors.toList()));
    }

    @Test
    void returnFieldFromName() throws NoSuchFieldException {
        List<String> fieldsName = new ArrayList<>();
        fieldsName.add("title");
        fieldsName.add("nbPage");
        fieldsName.add("size");

        List <Field> fields = EntityUtils.getFieldsFromName(fieldsName, new Book());

        assertEquals(fieldsName.get(0), fields.stream().filter(f -> f.getName().equals(fieldsName.get(0))).findFirst().orElseThrow().getName());

        assertEquals(fieldsName.get(1), fields.stream().filter(f -> f.getName().equals(fieldsName.get(1))).findFirst().orElseThrow().getName());

        assertEquals(fieldsName.get(2), fields.stream().filter(f -> f.getName().equals(fieldsName.get(2))).findFirst().orElseThrow().getName());
    }

    @Test
    void cantReturnFieldFromBadName() {
        List<String> fieldsName = new ArrayList<>();
        fieldsName.add("title");
        fieldsName.add("nbPage");
        fieldsName.add("size");

        assertThrows(NoSuchFieldException.class, () -> EntityUtils.getFieldsFromName(fieldsName, new Subscriber()));
    }

}