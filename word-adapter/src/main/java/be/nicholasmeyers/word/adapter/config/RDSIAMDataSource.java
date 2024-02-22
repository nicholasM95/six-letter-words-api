package be.nicholasmeyers.word.adapter.config;

import com.zaxxer.hikari.HikariDataSource;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.rds.RdsClient;
import software.amazon.awssdk.services.rds.RdsUtilities;
import software.amazon.awssdk.services.rds.model.GenerateAuthenticationTokenRequest;

import java.net.URI;

public class RDSIAMDataSource extends HikariDataSource {
    @Override
    public String getPassword() {
        RdsClient rdsClient = RdsClient.builder()
                .region(Region.of("eu-west-1"))
                .build();

        RdsUtilities rdsUtilities = rdsClient.utilities();
        URI jdbcUri = parseJdbcURL(getJdbcUrl());

        GenerateAuthenticationTokenRequest request = GenerateAuthenticationTokenRequest.builder()
                .username(getUsername())
                .hostname(jdbcUri.getHost())
                .port(jdbcUri.getPort())
                .build();

        return rdsUtilities.generateAuthenticationToken(request);
    }

    private URI parseJdbcURL(String jdbcUrl) {
        String uri = jdbcUrl.substring(5);
        return URI.create(uri);
    }
}
