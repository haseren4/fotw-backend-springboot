package wis.fotabackend.domains;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Maps Java Boolean to database String values "True"/"False" to be compatible with existing data.
 */
@Converter(autoApply = false)
public class BooleanToStringConverter implements AttributeConverter<Boolean, String> {
    @Override
    public String convertToDatabaseColumn(Boolean attribute) {
        if (attribute == null) return null;
        return attribute ? "True" : "False";
    }

    @Override
    public Boolean convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        return "True".equalsIgnoreCase(dbData);
    }
}
