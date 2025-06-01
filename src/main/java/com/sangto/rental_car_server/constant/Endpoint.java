package com.sangto.rental_car_server.constant;

public class Endpoint {
    public static final class V1 {
        public static final String PREFIX = "/api/v1";

        public static final class Home {
            public static final String BASE = PREFIX + "/home";
            public static final String CAR = BASE + "/car";
        }

        public static final class Admin {
            public static final String BASE = PREFIX + "/admin";
            public static final String CAR = BASE + "/cars";
            public static final String DETAIL_CAR = BASE + "/cars/{carId}";
            public static final String ADD_CAR = BASE + "/cars";
            public static final String UPDATE_CAR = BASE + "/cars/{carId}";
            public static final String VERIFY_CAR = BASE + "/cars/{carId}/verify";
            public static final String USER = BASE + "/users";
            public static final String ADD_USER = BASE + "/users";
            public static final String DETAIL_USER = BASE + "/users/{userId}";
            public static final String VERIFY_USER = BASE + "/users/{userId}/verify";
            public static final String UNVERIFY_USER = BASE + "/users/{userId}/unverify";
            public static final String BOOKING = BASE + "/bookings";
            public static final String DETAIL_BOOKING = BASE + "/bookings/{bookingId}";
            public static final String GET_WALLET = BASE + "/wallet";
            public static final String BRAND = BASE + "/brands";
            public static final String MODEL = BASE + "/models";
            public static final String DASHBOARD = BASE + "/dashboard";
            public static final String ESCROW = BASE + "/escrows";
        }

        public static final class Auth {
            public static final String BASE = PREFIX + "/auth";
            public static final String LOGIN = BASE + "/login";
            public static final String REGISTER = BASE + "/register";
            public static final String CHANGE_PASSWORD = BASE + "/change-password";
        }

        public static final class User {
            public static final String BASE = PREFIX + "/users";
            public static final String PROFILE = BASE + "/profile";
            public static final String CHANGE_ROLE = BASE + "/change-role";
            public static final String GET_WALLET = BASE + "/wallet";
            public static final String UPDATE_WALLET = BASE + "/wallet";
        }

        public static final class Car {
            public static final String BASE = PREFIX + "/cars";
            public static final String REGISTER = BASE + "/own/register/{carId}";
            public static final String GET_LIST_FOR_OWNER = BASE + "/own";
            public static final String DETAILS = BASE + "/{carId}";
            public static final String DETAILS_FOR_OWNER = BASE + "/own/{carId}";
            public static final String UPD_INFO = BASE + "/own/{carId}/info";
            public static final String UPD_PRICING = BASE + "/own/{carId}/pricing";
            public static final String STATUS = BASE + "/{carId}/status";
            public static final String LIST_CAR_BOOKINGS = BASE + "/own/{carId}/bookings";
        }

        public static final class Booking {
            public static final String BASE = PREFIX + "/bookings";
            public static final String LIST_FOR_USER = BASE + "/own";
            public static final String LIST_UNFINISHED = BASE + "/own/unfinished";
            public static final String LIST_FINISHED = BASE + "/own/finished";
            public static final String STATISTICS = BASE + "/statistics";
            public static final String DETAILS = BASE + "/{bookingId}";
            public static final String PAYMENT_BOOKING = BASE + "/{bookingId}/payment-booking";
            public static final String CONFIRM_BOOKING = BASE + "/{bookingId}/confirm-booking";
            public static final String CONFIRM_PICK_UP = BASE + "/{bookingId}/confirm-pickup";
            public static final String CONFIRM_RETURN = BASE + "/{bookingId}/confirm-return";
            public static final String COMPLETE_BOOKING = BASE + "/{bookingId}/complete-booking";
            public static final String CANCELLED_BOOKING = BASE + "/{bookingId}/cancel";
            public static final String FEEDBACK = BASE + "/{bookingId}/feedback";
        }

        public static final class Payment {
            public static final String BASE = PREFIX + "/payments";
            public static final String VNPAY_RETURN = BASE + "/vnpay_return";
        }

        public static final class Transaction {
            public static final String BASE = PREFIX + "/transactions";
        }

        public static final class Feedback {
            public static final String BASE = PREFIX + "/feedbacks";
            public static final String LIST_FOR_CAR = BASE + "/cars/{feedbackId}";
            public static final String GET_RATING = BASE + "/{feedbackId}/rating";
        }

        public static final class EscrowTransaction {
            public static final String BASE = PREFIX + "/escrow-transactions";
        }
    }
}
