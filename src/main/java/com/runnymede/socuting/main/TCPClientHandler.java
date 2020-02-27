package com.runnymede.socuting.main;

import com.google.gson.Gson;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.ssl.SslHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * The type Tcp client handler.
 */
public class TCPClientHandler extends ChannelDuplexHandler {


    /**
     * The Channels.
     */
    static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);


    @Override
    public void channelActive(final ChannelHandlerContext channelHandlerContext){
        channelHandlerContext.pipeline().get(SslHandler.class).handshakeFuture().addListener(
                (GenericFutureListener<Future<Channel>>) future -> {
                    System.out.println("Channel active " + channelHandlerContext.name());
                    channelHandlerContext.write("Connection to TCP server intiated");
                    channels.add(channelHandlerContext.channel());
                });
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println("reading message from client : " + ctx.channel().remoteAddress() + " message : " + msg.toString());

    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        System.out.println("The channel : " + ctx.channel().remoteAddress() + " has encountered an exception, we are removing all references of that channel from the active list");
        cause.printStackTrace();

    }



}
