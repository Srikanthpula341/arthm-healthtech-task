package com.opd_care.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ErrorConstants {
    // Validation Matrix Error Codes
    public static final String INVALID_NAME = "INVALID_NAME";
    public static final String INVALID_GENDER = "INVALID_GENDER";
    public static final String INVALID_AGE = "INVALID_AGE";
    public static final String INVALID_PHONE = "INVALID_PHONE";
    public static final String PHONE_EXISTS = "PHONE_EXISTS";
    public static final String INVALID_DATE = "INVALID_DATE";
    public static final String INVALID_TIME = "INVALID_TIME";
    public static final String INVALID_BP = "INVALID_BP";
    public static final String INVALID_TEMP = "INVALID_TEMP";
    public static final String INVALID_NOTES = "INVALID_NOTES";

    // Not Found Errors
    public static final String PATIENT_NOT_FOUND = "Patient not found with id: ";
    public static final String APPOINTMENT_NOT_FOUND = "Appointment not found with id: ";
    
    // Business Logic Errors
    public static final String PAST_DATE_BOOKING = "Cannot book appointment for past dates";
    public static final String DOUBLE_BOOKING = "Patient already has an appointment at this time";
    public static final String DOCTOR_DOUBLE_BOOKING = "Doctor already has an appointment at this time";
    public static final String CONSULTATION_ALREADY_EXISTS = "Consultation already exists for this appointment";
    public static final String UNEXPECTED_ERROR = "An unexpected error occurred";
}
