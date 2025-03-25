package com.project.database_2b2t_org_ru.utils;

import java.util.*;

public class CommonUtils {
    public static <T> List<T> sortByField(List<T> list, Comparator<T> comparator) {
        if(!list.isEmpty())
            list.sort(comparator);
        return list;
    }
}
