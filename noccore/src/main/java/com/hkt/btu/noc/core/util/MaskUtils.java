package com.hkt.btu.noc.core.util;

import org.apache.commons.lang3.StringUtils;

public class MaskUtils {

    private final static int DEFAULT_UNMASK_LENGTH = 4;
    private final static char DEFAULT_MASK_CHAR = '*';

    /**
     * <p>Left mask a String with a specified char.</p>
     *
     * <pre>
     * MaskUtils.leftMask("28833518", 0, '*')   = "********"
     * MaskUtils.leftMask("28833518", 4, '*')   = "****3518"
     * MaskUtils.leftMask("Y123456(7)", 4, '?') = "??????6(7)"
     * MaskUtils.leftMask("28833518", -1, '*')  = "28833518"
     * MaskUtils.leftMask(null, 4, '*')         = null
     * </pre>
     *
     * @param str   the String to mask out
     * @param unmaskLength   the length remains unmask starting
     *                       from the right of the String
     * @param maskChar  the char to mask with
     * @return  left-masked String, or original if no masking is necessary,
     *          if invalid param input.
     */
    public static String leftMask(final String str, final int unmaskLength, final char maskChar){
        if( unmaskLength < 0 ){
            return str;
        }

        int length = StringUtils.length(str);
        final int maskLength = length - unmaskLength;
        if ( maskLength <= 0 ) {
            return str;
        }

        String masking = StringUtils.repeat("*", maskLength);
        String unmask = StringUtils.substring(str, length-unmaskLength, length);
        return masking + unmask;
    }
    public static String leftMask(final String str, final int unmaskLength){
        return leftMask(str, unmaskLength, DEFAULT_MASK_CHAR);
    }
    public static String leftMask(final String str){
        return leftMask(str, DEFAULT_UNMASK_LENGTH, DEFAULT_MASK_CHAR);
    }

}
