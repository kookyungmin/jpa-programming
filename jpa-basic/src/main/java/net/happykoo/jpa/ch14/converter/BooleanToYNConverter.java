package net.happykoo.jpa.ch14.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

//모든 boolean 에 적용하고 싶으면, autoApply true
//@Converter(autoApply = true)
@Converter
public class BooleanToYNConverter implements AttributeConverter<Boolean, String> {
    @Override
    public String convertToDatabaseColumn(Boolean attribute) {
        return (attribute != null && attribute) ? "Y" : "N";
    }

    @Override
    public Boolean convertToEntityAttribute(String dbData) {
        return "Y".equals(dbData);
    }
}
