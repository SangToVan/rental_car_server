package com.sangto.rental_car_server.constant;

public class MailTemplate {
    public static final String PREFIX = "mail-template";

    public static class CANCEL_BOOKING {
        public static final String CANCEL_BOOKING_TEMPLATE = PREFIX + "/cancel-booking.html";
        public static final String CANCEL_BOOKING_SUBJECT = "A booking with your car has been cancelled";
        public static final String CANCEL_BOOKING_SUBJECT_2 = "Because you didn't pick up the car after 2 hours.Registration with your car has been canceled";
    }

    public static class CHANGE_PASSWORD {
        public static final String CHANGE_PASSWORD_TEMPLATE = PREFIX + "/change-password.html";
        public static final String CHANGE_PASSWORD_SUBJECT = "Password Reset";
    }

    public static class RENT_A_CAR {
        public static final String RENT_A_CAR_TEMPLATE = PREFIX + "/rent-a-car.html";
        public static final String RENT_A_CAR_SUBJECT = "Your car has been booked";
    }

    public static class CONFIRM_DEPOSIT {
        public static final String CONFIRM_DEPOSIT_TEMPLATE = PREFIX + "/confirm-deposit.html";
        public static final String CONFIRM_DEPOSIT_SUBJECT = "Deposit Confirmation";
    }

    public static class RETURN_A_CAR {
        public static final String RETURN_A_CAR_TEMPLATE = PREFIX + "/return-a-car.html";
        public static final String RETURN_A_CAR_SUBJECT = "Your car has been returned";
        public static final String RETURN_A_CAR_SUBJECT_2 = "Your car needs to be returned in 1 hour";
    }
}
