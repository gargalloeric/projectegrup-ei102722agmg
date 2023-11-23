package es.uji.ei1027.clubesportiu.validators;

import es.uji.ei1027.clubesportiu.model.UserLogin;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class LoginValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return UserLogin.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserLogin userLogin = (UserLogin) target;

        if (userLogin.getEmail().trim().isBlank()) {
            errors.rejectValue("email", "obligatory", "You must provide a valid email.");
        }

        if (userLogin.getPassword().trim().isBlank()) {
            errors.rejectValue("password", "obligatory", "You must provide a password.");
        }
    }
}
