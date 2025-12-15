package searchengine.dto.services;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import searchengine.model.Status;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Site {
    Integer id;
    String name;
    List<String> pageList;
    Status status;
}
