package com.svnyoung.pubsub.serialize;

import com.svnyoung.pubsub.exception.SerializationException;
import com.svnyoung.pubsub.Serializer;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.pool.KryoFactory;
import com.esotericsoftware.kryo.pool.KryoPool;
import com.esotericsoftware.kryo.serializers.DefaultSerializers;
import de.javakaffee.kryoserializers.*;
import de.javakaffee.kryoserializers.cglib.CGLibProxySerializer;
import de.javakaffee.kryoserializers.guava.*;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.InvocationHandler;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * @author: sunyang
 * @date: 2019/8/15 9:36
 * @version: 1.0
 * @since: 1.0
 * @see:
 */
public class KryoSerializer implements Serializer {

    private boolean registrationRequired = false;
    private volatile boolean kryoCreated;
    private final Set<Class> registrations = new LinkedHashSet();

    public void setRegistrations(Class clazz) {
        if (!this.kryoCreated) {
            this.registrations.add(clazz);
        }
    }

    public void setRegistrationRequired(boolean registrationRequired) {
        this.registrationRequired = registrationRequired;
    }

    private final static int BYTE_BUFFER_SIZE = 4096;

    private KryoPool pool;

    public KryoSerializer() {
        this.pool = (new KryoPool.Builder(new DefaultKryoFactory())).softReferences().build();
    }

    @Override
    public <T> byte[] serialize(T object) throws SerializationException {
        return this.pool.run((Kryo kryo) -> {
            try {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                Output output = new Output(out, BYTE_BUFFER_SIZE);
                kryo.writeObject(output, object);
                return output.toBytes();
            } catch (Exception e) {
                throw new SerializationException("serialize failed", e);
            }
        });

    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> tClass) throws SerializationException {
        return this.pool.run((Kryo kryo) -> {
            try {
                Input input = new Input(bytes);
                return kryo.readObject(input, tClass);
            } catch (Exception var3) {
                throw new SerializationException("deserialize failed", var3);
            }
        });
    }

    class DefaultKryoFactory implements KryoFactory {

        @Override
        public Kryo create() {
            if (!KryoSerializer.this.kryoCreated) {
                KryoSerializer.this.kryoCreated = true;
            }

            //注册常用序列化类
            Kryo kryo = new KryoReflectionFactorySupport() {
                @Override
                public com.esotericsoftware.kryo.Serializer<?> getDefaultSerializer(Class clazz) {
                    if (EnumSet.class.isAssignableFrom(clazz)) {
                        return new EnumSetSerializer();
                    } else if (EnumMap.class.isAssignableFrom(clazz)) {
                        return new EnumMapSerializer();
                    } else if (Collection.class.isAssignableFrom(clazz)) {
                        return new CopyForIterateCollectionSerializer();
                    } else if (Map.class.isAssignableFrom(clazz)) {
                        return new CopyForIterateMapSerializer();
                    } else if (Date.class.isAssignableFrom(clazz)) {
                        return new DateSerializer(clazz);
                    } else {
                        return CGLibProxySerializer.canSerialize(clazz) ? this.getSerializer(CGLibProxySerializer.CGLibProxyMarker.class) : super.getDefaultSerializer(clazz);
                    }
                }
            };
            kryo.setReferences(false);
            kryo.setRegistrationRequired(KryoSerializer.this.registrationRequired);
            kryo.register(Arrays.asList("").getClass(), new ArraysAsListSerializer());
            kryo.register(Collections.EMPTY_LIST.getClass(), new CollectionsEmptyListSerializer());
            kryo.register(Collections.EMPTY_MAP.getClass(), new CollectionsEmptyMapSerializer());
            kryo.register(Collections.EMPTY_SET.getClass(), new CollectionsEmptySetSerializer());
            kryo.register(Collections.singletonList("").getClass(), new CollectionsSingletonListSerializer());
            kryo.register(Collections.singleton("").getClass(), new CollectionsSingletonSetSerializer());
            kryo.register(Collections.singletonMap("", "").getClass(), new CollectionsSingletonMapSerializer());
            kryo.register(GregorianCalendar.class, new GregorianCalendarSerializer());
            kryo.register(InvocationHandler.class, new JdkProxySerializer());
            kryo.register(BigDecimal.class, new DefaultSerializers.BigDecimalSerializer());
            kryo.register(BigInteger.class, new DefaultSerializers.BigIntegerSerializer());
            kryo.register(Pattern.class, new RegexSerializer());
            kryo.register(BitSet.class, new BitSetSerializer());
            kryo.register(URI.class, new URISerializer());
            kryo.register(UUID.class, new UUIDSerializer());
            UnmodifiableCollectionsSerializer.registerSerializers(kryo);
            SynchronizedCollectionsSerializer.registerSerializers(kryo);
            kryo.register(CGLibProxySerializer.CGLibProxyMarker.class, new CGLibProxySerializer());
            ImmutableListSerializer.registerSerializers(kryo);
            ImmutableSetSerializer.registerSerializers(kryo);
            ImmutableMapSerializer.registerSerializers(kryo);
            ImmutableMultimapSerializer.registerSerializers(kryo);
            ReverseListSerializer.registerSerializers(kryo);
            UnmodifiableNavigableSetSerializer.registerSerializers(kryo);
            ArrayListMultimapSerializer.registerSerializers(kryo);
            HashMultimapSerializer.registerSerializers(kryo);
            LinkedHashMultimapSerializer.registerSerializers(kryo);
            LinkedListMultimapSerializer.registerSerializers(kryo);
            TreeMultimapSerializer.registerSerializers(kryo);
            kryo.register(HashMap.class);
            kryo.register(ArrayList.class);
            kryo.register(LinkedList.class);
            kryo.register(HashSet.class);
            kryo.register(TreeSet.class);
            kryo.register(Hashtable.class);
            kryo.register(Date.class);
            kryo.register(Calendar.class);
            kryo.register(ConcurrentHashMap.class);
            kryo.register(SimpleDateFormat.class);
            kryo.register(GregorianCalendar.class);
            kryo.register(Vector.class);
            kryo.register(BitSet.class);
            kryo.register(StringBuffer.class);
            kryo.register(StringBuilder.class);
            kryo.register(Object.class);
            kryo.register(Object[].class);
            kryo.register(String[].class);
            kryo.register(byte[].class);
            kryo.register(char[].class);
            kryo.register(int[].class);
            kryo.register(float[].class);
            kryo.register(double[].class);
            Iterator classIterator = KryoSerializer.this.registrations.iterator();

            while (classIterator.hasNext()) {
                Class clazz = (Class) classIterator.next();
                kryo.register(clazz);
            }
            return kryo;
        }
    }
}