package com.orm.util;

import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class SugarValidator {
    private Object parentObject;
    private List<Method> prePersistValidators = new ArrayList<Method>();
    private List<Method> postPersistValidators = new ArrayList<Method>();
    private List<Method> postLoadValidators = new ArrayList<Method>();
    private List<Method> postRemoveValidators = new ArrayList<Method>();
    private List<Method> preRemoveValidators = new ArrayList<Method>();
    private List<Method> preUpdateValidators = new ArrayList<Method>();
    private List<Method> postUpdateValidators = new ArrayList<Method>();

    public void setParentObject(Object parentObject) {
        this.parentObject = parentObject;
    }

    public void addPrePersistValidator(Method validator) {
        prePersistValidators.add(validator);
    }

    public void addPostPersistValidator(Method validator) {
        postPersistValidators.add(validator);
    }

    public void addPostLoadValidator(Method validator) {
        postLoadValidators.add(validator);
    }

    public void addPostRemoveValidator(Method validator) {
        postRemoveValidators.add(validator);
    }

    public void addPreRemoveValidator(Method validator) {
        preRemoveValidators.add(validator);
    }

    public void addPreUpdateValidator(Method validator) {
        preUpdateValidators.add(validator);
    }

    public void addPostUpdateValidator(Method validator) {
        postUpdateValidators.add(validator);
    }

    public boolean validatePrePersist(Object record) {
        return validate(prePersistValidators, record);
    }

    public void validatePostPersist(Object record) {
        validate(postPersistValidators, record);
    }

    public boolean validatePostLoad(Object record) {
        return validate(postLoadValidators, record);
    }

    public boolean validatePreRemove(Object record) {
        return validate(preRemoveValidators, record);
    }

    public void validatePostRemove(Object record) {
        validate(postRemoveValidators, record);
    }

    public boolean validatePreUpdate(Object record) {
        return validate(preUpdateValidators, record);
    }

    public void validatePostUpdate(Object record) {
        validate(postUpdateValidators, record);
    }

    public boolean validate(List<Method> validators, Object record) {
        for (Method validator : validators) {
            try {
                validator.invoke(parentObject, record);
            } catch (IllegalAccessException e) {
                Log.e("Sugar", "Could not access validation method '" + validator.getName() + "'. Please make sure it's accessible");
            } catch (InvocationTargetException e) {
                Log.i("Sugar", "Validator method '" + validator.getName() + "' threw an exception " + e.getTargetException().getMessage() + ". Assuming validation failure");
                return false;
            }
        }

        return true;
    }

}
