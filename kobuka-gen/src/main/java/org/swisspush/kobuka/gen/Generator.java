package org.swisspush.kobuka.gen;

import com.squareup.javapoet.*;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.config.ConfigDef;


import javax.lang.model.element.Modifier;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.squareup.javapoet.MethodSpec.methodBuilder;

public class Generator {
    public static void main(String args[]) throws IOException {

        String root = args[0];

        TypeSpec.Builder builder = TypeSpec.classBuilder("BaseConsumerConfigBuilder")
                .addModifiers(Modifier.PUBLIC);

        builder.addField(FieldSpec.builder(ParameterizedTypeName.get(Map.class, String.class, Object.class),
                "configs",
                Modifier.PRIVATE).build());

        ConsumerConfig.configDef().configKeys().forEach((key, value) -> {
            builder.addMethod(methodBuilder(convert2CamelCase(key))
                    .addModifiers(Modifier.PUBLIC)
                    .addJavadoc(value.documentation.replaceAll("\\. ", ".<br>\n"))
                    .returns(ClassName.get("org.swisspush.kobuka.client", "BaseConsumerConfigBuilder"))
                    .addParameter(resolveType(value.type), "value")
                    .addStatement("configs.put($S, value)", key)
                    .addStatement("return this")
                    .build());
        });

        JavaFile javaFile = JavaFile.builder("org.swisspush.kobuka.client", builder.build())
                .build();

        javaFile.writeTo(Paths.get(root+"/target/generated-sources/kobuka/org/swisspush/kobuka/client/BaseConsumerConfigBuilder.java"));
        javaFile.writeTo(System.out);
    }

    private static Type resolveType(ConfigDef.Type type) {
        switch (type) {
            case INT: return Integer.class;
            case BOOLEAN: return Boolean.class;
            case CLASS: return Class.class;
            case DOUBLE: return Double.class;
            case LONG: return Long.class;
            case SHORT: return Short.class;
            case LIST:
            case PASSWORD:
            case STRING:
                return String.class;
        }
        throw new RuntimeException("Cannot map type "+type);
    }

    private static String convert2CamelCase(String str) {
        Matcher matcher = Pattern.compile("[A-Z]{2,}(?=[A-Z][a-z]+[0-9]*|\\b)|[A-Z]?[a-z]+[0-9]*|[A-Z]|[0-9]+").matcher(str);
        List<String> matched = new ArrayList<>();
        while (matcher.find()) {
            matched.add(matcher.group(0));
        }
        String camelcase = matched.stream()
                .map(x -> x.substring(0, 1).toUpperCase() + x.substring(1).toLowerCase())
                .collect(Collectors.joining());
        return camelcase.substring(0, 1).toLowerCase() + camelcase.substring(1);
    }
}
