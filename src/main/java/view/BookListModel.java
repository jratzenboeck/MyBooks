package view;

import entity.Book;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.util.List;

public class BookListModel extends AbstractListModel {

    private final List<Book> books;

    public BookListModel(List<Book> books) {
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

    public void addBooksToModel(List<Book> books) {
        this.books.addAll(books);
    }

    @Override
    public void fireContentsChanged(Object source, int startIndex, int endIndex) {
        super.fireContentsChanged(source, startIndex, endIndex);
    }
}
