package com.svovnenko.crypto.service.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class RateLimitingFilter implements Filter {

    private static final int MAX_REQUESTS_PER_MINUTE = 10;
    private final Map<String, RequestCounter> requestCounts = new ConcurrentHashMap<>();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String clientIp = httpRequest.getRemoteAddr();
        long currentMinute = System.currentTimeMillis() / 60000;

        requestCounts.putIfAbsent(clientIp, new RequestCounter(currentMinute));
        RequestCounter counter = requestCounts.get(clientIp);

        synchronized (counter) {
            if (counter.minute != currentMinute) {
                counter.minute = currentMinute;
                counter.count.set(1);
            } else {
                counter.count.incrementAndGet();
            }

            System.out.println("RateLimitingFilter triggered for IP: " + clientIp + ", count: " + counter.count.get());

            if (counter.count.get() > MAX_REQUESTS_PER_MINUTE) {
                httpResponse.setStatus(429);
                httpResponse.getWriter().write("Too many requests. Please try again later.");
                return;
            }
        }

        chain.doFilter(request, response);
    }

    private static class RequestCounter {
        long minute;
        AtomicInteger count;

        RequestCounter(long minute) {
            this.minute = minute;
            this.count = new AtomicInteger(1);
        }
    }
}
