package com.xwtech.config;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

/**
 * Created by admini on 2019/1/14.
 */
@Configuration
public class ElasticsearchConfig {
    @Bean
    public TransportClient client() throws UnknownHostException{
        InetSocketTransportAddress node  = new InetSocketTransportAddress(
                InetAddress.getByName("locathost"),9300
        );
        Settings settings = Settings.builder().put("cluster.name","").build();
        TransportClient client = new PreBuiltTransportClient(settings);
        client.addTransportAddress(node);
        return client;
    }
}
