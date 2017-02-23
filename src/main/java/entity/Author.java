package entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Author extends BaseEntity {

    private final String name;

    public Author(Long id, String name) {
        super(id);
        this.name = name;
    }

    public Author(String name) {
        this.name = name;
    }
}
