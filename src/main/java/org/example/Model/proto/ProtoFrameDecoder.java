package org.example.Model.proto;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class ProtoFrameDecoder extends LengthFieldBasedFrameDecoder {

    public ProtoFrameDecoder() {
        this(1024*1014, 12, 4, 0, 0);
    }

    public ProtoFrameDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }
}
