package com.cagatay.validator;

import com.cagatay.model.service.Result;

public interface Validator<T> {

    Result<T> validate(T argument);
}
