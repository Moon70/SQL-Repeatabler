package lunartools.sqlrepeatabler;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.sqlrepeatabler.commands.Command;

public class CommandImplementationFactory {
	private static Logger logger = LoggerFactory.getLogger(CommandImplementationFactory.class);
	private static ArrayList<Command> commands;

	public static void main(String[] args) {
		try {
			getCommandImplementations(Command.class.getPackage().getName(),Command.class.getSimpleName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static ArrayList<Command> getCommandImplementations(String packageName, String superclassName) throws Exception{
		if(commands==null){
			commands=new ArrayList<>();
			List<Class<?>> classes = getClassesOfPackage(packageName);
			for (Class<?> clazz : classes) {
				if(clazz.getSuperclass().getSimpleName().equals(superclassName)){
					Command command=(Command)clazz.newInstance();
					commands.add(command);
					logger.debug("found command implementation: "+command.getClass().getSimpleName());
				}
			}
		}
		return commands;
	}

	private static List<Class<?>> getClassesOfPackage(String packageName) throws Exception{
		List<Class<?>> classesOfPackage = new ArrayList<>();
		String path = packageName.replace('.', '/');
		

		ClassLoader contextClassLoader=Thread.currentThread().getContextClassLoader();
		Enumeration<URL> resources=contextClassLoader.getResources(path);
		while (resources.hasMoreElements()) {
			URL resource=resources.nextElement();
			if(resource.getProtocol().equals("jar")) {
				String pathJar=resource.getPath().substring(5,resource.getPath().indexOf("!"));
				pathJar=pathJar.replace("%20"," ");
				classesOfPackage=findClassesInJar(path,pathJar);
			} else if(resource.getProtocol().equals("file")) {
				File directory=new File(resource.getFile());
				if (directory.exists()) {
					classesOfPackage.addAll(findClassesInFilesystem(directory, packageName));
				}
			}
		}
		return classesOfPackage;
	}

	private static List<Class<?>> findClassesInJar(String path,String jarPath) throws Exception{
		List<Class<?>> classes=new ArrayList<>();
		try (JarFile jarFile=new JarFile(new File(jarPath))){
			Enumeration<JarEntry> entries=jarFile.entries();
			while (entries.hasMoreElements()) {
				JarEntry entry=entries.nextElement();
				String entryName=entry.getName();
				if (entryName.startsWith(path) && entryName.endsWith(".class") && !entryName.contains("$")){
					String className = entryName.replace('/', '.').substring(0, entryName.length() - 6);
					classes.add(Class.forName(className));
				}
			}
		}
		return classes;
	}

	private static List<Class<?>> findClassesInFilesystem(File directory, String packageName) throws Exception {
		List<Class<?>> classes = new ArrayList<>();
		final String suffixClass=".class";
		File[] files = directory.listFiles();
		for (File file : files) {
			if (file.getName().endsWith(suffixClass)) {
				classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - suffixClass.length())));
			}
		}
		return classes;
	}
}

