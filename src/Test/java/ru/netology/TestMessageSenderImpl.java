package ru.netology;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoService;
import ru.netology.i18n.LocalizationService;
import ru.netology.sender.MessageSenderImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class TestMessageSenderImpl {
    private static GeoService geoService;
    private static LocalizationService localizationService;
    private static MessageSenderImpl messageSender;
    private static Map<String, String> headers;
    private static String IP_ADDRESS_HEADER;

    @BeforeAll
    public static void setUp() {
        geoService = Mockito.mock(GeoService.class);
        localizationService = Mockito.mock(LocalizationService.class);
        messageSender = new MessageSenderImpl(geoService, localizationService);
        headers = new HashMap<>();
        IP_ADDRESS_HEADER = "x-real-ip";
    }

    private static Stream<Arguments> geoServiceAndLocalizationServiceParameters() {
        return Stream.of(
                Arguments.arguments("172.0.32.11", "Добро пожаловать"),
                Arguments.arguments("172.и что-то еще:)", "Добро пожаловать"),
                Arguments.arguments("96.44.183.149", "Welcome"),
                Arguments.arguments("96.и что-то еще:)", "Welcome")
        );
    }

    @ParameterizedTest
    @MethodSource("geoServiceAndLocalizationServiceParameters")
    public void testsSend(String ipAddress, String expectedSting) {
        Mockito.when(geoService.byIp("172.0.32.11"))
                .thenReturn(new Location("Moscow", Country.RUSSIA, "Lenina", 15));
        Mockito.when(localizationService.locale(Country.RUSSIA))
                .thenReturn("Добро пожаловать");
        Mockito.when(geoService.byIp("172.и что-то еще:)"))
                .thenReturn(new Location("Moscow", Country.RUSSIA, null, 0));
        Mockito.when(localizationService.locale(Country.RUSSIA))
                .thenReturn("Добро пожаловать");
        Mockito.when(geoService.byIp("96.44.183.149"))
                .thenReturn(new Location("New York", Country.USA, " 10th Avenue", 32));
        Mockito.when(localizationService.locale(Country.USA))
                .thenReturn("Welcome");
        Mockito.when(geoService.byIp("96.и что-то еще:)"))
                .thenReturn(new Location("New York", Country.USA, null, 0));
        Mockito.when(localizationService.locale(Country.USA))
                .thenReturn("Welcome");
        headers.put(IP_ADDRESS_HEADER, ipAddress);
        String actualString = messageSender.send(headers);
        Assertions.assertEquals(expectedSting, actualString);
    }
}
