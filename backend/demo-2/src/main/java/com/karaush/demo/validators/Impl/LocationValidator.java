package com.karaush.demo.validators.Impl;

import com.karaush.demo.validators.annotations.LocationConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashMap;

/**
 *  Validator to check correct latitude and longitude format
 */
public class LocationValidator implements ConstraintValidator<LocationConstraint, String> {

    private String mode;
    private static final HashMap<String, String> regexMap;

    /*
     * correct and incorrect cases introduced in tests
     */
    static {
        regexMap = new HashMap<>();
        regexMap.put("latitude", "([+-]?([0-8]?[0-9]|90)(\\.[0-9]*)?° )?(([0-5]?[0-9]|60)(\\.[0-9]*)?′ )?(([0-5]?[0-9]|60)(\\.[0-9]*)?″ ?)?[NS]?");
        regexMap.put("longitude", "([+-]?([0-9]{1,2}|1[0-7][0-9]|180)(\\.[0-9]*)?° )?(([0-5]?[0-9]|60)(\\.[0-9]*)?′ )?(([0-5]?[0-9]|60)(\\.[0-9]*)?″ )?[EW]?");
    }

    @Override
    public void initialize(LocationConstraint contactNumber) {
        mode = contactNumber.mode();
    }

    @Override
    public boolean isValid(String value,
                           ConstraintValidatorContext cxt) {
        boolean valid = value.matches(regexMap.get(mode));
        //-180 - 180
        boolean test = "qwe".matches("^[+-]?([0-9]{0,2}|1[0-7][0-9]|180)$");
        //-90 0 90
        boolean test1 = "qwe".matches("^[+-]?([0-8]?[0-9]|90)$");

        //0 - 60
        boolean test3 = "qwe".matches("^\\d+(\\.\\d+)?");

        boolean lng = "qwe".matches("([+-]?([0-9]{1,2}|1[0-7][0-9]|180)(\\.[0-9]*)?° )?(([0-5]?[0-9]|60)(\\.[0-9]*)?′ )?(([0-5]?[0-9]|60)(\\.[0-9]*)?″ )?[EW]?");
        boolean lat = "qwe".matches("([+-]?([0-8]?[0-9]|90)(\\.[0-9]*)?° )?(([0-5]?[0-9]|60)(\\.[0-9]*)?′ )?(([0-5]?[0-9]|60)(\\.[0-9]*)?″ ?)?[NS]?");
        return valid;

    }
}
