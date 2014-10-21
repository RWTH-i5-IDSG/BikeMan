package de.rwth.idsg.bikeman.other;

import de.rwth.idsg.bikeman.web.rest.dto.modify.CreateEditAddressDTO;
import de.rwth.idsg.bikeman.web.rest.dto.modify.CreateEditCustomerDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.joda.time.LocalDate;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 21.10.2014
 */
@Slf4j
public class CardPinTest {

    private static Validator validator;
    private Set<ConstraintViolation<CreateEditCustomerDTO>> violations;

    private static CreateEditCustomerDTO dto;

    private static final List<String> allowedPins =  Arrays.asList
            ("1234", "9801", "0001", "0000", "0098");

    private static final List<String> violatingPins =  Arrays.asList
            (null, "", "  ", " 1 ", "1", "12", "123", "12345", "123456", "89UZI", "ZUUIIO", "?=)(/&=(");

    /**
     * Values are not important, as long as they don't violate
     * other constraints of CreateEditCustomerDTO
     */
    @BeforeClass
    public static void initClass() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();

        CreateEditAddressDTO add = new CreateEditAddressDTO();
        add.setStreetAndHousenumber(RandomStringUtils.randomAlphanumeric(8));
        add.setCity(RandomStringUtils.randomAlphabetic(8));
        add.setCountry(RandomStringUtils.randomAlphabetic(8));
        add.setZip(RandomStringUtils.randomAlphabetic(4));

        dto = new CreateEditCustomerDTO();
        dto.setLogin("sksk@gmail.com");
        dto.setCustomerId("asdfg");
        dto.setFirstname("tzu");
        dto.setCardId("cuus90");
        dto.setLastname("ooo");
        dto.setAddress(add);
        dto.setBirthday(new LocalDate().minusDays(2));
    }

    @Test
    public void testAllowedPins() {
        for (String pin : allowedPins) {
            log.info("Testing pin: '{}'", pin);
            dto.setCardPin(pin);

            violations = validator.validate(dto);
            assertTrue(violations.isEmpty());
        }
    }

    @Test
    public void testViolatingPins() {
        for (String pin : violatingPins) {
            log.info("Testing pin: '{}'", pin);
            dto.setCardPin(pin);

            violations = validator.validate(dto);
            if (!violations.isEmpty()) {
                for (ConstraintViolation<CreateEditCustomerDTO> v : violations) {
                    log.debug("[As expected] Invalid value: {}, {} ", v.getInvalidValue(), v);
                }
            }
            assertFalse(violations.isEmpty());
        }
    }
}