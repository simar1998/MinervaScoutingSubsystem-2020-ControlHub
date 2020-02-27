package com.runnymede.socuting.main;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.util.SelfSignedCertificate;

import javax.net.ssl.SSLException;
import java.security.cert.CertificateException;

/**
 * @author Simar Pal Kalsi
 * Live Long And Prosper -----> Simt'pal
 **/
public class TCPCommsThread implements Runnable{

    Thread thread;

    public TCPCommsThread(){
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        SslContext sslContext = null;
        SelfSignedCertificate ssc = null;
        try {
            ssc = new SelfSignedCertificate();
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        try {
            assert ssc != null;
            sslContext = SslContext.newServerContext(ssc.certificate(),ssc.privateKey());
        } catch (SSLException e) {
            e.printStackTrace();
        }

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try{
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).handler(new LoggingHandler(LogLevel.INFO)).childHandler(new ServerInit(sslContext));
            b.bind(CONST.PORT).sync().channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }

}
