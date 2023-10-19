package com.prorental.carrental;

//import org.modelmapper.ModelMapper;
//import org.modelmapper.config.Configuration;
//import org.modelmapper.convention.NamingConventions;
//import org.modelmapper.spi.NamingConvention;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;;


@SpringBootApplication
public class CarRentalServiceApplication {


    public static void main(String[] args) {
        SpringApplication.run(CarRentalServiceApplication.class, args);
    }

    //We tell spring to make an object of modelMapper and
    // throw it into the app context to use later.
    //We need modelMapper to convert entity class to DTO class.
    //User DTO ya ceviriyoruz
//    @Bean
//    public ModelMapper modelMapper(){
//        ModelMapper modelMapper = new ModelMapper();
//        modelMapper.getConfiguration()// we need this to arrange access points like private, public, protected
//                .setFieldMatchingEnabled(true)
//                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE)// access upto private
//                .setSourceNamingConvention(NamingConventions.JAVABEANS_MUTATOR);
//        return modelMapper;
//    }

}
