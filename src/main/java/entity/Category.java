package entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Category extends BaseEntity {

    private Long id;
    private final String name;

    public Category(Long id, String name) {
        super(id);
        this.name = name;
    }

    public Category(String name) {
        this.name = name;
    }
}
