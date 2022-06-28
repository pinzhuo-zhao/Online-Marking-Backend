package com.feredback.feredback_backend.util;

import java.util.Collection;
import java.util.Objects;

/**
 * @program: FE-Redback
 * @description: utils class for SQL operation
 * @author: Xun Zhang (854776)
 * @date: 2022/5/7
 **/
public class ResultUtils {
    private static final int single = 1;
    private static final int percentage = 100;

    /**
     * Check if an operation on a single object works correctly
     *
     * @param rowAffected the number of affected row
     * @return if this operation works correctly
     */
    public static boolean singleCheck(int rowAffected) {
        return rowAffected == single;
    }

    /**
     * check if an collection object is empty
     *
     * @param collection collection object
     * @return if this collection object if empty
     */
    public static boolean isEmpty(Collection collection) {
        return Objects.isNull(collection) || collection.isEmpty();
    }

    /**
     * given mark of rubric item and its weighting, calculate the real mark in total
     * @param mark mark of rubric item
     * @param weighting weighting of rubric item
     * @return this part of real mark
     */
    public static double calculateMark(double mark, int maximum, int weighting) {
        return mark / maximum * weighting;
    }
}
