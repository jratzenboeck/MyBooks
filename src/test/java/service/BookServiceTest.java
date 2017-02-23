package service;

import entity.Book;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class BookServiceTest {

    private final BookService bookService;

    public BookServiceTest() {
        this.bookService = new BookService();
    }

    @Test
    public void testFetchBooksByCategory() {
        List<Book> books = bookService.fetchBooksByCategory("flowers");

        Assert.assertEquals(10, books.size());
    }
}
