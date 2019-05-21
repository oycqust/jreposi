package com.utstar.integral.utils;

import com.utstar.integral.Exception.CoreException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.regex.Pattern;

/**
 * created by UTSC1244 at 2019/5/9
 */
public class Assert
{
    public static final String USERNAME_REGEX = "[a-zA-Z0-9_]{6,16}";

    public static final String PASSWOED_REGEX = "[a-zA-Z0-9_#$]{6,16}";

    public static void isTrue(boolean isTrue, String msg)
    {
        if(!isTrue) throw new CoreException(msg);
    }

    public static void isNotNull(Object o, String msg)
    {
        if(o == null) throw new CoreException(msg);
    }

    public static void isUsername(String username, String msg)
    {
        if(!Pattern.compile(USERNAME_REGEX).matcher(username).find())
        {
            throw new CoreException(msg);
        }
    }

    public static void isPassword(String username, String msg)
    {
        if(!Pattern.compile(PASSWOED_REGEX).matcher(username).find())
        {
            throw new CoreException(msg);
        }
    }

    public static void isNotEmpty(String string, String msg)
    {
        if(StringUtils.isBlank(string))
        {
            throw new CoreException(msg);
        }
    }

    public static void isNotEmpty(Collection collection, String msg)
    {
        if(CollectionUtils.isEmpty(collection))
        {
            throw new CoreException(msg);
        }
    }
}
