package com.feredback.feredback_backend.config;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.collect.Sets;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.OperationBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiDescription;
import springfox.documentation.service.Operation;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ApiListingBuilderPlugin;
import springfox.documentation.spi.service.ApiListingScannerPlugin;
import springfox.documentation.spi.service.contexts.DocumentationContext;
import springfox.documentation.spring.web.readers.operation.CachingOperationNameGenerator;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @program: FE-Redback
 * @description:
 * @author: Pinzhuo Zhao, StudentID:1043915
 * @create: 2022-04-12 22:33
 **/
@Component
public class SwaggerAddition implements ApiListingScannerPlugin {
    @Override
    public List<ApiDescription> apply(DocumentationContext documentationContext) {
        Operation usernamePasswordOperation = new OperationBuilder(new CachingOperationNameGenerator())
                .method(HttpMethod.POST)
                .summary("login")
                .notes("login")
                .consumes(Sets.newHashSet(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .produces(Sets.newHashSet(MediaType.APPLICATION_JSON_VALUE))
                .tags(Sets.newHashSet("login"))
                .parameters(Arrays.asList(
                        new ParameterBuilder()
                                .description("username")
                                .type(new TypeResolver().resolve(String.class))
                                .name("username")
                                .defaultValue("TEST")
                                .parameterType("query")
                                .parameterAccess("access")
                                .required(true)
                                .modelRef(new ModelRef("string"))
                                .build(),
                        new ParameterBuilder()
                                .description("password")
                                .type(new TypeResolver().resolve(String.class))
                                .name("password")
                                .defaultValue("newpassword")
                                .parameterType("query")
                                .parameterAccess("access")
                                .required(true)
                                .modelRef(new ModelRef("string"))
                                .build()
                ))
                .responseMessages(Collections.singleton(
                        new ResponseMessageBuilder().code(200).message("success")
                                .responseModel(new ModelRef(
                                        "")
                                ).build()))
                .build();

        Operation logOutOperation = new OperationBuilder(new CachingOperationNameGenerator())
                .method(HttpMethod.POST)
                .summary("logout")
                .notes("logout")
                .consumes(Sets.newHashSet(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .produces(Sets.newHashSet(MediaType.APPLICATION_JSON_VALUE))
                .tags(Sets.newHashSet("logout"))
                .responseMessages(Collections.singleton(
                        new ResponseMessageBuilder().code(200).message("Success")
                                .responseModel(new ModelRef(
                                        "")
                                ).build()))
                .build();

        ApiDescription loginApiDescription = new ApiDescription("/login", "login",
                Arrays.asList(usernamePasswordOperation), false);

        ApiDescription logoutApiDescription = new ApiDescription("/logout","logout",
                Arrays.asList(logOutOperation),false);
        return Arrays.asList(loginApiDescription,logoutApiDescription);


    }

    @Override
    public boolean supports(DocumentationType documentationType) {
        return DocumentationType.SWAGGER_2.equals(documentationType);
    }
}
