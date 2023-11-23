package es.uji.ei1027.clubesportiu.validators;

import es.uji.ei1027.clubesportiu.model.Action;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class ActionValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Action.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Action action = (Action) target;
        if (action.getName().trim().isBlank())
            errors.rejectValue("name", "obligatory", "You must provide a name.");

        if (action.getIdTarget() <= 0)
            errors.rejectValue("idSdg", "obligatory", "You must provide an associated SDG.");

        if (action.getStartDate() == null)
            errors.rejectValue("startDate", "empty", "You must provide a start date.");

        if (action.getEndDate() == null)
            errors.rejectValue("endDate", "empty", "You must provide an end date.");

        if ((action.getStartDate() != null && action.getEndDate() != null)) {

            if (action.getEndDate().isEqual(action.getStartDate()))
                errors.rejectValue("endDate", "invalid", "The end date cannot be the same as the start date.");

            if (action.getEndDate().isBefore(action.getStartDate()))
                errors.rejectValue("endDate", "invalid", "The end date cannot be the prior to the start date.");
        }

        if (action.getDescription().trim().isBlank())
            errors.rejectValue("description", "obligatory", "You must provide a description.");

    }
}
