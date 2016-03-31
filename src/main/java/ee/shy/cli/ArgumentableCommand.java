package ee.shy.cli;

import com.github.drapostolos.typeparser.TypeParser;
import com.github.drapostolos.typeparser.TypeParserException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by simmo on 1.04.16.
 */
public abstract class ArgumentableCommand implements Command {
    @Override
    public void execute(String[] args) throws IOException {
        Method enclosingMethod = new Object(){}.getClass().getEnclosingMethod();

        TypeParser typeParser = TypeParser.newBuilder().build();

        Method[] methods = getClass().getMethods();
        for (Method method : methods) {
            if (!method.equals(enclosingMethod) && Objects.equals(method.getName(), "execute") && method.getReturnType().equals(boolean.class)) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (args.length < parameterTypes.length)
                    continue;

                try {
                    Object[] parameters = new Object[parameterTypes.length];
                    for (int i = 0; i < parameterTypes.length; i++) {
                        parameters[i] = typeParser.parse(args[i], parameterTypes[i]);
                    }

                    try {
                        if ((boolean) method.invoke(this, parameters))
                            break;
                    }
                    catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                }
                catch (TypeParserException e) {

                }
            }
        }
    }
}
