package toy.api.toyapi;

 import com.fasterxml.jackson.databind.DeserializationFeature;
 import com.fasterxml.jackson.databind.ObjectMapper;
 import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
 import toy.api.toyapi.Model.Assignment;
 import toy.api.toyapi.Model.Course;
 import toy.api.toyapi.Model.Exercise;

 import java.io.BufferedReader;
 import java.io.File;
 import java.io.FileInputStream;
 import java.io.InputStreamReader;
 import java.util.*;

public class Cacher {
 	static final String REPO_DIR = "repo";
	static final String REPO_URL = "https://github.com/mp-access/course_structure.git";

	// FOLDERS
	static final String ASSIGNMENT_FOLDER_PREFIX = "assignment";
	static final String EXERCISE_FOLDER_PREFIX = "exercise";
	static final String PUBLIC_EVAL_NAME = "public_eval";
	static final String PRIVATE_EVAL_NAME = "private_eval";
	static final String TEMPLATE_NAME = "template";
	static final String RESOURCE_NAME = "src";
	static final String SOURCE_NAME = "res";

	// FILES
	static final String COURSE_FILE_NAME = "course.yml";
	static final String ASSIGNMENT_FILE_NAME = "assignment.yml";
	static final String EXERCISE_FILE_NAME = "exercise.yml";
	static final String QUESTION_FILE_NAME = "question.md";


	static public List<Course> courses = new ArrayList<>();
	static ObjectMapper mapper;

	List<String> media_ext = Arrays.asList(".jpg", ".jpeg", ".png", ".mp3", ".mp4");
	List<String> ignore_dir = Arrays.asList(".git");
	List<String> ignore_file = Arrays.asList(".gitattributes", ".gitignore", "README.md");
 	

    public static void main(String[] args) {
		gitPull();

	 	Cacher cacher = new Cacher();
		mapper = new ObjectMapper(new YAMLFactory());
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		Course course = new Course();
		courses.add(course);

		cacher.cacheRepo(new File(REPO_DIR), course);
		System.out.println(course);
    }

	static String readFile(File file){
		try{
			byte[] data = null;
			FileInputStream fis = new FileInputStream(file);
			data = new byte[(int) file.length()];
			fis.read(data);
			fis.close();
			return new String(data, "UTF-8");
		}catch(Exception e){
			e.printStackTrace();	
			return "";
		}
	}

    void cacheRepo(File file, Object context){
		if (file.isDirectory()) 
	  	{
	  		if(ignore_dir.contains(file.getName())) return;

	  		Object next_context = context;
			if(file.getName().startsWith(ASSIGNMENT_FOLDER_PREFIX)){
				Assignment assignment = new Assignment();
				((Course)context).assignments.add(assignment);
				next_context = assignment;
			}else if(file.getName().startsWith(EXERCISE_FOLDER_PREFIX)){
				Exercise exercise = new Exercise();
				((Assignment)context).exercises.add(exercise);
				next_context = exercise;
			}else if(file.getName().startsWith(PRIVATE_EVAL_NAME)){
				listFiles(file, ((Exercise)context).privateTests);
			}else if(file.getName().startsWith(PUBLIC_EVAL_NAME)){
				listFiles(file, ((Exercise)context).publicTests);
			}else if(file.getName().startsWith(TEMPLATE_NAME)){
				listFiles(file, ((Exercise)context).templates);
			}else if(file.getName().startsWith(RESOURCE_NAME)){
				listFiles(file, ((Exercise)context).resources);
			}else if(file.getName().startsWith(SOURCE_NAME)){
				listFiles(file, ((Exercise)context).sources);
			}

	    	String[] children = file.list(); 
	    	for (int i=0; i < children.length; i++)
	      		cacheRepo(new File(file, children[i]), next_context);
	  	}else{
			if(ignore_file.contains(file.getName())) return;

			if(context instanceof Course) {
				if(file.getName().equals(COURSE_FILE_NAME)){
					try {
						((Course)context).set(mapper.readValue(file, Course.class));
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}else if(context instanceof Assignment) {
				if(file.getName().equals(ASSIGNMENT_FILE_NAME)){
					try {
						((Assignment)context).set(mapper.readValue(file, Assignment.class));
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}else if(context instanceof Exercise){
				if(file.getName().equals(QUESTION_FILE_NAME)){
					((Exercise)context).question =	readFile(file);
				}else if(file.getName().equals(EXERCISE_FILE_NAME)){
					try {
						((Exercise)context).set(mapper.readValue(file, Exercise.class));
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
	  	}
    }

    public static void listFiles(File dir, List<File> fileList){
		if (dir.isDirectory())
		{
			String[] children = dir.list();
			for (int i=0; i<children.length; i++)
				listFiles(new File(dir, children[i]), fileList);
		}else {
			fileList.add(dir);
		}
	}

	public static boolean deleteDir(File dir) 
	{ 
	  if (dir.isDirectory()) 
	  { 
	    String[] children = dir.list(); 
	    for (int i=0; i<children.length; i++)
	      return deleteDir(new File(dir, children[i])); 
	  }
	  return dir.delete(); 
	} 

    static void gitPull(){
    	File repoDir = new File(REPO_DIR);
    	deleteDir(repoDir);
    	repoDir.mkdirs();
    	
    	try{
			Process process = Runtime.getRuntime().exec("git clone --branch proposal " + REPO_URL + " " + REPO_DIR);
			BufferedReader output = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String msg = output.readLine();
			System.out.println(msg);
    	}catch(Exception e){
    		e.printStackTrace();
   		}
	}
 }