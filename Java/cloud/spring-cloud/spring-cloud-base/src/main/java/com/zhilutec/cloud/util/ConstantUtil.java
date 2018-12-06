package com.zhilutec.cloud.util;

public class ConstantUtil {

    //redis前缀
    public static String GW_KEY_PRE = "GATEWAY";
    public static String GW_SORT_KEY = "GWSET";
    public static Long REDIS_DEFAULT_TTL = -1L;

    //路由uri前缀
    public static String GW_HTTP_PRE = "http://";
    public static String GW_LB_PRE= "lb://";

    public static String GW_URI_HTTP = "HTTP";
    public static String GW_URI_LB = "LB";

    // 路由PredicateDefinition支持的类型有After、Before、Between、Cookie、Header、Host、Method、Path、Query、RemoteAddr
    public static String AFTER_ROUTE = "After";
    public static String BEFORE_ROUTE = "Before";
    public static String BETWEEN_ROUTE = "Between";
    public static String COOKIE_ROUTE = "Cookie";
    public static String HEADER_ROUTE = "Header";
    public static String HOST_ROUTE = "Host";
    public static String METHOD_ROUTE = "Method";
    public static String PATH_ROUTE = "Path";
    public static String QUERY_ROUTE = "Query";
    public static String REMOTE_ROUTE = "RemoteAddr";

    // 路由过滤，这里只例了19个,总共有21种
    public static String ADD_REQUEST_HEADER_FILTER = "AddRequestHeader";
    public static String ADD_REQUEST_PARAMETER_FILTER = "AddRequestParameter";
    public static String ADD_RESPONSE_HEADER_FILTER = "AddResponseHeader";
    public static String HYSTRIX_FILTER = "Hystrix";
    public static String PREFIX_PATH_FILTER = "PrefixPath";
    public static String PRESERVE_HOST_HEADER_FILTER = "PreserveHostHeader";
    public static String REUQEST_RATE_LIMITER_FILTER = "RequestRateLimiter";
    public static String REDIRECT_TO_FILTER = "RedirectTo";
    public static String REMOVE_NON_PROXY_HEADERS_FILTER = "RemoveNonProxyHeaders";
    public static String REMOVE_REQUEST_HEADER_FILTER = "RemoveRequestHeader";
    public static String REMOVE_RESPONSE_HEADER_FILTER = "RemoveResponseHeader";
    public static String REWRITE_PATH_FILTER = "RewritePath";
    public static String SAVE_SESSION_FILTER = "SaveSession";
    public static String SECURE_HEADERS_FILTER = "SecureHeaders ";
    public static String SET_PATH_FILTER = "SetPath";
    public static String SET_RESPONSE_HEADER_FILTER = "SetResponseHeader";
    public static String SET_STATUS_FILTER= "SetStatus";
    public static String STRIP_PREFIX_FILTER = "StripPrefix";
    public static String RETRY_FILTER = "Retry";

}
