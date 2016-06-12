package cn.cloudtop.strawberry.service.registry;

import mousio.etcd4j.EtcdClient;
import mousio.etcd4j.responses.EtcdAuthenticationException;
import mousio.etcd4j.responses.EtcdException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

/**
 * Created by jackie on 16-6-2
 */
@Component
public class RegistryTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegistryTask.class);

    @Value("${service.name}")
    private String serviceName;
    @Value("${service.version}")
    private String version;
    @Value("${host}")
    private String serverIp;
    @Value("${port}")
    private String servicePort;
    @Value("${registry.url}")
    private String registryUrl;

    private boolean registryDirExists = false;

    private String instanceId = UUID.randomUUID().toString().replace("-", "");

    @Scheduled(fixedRate = 60000)
    public void registry() {
        try {
            LOGGER.debug("registry start!");
            EtcdClient client = new EtcdClient(URI.create(registryUrl));
            String registryDir = String.format("registry/%s/%s", this.serviceName, this.version);
            if (createDirIfNotExists(client, registryDir)) {
                String registryKey = String.format("%s/%s", registryDir, instanceId);
                String serviceUrl = String.format("http://%s:%s", this.serverIp, this.servicePort);
                LOGGER.debug("try to registry service");
                client.put(registryKey, serviceUrl).ttl(60).send().get();
                LOGGER.info("registry success!server[name:{},version:{},instanceId:{}] url is {}", serviceName, version, registryKey, serviceUrl);
            }
        } catch (Exception ex) {
            LOGGER.error("registry error!", ex);
        }
    }

    private boolean createDirIfNotExists(EtcdClient client, String registryDir) throws IOException, EtcdAuthenticationException, TimeoutException {
        if (registryDirExists) {
            LOGGER.debug("registry dir exists!");
            return true;
        }

        try {
            LOGGER.debug("try to get registry dir!");
            client.getDir(registryDir).send().get();
            LOGGER.debug("registry dir exists!");
            return true;
        } catch (EtcdException e) {
            try {
                LOGGER.debug("registry dir is not exists,try to create it");
                client.putDir(registryDir).send().get();
                LOGGER.debug("registry dir create success!");
                registryDirExists = true;
                return true;
            } catch (EtcdException ee) {
                LOGGER.error("create directory[{}] error!", registryDir);
                LOGGER.error(ee.getMessage(), ee);
                return false;
            }
        }
    }
}
