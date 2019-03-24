package cc.whohow.tool.docker.vm;

import cc.whohow.tool.engine.SimpleViewModel;
import cc.whohow.tool.json.Json;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.netty.NettyDockerCmdExecFactory;
import javafx.scene.Parent;
import lombok.extern.log4j.Log4j2;

import java.util.concurrent.CompletableFuture;

@Log4j2
public class DockerViewModel extends SimpleViewModel<Parent> {
    protected DockerClient docker;

    @Override
    public void setValue(ObjectNode data) {
        if (docker == null) {
            docker = DockerClientBuilder.getInstance(DefaultDockerClientConfig.createDefaultConfigBuilder()
                    .withDockerHost(data.path("host").asText())
                    .withDockerTlsVerify(true)
                    .withDockerCertPath(data.path("certPath").asText())
                    .build())
                    .withDockerCmdExecFactory(new NettyDockerCmdExecFactory())
                    .build();
        }
        super.setValue(data);
    }

    @Override
    public CompletableFuture<JsonNode> get(String expression) {
        switch (expression) {
            case "containers": {
                return listContainers();
            }
            default: {
                return super.get(expression);
            }
        }
    }

    private CompletableFuture<JsonNode> listContainers() {
        return CompletableFuture
                .supplyAsync(() -> Json.from(docker.listContainersCmd().exec()))
                .thenApply((value) -> {
                    synchronized (DockerViewModel.this) {
                        model.set("containers", value);
                    }
                    log.debug("{}", value);
                    return value;
                });
    }

    @Override
    public void close() throws Exception {
        try {
            docker.close();
        } finally {
            super.close();
        }
    }
}
