package view;

import entity.Book;

import javax.swing.*;
import java.util.List;

public class BookListModel extends AbstractListModel {

    private final List<Book> books;

    BookListModel(List<Book> books) {
        this.books = books;
    }

    @Override
    public int getSize() {
        return books.size();
    }

    @Override
    public Object getElementAt(int index) {
        return books.get(index);
    }

    void addBooksToModel(List<Book> books) {
        this.books.addAll(books);
    }

    @Override
    public void fireContentsChanged(Object source, int startIndex, int endIndex) {
        super.fireContentsChanged(source, startIndex, endIndex);
    }
}
