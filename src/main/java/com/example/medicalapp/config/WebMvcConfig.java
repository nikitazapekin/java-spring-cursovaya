package com.example.medicalapp.config;


import com.example.medicalapp.middleware.DoctorAuthMiddleware;
import com.example.medicalapp.middleware.JwtAuthMiddleware;
import com.example.medicalapp.middleware.RefreshTokenMiddleware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private JwtAuthMiddleware jwtAuthMiddleware;

    @Autowired
    private DoctorAuthMiddleware doctorAuthMiddleware;

    @Autowired
    private RefreshTokenMiddleware refreshTokenMiddleware;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(refreshTokenMiddleware)
                .addPathPatterns("/api/auth/refresh");


/*registry.addInterceptor(doctorAuthMiddleware)
                .addPathPatterns("/api/doctors/**", "/api/doctor/**");


 */
        registry.addInterceptor(jwtAuthMiddleware)
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/auth/**", "/api/doctors/**", "/api/doctor/**");
    }
}