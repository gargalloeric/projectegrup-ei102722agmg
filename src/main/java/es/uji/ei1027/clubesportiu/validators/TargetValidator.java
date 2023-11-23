package es.uji.ei1027.clubesportiu.validators;

import es.uji.ei1027.clubesportiu.model.Target;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class TargetValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Target.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Target t = (Target) target;

        if (t.getNumber().trim().isBlank()) {
            errors.rejectValue("number", "obligatory","You must provide de number of the target");
        }

        if (t.getDescription().trim().isBlank()) {
            errors.rejectValue("description", "obligatory","You must provide a description");
        }
    }
}
