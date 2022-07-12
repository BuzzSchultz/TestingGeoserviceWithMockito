package ru.netology.i18n;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.netology.entity.Country;
import ru.netology.i18n.LocalizationServiceImpl;

import java.util.stream.Stream;

public class TestLocalizationServiceImpl {
    private static Stream<Arguments> enumAndStringParameters() {
        return Stream.of(
                Arguments.arguments(Country.RUSSIA, "Добро пожаловать"),
                Arguments.arguments(Country.USA, "Welcome"),
                Arguments.arguments(Country.BRAZIL, "Welcome"),
                Arguments.arguments(Country.GERMANY, "Welcome")
        );
    }

    @ParameterizedTest
    @MethodSource("enumAndStringParameters")
    public void testLocale(Country country, String expectedString) {
        LocalizationServiceImpl localizationService = new LocalizationServiceImpl();
        String actual = localizationService.locale(country);
        Assertions.assertEquals(expectedString, actual);
    }

}

