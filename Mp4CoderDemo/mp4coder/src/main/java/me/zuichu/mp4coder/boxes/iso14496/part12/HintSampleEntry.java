package me.zuichu.mp4coder.boxes.iso14496.part12;


import me.zuichu.mp4coder.tools.IsoTypeWriter;
import me.zuichu.mp4coder.BoxParser;
import me.zuichu.mp4coder.boxes.sampleentry.AbstractSampleEntry;
import me.zuichu.mp4coder.tools.CastUtils;
import me.zuichu.mp4coder.tools.IsoTypeReader;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

public class HintSampleEntry extends AbstractSampleEntry {
    protected byte[] data;

    public HintSampleEntry(String type) {
        super(type);
    }

    @Override
    public void parse(ReadableByteChannel dataSource, ByteBuffer header, long contentSize, BoxParser boxParser) throws IOException {
        ByteBuffer b1 = ByteBuffer.allocate(8);
        dataSource.read(b1);
        b1.position(6);
        dataReferenceIndex = IsoTypeReader.readUInt16(b1);
        data = new byte[CastUtils.l2i(contentSize - 8)];
        dataSource.read(ByteBuffer.wrap(data));
    }

    @Override
    public void getBox(WritableByteChannel writableByteChannel) throws IOException {
        writableByteChannel.write(getHeader());

        ByteBuffer byteBuffer = ByteBuffer.allocate(8);
        byteBuffer.position(6);
        IsoTypeWriter.writeUInt16(byteBuffer, dataReferenceIndex);
        byteBuffer.rewind();
        writableByteChannel.write(byteBuffer);
        writableByteChannel.write(ByteBuffer.wrap(data));
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }


    @Override
    public long getSize() {
        long s = 8 + data.length;
        return s + ((largeBox || (s + 8) >= (1L << 32)) ? 16 : 8);
    }
}