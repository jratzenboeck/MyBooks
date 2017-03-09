package service;

import com.fasterxml.jackson.databind.JsonNode;
import entity.*;
import exceptions.BookNotSavedException;
import exceptions.NoReadingActivitiesException;
import exceptions.ReadingActivityNotSavedException;
import repository.*;
import util.DatabaseUtils;
import util.HttpNoSuccessException;
import util.HttpUtils;
import util.PasswordHashing;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class MyBooksService extends Observable {

    private static final String API_KEY = "AIzaSyDd3sICzBWilfA9vV5uERysF-FEDFu5_mI";
    private static final String BASE_URL = "https://www.googleapis.com/books/v1/volumes?q=";
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ReadingActivityRepository readingActivityRepository;
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public MyBooksService() {
        final Connection dbConnection = DatabaseUtils.getDatabaseConnectionForProd();
        categoryRepository = new CategoryRepository(dbConnection);
        userRepository = new UserRepository(dbConnection);
        readingActivityRepository = new ReadingActivityRepository(dbConnection);
        bookRepository = new BookRepository(dbConnection);
        authorRepository = new AuthorRepository(dbConnection);
    }

    private String getUrlOfRequest(String query) {
        return BASE_URL + query + "&key=" + API_KEY;
    }

    public List<CompletableFuture<List<Book>>> getBooksByCategoryAsync(List<Category> categories) {
        return categories.stream().map(category -> {
            CompletableFuture<List<Book>> completableFuture = CompletableFuture
                    .supplyAsync(() -> {
                                try {
                                    return fetchBooksByCategory(category.getName());
                                } catch (IOException | HttpNoSuccessException e) {
                                    e.printStackTrace();
                                }
                                return new ArrayList<>();
                            },
                            Executors.newFixedThreadPool(10));
            return completableFuture;
        }).collect(Collectors.toList());
    }

    private List<Book> fetchBooksByCategory(String categoryName) throws IOException, HttpNoSuccessException {
        final String route = getUrlOfRequest("subject:" + categoryName);
        JsonNode root = HttpUtils.get(route);
        return extractBooksFromJsonPayload(root);
    }

    private List<Book> extractBooksFromJsonPayload(JsonNode root) {
        List<Book> books = new ArrayList<>();

        JsonNode items = root.get("items");
        items.forEach(item -> {
            JsonNode volumeInfo = item.get("volumeInfo");
            String title = volumeInfo.get("title").asText();
            int pageCount = volumeInfo.get("pageCount").asInt();
            String language = volumeInfo.get("language").asText();

            JsonNode saleInfo = item.get("saleInfo");
            JsonNode saleability = saleInfo.get("saleability");
            String retailPrice = "0.0";
            if ("FOR_SALE".equals(saleability.asText())) {
                retailPrice = saleInfo.get("retailPrice").get("amount").asText();
            }

            Book book = new Book(title, language, pageCount, Float.parseFloat(retailPrice));

            Optional<JsonNode> authorsNode = Optional.ofNullable(volumeInfo.get("authors"));
            authorsNode
                    .ifPresent(authors -> authors
                            .forEach(author -> book.addAuthor(new Author(author.asText()))));

            books.add(book);
        });

        return books;
    }

    public List<Category> getCategories() {
        return categoryRepository.findAll().orElse(new ArrayList<>());
    }

    public User registerUser(String username, char[] password, List<Category> readingInterests) {
        byte[] salt = PasswordHashing.generateSalt();
        UserCredentials credentials = new UserCredentials(
                PasswordHashing.hashPassword(password, salt), salt);
        User user = new User(username, credentials);
        user.addReadingInterests(readingInterests);
        return userRepository.save(user).orElseThrow(() -> new IllegalArgumentException("User could not be registered."));
    }

    public User loginUser(String username, char[] password) throws IllegalArgumentException {
        User user = userRepository.authenticate(username, password)
                .orElseThrow(() -> new IllegalArgumentException("Username or Password invalid"));
        user.addReadingInterests(categoryRepository
                .getReadingInterests(user.getId())
                .orElseGet(this::getCategories));
        return user;
    }

    public List<ReadingActivity> getMyCurrentReadingActivities(Long userId) throws NoReadingActivitiesException {
        return readingActivityRepository.getCurrentReadingActivities(userId)
                .orElseThrow(() -> new NoReadingActivitiesException("Reading activities could not be fetched"));
    }

    public ReadingActivity startReadingActivity(User user, Book book) throws ReadingActivityNotSavedException, BookNotSavedException {
        List<Author> bookAuthors = new ArrayList<>();
        book.getAuthors().forEach(author -> {
            Optional<Author> savedAuthorOptional = authorRepository.find(author.getName());
            if (!savedAuthorOptional.isPresent()) {
                savedAuthorOptional = authorRepository.save(author);
            }
            savedAuthorOptional.ifPresent(bookAuthors::add);
        });
        Optional<Book> savedBookOptional = bookRepository.find(book.getTitle());
        if (!savedBookOptional.isPresent()) {
            savedBookOptional = bookRepository.save(book);
        }
        Book savedBook = savedBookOptional.orElseThrow(() -> new BookNotSavedException("Book " + book.getTitle() + " could not be saved"));
        savedBook.getAuthors().addAll(bookAuthors);

        ReadingActivity readingActivity = readingActivityRepository.save(new ReadingActivity(user, savedBook))
                .orElseThrow(() -> new ReadingActivityNotSavedException("Reading activity could not be saved."));
        super.setChanged();
        super.notifyObservers(readingActivity);

        return readingActivity;
    }
}
