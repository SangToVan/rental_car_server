package com.sangto.rental_car_server.service.impl;

import com.sangto.rental_car_server.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Service
@RequiredArgsConstructor
@EnableTransactionManagement
public class BookingServiceImpl implements BookingService {
}
