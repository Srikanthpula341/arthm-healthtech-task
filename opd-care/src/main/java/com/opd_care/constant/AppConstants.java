package com.opd_care.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AppConstants {
    // Regex Patterns
    public static final String PHONE_REGEX = "^[0-9]{10}$";
    
    // ID Prefixes
    public static final String PATIENT_ID_PREFIX = "P";
    public static final String APPOINTMENT_ID_PREFIX = "A";
    public static final String CONSULTATION_ID_PREFIX = "C";
    public static final String USER_ID_PREFIX = "U";
    
    // Date/Time Constraints
    public static final int START_HOUR = 9;
    public static final int END_HOUR = 17;
    
    // Common Messages
    public static final String SUCCESS_PATIENT_REGISTERED = "Patient registered successfully";
    public static final String SUCCESS_APPOINTMENT_BOOKED = "Appointment booked successfully";
    public static final String SUCCESS_CONSULTATION_COMPLETED = "Consultation completed successfully";
}
