package cc.whohow.tool.conf;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfigurationHandlerFactory implements Function<String, ConfigurationHandler> {
    private static final Pattern PATTERN = Pattern.compile(".+\\.(?<type>[a-zA-Z0-9-_]+)\\.hot");
    private Map<String, ConfigurationHandler> configurationHandlers = new HashMap<>();

    public ConfigurationHandlerFactory(ConfigurationHandler... configurationHandlers) {
        this(Arrays.asList(configurationHandlers));
    }

    public ConfigurationHandlerFactory(Iterable<ConfigurationHandler> configurationHandlers) {
        for (ConfigurationHandler configurationHandler : configurationHandlers) {
            this.configurationHandlers.put(configurationHandler.getType(), configurationHandler);
        }
    }

    @Override
    public ConfigurationHandler apply(String uri) {
        Matcher matcher = PATTERN.matcher(uri);
        if (matcher.matches()) {
            return configurationHandlers.get(matcher.group("type"));
        }
        return null;
    }
}
