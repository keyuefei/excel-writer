package org.keyuefei.write.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * Collection utils
 *
 * @author jipengfei
 */
public class CollectionUtils {

    private CollectionUtils() {}

    public static boolean isEmpty(Collection<?> collection) {
        return (collection == null || collection.isEmpty());
    }

    public static boolean clone(Collection<?> collection) {
        Collection c = new ArrayList();
        for (Object o : collection) {

        }


        return (collection == null || collection.isEmpty());
    }


    public static boolean isEmpty(Map<?, ?> map) {
        return (map == null || map.isEmpty());
    }



}
