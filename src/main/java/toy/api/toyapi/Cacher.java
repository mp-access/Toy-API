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
	static public List<Course> courses = new ArrayList<>();
	static ObjectMapper mapper;
 	//static public HashMap<File, String> cache = new HashMap<>();

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
			if(file.getName().startsWith("assignment")){
				Assignment assignment = new Assignment();
				((Course)context).assignments.add(assignment);
				next_context = assignment;
			}else if(file.getName().startsWith("exercise")){
				Exercise exercise = new Exercise();
				((Assignment)context).exercises.add(exercise);
				next_context = exercise;
			}

	    	String[] children = file.list(); 
	    	for (int i=0; i < children.length; i++)
	      		cacheRepo(new File(file, children[i]), next_context);
	  	}else{
			if(ignore_file.contains(file.getName())) return;

			if(context instanceof Course) {
				if(file.getName().equals("course.yml")){
					try {
						context = mapper.readValue(file, Course.class);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}else if(context instanceof Assignment) {
				if(file.getName().equals("assignment.yml")){
					try {
						context = mapper.readValue(file, Assignment.class);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}else if(context instanceof Exercise){
				if(file.getName().equals("question.md")){
					((Exercise)context).question =	readFile(file);
				}else if(file.getName().equals("exercise.yml")){
					try {
						context = mapper.readValue(file, Exercise.class);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}

			/*
	  		if(media_ext.contains(file.getName().substring(file.getName().lastIndexOf(".")))) {
		  		cache.put(file, file.getPath());				
  			}else{  				
		  		cache.put(file, readFile(file));
			}
	  		*/
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