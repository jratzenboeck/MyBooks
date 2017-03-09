package service;

import entity.Book;
import entity.Category;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MyBooksServiceTest {

    private final MyBooksService bookService;

    public MyBooksServiceTest() {
        this.bookService = new MyBooksService();
    }

    @Test
    public void testFindBooksByCategory() throws InterruptedException {
        List<Category> categories = Arrays.asList(new Category("Sports"), new Category("Science"));

        List<CompletableFuture<List<Book>>> futureBooks = bookService.getBooksByCategoryAsync(categories);
//        futureBooks.stream()
//                .map(CompletableFuture::join)
//                .forEach(books -> Assert.assertEquals(10, books.size()));
        futureBooks.forEach(x -> x.thenAcceptAsync(System.out::println));

        System.out.println("Processing....");
        Thread.sleep(15000);
    }
}
