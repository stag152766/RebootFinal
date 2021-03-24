package com.bulletinboard.server.validations;


import com.bulletinboard.server.annotations.ValidPassword;
import com.bulletinboard.server.payload.request.SignupRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Класс для валидации пароля (на совпадение)
 */
public class PasswordValidator implements ConstraintValidator<ValidPassword, Object> {

    /**
     * Метод сравнивает пароль и подтверждение пароля,
     * которые вводит пользователь при регистрации
     * @param obj
     * @param constraintValidatorContext
     * @return
     */
    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext constraintValidatorContext) {
        SignupRequest userSignupRequest = (SignupRequest) obj;
        return userSignupRequest.getPassword().equals(userSignupRequest.getConfirmPassword());
    }

    @Override
    public void initialize(ValidPassword constraintAnnotation) {

    }
}
