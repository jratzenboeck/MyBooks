package view;

import entity.Category;

import javax.swing.*;
import java.util.List;

public class ReadingInterestListModel extends AbstractListModel {

    private final List<Category> readingInterests;

    ReadingInterestListModel(List<Category> readingInterests) {
        this.readingInterests = readingInterests;
    }

    @Override
    public int getSize() {
        return readingInterests.size();
    }

    @Override
    public Object getElementAt(int index) {
        return readingInterests.get(index);
    }
}
