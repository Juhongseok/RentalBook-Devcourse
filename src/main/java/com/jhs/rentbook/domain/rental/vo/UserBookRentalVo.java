package com.jhs.rentbook.domain.rental.vo;

import com.jhs.rentbook.domain.book.vo.BookVo;
import com.jhs.rentbook.domain.user.vo.UserVo;

public record UserBookRentalVo(Long rentalId, UserVo userValue, BookVo bookValue) {
}
