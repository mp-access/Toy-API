package toy.api.toyapi.Model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Exercise {
    public ExerciseType type;
    public String question;

    public List<File> resources = new ArrayList<>();

    public List<File> templates = new ArrayList<>();
    public List<File> publicTests = new ArrayList<>();
    public List<File> privateTests = new ArrayList<>();
}