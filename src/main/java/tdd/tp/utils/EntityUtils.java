package tdd.tp.utils;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EntityUtils {

    public static List<Field> getFieldsFromName(List<String> fieldsName, Object object) throws NoSuchFieldException {
        List<Field> fields = new ArrayList<>();
        for (String fieldName : fieldsName)
            fields.add(object.getClass().getDeclaredField(fieldName));

        return fields;
    }

    public static void replaceValuesFromFieldName(List<Field> fields, Object objectToFill, Object objectThatFill) {
        try {
            for (Field fieldError : fields) {
                fieldError.setAccessible(true);
                Object valueFromWeb = fieldError.get(objectThatFill);
                fieldError.set(objectToFill, valueFromWeb);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static List<Field> containsEmptyFieldsOrNegativeInteger(Object objectToVerify) {
        List<Field> fields = Arrays.stream(objectToVerify.getClass().getDeclaredFields()).toList();
        List<Field> fieldsInError = new ArrayList<>();

        for (Field field : fields) {
            try {
                field.setAccessible(true);
                Object value = field.get(objectToVerify);
                if (field.getType().equals(int.class)) {
                    if (value == null || (int) value <= 0)
                        fieldsInError.add(field);
                }
                else if (field.getType().equals(String.class)) {
                    String vString = (String) value;
                    if (vString.isEmpty())
                        fieldsInError.add(field);
                }
                else if (field.getType().equals(LocalDate.class)) {
                    LocalDate vDate = (LocalDate) value;
                    if (vDate.equals(LocalDate.MAX) || vDate.equals(LocalDate.MIN))
                        fieldsInError.add(field);
                }
                else {
                    if (value == null)
                        fieldsInError.add(field);
                }
            }  catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return fieldsInError;
    }
}
