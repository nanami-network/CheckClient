package xyz.n7mn.dev.checkclient.util.bytebuf;

import com.google.common.collect.Maps;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.IntFunction;

public class MinecraftByteBuf {

    private final ByteBuf buf;

    public MinecraftByteBuf(ByteBuf buf) {
        this.buf = buf;
    }

    public MinecraftByteBuf writeVarInt(int b) {
        while ((b & -128) != 0) {
            this.buf.writeByte(b & 127 | 128);
            b >>>= 7;
        }

        this.buf.writeByte(b);
        return this;
    }

    public int readVarInt() {
        int i = 0;
        int j = 0;

        byte b;

        do {
            b = buf.readByte();
            i |= (b & 127) << j++ * 7;
            if (j > 5) {
                throw new RuntimeException("VarInt too big");
            }
        } while((b & 128) == 128);

        return i;
    }

    public MinecraftByteBuf writeVarLong(long b) {
        while((b & -128) != 0) {
            this.writeVarLong(b & 127 | 128);
            b >>>= 7;
        }

        this.buf.writeByte((int) b);
        return this;
    }

    public long readVarLong() {
        long i = 0;
        int j = 0;

        byte b;

        do {
            b = buf.readByte();
            i |= (long) (b & 127) << j++ * 7;
            if (j > 10) {
                throw new RuntimeException("VarLong too big");
            }
        } while((b & 128) == 128);

        return i;
    }

    public MinecraftByteBuf writeUUID(UUID uuid) {
        this.buf.writeLong(uuid.getMostSignificantBits());
        this.buf.writeLong(uuid.getLeastSignificantBits());

        return this;
    }

    public UUID readUUID() {
        return new UUID(this.buf.readLong(), this.buf.readLong());
    }

    public String readUtf() {
        return readUtf(Short.MAX_VALUE);
    }

    public String readUtf(int max) {
        int i = readVarInt();
        if (i > max * 4) {
            throw new DecoderException("The received encoded string buffer length is longer than maximum allowed (" + i + " > " + max * 4 + ")");
        } else if (i < 0) {
            throw new DecoderException("The received encoded string buffer length is less than zero! Weird string!");
        }
        String s = this.buf.toString(this.buf.readerIndex(), i, StandardCharsets.UTF_8);
        this.buf.readerIndex(this.buf.readerIndex() + i);
        if (s.length() > max) {
            throw new DecoderException("The received string length is longer than maximum allowed (" + i + " > " + max + ")");
        } else {
            return s;
        }
    }

    public MinecraftByteBuf writeUtf(String b) {
        writeUtf(b, Short.MAX_VALUE);
        return this;
    }

    public MinecraftByteBuf writeUtf(String b, int max) {
        byte[] bytes = b.getBytes(StandardCharsets.UTF_8);

        if (bytes.length > max) {
            throw new EncoderException("String too big (was " + bytes.length + " bytes encoded, max " + max + ")");
        } else {
            this.writeVarInt(bytes.length);
            this.buf.writeBytes(bytes);
            return this;
        }
    }

    public <K, V> void writeMap(Map<K, V> map, MinecraftByteBuf.Writer<K> p_236833_, MinecraftByteBuf.Writer<V> p_236834_) {
        this.writeVarInt(map.size());
        map.forEach((p_236856_, p_236857_) -> {
            p_236833_.accept(this, p_236856_);
            p_236834_.accept(this, p_236857_);
        });
    }

    public <K, V, M extends Map<K, V>> M readMap(IntFunction<M> maps, MinecraftByteBuf.Reader<K> reader1,  MinecraftByteBuf.Reader<V> reader2) {
        int i = this.readVarInt();
        M m = maps.apply(i);

        for (int j = 0; j < i; ++j) {
            K k = reader1.apply(this);
            V v = reader2.apply(this);
            m.put(k, v);
        }

        return m;
    }

    public <K, V> Map<K, V> readMap(MinecraftByteBuf.Reader<K> reader1, MinecraftByteBuf.Reader<V> reader2) {
        return this.readMap(Maps::newHashMapWithExpectedSize, reader1, reader2);
    }

    public <T> Optional<T> readOptional(MinecraftByteBuf.Reader<T> p_236861_) {
        return this.buf.readBoolean() ? Optional.of(p_236861_.apply(this)) : Optional.empty();
    }

    public <T> void writeOptional(Optional<T> optional, MinecraftByteBuf.Writer<T> writer) {
        if (optional.isPresent()) {
            this.buf.writeBoolean(true);
            writer.accept(this, optional.get());
        } else {
            this.buf.writeBoolean(false);
        }
    }

    public ByteBuf getBuf() {
        return buf;
    }

    @FunctionalInterface
    public interface Reader<T> extends Function<MinecraftByteBuf, T> {
        default MinecraftByteBuf.Reader<Optional<T>> asOptional() {
            return (buf) -> buf.readOptional(this);
        }
    }

    @FunctionalInterface
    public interface Writer<T> extends BiConsumer<MinecraftByteBuf, T> {
        default MinecraftByteBuf.Writer<Optional<T>> asOptional() {
            return (buf, optional) -> buf.writeOptional(optional, this);
        }
    }
}