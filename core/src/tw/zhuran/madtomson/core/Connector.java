package tw.zhuran.madtomson.core;

import com.google.common.base.Charsets;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.json.JsonObjectDecoder;
import tw.zhuran.madtom.server.MadPacketFactory;
import tw.zhuran.madtom.server.common.Packet;
import tw.zhuran.madtom.server.packet.MadPacket;

import java.util.List;

public class Connector {
    private EventLoopGroup group;
    private Bootstrap bootstrap = null;
    private Channel channel;
    private Client client;

    public void init() {
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) {
                        ch.pipeline().addLast(new JsonObjectDecoder()).addLast(new ClientHandler(client));
                    }
                })
                .option(ChannelOption.TCP_NODELAY, true);
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public ChannelFuture connect(String address, int port) throws InterruptedException {
        if (bootstrap == null) {
            init();
        }
        ChannelFuture channelFuture = bootstrap.connect(address, port).sync();
        channel = channelFuture.channel();
        return channelFuture;
    }

    public void send(MadPacket madPacket) {
        byte[] bytes = madPacket.bytes();
        ByteBuf byteBuf = Unpooled.copiedBuffer(bytes);
        channel.writeAndFlush(byteBuf);
    }

    public void shutdownGracefully() {
        group.shutdownGracefully();
        bootstrap = null;
    }
}

class ClientHandler extends MessageToMessageDecoder<ByteBuf> {

    private Client client;

    public ClientHandler(Client client) {
        this.client = client;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        String s = in.toString(Charsets.UTF_8);
        System.out.println(s);
        Packet packet = new MadPacketFactory().packet(s);
        client.handle((MadPacket) packet);
    }
}
