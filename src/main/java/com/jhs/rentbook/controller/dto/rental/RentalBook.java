package com.jhs.rentbook.controller.dto.rental;

import java.util.List;

public record RentalBook(Long userId, String username, List<RentalBookInfo> rentalList) {
}
