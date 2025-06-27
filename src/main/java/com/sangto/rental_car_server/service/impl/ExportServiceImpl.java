package com.sangto.rental_car_server.service.impl;

import com.sangto.rental_car_server.constant.TimeFormatConstant;
import com.sangto.rental_car_server.domain.entity.Booking;
import com.sangto.rental_car_server.domain.enums.EBookingStatus;
import com.sangto.rental_car_server.repository.BookingRepository;
import com.sangto.rental_car_server.service.ExportService;
import com.sangto.rental_car_server.utility.RentalCalculateUtil;
import com.sangto.rental_car_server.utility.TimeUtil;
import jakarta.servlet.ServletOutputStream;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@EnableTransactionManagement
public class ExportServiceImpl implements ExportService {

    public final BookingRepository bookingRepo;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");

    @Override
    public void exportRevenue(Integer ownerId, LocalDateTime startDate, LocalDateTime endDate, ServletOutputStream out) throws IOException {
        List<Booking> list = new ArrayList<>();
        String time = "";


        if (startDate != null && endDate != null) {
            list = bookingRepo.findBookingsByOwnerAndStatus(ownerId, startDate, endDate, EBookingStatus.COMPLETED);
            time = " từ " + startDate.format(FORMATTER) + " đến " + endDate.format(FORMATTER);
        } else {
            list = bookingRepo.findAllBookingsByOwnerAndStatus(ownerId, EBookingStatus.COMPLETED);
        }

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Revenue Report");

        // Title row
        String ownerName = list.isEmpty() ? "N/A" : list.getFirst().getCar().getCarOwner().getUsername();
        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("Thống kê doanh thu chủ xe: " + ownerName + time);
        titleCell.setCellStyle(createTitleCellStyle(workbook));
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5)); // Merge title cells

        // Header row
        Row headerRow = sheet.createRow(1);
        String[] headers = {"STT", "Tên xe", "Biển số", "Mã đơn", "Tổng hoá đơn", "Phí hệ thống", "Phí khác", "Thực nhận"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(createHeaderCellStyle(workbook));
        }

        // Data rows
        int rowNum = 2;
        int serialNumber = 1;
        BigDecimal totalSystemFee = BigDecimal.ZERO;
        BigDecimal totalInsuranceFee = BigDecimal.ZERO;
        BigDecimal totalRevenue = BigDecimal.ZERO;
        for (Booking booking : list) {
            Row row = sheet.createRow(rowNum++);
            Cell serialCell = row.createCell(0);
            serialCell.setCellValue(serialNumber++);
            serialCell.setCellStyle(createDataCellStyle(workbook));

            Cell carNameCell = row.createCell(1);
            carNameCell.setCellValue(booking.getCar().getName());
            carNameCell.setCellStyle(createDataCellStyle(workbook));

            Cell carLicensePlateCell = row.createCell(2);
            carLicensePlateCell.setCellValue(booking.getCar().getLicensePlate());
            carLicensePlateCell.setCellStyle(createDataCellStyle(workbook));

            Cell bookingIdCell = row.createCell(3);
            bookingIdCell.setCellValue(booking.getId());
            bookingIdCell.setCellStyle(createDataCellStyle(workbook));

            Cell totalPriceCell = row.createCell(4);
            totalPriceCell.setCellValue(booking.getTotalPrice().toString());
            totalPriceCell.setCellStyle(createDataCellStyle(workbook));

            BigDecimal insuranceFee = RentalCalculateUtil.calculateInsuranceFee(booking.getStartDateTime(), booking.getEndDateTime());
            BigDecimal systemFee = RentalCalculateUtil.calculateSystemFee(booking.getTotalPrice(), insuranceFee);
            BigDecimal revenue = (booking.getTotalPrice().subtract(insuranceFee)).subtract(systemFee);

            Cell systemFeeCell = row.createCell(5);
            systemFeeCell.setCellValue(systemFee.toString());
            systemFeeCell.setCellStyle(createDataCellStyle(workbook));

            Cell insuranceCell = row.createCell(6);
            insuranceCell.setCellValue(insuranceFee.toString());
            insuranceCell.setCellStyle(createDataCellStyle(workbook));

            Cell revenueCell = row.createCell(7);
            revenueCell.setCellValue(revenue.toString());
            revenueCell.setCellStyle(createDataCellStyle(workbook));

            totalSystemFee = totalSystemFee.add(systemFee);
            totalInsuranceFee = totalInsuranceFee.add(insuranceFee);
            totalRevenue = totalRevenue.add(revenue);
        }

        Row blankRow = sheet.createRow(rowNum++);
        blankRow.createCell(0).setCellValue("");

        Row totalRow = sheet.createRow(rowNum++);
        Cell totalLabelCell = totalRow.createCell(4);
        totalLabelCell.setCellValue("Tổng cộng:");
        totalLabelCell.setCellStyle(createHeaderCellStyle(workbook));

        // Total system fee
        Cell totalSystemFeeCell = totalRow.createCell(5);
        totalSystemFeeCell.setCellValue(totalSystemFee.toString());
        totalSystemFeeCell.setCellStyle(createDataCellStyle(workbook));

        // Total insurance fee
        Cell totalInsuranceFeeCell = totalRow.createCell(6);
        totalInsuranceFeeCell.setCellValue(totalInsuranceFee.toString());
        totalInsuranceFeeCell.setCellStyle(createDataCellStyle(workbook));

        // Total revenue fee
        Cell totalRevenueCell = totalRow.createCell(7);
        totalRevenueCell.setCellValue(totalRevenue.toString());
        totalRevenueCell.setCellStyle(createDataCellStyle(workbook));

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }


        workbook.write(out);
        workbook.close();
    }

    private CellStyle createHeaderCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    private CellStyle createTitleCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 14);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    private CellStyle createDataCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }
}
