package com.sprintform.sprintform.util;

import com.sprintform.sprintform.exception.InvalidUUIDException;

import java.util.UUID;

public class UUIDValidator {

    public static boolean isValidUUID(String uuidStr) {
        try {
            UUID uuid = UUID.fromString(uuidStr);
            return true;
        } catch (Exception e) {
            throw new InvalidUUIDException("The UUID invalid " + uuidStr);
        }
    }

}
