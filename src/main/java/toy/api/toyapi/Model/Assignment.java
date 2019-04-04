package toy.api.toyapi.Model;

import java.util.ArrayList;
import java.util.List;

public class Assignment {
    public String releaseDate;
    public String dueDate;

    public List<Exercise> exercises = new ArrayList<>();

    public void set(Assignment other){
        this.releaseDate = other.releaseDate;
        this.dueDate = other.dueDate;
    }
}

