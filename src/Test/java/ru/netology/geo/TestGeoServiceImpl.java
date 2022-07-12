package ru.netology.geo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoServiceImpl;

import java.util.stream.Stream;

public class TestGeoServiceImpl {
    private GeoServiceImpl geoService;

    private static Stream<Arguments> ipAndLocationParameters() {
        return Stream.of(
                Arguments.arguments("127.0.0.1",
                        new Location(null, null, null, 0)),
                Arguments.arguments("172.0.32.11",
                        new Location("Moscow", Country.RUSSIA, "Lenina", 15)),
                Arguments.arguments("96.44.183.149",
                        new Location("New York", Country.USA, " 10th Avenue", 32)),
                Arguments.arguments("172.и что-то еще:)",
                        new Location("Moscow", Country.RUSSIA, null, 0)),
                Arguments.arguments("96.и что-то еще:)",
                        new Location("New York", Country.USA, null, 0))
        );
    }

    @BeforeEach
    public void setUp() {
        geoService = new GeoServiceImpl();
    }

    @ParameterizedTest
    @MethodSource("ipAndLocationParameters")
    public void testByIp(String ip, Location expectedLocation) {
        Location actualLocation = geoService.byIp(ip);
        Assertions.assertEquals(expectedLocation.getCity(), actualLocation.getCity());
        Assertions.assertEquals(expectedLocation.getCountry(), actualLocation.getCountry());
        Assertions.assertEquals(expectedLocation.getStreet(), actualLocation.getStreet());
        Assertions.assertEquals(expectedLocation.getBuiling(), actualLocation.getBuiling());
    }

    @Test
    public void testByIp_OnNull() {
        String ip = "что-то непонятное:)";
        Location actualLocation = geoService.byIp(ip);
        Assertions.assertNull(actualLocation);
    }

    @Test
    public void testByCoordinates() {
        Assertions.assertThrowsExactly(RuntimeException.class,
                () -> geoService.byCoordinates(1, 2));
    }
}
