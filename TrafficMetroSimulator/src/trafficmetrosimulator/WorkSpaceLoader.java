/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trafficmetrosimulator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Classe di supporto al caricamento di una WorkSpace precedentemente salvata.
 * @author damiano
 */
public class WorkSpaceLoader {
    
    /**
     * L'indice del File da caricare.
     */
    static int fileToLoadIndex = 0;

    /**
     * Restituisce la lista dei nomi dei File con estensione ".wrsp" salvati.
     * @return 
     */
    static String [] getListFilesNames() {
        File [] files = getListFiles();
        String[] names = new String[files.length];
        for (int i = 0; i < files.length; i++) {
           names[i] = files[i].getName();
        }
        return names;
    }
    
    /**
     * Restituisce la lista dei File con estensione ".wrsp" salvati.
     * @return 
     */
    private static File [] getListFiles() {
        File dir = new File(".");
        File [] files = dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".wrsp");
            }
        });
        return files;
    }

    /**
     * Restituisce la WorkSpace salvata nel File in input.
     * @param file
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    static WorkSpace loadWorkSpaceFromFile() throws FileNotFoundException, IOException, ClassNotFoundException {
        File [] files = getListFiles();
        FileInputStream fi = new FileInputStream(files[fileToLoadIndex]);
        ObjectInputStream oi = new ObjectInputStream(fi);
        WorkSpace workSpace = (WorkSpace) oi.readObject();
        oi.close();
        fi.close();
        fileToLoadIndex = 0;
        return workSpace;
    }
    
}
