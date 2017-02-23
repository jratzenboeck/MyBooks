package service;

import entity.Book;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class BookServiceTest {

    private final BookService bookService;

    public BookServiceTest() {
        this.bookService = new BookService();
    }

    @Test
    public void testFindBooksByCategory() throws InterruptedException {
        CompletableFuture<List<Book>> futureBooks = bookService.getBooksByCategoryAsync("flowers");
        futureBooks.handle((books, exception) -> {
            if (books != null) {
                System.out.println(books);
                Assert.assertEquals(10, books.size());
                return true;
            } else {
                System.out.println(exception.toString());
                Assert.fail();
                return false;
            }
        });

        System.out.println("Processing....");
        Thread.sleep(10000);
    }
}
