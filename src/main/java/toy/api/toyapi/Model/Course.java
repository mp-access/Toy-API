package toy.api.toyapi.Model;

import java.util.ArrayList;
import java.util.List;

public class Course {
    public String name;
    public String description;
    public List<Assignment> assignments = new ArrayList<>();
}