package object;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class SerializerNodes {
    
    private Playground p;
    
    
    public SerializerNodes(Playground p){
        this.p = p;
    }
    
    public void saveInFile(){
        
        Nodes[][] iNodes = p.getNodes();
        
        StringBuffer fileNameBuf = new StringBuffer((new Date()).toLocaleString());
        for(int i=0; i<fileNameBuf.length(); i++){
			if((fileNameBuf.charAt(i) == ' ')||(fileNameBuf.charAt(i) == '.')||(fileNameBuf.charAt(i) == ':'))
			    fileNameBuf.setCharAt(i, '-');
		}
		String nameFile = fileNameBuf.toString();
		
		String nameZip = fileNameBuf.toString() + ".map";
		try{
		    FileOutputStream myZip = new FileOutputStream(nameZip.toString());
		    ZipOutputStream myFileZip = new ZipOutputStream(myZip);
		    myFileZip.setMethod(ZipOutputStream.DEFLATED);
		    myFileZip.setLevel(Deflater.BEST_COMPRESSION);
		    
		    ZipEntry entryZip = new ZipEntry(nameFile);
		    myFileZip.putNextEntry(entryZip);
		     
		    BufferedOutputStream bufferedOutput = new BufferedOutputStream(myFileZip);
		    ObjectOutputStream fileOutput = new ObjectOutputStream(bufferedOutput);
		    
		    fileOutput.writeObject(iNodes);
		    
		    fileOutput.flush();
		    myFileZip.closeEntry();
		    myFileZip.close();
		    fileOutput.close();
		    myZip.flush();
		    myZip.close();
		    
		    System.out.println("Map saved in " + nameZip);
		}
		catch (IOException e) {
		    e.printStackTrace();
		}
		
		
    }
    
    public void loadFromFile(String myFile) throws IOException, ClassNotFoundException {
		
		ZipInputStream zipFile = new ZipInputStream(new FileInputStream(myFile));
		ZipEntry entryZip = zipFile.getNextEntry();
		
		BufferedInputStream buff = new BufferedInputStream(zipFile);
		ObjectInputStream oos = new ObjectInputStream(buff);
		
		Nodes[][] newNodes = (Nodes[][]) oos.readObject();
		
		oos.close();
		zipFile.close();
		buff.close();
				
		p.setNodes(newNodes);
		p.repaint();
	}
     
}