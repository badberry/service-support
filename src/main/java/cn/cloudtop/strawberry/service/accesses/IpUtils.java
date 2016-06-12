package cn.cloudtop.strawberry.service.accesses;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by jackie on 16-4-24
 */
public class IpUtils {
    /**
     * 从request对象获取IP
     *
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
    }

    /**
     * 将IP转为256进制整数
     *
     * @param ip
     * @return
     */
    public static long convertIpToLong(String ip) {
        String[] checkIp = ip.split("\\.", 4);
        long intIp = 0;

        for (int i = 3, j = 0; i >= 0 && j <= 3; i--, j++) {
            intIp += Integer.parseInt(checkIp[j]) * Math.pow(256, i);
        }
        return intIp;
    }

    public static String convertLongToIp(long ipaddress) {
        StringBuffer sb = new StringBuffer("");
        // 直接右移24位
        sb.append(String.valueOf((ipaddress >>> 24)));
        sb.append(".");
        // 将高8位置0，然后右移16位
        sb.append(String.valueOf((ipaddress & 0x00FFFFFF) >>> 16));
        sb.append(".");
        // 将高16位置0，然后右移8位
        sb.append(String.valueOf((ipaddress & 0x0000FFFF) >>> 8));
        sb.append(".");
        // 将高24位置0
        sb.append(String.valueOf((ipaddress & 0x000000FF)));
        return sb.toString();
    }

    public static String convertSubToIp(long ipaddress) {
        StringBuffer sb = new StringBuffer("");
        // 直接右移24位
        sb.append(String.valueOf((ipaddress & 0x000000FF)));
        sb.append(".");
        sb.append(String.valueOf((ipaddress & 0x0000FFFF) >>> 8));
        sb.append(".");
        sb.append(String.valueOf((ipaddress & 0x00FFFFFF) >>> 16));
        sb.append(".");
        sb.append(String.valueOf((ipaddress >>> 24)));
        // 将高8位置0，然后右移16位
        // 将高16位置0，然后右移8位
        // 将高24位置0

        return sb.toString();
    }

    private IpUtils() {
    }
}
