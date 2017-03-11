package entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity {

    private String username;
    private UserCredentials credentials;
    private final List<Category> readingInterests = new ArrayList<>();

    public User(Long id, String username, UserCredentials credentials) {
        super(id);
        this.username = username;
        this.credentials = credentials;
    }

    public User(String username, UserCredentials credentials) {
        this.username = username;
        this.credentials = credentials;
    }

    public byte[] getPassword() {
        return credentials.getPassword();
    }

    public byte[] getSalt() {
        return credentials.getSalt();
    }

    public void addReadingInterest(Category category) {
        readingInterests.add(category);
    }

    public void addReadingInterests(List<Category> categories) {
        readingInterests.addAll(categories);
    }
}
