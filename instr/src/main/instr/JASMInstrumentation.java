package main.instr;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JASMInstrumentation {
    private JarFile jar;
    private URLClassLoader loader;
    private URL jarUrl;

    public int instr(String jarname){
        try {
            jarUrl = new URL("file:///" + jarname);
            loader = new URLClassLoader(new URL[]{jarUrl});
            jar = new JarFile(jarname);
            classReader();
        }catch(MalformedURLException e) {
            e.printStackTrace();;
        }catch (IOException e){
            e.printStackTrace();
        }
        return 0;
    }

    private void classReader(){
        JarEntry entry;
        String fileName, className;
        Class<?> clazz;
        InputStream is;// = clazz.getClassLoader().getResourceAsStream(fileName);
        byte[] out;

        File outFile;

        for (Enumeration<JarEntry> entries = jar.entries(); entries.hasMoreElements(); ){
            entry = entries.nextElement();
            fileName = entry.getName();

            if(fileName.endsWith(".class")){
                className = fileName.replace('/', '.').substring(0, fileName.length() - 6);
                try{
                    clazz = loader.loadClass(className);
                    is = clazz.getClassLoader().getResourceAsStream(fileName);
                    out = Transformer.transform(is.readAllBytes());

                    outFile = new File(String.format("ModifyClasses/%s", fileName));
                    Files.createDirectories(outFile.getParentFile().toPath());

                    try(FileOutputStream fos = new FileOutputStream(outFile)){
                        fos.write(out);
                    }

                }catch (ClassNotFoundException | IOException e){
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}