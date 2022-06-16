package com.reporter.formatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class FormatterFactory {
    private static final Logger log = LoggerFactory.getLogger(FormatterFactory.class);
    public final Map<String, Class<? extends Formatter>> formatterClassMap = new HashMap<>();

    /**
     * Collects all Formatter-s in a list via Spring IoC
     * @param formatters list of formatters
     */
    public FormatterFactory(List<Formatter> formatters) {
        formatters.forEach(
            formatter ->
                formatterClassMap
                    .put(
                        formatter.getExtension(),
                        formatter.getClass()
                    )
        );
    }

    /**
     * Creates formatter by parameters based on
     * previous collected formatters in {@link FormatterFactory#formatterClassMap}
     *
     * @param ext target extension
     * @param formatterContext context
     * @return new instance of Formatter
     */
    public Formatter createFormatter(String ext, FormatterContext formatterContext) {
        if (!formatterClassMap.containsKey(ext)) {
            log.error("Improper export format: {}", ext);

            throw new IllegalArgumentException(
                String.format("Improper report format %s in request", ext)
            );
        }
        try {
            final var constructor =
                formatterClassMap
                    .get(ext)
                    .getConstructor(FormatterContext.class);
            return constructor.newInstance(formatterContext);
        } catch (Exception e) {
            throw new IllegalStateException(
                String.format("Can't instantiate \"%s\"", formatterClassMap.get(ext)),
                e
            );
        }
    }

    public Map<String, Class<? extends Formatter>> getFormatterClassMap() {
        return formatterClassMap;
    }
}
