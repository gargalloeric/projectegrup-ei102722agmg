package es.uji.ei1027.clubesportiu.validators;

import es.uji.ei1027.clubesportiu.model.Sdg;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class SdgValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Sdg.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Sdg s = (Sdg) target;

        if (s.getName().trim().isBlank()) {
            errors.rejectValue("name", "obligatory", "You must provide a name");
        }

        if (s.getNumber() <= 0) {
            errors.rejectValue("number", "ZeroOrNegative", "You must provide a number greater than 0");
        }

    }
}
