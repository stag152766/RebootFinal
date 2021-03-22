package com.bulletinboard.server.validations;


import com.bulletinboard.server.annotations.ValidPassword;
import com.bulletinboard.server.payload.request.SignupRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<ValidPassword, Object> {

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext constraintValidatorContext) {
        SignupRequest userSignupRequest = (SignupRequest) obj;
        return userSignupRequest.getPassword().equals(userSignupRequest.getConfirmPassword());
    }

    @Override
    public void initialize(ValidPassword constraintAnnotation) {

    }
}
