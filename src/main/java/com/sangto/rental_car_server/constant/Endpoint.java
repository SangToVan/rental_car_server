package com.sangto.rental_car_server.constant;

public class Endpoint {
    public static final class V1 {
        public static final String PREFIX = "/api/v1";

        public static final class Auth {
            public static final String BASE = PREFIX + "/auth";
            public static final String LOGIN = BASE + "/login";
            public static final String REGISTER = BASE + "/register";
            public static final String CHANGE_PASSWORD = BASE + "/change-password";
        }

        public static final class User {
            public static final String BASE = PREFIX + "/users";
            public static final String PROFILE = BASE + "/profile";;
        }
    }
}
