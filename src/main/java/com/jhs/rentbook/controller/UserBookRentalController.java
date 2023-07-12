package com.jhs.rentbook.controller;

import com.jhs.rentbook.controller.dto.IdResponse;
import com.jhs.rentbook.controller.dto.rental.RentalBook;
import com.jhs.rentbook.controller.dto.rental.RentalBookInfo;
import com.jhs.rentbook.controller.dto.rental.ReturnBookIds;
import com.jhs.rentbook.domain.rental.UserBookRental;
import com.jhs.rentbook.domain.rental.vo.UserBookRentalVo;
import com.jhs.rentbook.service.BookRentalService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserBookRentalController {

    private final BookRentalService service;

    @GetMapping("/rental/books")
    public List<RentalBook> getAllRentalBooks() {
        List<UserBookRental> rentals = service.findAllRentalInfo();
        Map<Long, List<RentalBookInfo>> map = rentalBookGroupByUserId(rentals);

        return rentals.stream()
                .map(UserBookRental::values)
                .map(value -> new RentalBook(value.userValue().userId(), value.userValue().username(), map.get(value.userValue().userId())))
                .toList();
    }

    private static Map<Long, List<RentalBookInfo>> rentalBookGroupByUserId(List<UserBookRental> rentals) {
        Map<Long, List<RentalBookInfo>> map = new HashMap<>();

        for (UserBookRental rental : rentals) {
            UserBookRentalVo value = rental.values();

            Long userId = value.userValue().userId();
            List<RentalBookInfo> list = map.getOrDefault(userId, new ArrayList<>());
            list.add(new RentalBookInfo(value.bookValue().id(), value.bookValue().name(), value.bookValue().type()));
            map.put(userId, list);
        }

        return map;
    }

    @DeleteMapping("/rental/{rentalId}")
    public IdResponse returnRentalBook(@PathVariable Long rentalId, @RequestBody ReturnBookIds returnBookIds) {
        return new IdResponse(service.returnBook(rentalId, returnBookIds));
    }
}
