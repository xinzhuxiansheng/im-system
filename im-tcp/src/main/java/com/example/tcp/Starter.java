package com.example.tcp;

import com.example.codec.config.BootstrapConfig;
import com.example.tcp.reciver.MessageReciver;
import com.example.tcp.redis.RedisManager;
import com.example.tcp.register.RegistryZK;
import com.example.tcp.register.ZKit;
import com.example.tcp.server.LimServer;
import com.example.tcp.server.LimWebSocketServer;
import com.example.tcp.utils.MqFactory;
import org.I0Itec.zkclient.ZkClient;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Starter {


//    HTTP GET POST PUT DELETE 1.0 1.1 2.0
    //client IOS 安卓 pc(windows mac) web //支持json 也支持 protobuf
    //appId
    //28 + imei + body
    //请求头（指令 版本 clientType 消息解析类型 imei长度 appId bodylen）+ imei号 + 请求体
    //len+body


    public static void main(String[] args)  {
        if(args.length > 0){
            start(args[0]);
        }
    }

    private static void start(String path){
        try {
            Yaml yaml = new Yaml();
            InputStream inputStream = new FileInputStream(path);
            BootstrapConfig bootstrapConfig = yaml.loadAs(inputStream, BootstrapConfig.class);

            new LimServer(bootstrapConfig.getLim()).start();
            new LimWebSocketServer(bootstrapConfig.getLim()).start();

            RedisManager.init(bootstrapConfig);
            MqFactory.init(bootstrapConfig.getLim().getRabbitmq());
            MessageReciver.init(bootstrapConfig.getLim().getBrokerId()+"");
            registerZK(bootstrapConfig);

        }catch (Exception e){
            e.printStackTrace();
            System.exit(500);
        }
    }

    public static void registerZK(BootstrapConfig config) throws UnknownHostException {
        String hostAddress = InetAddress.getLocalHost().getHostAddress();
        ZkClient zkClient = new ZkClient(config.getLim().getZkConfig().getZkAddr(),
                config.getLim().getZkConfig().getZkConnectTimeOut());
        ZKit zKit = new ZKit(zkClient);
        RegistryZK registryZK = new RegistryZK(zKit, hostAddress, config.getLim());
        Thread thread = new Thread(registryZK);
        thread.start();

    }
}
