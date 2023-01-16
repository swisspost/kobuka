package org.swisspush.kobuka.gen;

import com.squareup.javapoet.*;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.types.Password;
import org.apache.kafka.streams.StreamsConfig;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.nio.file.Path;
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
        Path path = Paths.get(args[0] + "/target/generated-sources/kobuka/");

        generateBuilder(CLIENT_PACKAGE,
                "ConsumerConfig",
                stream(ConsumerConfig.configDef()),
                path);
        generateBuilder(CLIENT_PACKAGE,
                "ProducerConfig",
                stream(ProducerConfig.configDef()),
                path);
        generateBuilder(CLIENT_PACKAGE,
                "AdminClientConfig",
                stream(AdminClientConfig.configDef()),
                path);
        generateBuilder(CLIENT_PACKAGE,
                "StreamsConfig",
                stream(StreamsConfig.configDef()),
                path);

        // Generate common keys
        Set<String> commonKeys = new HashSet<>(ConsumerConfig.configDef().configKeys().keySet());
        commonKeys.retainAll(ProducerConfig.configDef().configKeys().keySet());
        commonKeys.retainAll(AdminClientConfig.configDef().configKeys().keySet());
        commonKeys.retainAll(StreamsConfig.configDef().configKeys().keySet());

        Stream<Map.Entry<String, ConfigDef.ConfigKey>> commonConfigMap = AdminClientConfig.configDef().configKeys().entrySet().stream()
                .filter(entry -> commonKeys.contains(entry.getKey()));

        generateBuilder(CLIENT_PACKAGE, "CommonClientConfig", commonConfigMap, path);
    }

    private static Stream<Map.Entry<String, ConfigDef.ConfigKey>> stream(ConfigDef configDef) {
        return configDef.configKeys().entrySet().stream();
    }

    private static void generateBuilder(String packageName, String baseName, Stream<Map.Entry<String, ConfigDef.ConfigKey>> definitions, Path path)
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

        interfaceJavaFile.writeTo(path);
        classJavaFile.writeTo(path);
    }

    private static void generateMethod(TypeSpec.Builder interfaceBuilder, TypeSpec.Builder classBuilder, ConfigDef.ConfigKey key, TypeName type) {
        interfaceBuilder.addMethod(methodBuilder(toCamelCase(key.displayName))
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)

                /*
                    The JavaDoc in kafka-clients contains html errors (e.g. 'self-closing element not allowed')
                    which cause a failure in the build of maven-javadoc-plugin.
                    Since the source code of kafka-clients is not under our control, 'failOnError' configuration is set
                    to 'false'

                    See: https://maven.apache.org/plugins/maven-javadoc-plugin/javadoc-mojo.html#failOnError
                 */
                .addJavadoc(
                        "<p><b>" + key.displayName + "</b></p>\n" +
                                key.documentation.replaceAll("\\. ", ".<br>") +
                                "\n<p><b>Default:</b> " + renderDefault(key) + "</p>" +
                                "\n<p><b>Valid Values:</b> " + (key.validator != null ? key.validator.toString() : "") + "</p>" +
                                "\n<p><b>Importance:</b> " + key.importance.toString().toLowerCase(Locale.ROOT) + "</p>")

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
        return switch (type) {
            case INT -> ClassName.get(Integer.class);
            case BOOLEAN -> ClassName.get(Boolean.class);
            case CLASS -> ClassName.get(Class.class);
            case DOUBLE -> ClassName.get(Double.class);
            case LONG -> ClassName.get(Long.class);
            case SHORT -> ClassName.get(Short.class);
            case LIST -> ParameterizedTypeName.get(List.class, String.class);
            case PASSWORD -> ClassName.get(Password.class);
            default -> ClassName.get(String.class);
        };
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
        return switch (i) {
            case 1 -> " (" + value + " kibibyte" + (value == 1 ? ")" : "s)");
            case 2 -> " (" + value + " mebibyte" + (value == 1 ? ")" : "s)");
            case 3 -> " (" + value + " gibibyte" + (value == 1 ? ")" : "s)");
            case 4 -> " (" + value + " tebibyte" + (value == 1 ? ")" : "s)");
            default -> "";
        };
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
