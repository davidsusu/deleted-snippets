package snippets.config.loadsysproporenv;

import java.util.Optional;
import java.util.concurrent.Callable;

public final class ValueLoadUtil {

    private ValueLoadUtil() {
        // utility class
    }
    

    public static String loadExternalValue(
            String systemPropertyName,
            String environmentValueName,
            String defaultValue) {
        
        return loadExternalValue(systemPropertyName, environmentValueName)
                .orElse(defaultValue);
    }
    
    public static <T> T loadExternalValue(
            String systemPropertyName,
            String environmentValueName,
            ThrowingFunction<String, T, ? extends Exception> factory,
            T defaultValue) {
        
        Optional<String> optionalRawValue =
                loadExternalValue(systemPropertyName, environmentValueName);
        if (!optionalRawValue.isPresent()) {
            return defaultValue;
        }
        
        String rawValue = optionalRawValue.get();
        
        T value;
        try {
            value = factory.apply(rawValue);
        } catch (Exception e) {
            return defaultValue;
        }
        
        return value;
    }

    public static Optional<String> loadExternalValue(
            String systemPropertyName,
            String environmentValueName) {
        
        return loadSystemProperty(systemPropertyName)
                .or(() -> loadEnvironmentVariable(environmentValueName));
    }

    public static Optional<String> loadSystemProperty(String name) {
        return loadValue(() -> System.getProperty(name));
    }

    public static Optional<String> loadEnvironmentVariable(String name) {
        return loadValue(() -> System.getenv(name));
    }
    
    public static Optional<String> loadValue(Callable<String> supplier) {
        String value;
        try {
            value = supplier.call();
        } catch (Exception e) {
            return Optional.empty();
        }
        
        if (value == null || value.isEmpty()) {
            return Optional.empty();
        }
        
        return Optional.of(value);
    }
    
    
    

}
