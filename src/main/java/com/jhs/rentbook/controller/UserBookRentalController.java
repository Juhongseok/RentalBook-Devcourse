package com.jhs.rentbook.controller;

import com.jhs.rentbook.controller.dto.IdResponse;
import com.jhs.rentbook.controller.dto.rental.RentalBook;
import com.jhs.rentbook.controller.dto.rental.ReturnBookIds;
import com.jhs.rentbook.service.BookRentalService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserBookRentalController {

    private final BookRentalService service;

    @GetMapping("/rental/books")
    public List<RentalBook> getAllRentalBooks() {
        return new ArrayList<>();
    }

    @DeleteMapping("/rental/{rentalId}")
    public IdResponse returnRentalBook(@PathVariable Long rentalId, @RequestBody ReturnBookIds returnBookIds) {
        return new IdResponse(0L);
    }
}
