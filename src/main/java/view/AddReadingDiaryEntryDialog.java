package view;

import entity.ReadingActivity;
import entity.ReadingDiaryEntry;
import exceptions.ReadingDiaryEntryNotSaved;
import service.MyBooksService;
import util.CalendarUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Calendar;

class AddReadingDiaryEntryDialog extends JDialog {

    private final JTextArea tDiaryEntry;
    private final MyBooksService booksService;
    private final ReadingActivity activity;

    AddReadingDiaryEntryDialog(ReadingActivity activity) {
        this.activity = activity;
        booksService = new MyBooksService();

        this.setTitle("New reading diary entry for " + activity.getBook().getTitle());
        this.setModal(true);

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout(10, 10));

        JLabel lHeader = new JLabel("Your reading diary entry on " +
                CalendarUtils.formatDate(Calendar.getInstance().getTime()));
        lHeader.setPreferredSize(new Dimension(400, 20));
        lHeader.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(lHeader, BorderLayout.PAGE_START);

        JPanel textAreaPanel = new JPanel(new BorderLayout());
        textAreaPanel.setBorder(new EmptyBorder(0, 10, 0, 10));
        tDiaryEntry = new JTextArea(10, 10);
        textAreaPanel.add(tDiaryEntry, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton bCancel = new JButton("Cancel");
        bCancel.setPreferredSize(new Dimension(100, 20));
        bCancel.addActionListener((e) -> this.dispose());

        JButton bSave = new JButton("Save");
        bSave.setPreferredSize(new Dimension(100, 20));
        bSave.addActionListener((e) -> saveReadingDiaryEntry());

        buttonPanel.add(bCancel);
        buttonPanel.add(bSave);

        contentPane.add(textAreaPanel, BorderLayout.CENTER);
        contentPane.add(buttonPanel, BorderLayout.PAGE_END);

        this.pack();
        this.setVisible(true);
    }

    private void saveReadingDiaryEntry() {
        try {
            booksService.saveReadingDiaryEntry(
                    new ReadingDiaryEntry(tDiaryEntry.getText(), activity));
            JOptionPane.showMessageDialog(this,
                    "Your diary entry has been saved successfully",
                    "Saved successfully", JOptionPane.INFORMATION_MESSAGE);
        } catch (ReadingDiaryEntryNotSaved readingDiaryEntryNotSaved) {
            JOptionPane.showMessageDialog(this,
                    "Your diary entry could not be saved",
                    "Error while saving", JOptionPane.ERROR_MESSAGE);
        }
        this.dispose();
    }
}
