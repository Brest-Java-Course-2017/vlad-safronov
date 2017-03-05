package com.vladsafronov.librarymanagement.service.impl.integration;

/**
 * Class for some commons validation methods
 */
public class Validation {

    public static void validateId(Integer id){
        if( id<=0) {
            throw new IllegalArgumentException(ServiceErrorMessages.ID_IS_NOT_IN_ACCEPTABLE_RANGE);
        }
    }
}
