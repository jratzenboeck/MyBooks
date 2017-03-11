package view;

import entity.Book;
import entity.ReadingActivity;
import entity.User;
import exceptions.BookNotSavedException;
import exceptions.NoReadingActivitiesException;
import exceptions.ReadingActivityNotSavedException;
import service.MyBooksService;
import util.CalendarUtils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class MainView extends JFrame implements Observer {

    private MyBooksService booksService;
    private final User user;
    private JPanel myBooks;
    private JList listBooks;
    private JButton bPrev;
    private JButton bNext;
    private JLabel lReadingActivityTitle;
    private JLabel lReadingActivityStartReading;
    private GridBagConstraints constraints;
    private List<ReadingActivity> readingActivities;
    private int currentReadingActivityIndex;
    private BookListModel bookListModel;

    MainView(User user) {
        this.user = user;
        initReadingActivities();

        prepareGUI();
    }

    private void initReadingActivities() {
        booksService = new MyBooksService();
        booksService.addObserver(this);
        currentReadingActivityIndex = 0;
        try {
            readingActivities = booksService
                    .getMyCurrentReadingActivities(user.getId());
        } catch (NoReadingActivitiesException e) {
            JOptionPane.showMessageDialog(this, "Your reading activities could not be loaded", "Error while loading reading activities", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void prepareGUI() {
        this.setTitle("MyBooks - " + user.getUsername());
        this.setSize(1000, 800);
        Container contentPane = getContentPane();

        JPanel interestingBooks = new JPanel(new BorderLayout(10, 10));

        JLabel lHeaderInterestingBooks = new JLabel("Books you could be interested in");
        lHeaderInterestingBooks.setPreferredSize(new Dimension(300, 20));
        interestingBooks.add(lHeaderInterestingBooks, BorderLayout.NORTH);

        bookListModel = new BookListModel(new ArrayList<>());
        listBooks = new JList(bookListModel);
        listBooks.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        listBooks.setLayoutOrientation(JList.VERTICAL);
        listBooks.setVisibleRowCount(10);

        JScrollPane scrollPane = new JScrollPane(listBooks);
        interestingBooks.add(scrollPane, BorderLayout.CENTER);

        prepareInterestingBooks();

        myBooks = new JPanel();
        myBooks.setLayout(new GridBagLayout());
        constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.weightx = 0.5;

        JLabel lMyCurrentBooks = new JLabel("Your current Books");
        lMyCurrentBooks.setPreferredSize(new Dimension(300, 20));
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        myBooks.add(lMyCurrentBooks, constraints);

        bPrev = new JButton("<");
        bPrev.setEnabled(false);
        bNext = new JButton(">");
        bNext.setEnabled(false);

        showFirstReadingActivity();
        showReadingDiaryButtons();
        showControlButtons();

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, interestingBooks, myBooks);
        splitPane.setDividerLocation(550);

        contentPane.add(splitPane);

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    private void prepareInterestingBooks() {
        booksService.getBooksByCategoryAsync(user.getReadingInterests())
                .forEach(completableFuture ->
                        completableFuture.thenAcceptAsync(this::showBookInformation));
    }

    private void showFirstReadingActivity() {
        lReadingActivityTitle = new JLabel();
        lReadingActivityTitle.setPreferredSize(new Dimension(300, 20));
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        myBooks.add(lReadingActivityTitle, constraints);

        lReadingActivityStartReading = new JLabel();
        lReadingActivityStartReading.setPreferredSize(new Dimension(300, 20));
        constraints.gridy = 2;
        myBooks.add(lReadingActivityStartReading, constraints);

        showReadingActivity();
    }

    private void showReadingActivity() {
        String currentReadingActivityTitle = "No current Books";

        if (!readingActivities.isEmpty()) {
            bPrev.setEnabled(true);
            bNext.setEnabled(true);

            if (currentReadingActivityIndex >= readingActivities.size()) {
                currentReadingActivityIndex = 0;
            } else if (currentReadingActivityIndex < 0) {
                currentReadingActivityIndex = readingActivities.size() - 1;
            }
            ReadingActivity readingActivity = readingActivities.get(currentReadingActivityIndex);
            currentReadingActivityTitle = readingActivity.toString();
            lReadingActivityStartReading.setText("Started reading at: " +
                    CalendarUtils.formatDate(readingActivity.getStartReading().getTime()));
        }
        lReadingActivityTitle.setText(currentReadingActivityTitle);
    }

    private void showControlButtons() {
        constraints.gridx = 0;
        constraints.gridwidth = 1;

        bPrev.setPreferredSize(new Dimension(100, 20));
        bPrev.addActionListener((event) -> showNextReadingActivity());
        constraints.gridy = 4;
        myBooks.add(bPrev, constraints);

        bPrev.setPreferredSize(new Dimension(100, 20));
        bNext.addActionListener((event) -> showPrevReadingActivity());
        constraints.gridx = 1;
        myBooks.add(bNext, constraints);
    }

    private void showReadingDiaryButtons() {
        constraints.gridy = 3;
        constraints.gridwidth = 1;

        JButton bAddDiaryEntry = new JButton("Add reading diary entry");
        bAddDiaryEntry.setPreferredSize(new Dimension(100, 20));
        bAddDiaryEntry.addActionListener((l) ->
                new AddReadingDiaryEntryDialog(readingActivities
                        .get(currentReadingActivityIndex)));
        constraints.gridx = 0;
        myBooks.add(bAddDiaryEntry, constraints);

        JButton bShowDiaryEntries = new JButton(("Show reading diary"));
        bShowDiaryEntries.setPreferredSize(new Dimension(100, 20));
        // TODO: Add action listener
        constraints.gridx = 1;
        myBooks.add(bShowDiaryEntries, constraints);
    }

    private void showNextReadingActivity() {
        currentReadingActivityIndex++;
        showReadingActivity();
    }

    private void showPrevReadingActivity() {
        currentReadingActivityIndex--;
        showReadingActivity();
    }

    private void showBookInformation(List<Book> books) {
        int startIndex = bookListModel.getSize();
        bookListModel.addBooksToModel(books);

        if (bookListModel.getSize() > 0) {
            listBooks.setModel(bookListModel);
            bookListModel.fireContentsChanged(this, startIndex, bookListModel.getSize());

            listBooks.addListSelectionListener((e) -> {
                Book selectedBook = (Book) bookListModel.getElementAt(e.getLastIndex());
                int response = JOptionPane.showConfirmDialog(listBooks,
                        selectedBook.getDetailsFormatted() +
                                "\n\nWould you like to start reading this book?",
                        "Book Details",
                        JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    try {
                        booksService.startReadingActivity(user, selectedBook);
                    } catch (ReadingActivityNotSavedException | BookNotSavedException e1) {
                        JOptionPane.showMessageDialog(this,
                                e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof ReadingActivity) {
            readingActivities.add((ReadingActivity) arg);
            currentReadingActivityIndex = readingActivities.size() - 1;
            showReadingActivity();
        }
    }
}