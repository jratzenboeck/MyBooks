package service;

import com.fasterxml.jackson.databind.JsonNode;
import entity.*;
import repository.CategoryRepository;
import repository.UserRepository;
import util.*;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

public class MyBooksService {

    private static final String API_KEY = "AIzaSyDd3sICzBWilfA9vV5uERysF-FEDFu5_mI";
    private static final String BASE_URL = "https://www.googleapis.com/books/v1/volumes?q=";
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public MyBooksService() {
        final Connection dbConnection = DatabaseUtils.getDatabaseConnectionForProd();
        categoryRepository = new CategoryRepository(dbConnection);
        userRepository = new UserRepository(dbConnection);
    }

    private String getUrlOfRequest(String query) {
        return BASE_URL + query + "&key=" + API_KEY;
    }

    public CompletableFuture<List<Book>> getBooksByCategoryAsync(String categoryName) {
        return CompletableFuture
                .supplyAsync(() -> fetchBooksByCategory(categoryName),
                        Executors.newFixedThreadPool(10));
    }

    private List<Book> fetchBooksByCategory(String categoryName) {
        final String route = getUrlOfRequest("subject:" + categoryName);
        try {
            JsonNode root = HttpUtils.get(route);
            return extractBooksFromJsonPayload(root, categoryName);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (HttpNoSuccessException e) {
            System.out.println(e);
        }
        return null;
    }

    private List<Book> extractBooksFromJsonPayload(JsonNode root, String categoryName) {
        List<Book> books = new ArrayList<>();

        JsonNode items = root.get("items");
        items.forEach(item -> {
            JsonNode volumeInfo = item.get("volumeInfo");
            String title = volumeInfo.get("title").asText();
            Calendar publishedDate = CalendarUtils.parseCalendar(volumeInfo.get("publishedDate").asText());
            int pageCount = volumeInfo.get("pageCount").asInt();
            String language = volumeInfo.get("language").asText();

            JsonNode saleability = item.get("saleInfo").get("saleability");
            String retailPrice = "0.0";
            if ("FOR_SALE".equals(saleability)) {
                retailPrice = saleability.get("amount").asText();
            }

            Book book = new Book(title, language, publishedDate, new Category(categoryName), pageCount, Float.parseFloat(retailPrice));

            JsonNode authors = volumeInfo.get("authors");
            authors.forEach(author -> book.addAuthor(new Author(author.asText())));

            books.add(book);
        });

        return books;
    }

    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    public User registerUser(String username, char[] password, List<Category> readingInterests) {
        byte[] salt = PasswordHashing.generateSalt();
        UserCredentials credentials = new UserCredentials(
                PasswordHashing.hashPassword(password, salt), salt);
        User user = new User(username, credentials);
        user.addReadingInterests(readingInterests);
        return userRepository.save(user);
    }

    public User loginUser(String username, char[] password) {
        return userRepository.authenticate(username, password);
    }
}
