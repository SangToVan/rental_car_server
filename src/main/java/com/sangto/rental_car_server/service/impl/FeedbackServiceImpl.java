package com.sangto.rental_car_server.service.impl;

import com.sangto.rental_car_server.constant.MetaConstant;
import com.sangto.rental_car_server.domain.dto.feedback.AddFeedbackRequestDTO;
import com.sangto.rental_car_server.domain.dto.feedback.FeedbackResponseDTO;
import com.sangto.rental_car_server.domain.dto.meta.MetaRequestDTO;
import com.sangto.rental_car_server.domain.dto.meta.MetaResponseDTO;
import com.sangto.rental_car_server.domain.dto.meta.SortingDTO;
import com.sangto.rental_car_server.domain.entity.Booking;
import com.sangto.rental_car_server.domain.entity.Car;
import com.sangto.rental_car_server.domain.entity.Feedback;
import com.sangto.rental_car_server.domain.entity.User;
import com.sangto.rental_car_server.domain.enums.EBookingStatus;
import com.sangto.rental_car_server.domain.mapper.FeedbackMapper;
import com.sangto.rental_car_server.exceptions.AppException;
import com.sangto.rental_car_server.repository.CarRepository;
import com.sangto.rental_car_server.repository.FeedbackRepository;
import com.sangto.rental_car_server.repository.UserRepository;
import com.sangto.rental_car_server.responses.MetaResponse;
import com.sangto.rental_car_server.responses.Response;
import com.sangto.rental_car_server.service.BookingService;
import com.sangto.rental_car_server.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@EnableTransactionManagement
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepo;
    private final UserRepository userRepo;
    private final CarRepository carRepo;
    private final BookingService bookingService;
    private final FeedbackMapper feedbackMapper;

    @Override
    @Transactional
    public Feedback addFeedback(Integer userId, Integer bookingId, AddFeedbackRequestDTO requestDTO) {
        Booking booking = bookingService.verifyBookingCustomer(userId, bookingId);

        if (booking.getStatus() != EBookingStatus.RETURNED && booking.getStatus() != EBookingStatus.COMPLETED) {
            throw new AppException("This booking don't allow for feedback");
        }

        Optional<Feedback> findFeedback = feedbackRepo.findByBookingId(booking.getId());
        if (findFeedback.isPresent()) {
            Feedback feedback = findFeedback.get();
            feedback.setRating(requestDTO.rating());
            feedback.setContent(requestDTO.content());
            feedback.setCreatedAt(LocalDate.now());
            Feedback savedFeedback = feedbackRepo.save(feedback);

            Double ratingCar = feedbackRepo.getRatingByCarId(booking.getCar().getId());
            Car car = booking.getCar();
            car.setRating(ratingCar);
            carRepo.save(car);

            return savedFeedback;

        }
        Feedback newFeedback = feedbackMapper.addFeedbackRequestToEntity(requestDTO);
        newFeedback.setBooking(booking);
        Feedback savedFeedback = feedbackRepo.save(newFeedback);

        Double ratingCar = feedbackRepo.getRatingByCarId(booking.getCar().getId());
        Car car = booking.getCar();
        car.setRating(ratingCar);
        carRepo.save(car);

        return savedFeedback;
    }

    @Override
    public MetaResponse<MetaResponseDTO, List<FeedbackResponseDTO>> getListFeedbackByOwner(Integer ownerId, Integer rating, MetaRequestDTO metaRequestDTO) {
        Optional<User> findOwner = userRepo.findById(ownerId);
        if (findOwner.isEmpty()) throw new AppException("This user is not existed");

        String sortField = metaRequestDTO.sortField();
        if (sortField.compareTo(MetaConstant.Sorting.DEFAULT_FIELD) == 0) sortField = "feedback_id";
        Sort sort = metaRequestDTO.sortDir().equals(MetaConstant.Sorting.DEFAULT_DIRECTION)
                ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(metaRequestDTO.currentPage(), metaRequestDTO.pageSize(), sort);

        Page<Feedback> page = feedbackRepo.getListByOwnerId(ownerId, rating, pageable);

        List<FeedbackResponseDTO> liRes = page.getContent().stream()
                .map(feedbackMapper::toFeedbackResponseDTO)
                .toList();
        return MetaResponse.successfulResponse(
                "Get list feedbacks success",
                MetaResponseDTO.builder()
                        .totalItems((int) page.getTotalElements())
                        .totalPages(page.getTotalPages())
                        .currentPage(metaRequestDTO.currentPage())
                        .pageSize(metaRequestDTO.pageSize())
                        .sorting(SortingDTO.builder()
                                .sortField(metaRequestDTO.sortField())
                                .sortDir(metaRequestDTO.sortDir())
                                .build())
                        .build(),
                liRes);
    }

    @Override
    public MetaResponse<MetaResponseDTO, List<FeedbackResponseDTO>> getListFeedbackByCar(Integer carId, MetaRequestDTO metaRequestDTO) {
        Optional<Car> findCar = carRepo.findCarById(carId);
        if (findCar.isEmpty()) throw new AppException("This car is not existed");

        String sortField = metaRequestDTO.sortField();
        if (sortField.compareTo(MetaConstant.Sorting.DEFAULT_FIELD) == 0) sortField = "feedback_id";
        Sort sort = metaRequestDTO.sortDir().equals(MetaConstant.Sorting.DEFAULT_DIRECTION)
                ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(metaRequestDTO.currentPage(), metaRequestDTO.pageSize(), sort);

        Page<Feedback> page = feedbackRepo.getPageByCarId(carId, pageable);

        List<FeedbackResponseDTO> liRes = page.getContent().stream()
                .map(feedbackMapper::toFeedbackResponseDTO)
                .toList();
        return MetaResponse.successfulResponse(
                "Get list feedbacks success",
                MetaResponseDTO.builder()
                        .totalItems((int) page.getTotalElements())
                        .totalPages(page.getTotalPages())
                        .currentPage(metaRequestDTO.currentPage())
                        .pageSize(metaRequestDTO.pageSize())
                        .sorting(SortingDTO.builder()
                                .sortField(metaRequestDTO.sortField())
                                .sortDir(metaRequestDTO.sortDir())
                                .build())
                        .build(),
                liRes);
    }

    @Override
    public Response<Map<String, String>> getRating(Integer carId) {
        Double rating = feedbackRepo.getRatingByCarId(carId);
        // Kiểm tra null và gán giá trị mặc định
        String ratingValue = (rating != null) ? rating.toString() : "0.0";
        Map<String, String> res = Map.ofEntries(Map.entry("rating", ratingValue));
        return Response.successfulResponse("Get rating for car success", res);
    }
}
