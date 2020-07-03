package org.keyuefei.write.metadata.holder;


import org.keyuefei.write.converters.Converter;

import java.util.Map;

public interface WriteHolder  {

    /**
     * Is to determine if a field needs to be ignored
     *
     * @param fieldName
     * @return
     */
    boolean ignore(String fieldName);


    /**
     * Writes the head relative to the existing contents of the sheet. Indexes are zero-based.
     *
     * @return
     */
    int relativeHeadRowIndex();



    Map<String, Converter> converterMap();
}
