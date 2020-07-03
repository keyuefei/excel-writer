package org.keyuefei.write.converters;


import org.keyuefei.write.converters.bigdecimal.BigDecimalNumberConverter;
import org.keyuefei.write.converters.booleanconverter.BooleanBooleanConverter;
import org.keyuefei.write.converters.byteconverter.ByteNumberConverter;
import org.keyuefei.write.converters.date.DateStringConverter;
import org.keyuefei.write.converters.doubleconverter.DoubleNumberConverter;
import org.keyuefei.write.converters.floatconverter.FloatNumberConverter;
import org.keyuefei.write.converters.integer.IntegerNumberConverter;
import org.keyuefei.write.converters.longconverter.LongNumberConverter;
import org.keyuefei.write.converters.shortconverter.ShortNumberConverter;
import org.keyuefei.write.converters.string.StringStringConverter;

import java.util.HashMap;
import java.util.Map;

public class DefaultConverterLoader {
    private static Map<String, Converter> defaultWriteConverter;

    static {
        initDefaultWriteConverter();
    }

    private static void initDefaultWriteConverter() {
        defaultWriteConverter = new HashMap<>(32);
        putWriteConverter(new BigDecimalNumberConverter());
        putWriteConverter(new BooleanBooleanConverter());
        putWriteConverter(new ByteNumberConverter());
        putWriteConverter(new DateStringConverter());
        putWriteConverter(new DoubleNumberConverter());
        putWriteConverter(new FloatNumberConverter());
        putWriteConverter(new IntegerNumberConverter());
        putWriteConverter(new LongNumberConverter());
        putWriteConverter(new ShortNumberConverter());
        putWriteConverter(new StringStringConverter());
    }

    /**
     * Load default write converter
     *
     * @return
     */
    public static Map<String, Converter> loadDefaultWriteConverter() {
        return defaultWriteConverter;
    }

    private static void putWriteConverter(Converter converter) {
        defaultWriteConverter.put(ConverterKeyBuild.buildKey(converter.supportJavaTypeKey()), converter);
    }


}
