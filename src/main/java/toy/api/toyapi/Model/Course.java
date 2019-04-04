package toy.api.toyapi.Model;

import java.util.ArrayList;
import java.util.List;

public class Course {
    public String name;
    public String description;
    public String owner;

    public List<Assignment> assignments = new ArrayList<>();

    public void set(Course other){
        this.name = other.name;
        this.description = other.description;
        this.owner = other.owner;
    }
}