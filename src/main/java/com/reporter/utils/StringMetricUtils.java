package com.reporter.utils;

public abstract class StringMetricUtils {

    /**
     * Returns "similarity" of strings in Levenshtein metric
     *
     * @param str1
     * @param str2
     * @return - number of operations (insert/delete/replace) for
     * converting one string to another
     */
    public static int levenshteinDistance(String str1, String str2) {

        final int len1 = str1.length();
        final int len2 = str2.length();
        final int[][] distance = new int[len1 + 1][len2 + 1];

        for (int i = 0; i <= len1; i++) {
            distance[i][0] = i;
        }

        for (int j = 0; j <= len2; j++) {
            distance[0][j] = j;
        }

        for (int i = 1; i <= len1; i++) {
            for (int j = 1; j <= len2; j++) {
                final char ch1 = str1.charAt(i - 1);
                final char ch2 = str2.charAt(j - 1);
                final int offset = (ch1 == ch2) ? 0 : 1;
                distance[i][j] = Math.min(
                    Math.min(
                        distance[i - 1][j] + 1,
                        distance[i][j - 1] + 1
                    ),
                    distance[i - 1][j - 1] + offset
                );
            }
        }
        return distance[len1][len2];
    }
}
