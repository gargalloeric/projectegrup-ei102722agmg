package es.uji.ei1027.clubesportiu.validators;

import es.uji.ei1027.clubesportiu.model.Initiative;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class InitiativeValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Initiative.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Initiative initiative = (Initiative) target;
        if (initiative.getName().trim().isBlank())
            errors.rejectValue("name", "obligatory", "You must provide a name.");

        if (initiative.getIdSdg() <= 0)
            errors.rejectValue("idSdg", "obligatory", "You must provide an associated SDG.");

        if (initiative.getStartDate() == null)
            errors.rejectValue("startDate", "empty", "You must provide a start date.");

        if (initiative.getEndDate() == null)
            errors.rejectValue("endDate", "empty", "You must provide an end date.");

        if ((initiative.getStartDate() != null && initiative.getEndDate() != null)) {

            if (initiative.getEndDate().isEqual(initiative.getStartDate()))
                errors.rejectValue("endDate", "invalid", "The end date cannot be the same as the start date.");

            if (initiative.getEndDate().isBefore(initiative.getStartDate()))
                errors.rejectValue("endDate", "invalid", "The end date cannot be the prior to the start date.");
        }

        if (initiative.getDescription().trim().isBlank())
            errors.rejectValue("description", "obligatory", "You must provide a description.");
    }
}
