package com.prorental.carrental.helper;

import com.prorental.carrental.domain.Car;
import com.prorental.carrental.domain.Reservation;
import com.prorental.carrental.domain.User;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelHelper {
    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    static String[] HEADERS_USER = {"Id", "FirstName", "LastName", "PhoneNumber", "Email", "Address", "ZipCode", "Roles"};
    static String SHEET_USER = "Customers";

    static String[] HEADERS_CAR = {"Id", "Model", "Doors", "Seats", "Luggage", "Transmission", "AirConditioning",
            "Age", "pricePerDay", "FuelType"};
    static String SHEET_CAR = "Cars";

    static String[] HEADERS_RESERVATION = {"Id", "CarId", "CarModel", "CustomerId", "CustomerFullName",
            "CustomerPhone", "PickUpTime", "DropOffTime", "PickUpLocation", "DropOfLocation", "Status"};
    static String SHEET_RESERVATION = "Reservations";


    public static ByteArrayInputStream usersExcel(List<User> users) {
        try (
                Workbook workBook = new XSSFWorkbook();
                //out is to write data
                ByteArrayOutputStream out = new ByteArrayOutputStream();
        ) {
            // Create a new Excel workbook (XSSFWorkbook) and an output stream.

            Sheet sheet = workBook.createSheet(SHEET_USER);
            // Create a new sheet in the workbook with the name "SHEET_USER."

            Row headerRow = sheet.createRow(0);
            // Create a row at the top of the sheet (row 0) to hold column headers.

            for (int column = 0; column < HEADERS_USER.length; column++) {
                // Iterate over the column headers specified in HEADERS_USER.
                Cell cell = headerRow.createCell(column);
                cell.setCellValue(HEADERS_USER[column]);
                // Create cells in the header row and set the header labels.
            }

            int rowId = 1;
            // Start from row 1 to populate user data.
            for (User user : users) {
                //rowId is increased after making a new row.
                //So row 1 is made then rowId is increased.
                Row row = sheet.createRow(rowId++);
                // Create a new row for each user.

                // Populate user data into the cells.
                row.createCell(0).setCellValue(user.getId());
                row.createCell(1).setCellValue(user.getFirstName());
                row.createCell(2).setCellValue(user.getLastName());
                row.createCell(3).setCellValue(user.getPhoneNumber());
                row.createCell(4).setCellValue(user.getEmail());
                row.createCell(5).setCellValue(user.getAddress());
                row.createCell(6).setCellValue(user.getZipCode());
                row.createCell(7).setCellValue(user.getRoles().toString());
            }

            workBook.write(out);
            // Write the workbook to the output stream.

            return new ByteArrayInputStream(out.toByteArray());
            // Create an InputStream from the output stream's data to read the data.
        } catch (IOException e) {
            throw new RuntimeException("Failed to import data to Excel file: " + e.getMessage());
            // In case of an exception, throw a runtime exception with an error message.
        }
    }


    public static ByteArrayInputStream carsExcel(List<Car> cars){
       try( Workbook workbook = new XSSFWorkbook();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ) {
           Sheet sheet = workbook.createSheet(SHEET_CAR);
           Row headerRow = sheet.createRow(0);
           int column = 0;
           for (String header : HEADERS_CAR) {
               headerRow.createCell(column++).setCellValue(header);
           }

           int row = 1;
           for (Car car : cars) {
               Row rowdy = sheet.createRow(row++);
               rowdy.createCell(0).setCellValue(car.getId());
               rowdy.createCell(1).setCellValue(car.getModel());
               rowdy.createCell(2).setCellValue(car.getDoors());
               rowdy.createCell(3).setCellValue(car.getSeats());
               rowdy.createCell(4).setCellValue(car.getLuggage());
               rowdy.createCell(5).setCellValue(car.getTransmission());
               rowdy.createCell(6).setCellValue(car.getAirConditioning());
               rowdy.createCell(7).setCellValue(car.getAge());
               rowdy.createCell(8).setCellValue(car.getPricePerHour());
               rowdy.createCell(9).setCellValue(car.getFuelType());
           }
           workbook.write(out);
           return new ByteArrayInputStream(out.toByteArray());
       } catch (IOException e){
           throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
       }


    }


    public static ByteArrayInputStream reservationExcel(List<Reservation> reservations) {
        try(
                Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                ){
                Sheet sheet = workbook.createSheet(SHEET_RESERVATION);
                Row headerRow = sheet.createRow(0);

                int cellNo = 0;
                for(String reservation: HEADERS_RESERVATION){
                    headerRow.createCell(cellNo++).setCellValue(reservation);
                }

                int rowNo = 1;
                for(Reservation reservation: reservations){
                    Row row = sheet.createRow(rowNo++);
                    row.createCell(0).setCellValue(reservation.getId());
                    row.createCell(1).setCellValue(reservation.getCarId().getId());
                    row.createCell(2).setCellValue(reservation.getCarId().getModel());
                    row.createCell(3).setCellValue(reservation.getUserId().getId());
                    row.createCell(4).setCellValue(reservation.getUserId().getFullName());
                    row.createCell(5).setCellValue(reservation.getUserId().getPhoneNumber());
                    row.createCell(6).setCellValue(reservation.getPickUpTime().toString());
                    row.createCell(7).setCellValue(reservation.getDropOffTime().toString());
                    row.createCell(8).setCellValue(reservation.getPickUpLocation());
                    row.createCell(9).setCellValue(reservation.getDropOfLocation());
                    row.createCell(10).setCellValue(reservation.getStatus().toString());
                }
                //write the book to the outputStream and return a line below it.
                workbook.write(out);
                return new ByteArrayInputStream(out.toByteArray());

        }catch (IOException e){
            throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
        }



    }
}