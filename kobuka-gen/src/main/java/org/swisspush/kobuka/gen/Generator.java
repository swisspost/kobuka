package org.swisspush.kobuka.gen;

import com.squareup.javapoet.*;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.types.Password;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.squareup.javapoet.MethodSpec.methodBuilder;

public class Generator {

    public final static String CLIENT_PACKAGE = "org.swisspush.kobuka.client.base";

    public static void main(String[] args) throws IOException {
        String rootDir = args[0];

        generateBuilder(CLIENT_PACKAGE,
                "ConsumerConfig",
                stream(ConsumerConfig.configDef()),
                rootDir);
        generateBuilder(CLIENT_PACKAGE,
                "ProducerConfig",
                stream(ProducerConfig.configDef()),
                rootDir);
        generateBuilder(CLIENT_PACKAGE,
                "AdminClientConfig",
                stream(AdminClientConfig.configDef()),
                rootDir);

        // Generate common keys

        Set<String> commonKeys = new HashSet<>(ConsumerConfig.configDef().configKeys().keySet());
        commonKeys.retainAll(ProducerConfig.configDef().configKeys().keySet());
        commonKeys.retainAll(AdminClientConfig.configDef().configKeys().keySet());

        Stream<Map.Entry<String, ConfigDef.ConfigKey>> commonConfigMap = AdminClientConfig.configDef().configKeys().entrySet().stream()
                .filter(entry -> commonKeys.contains(entry.getKey()));

        generateBuilder(CLIENT_PACKAGE, "CommonClientConfig", commonConfigMap, rootDir);
    }

    private static Stream<Map.Entry<String, ConfigDef.ConfigKey>> stream(ConfigDef configDef) {
        return configDef.configKeys().entrySet().stream();
    }

    private static void generateBuilder(String packageName, String baseName, Stream<Map.Entry<String, ConfigDef.ConfigKey>> definitions, String rootDir)
            throws IOException {

        String interfaceName = baseName + "Fields";
        String className = "Abstract" + baseName + "Builder";

        TypeSpec.Builder interfaceBuilder = TypeSpec.interfaceBuilder(interfaceName)
                .addModifiers(Modifier.PUBLIC)
                .addTypeVariable(TypeVariableName.get("T",
                        ParameterizedTypeName.get(ClassName.get(packageName, interfaceName), TypeVariableName.get("T"))));

        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(className)
                .addSuperinterface(ParameterizedTypeName.get(ClassName.get(packageName, interfaceName), TypeVariableName.get("T")))
                .addTypeVariable(TypeVariableName.get("T",
                        ParameterizedTypeName.get(ClassName.get(packageName, className), TypeVariableName.get("T"))));

        classBuilder
                .addField(FieldSpec.builder(ParameterizedTypeName.get(Map.class, String.class, Object.class), "configs")
                        .initializer(CodeBlock.builder().add("new $T<>()", HashMap.class).build())
                        .build());

        definitions
                .filter(entry -> !entry.getValue().internalConfig)
                .forEach(entry -> {

                    generateMethod(
                            interfaceBuilder,
                            classBuilder,
                            entry.getValue(),
                            resolveType(entry.getValue().type));

                    // List types can also be comma-separated strings
                    // Password can also be a string
                    if (entry.getValue().type == ConfigDef.Type.LIST || entry.getValue().type == ConfigDef.Type.PASSWORD) {
                        generateMethod(
                                interfaceBuilder,
                                classBuilder,
                                entry.getValue(),
                                ClassName.get(String.class));
                    }
                });

        interfaceBuilder.addMethod(methodBuilder("self")
                .addModifiers(Modifier.PUBLIC, Modifier.DEFAULT)
                .returns(TypeVariableName.get("T"))
                .addStatement("return (T)this")
                .build());

        JavaFile interfaceJavaFile = JavaFile.builder(packageName, interfaceBuilder.build())
                .build();
        JavaFile classJavaFile = JavaFile.builder(packageName, classBuilder.build())
                .build();

        interfaceJavaFile.writeTo(Paths.get(rootDir + "/target/generated-sources/kobuka/" + packageName.replaceAll("\\.", "/") + "/" + interfaceName + ".java"));
        classJavaFile.writeTo(Paths.get(rootDir + "/target/generated-sources/kobuka/" + packageName.replaceAll("\\.", "/") + "/" + className + ".java"));
    }

    private static void generateMethod(TypeSpec.Builder interfaceBuilder, TypeSpec.Builder classBuilder, ConfigDef.ConfigKey key, TypeName type) {
        interfaceBuilder.addMethod(methodBuilder(toCamelCase(key.displayName))
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addJavadoc(
                        "<b>" + key.displayName + "</b><p>\n" +
                                key.documentation.replaceAll("\\. ", ".<p>") +
                                "\n<p><b>Default:</b> " + renderDefault(key) +
                                "\n<p><b>Valid Values:</b> " + (key.validator != null ? key.validator.toString() : "") +
                                "\n<p><b>Importance:</b> " + key.importance.toString().toLowerCase(Locale.ROOT))

                .returns(TypeVariableName.get("T"))
                .addParameter(type, "value")
                .build());

        classBuilder.addMethod(methodBuilder(toCamelCase(key.name))
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeVariableName.get("T"))
                .addParameter(type, "value")
                .addStatement("configs.put($S, value)", key.name)
                .addStatement("return self()")
                .build());
    }

    private static TypeName resolveType(ConfigDef.Type type) {
        switch (type) {
            case INT:
                return ClassName.get(Integer.class);
            case BOOLEAN:
                return ClassName.get(Boolean.class);
            case CLASS:
                return ClassName.get(Class.class);
            case DOUBLE:
                return ClassName.get(Double.class);
            case LONG:
                return ClassName.get(Long.class);
            case SHORT:
                return ClassName.get(Short.class);
            case LIST:
                return ParameterizedTypeName.get(List.class, String.class);
            case PASSWORD:
                return ClassName.get(Password.class);
            default:
                return ClassName.get(String.class);
        }
    }

    private static String renderDefault(ConfigDef.ConfigKey key) {
        if (key.hasDefault()) {
            if (key.defaultValue == null)
                return "null";
            String defaultValueStr = ConfigDef.convertToString(key.defaultValue, key.type);
            if (defaultValueStr.isEmpty())
                return "\"\"";
            else {
                String suffix = "";
                if (key.name.endsWith(".bytes")) {
                    suffix = niceMemoryUnits(((Number) key.defaultValue).longValue());
                } else if (key.name.endsWith(".ms")) {
                    suffix = niceTimeUnits(((Number) key.defaultValue).longValue());
                }
                return defaultValueStr + suffix;
            }
        } else
            return "";
    }

    private static String niceMemoryUnits(long bytes) {
        long value = bytes;
        int i = 0;
        while (value != 0 && i < 4) {
            if (value % 1024L == 0) {
                value /= 1024L;
                i++;
            } else {
                break;
            }
        }
        switch (i) {
            case 1:
                return " (" + value + " kibibyte" + (value == 1 ? ")" : "s)");
            case 2:
                return " (" + value + " mebibyte" + (value == 1 ? ")" : "s)");
            case 3:
                return " (" + value + " gibibyte" + (value == 1 ? ")" : "s)");
            case 4:
                return " (" + value + " tebibyte" + (value == 1 ? ")" : "s)");
            default:
                return "";
        }
    }

    private static String niceTimeUnits(long millis) {
        long value = millis;
        long[] divisors = {1000, 60, 60, 24};
        String[] units = {"second", "minute", "hour", "day"};
        int i = 0;
        while (value != 0 && i < 4) {
            if (value % divisors[i] == 0) {
                value /= divisors[i];
                i++;
            } else {
                break;
            }
        }
        if (i > 0) {
            return " (" + value + " " + units[i - 1] + (value > 1 ? "s)" : ")");
        }
        return "";
    }

    private static String toCamelCase(String str) {
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
