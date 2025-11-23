package io.github.roilin.crossplatform_updater.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT")
public class OpenApiConfig {

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("Cross Platform Updater API")
            .version("1.0.0")
            .description("API for managing applications, versions and user devices")
            .contact(new Contact()
                .name("Support")
                .email("support@example.com"))
            .license(new License()
                .name("Apache 2.0")
                .url("http://springdoc.org")))
        .components(new Components()
            .addSchemas("ApplicationRequest", createApplicationRequestSchema())
            .addSchemas("ApplicationResponse", createApplicationResponseSchema())
            .addSchemas("AppVersionRequest", createAppVersionRequestSchema())
            .addSchemas("AppVersionResponse", createAppVersionResponseSchema())
            .addSchemas("UserDeviceRequest", createUserDeviceRequestSchema())
            .addSchemas("UserDeviceResponse", createUserDeviceResponseSchema())
            .addSchemas("LoginRequest", createLoginRequestSchema())
            .addSchemas("LoginResponse", createLoginResponseSchema())
            .addSchemas("ChangePasswordRequest", createChangePasswordRequestSchema())
            .addSchemas("UserLoggedDto", createUserLoggedDtoSchema())
            .addSchemas("Pageable", createPageableSchema()))
        .paths(createPaths())
        .tags(Arrays.asList(
            new Tag().name("Application Management").description("APIs for managing applications"),
            new Tag().name("Version Management").description("APIs for managing application versions"),
            new Tag().name("Device Management").description("APIs for managing user devices"),
            new Tag().name("Authentication").description("APIs for authentication and user management")));
  }

  private Schema createApplicationRequestSchema() {
    return new ObjectSchema()
        .addProperty("title", new StringSchema().description("Application title").example("My App"))
        .addProperty("type", new StringSchema().description("Application type").example("Game"))
        .addProperty("supportedPlatform", new ArraySchema()
            .items(new StringSchema()._enum(Arrays.asList("IOS", "ANDROID", "WINDOWS", "MACOS", "LINUX")))
            .description("Supported platforms"))
        .addProperty("developer", new StringSchema().description("Developer").example("Developer Inc"))
        .addProperty("publisher", new StringSchema().description("Publisher").example("Publisher LLC"));
  }

  private Schema createApplicationResponseSchema() {
    return new ObjectSchema()
        .addProperty("id", new StringSchema().description("Application ID").example("1"))
        .addProperty("title", new StringSchema().description("Application title").example("My App"))
        .addProperty("type", new StringSchema().description("Application type").example("Game"))
        .addProperty("supportedPlatform", new ArraySchema()
            .items(new StringSchema()._enum(Arrays.asList("IOS", "ANDROID", "WINDOWS", "MACOS", "LINUX")))
            .description("Supported platforms"))
        .addProperty("developer", new StringSchema().description("Developer").example("Developer Inc"))
        .addProperty("publisher", new StringSchema().description("Publisher").example("Publisher LLC"))
        .addProperty("lastUpdate", new StringSchema().description("Last update date").example("2024-01-15T10:30:00"))
        .addProperty("releaseDate", new StringSchema().description("Release date").example("2024-01-01T00:00:00"));
  }

  private Schema createAppVersionRequestSchema() {
    return new ObjectSchema()
        .addProperty("version", new StringSchema().description("Version").example("1.0.1"))
        .addProperty("platform", new StringSchema().description("Platform").example("IOS"))
        .addProperty("releaseDate", new StringSchema().description("Release date").example("2024-01-15T10:30:00"))
        .addProperty("changeLog", new StringSchema().description("Change log").example("New features added"))
        .addProperty("active", new StringSchema().description("Is active").example("true"))
        .addProperty("updateType", new StringSchema().description("Update type").example("MANDATORY"));
  }

  private Schema createAppVersionResponseSchema() {
    return new ObjectSchema()
        .addProperty("id", new StringSchema().description("Version ID").example("1"))
        .addProperty("version", new StringSchema().description("Version").example("1.0.1"))
        .addProperty("platform", new StringSchema().description("Platform").example("IOS"))
        .addProperty("releaseDate", new StringSchema().description("Release date").example("2024-01-15T10:30:00"))
        .addProperty("application", new ObjectSchema().description("Application"))
        .addProperty("changeLog", new StringSchema().description("Change log").example("New features added"))
        .addProperty("active", new StringSchema().description("Is active").example("true"))
        .addProperty("updateType", new StringSchema().description("Update type").example("MANDATORY"));
  }

  private Schema createUserDeviceRequestSchema() {
    return new ObjectSchema()
        .addProperty("name", new StringSchema().description("Device name").example("My iPhone"))
        .addProperty("platform", new StringSchema().description("Platform").example("IOS"));
  }

  private Schema createUserDeviceResponseSchema() {
    return new ObjectSchema()
        .addProperty("id", new StringSchema().description("Device ID").example("1"))
        .addProperty("name", new StringSchema().description("Device name").example("My iPhone"))
        .addProperty("platform", new StringSchema().description("Platform").example("IOS"))
        .addProperty("owner", new StringSchema().description("Owner").example("user123"))
        .addProperty("installedApps",
            new ArraySchema().items(new ObjectSchema()).description("Installed applications"));
  }

  private Schema createLoginRequestSchema() {
    return new ObjectSchema()
        .addProperty("username", new StringSchema().description("Username").example("admin"))
        .addProperty("password", new StringSchema().description("Password").example("12345"));
  }

  private Schema createLoginResponseSchema() {
    return new ObjectSchema()
        .addProperty("isLogged", new StringSchema().description("Login status").example("true"))
        .addProperty("username", new StringSchema().description("Username").example("admin"))
        .addProperty("role", new StringSchema().description("Role").example("ADMIN"));
  }

  private Schema createChangePasswordRequestSchema() {
    return new ObjectSchema()
        .addProperty("currentPassword", new StringSchema().description("Current password"))
        .addProperty("newPassword", new StringSchema().description("New password"));
  }

  private Schema createUserLoggedDtoSchema() {
    return new ObjectSchema()
        .addProperty("username", new StringSchema().description("Username"))
        .addProperty("role", new StringSchema().description("Role"))
        .addProperty("permissions", new ArraySchema().items(new StringSchema()).description("Permissions"));
  }

  private Schema createPageableSchema() {
    return new ObjectSchema()
        .addProperty("page", new StringSchema().description("Page number"))
        .addProperty("size", new StringSchema().description("Page size"))
        .addProperty("sort", new ArraySchema().items(new StringSchema()).description("Sorting"));
  }

  private Paths createPaths() {
    Paths paths = new Paths();

    // Application endpoints
    paths.addPathItem("/api/apps", createApplicationPaths());
    paths.addPathItem("/api/apps/{id}", createApplicationByIdPaths());
    paths.addPathItem("/api/apps/filter", createApplicationFilterPaths());

    // AppVersion endpoints
    paths.addPathItem("/api/versions", createVersionsPaths());
    paths.addPathItem("/api/versions/{id}", createVersionByIdPaths());
    paths.addPathItem("/api/versions/latest", createLatestVersionPaths());
    paths.addPathItem("/api/versions/rangeDate", createRangeDatePaths());

    // UserDevice endpoints
    paths.addPathItem("/api/devices", createDevicesPaths());
    paths.addPathItem("/api/devices/{id}", createDeviceByIdPaths());

    // Auth endpoints
    paths.addPathItem("/api/auth/login", createLoginPaths());
    paths.addPathItem("/api/auth/password/change", createChangePasswordPaths());
    paths.addPathItem("/api/auth/refresh", createRefreshPaths());
    paths.addPathItem("/api/auth/logout", createLogoutPaths());
    paths.addPathItem("/api/auth/info", createAuthInfoPaths());

    return paths;
  }

  private PathItem createApplicationPaths() {
    return new PathItem()
        .get(new Operation()
            .summary("Get all applications")
            .description("Returns list of all applications")
            .tags(Collections.singletonList("Application Management"))
            .responses(new ApiResponses()
                .addApiResponse("200", new ApiResponse()
                    .description("Successfully retrieved applications list")
                    .content(new io.swagger.v3.oas.models.media.Content()
                        .addMediaType(MediaType.APPLICATION_JSON_VALUE,
                            new io.swagger.v3.oas.models.media.MediaType()
                                .schema(new ArraySchema()
                                    .items(new Schema().$ref("#/components/schemas/ApplicationResponse"))))))))
        .post(new Operation()
            .summary("Create new application")
            .description("Creates a new application")
            .tags(Collections.singletonList("Application Management"))
            .requestBody(new RequestBody()
                .content(new io.swagger.v3.oas.models.media.Content()
                    .addMediaType(MediaType.APPLICATION_JSON_VALUE,
                        new io.swagger.v3.oas.models.media.MediaType()
                            .schema(new Schema().$ref("#/components/schemas/ApplicationRequest"))))
                .required(true))
            .responses(new ApiResponses()
                .addApiResponse("201", new ApiResponse()
                    .description("Application successfully created")
                    .content(new io.swagger.v3.oas.models.media.Content()
                        .addMediaType(MediaType.APPLICATION_JSON_VALUE,
                            new io.swagger.v3.oas.models.media.MediaType()
                                .schema(new Schema().$ref("#/components/schemas/ApplicationResponse")))))));
  }

  private PathItem createApplicationByIdPaths() {
    return new PathItem()
        .get(new Operation()
            .summary("Get application by ID")
            .description("Returns application by specified ID")
            .tags(Collections.singletonList("Application Management"))
            .addParametersItem(new Parameter()
                .name("id")
                .in("path")
                .required(true)
                .schema(new StringSchema())
                .description("Application ID"))
            .responses(new ApiResponses()
                .addApiResponse("200", new ApiResponse()
                    .description("Application found")
                    .content(new io.swagger.v3.oas.models.media.Content()
                        .addMediaType(MediaType.APPLICATION_JSON_VALUE,
                            new io.swagger.v3.oas.models.media.MediaType()
                                .schema(new Schema().$ref("#/components/schemas/ApplicationResponse")))))
                .addApiResponse("404", new ApiResponse().description("Application not found"))))
        .put(new Operation()
            .summary("Update application")
            .description("Updates application data by specified ID")
            .tags(Collections.singletonList("Application Management"))
            .addParametersItem(new Parameter()
                .name("id")
                .in("path")
                .required(true)
                .schema(new StringSchema())
                .description("Application ID to update"))
            .requestBody(new RequestBody()
                .content(new io.swagger.v3.oas.models.media.Content()
                    .addMediaType(MediaType.APPLICATION_JSON_VALUE,
                        new io.swagger.v3.oas.models.media.MediaType()
                            .schema(new Schema().$ref("#/components/schemas/ApplicationRequest"))))
                .required(true))
            .responses(new ApiResponses()
                .addApiResponse("200", new ApiResponse()
                    .description("Application successfully updated")
                    .content(new io.swagger.v3.oas.models.media.Content()
                        .addMediaType(MediaType.APPLICATION_JSON_VALUE,
                            new io.swagger.v3.oas.models.media.MediaType()
                                .schema(new Schema().$ref("#/components/schemas/ApplicationResponse")))))
                .addApiResponse("404", new ApiResponse().description("Application not found"))))
        .delete(new Operation()
            .summary("Delete application")
            .description("Deletes application by specified ID")
            .tags(Collections.singletonList("Application Management"))
            .addParametersItem(new Parameter()
                .name("id")
                .in("path")
                .required(true)
                .schema(new StringSchema())
                .description("Application ID to delete"))
            .responses(new ApiResponses()
                .addApiResponse("200", new ApiResponse().description("Application successfully deleted"))
                .addApiResponse("404", new ApiResponse().description("Application not found"))));
  }

  private PathItem createApplicationFilterPaths() {
    return new PathItem()
        .get(new Operation()
            .summary("Filter applications")
            .description("Search and filter applications with pagination and sorting")
            .tags(Collections.singletonList("Application Management"))
            .addParametersItem(new Parameter()
                .name("title")
                .in("query")
                .required(false)
                .schema(new StringSchema())
                .description("Application title"))
            .addParametersItem(new Parameter()
                .name("publisher")
                .in("query")
                .required(false)
                .schema(new StringSchema())
                .description("Publisher"))
            .addParametersItem(new Parameter()
                .name("developer")
                .in("query")
                .required(false)
                .schema(new StringSchema())
                .description("Developer"))
            .addParametersItem(new Parameter()
                .name("type")
                .in("query")
                .required(false)
                .schema(new StringSchema())
                .description("Application type"))
            .addParametersItem(new Parameter()
                .name("page")
                .in("query")
                .required(false)
                .schema(new StringSchema().example("0"))
                .description("Page number"))
            .addParametersItem(new Parameter()
                .name("count")
                .in("query")
                .required(false)
                .schema(new StringSchema().example("10"))
                .description("Number of items per page"))
            .addParametersItem(new Parameter()
                .name("order")
                .in("query")
                .required(false)
                .schema(new StringSchema()._enum(Arrays.asList("ASC", "DESC")).example("ASC"))
                .description("Sort order"))
            .addParametersItem(new Parameter()
                .name("sort")
                .in("query")
                .required(false)
                .schema(new StringSchema().example("title"))
                .description("Sort field"))
            .responses(new ApiResponses()
                .addApiResponse("200", new ApiResponse()
                    .description("Successful filtering")
                    .content(new io.swagger.v3.oas.models.media.Content()
                        .addMediaType(MediaType.APPLICATION_JSON_VALUE,
                            new io.swagger.v3.oas.models.media.MediaType()
                                .schema(new Schema().$ref("#/components/schemas/Pageable")))))));
  }

  private PathItem createVersionsPaths() {
    return new PathItem()
        .get(new Operation()
            .summary("Get all application versions")
            .description("Returns list of all application versions")
            .tags(Collections.singletonList("Version Management"))
            .addParametersItem(new Parameter()
                .name("applicationId")
                .in("query")
                .required(true)
                .schema(new StringSchema())
                .description("Application ID"))
            .responses(new ApiResponses()
                .addApiResponse("200", new ApiResponse()
                    .description("Successfully retrieved versions list")
                    .content(new io.swagger.v3.oas.models.media.Content()
                        .addMediaType(MediaType.APPLICATION_JSON_VALUE,
                            new io.swagger.v3.oas.models.media.MediaType()
                                .schema(new ArraySchema()
                                    .items(new Schema().$ref("#/components/schemas/AppVersionResponse"))))))))
        .post(new Operation()
            .summary("Create new application version")
            .description("Creates a new application version")
            .tags(Collections.singletonList("Version Management"))
            .requestBody(new RequestBody()
                .content(new io.swagger.v3.oas.models.media.Content()
                    .addMediaType(MediaType.APPLICATION_JSON_VALUE,
                        new io.swagger.v3.oas.models.media.MediaType()
                            .schema(new Schema().$ref("#/components/schemas/AppVersionRequest"))))
                .required(true))
            .responses(new ApiResponses()
                .addApiResponse("201", new ApiResponse()
                    .description("Version successfully created")
                    .content(new io.swagger.v3.oas.models.media.Content()
                        .addMediaType(MediaType.APPLICATION_JSON_VALUE,
                            new io.swagger.v3.oas.models.media.MediaType()
                                .schema(new Schema().$ref("#/components/schemas/AppVersionResponse")))))));
  }

  private PathItem createVersionByIdPaths() {
    return new PathItem()
        .get(new Operation()
            .summary("Get version by ID")
            .description("Returns application version by specified ID")
            .tags(Collections.singletonList("Version Management"))
            .addParametersItem(new Parameter()
                .name("id")
                .in("path")
                .required(true)
                .schema(new StringSchema())
                .description("Version ID"))
            .responses(new ApiResponses()
                .addApiResponse("200", new ApiResponse()
                    .description("Version found")
                    .content(new io.swagger.v3.oas.models.media.Content()
                        .addMediaType(MediaType.APPLICATION_JSON_VALUE,
                            new io.swagger.v3.oas.models.media.MediaType()
                                .schema(new Schema().$ref("#/components/schemas/AppVersionResponse")))))
                .addApiResponse("404", new ApiResponse().description("Version not found"))))
        .put(new Operation()
            .summary("Update version")
            .description("Updates application version data by specified ID")
            .tags(Collections.singletonList("Version Management"))
            .addParametersItem(new Parameter()
                .name("id")
                .in("path")
                .required(true)
                .schema(new StringSchema())
                .description("Version ID to update"))
            .requestBody(new RequestBody()
                .content(new io.swagger.v3.oas.models.media.Content()
                    .addMediaType(MediaType.APPLICATION_JSON_VALUE,
                        new io.swagger.v3.oas.models.media.MediaType()
                            .schema(new Schema().$ref("#/components/schemas/AppVersionRequest"))))
                .required(true))
            .responses(new ApiResponses()
                .addApiResponse("200", new ApiResponse()
                    .description("Version successfully updated")
                    .content(new io.swagger.v3.oas.models.media.Content()
                        .addMediaType(MediaType.APPLICATION_JSON_VALUE,
                            new io.swagger.v3.oas.models.media.MediaType()
                                .schema(new Schema().$ref("#/components/schemas/AppVersionResponse")))))
                .addApiResponse("404", new ApiResponse().description("Version not found"))))
        .delete(new Operation()
            .summary("Delete version")
            .description("Deletes application version by specified ID")
            .tags(Collections.singletonList("Version Management"))
            .addParametersItem(new Parameter()
                .name("id")
                .in("path")
                .required(true)
                .schema(new StringSchema())
                .description("Version ID to delete"))
            .responses(new ApiResponses()
                .addApiResponse("200", new ApiResponse().description("Version successfully deleted"))
                .addApiResponse("404", new ApiResponse().description("Version not found"))));
  }

  private PathItem createLatestVersionPaths() {
    return new PathItem()
        .get(new Operation()
            .summary("Get latest application version")
            .description("Returns latest application version for specified platform")
            .tags(Collections.singletonList("Version Management"))
            .addParametersItem(new Parameter()
                .name("applicationId")
                .in("query")
                .required(true)
                .schema(new StringSchema())
                .description("Application ID"))
            .responses(new ApiResponses()
                .addApiResponse("200", new ApiResponse()
                    .description("Latest version found")
                    .content(new io.swagger.v3.oas.models.media.Content()
                        .addMediaType(MediaType.APPLICATION_JSON_VALUE,
                            new io.swagger.v3.oas.models.media.MediaType()
                                .schema(new Schema().$ref("#/components/schemas/AppVersionResponse")))))));
  }

  private PathItem createRangeDatePaths() {
    return new PathItem()
        .get(new Operation()
            .summary("Get versions by date range")
            .description("Returns application versions by release date range")
            .tags(Collections.singletonList("Version Management"))
            .addParametersItem(new Parameter()
                .name("max")
                .in("query")
                .required(false)
                .schema(new StringSchema())
                .description("Maximum date (YYYY-DD-MM HH:MM:SS)"))
            .addParametersItem(new Parameter()
                .name("min")
                .in("query")
                .required(false)
                .schema(new StringSchema())
                .description("Minimum date (YYYY-DD-MM HH:MM:SS)"))
            .responses(new ApiResponses()
                .addApiResponse("200", new ApiResponse()
                    .description("Successfully retrieved versions")
                    .content(new io.swagger.v3.oas.models.media.Content()
                        .addMediaType(MediaType.APPLICATION_JSON_VALUE,
                            new io.swagger.v3.oas.models.media.MediaType()
                                .schema(new Schema().$ref("#/components/schemas/Pageable")))))));
  }

  private PathItem createDevicesPaths() {
    return new PathItem()
        .get(new Operation()
            .summary("Get all devices")
            .description("Returns list of all user devices")
            .tags(Collections.singletonList("Device Management"))
            .responses(new ApiResponses()
                .addApiResponse("200", new ApiResponse()
                    .description("Successfully retrieved devices list")
                    .content(new io.swagger.v3.oas.models.media.Content()
                        .addMediaType(MediaType.APPLICATION_JSON_VALUE,
                            new io.swagger.v3.oas.models.media.MediaType()
                                .schema(new ArraySchema()
                                    .items(new Schema().$ref("#/components/schemas/UserDeviceResponse"))))))))
        .post(new Operation()
            .summary("Create new device")
            .description("Creates a new user device")
            .tags(Collections.singletonList("Device Management"))
            .requestBody(new RequestBody()
                .content(new io.swagger.v3.oas.models.media.Content()
                    .addMediaType(MediaType.APPLICATION_JSON_VALUE,
                        new io.swagger.v3.oas.models.media.MediaType()
                            .schema(new Schema().$ref("#/components/schemas/UserDeviceRequest"))))
                .required(true))
            .responses(new ApiResponses()
                .addApiResponse("201", new ApiResponse()
                    .description("Device successfully created")
                    .content(new io.swagger.v3.oas.models.media.Content()
                        .addMediaType(MediaType.APPLICATION_JSON_VALUE,
                            new io.swagger.v3.oas.models.media.MediaType()
                                .schema(new Schema().$ref("#/components/schemas/UserDeviceResponse")))))));
  }

  private PathItem createDeviceByIdPaths() {
    return new PathItem()
        .get(new Operation()
            .summary("Get device by ID")
            .description("Returns user device by specified ID")
            .tags(Collections.singletonList("Device Management"))
            .addParametersItem(new Parameter()
                .name("id")
                .in("path")
                .required(true)
                .schema(new StringSchema())
                .description("Device ID"))
            .responses(new ApiResponses()
                .addApiResponse("200", new ApiResponse()
                    .description("Device found")
                    .content(new io.swagger.v3.oas.models.media.Content()
                        .addMediaType(MediaType.APPLICATION_JSON_VALUE,
                            new io.swagger.v3.oas.models.media.MediaType()
                                .schema(new Schema().$ref("#/components/schemas/UserDeviceResponse")))))
                .addApiResponse("404", new ApiResponse().description("Device not found"))))
        .put(new Operation()
            .summary("Update device")
            .description("Updates user device data by specified ID")
            .tags(Collections.singletonList("Device Management"))
            .addParametersItem(new Parameter()
                .name("id")
                .in("path")
                .required(true)
                .schema(new StringSchema())
                .description("Device ID to update"))
            .requestBody(new RequestBody()
                .content(new io.swagger.v3.oas.models.media.Content()
                    .addMediaType(MediaType.APPLICATION_JSON_VALUE,
                        new io.swagger.v3.oas.models.media.MediaType()
                            .schema(new Schema().$ref("#/components/schemas/UserDeviceRequest"))))
                .required(true))
            .responses(new ApiResponses()
                .addApiResponse("200", new ApiResponse()
                    .description("Device successfully updated")
                    .content(new io.swagger.v3.oas.models.media.Content()
                        .addMediaType(MediaType.APPLICATION_JSON_VALUE,
                            new io.swagger.v3.oas.models.media.MediaType()
                                .schema(new Schema().$ref("#/components/schemas/UserDeviceResponse")))))
                .addApiResponse("404", new ApiResponse().description("Device not found"))))
        .delete(new Operation()
            .summary("Delete device")
            .description("Deletes user device by specified ID")
            .tags(Collections.singletonList("Device Management"))
            .addParametersItem(new Parameter()
                .name("id")
                .in("path")
                .required(true)
                .schema(new StringSchema())
                .description("Device ID to delete"))
            .responses(new ApiResponses()
                .addApiResponse("200", new ApiResponse().description("Device successfully deleted"))
                .addApiResponse("404", new ApiResponse().description("Device not found"))));
  }

  private PathItem createLoginPaths() {
    return new PathItem()
        .post(new Operation()
            .summary("User login")
            .description("Authenticates user and returns tokens")
            .tags(Collections.singletonList("Authentication"))
            .requestBody(new RequestBody()
                .content(new io.swagger.v3.oas.models.media.Content()
                    .addMediaType(MediaType.APPLICATION_JSON_VALUE,
                        new io.swagger.v3.oas.models.media.MediaType()
                            .schema(new Schema().$ref("#/components/schemas/LoginRequest"))))
                .required(true))
            .responses(new ApiResponses()
                .addApiResponse("200", new ApiResponse()
                    .description("Successful authentication")
                    .content(new io.swagger.v3.oas.models.media.Content()
                        .addMediaType(MediaType.APPLICATION_JSON_VALUE,
                            new io.swagger.v3.oas.models.media.MediaType()
                                .schema(new Schema().$ref("#/components/schemas/LoginResponse")))))));
  }

  private PathItem createChangePasswordPaths() {
    return new PathItem()
        .patch(new Operation()
            .summary("Change password")
            .description("Changes user password")
            .tags(Collections.singletonList("Authentication"))
            .requestBody(new RequestBody()
                .content(new io.swagger.v3.oas.models.media.Content()
                    .addMediaType(MediaType.APPLICATION_JSON_VALUE,
                        new io.swagger.v3.oas.models.media.MediaType()
                            .schema(new Schema().$ref("#/components/schemas/ChangePasswordRequest"))))
                .required(true))
            .responses(new ApiResponses()
                .addApiResponse("200", new ApiResponse()
                    .description("Password successfully changed")
                    .content(new io.swagger.v3.oas.models.media.Content()
                        .addMediaType(MediaType.APPLICATION_JSON_VALUE,
                            new io.swagger.v3.oas.models.media.MediaType()
                                .schema(new Schema().$ref("#/components/schemas/LoginResponse")))))));
  }

  private PathItem createRefreshPaths() {
    return new PathItem()
        .post(new Operation()
            .summary("Refresh token")
            .description("Refreshes access token using refresh token")
            .tags(Collections.singletonList("Authentication"))
            .responses(new ApiResponses()
                .addApiResponse("200", new ApiResponse()
                    .description("Token successfully refreshed")
                    .content(new io.swagger.v3.oas.models.media.Content()
                        .addMediaType(MediaType.APPLICATION_JSON_VALUE,
                            new io.swagger.v3.oas.models.media.MediaType()
                                .schema(new Schema().$ref("#/components/schemas/LoginResponse")))))));
  }

  private PathItem createLogoutPaths() {
    return new PathItem()
        .post(new Operation()
            .summary("User logout")
            .description("Ends user session")
            .tags(Collections.singletonList("Authentication"))
            .responses(new ApiResponses()
                .addApiResponse("200", new ApiResponse()
                    .description("Successfully logged out")
                    .content(new io.swagger.v3.oas.models.media.Content()
                        .addMediaType(MediaType.APPLICATION_JSON_VALUE,
                            new io.swagger.v3.oas.models.media.MediaType()
                                .schema(new Schema().$ref("#/components/schemas/LoginResponse")))))));
  }

  private PathItem createAuthInfoPaths() {
    return new PathItem()
        .get(new Operation()
            .summary("User information")
            .description("Gets information about current authenticated user")
            .tags(Collections.singletonList("Authentication"))
            .responses(new ApiResponses()
                .addApiResponse("200", new ApiResponse()
                    .description("Information retrieved")
                    .content(new io.swagger.v3.oas.models.media.Content()
                        .addMediaType(MediaType.APPLICATION_JSON_VALUE,
                            new io.swagger.v3.oas.models.media.MediaType()
                                .schema(new Schema().$ref("#/components/schemas/UserLoggedDto")))))
                .addApiResponse("401", new ApiResponse().description("User not authenticated"))));
  }
}