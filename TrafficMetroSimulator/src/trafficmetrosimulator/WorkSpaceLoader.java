/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trafficmetrosimulator;

import java.io.File;
import java.io.FilenameFilter;

/**
 *
 * @author damiano
 */
public class WorkSpaceLoader {

    static void getListFiles() {
        File dir = new File(".");
        File [] files = dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".wrsp");
            }
        });

        for (File xmlfile : files) {
            System.out.println(xmlfile);
        }
    }
    
}
