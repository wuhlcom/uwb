// package com.zhilutec.config.security;
//
// import org.springframework.security.web.util.matcher.RegexRequestMatcher;
// import org.springframework.security.web.util.matcher.RequestMatcher;
//
// import javax.servlet.http.HttpServletRequest;
// import java.util.List;
// import java.util.regex.Pattern;
//
// public class CsrfSecurityRequestMatcher implements RequestMatcher {
//     /**
//      * 需要排除的url列表
//      */
//     private List<String> execludeUrls;
//
//     private RegexRequestMatcher unprotectedMatcher = new RegexRequestMatcher("^/rest/.*", null);
//
//     // @Override
//     // public boolean matches(HttpServletRequest request) {
//     //     if(allowedMethods.matcher(request.getMethod()).matches()){
//     //         return false;
//     //     }
//     //
//     //     return !unprotectedMatcher.matches(request);
//     // }
//
//     private Pattern allowedMethods = Pattern
//             .compile("^(GET|HEAD|TRACE|OPTIONS)$");
//
//     @Override
//     public boolean matches(HttpServletRequest request) {
//
//         if (execludeUrls != null && execludeUrls.size() > 0) {
//             String servletPath = request.getServletPath();
//             for (String url : execludeUrls) {
//                 if (servletPath.contains(url)) {
//                     return false;
//                 }
//             }
//         }
//         return !allowedMethods.matcher(request.getMethod()).matches();
//     }
//
//
//     public List<String> getExecludeUrls() {
//         return execludeUrls;
//     }
//
//     public void setExecludeUrls(List<String> execludeUrls) {
//         this.execludeUrls = execludeUrls;
//     }
// }