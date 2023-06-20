package ru.clevertec.UserManager.common;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.springframework.stereotype.Component;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;

/**
 A component that initializes and configures WireMock for simulating HTTP-based APIs.
 */
@Component
public class WireMockInitializer {

    private static WireMockServer wireMockServer;

    /**
     Sets up and starts the WireMock server for Config Cloud.
     */
    public static void setup() {
        WireMockConfiguration wireMockConfig = WireMockConfiguration.options()
                .port(8086);

        wireMockServer = new WireMockServer(wireMockConfig);
        wireMockServer.start();

        wireMockServer.stubFor(get(urlEqualTo("/user-api/default"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("user-api.json")));
    }

    /**
     Stops the WireMock server.
     */
    public static void teardown() {
        wireMockServer.stop();
    }

}
