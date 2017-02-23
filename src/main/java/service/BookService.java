package service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import entity.Book;
import entity.Category;
import util.CalendarUtils;
import util.HttpNoSuccessException;
import util.HttpUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class BookService {

    private static final String API_KEY = "AIzaSyDd3sICzBWilfA9vV5uERysF-FEDFu5_mI";
    private static final String BASE_URL = "https://www.googleapis.com/books/v1/volumes?q=";
    private final ObjectMapper objectMapper;

    public BookService() {
        objectMapper = new ObjectMapper();
    }

    private String getUrlOfRequest(String query) {
        return BASE_URL + query + "&key=" + API_KEY;
    }

    public CompletableFuture<List<Book>> getBooksByCategoryAsync(String categoryName) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return fetchBooksByCategory(categoryName);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        });
    }

    List<Book> fetchBooksByCategory(String categoryName) {
        // TODO: Build url for given use case, execute HTTP GET call, parse JSON to List<Book>
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
            books.add(new Book(title, language, publishedDate, new Category(categoryName), pageCount, Float.parseFloat(retailPrice)));
        });

        return books;
    }
}
