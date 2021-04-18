package snippets.config.loadsysproporenv;

@FunctionalInterface
public interface ThrowingFunction<T, R, E extends Throwable> {

    public R apply(T value) throws E;
    
}
